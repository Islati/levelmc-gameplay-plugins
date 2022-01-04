package com.levelmc.wizards.spells;

import com.destroystokyo.paper.ParticleBuilder;
import com.levelmc.core.api.entities.EntityUtils;
import com.levelmc.core.api.utils.NumberUtil;
import com.levelmc.core.api.utils.SoundUtils;
import com.levelmc.core.api.world.LocationUtils;
import com.levelmc.loot.Loot;
import com.levelmc.loot.api.utils.DamageIndicatorUtil;
import com.levelmc.wizards.users.Wizard;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;

public class SpellFlameCircle extends ParticleSpell {

    private static final int baseRange = 8;
    private static final int rangeIncreasePerLevel = 1;

    private int defaultCost = 20;

    public SpellFlameCircle() {
        super(Particle.FLAME, 0.15, 550, 2, "flame-circle", MagicType.FIRE, 5, 8, 2);
        setMenuMaterial(Material.BLAZE_POWDER);
    }

    @Override
    public int getManaCost(Player player, int level) {
        if (level > getDefaultMaxLevel()) {
            return getManaCost(player, getDefaultMaxLevel()) - 5;
        }

        int cost = defaultCost - (level > 1 ? level * 2 : 0);
        return Math.max(cost, 0);
    }

    @Override
    public int getExpAward(Player player, int level) {
        return 25 * level;
    }

    @Override
    public List<String> getSpellDescription(Player player) {
        return Arrays.asList("&cCast a circle of flames and roast your enemies", "&calong with everything around them");
    }

    @Override
    public void onSpellCast(Player player) {
        SoundUtils.playSound(player, Sound.ENTITY_FIREWORK_ROCKET_SHOOT, 1.0F, 1.4F);
        new ParticleBuilder(Particle.SMOKE_NORMAL).location(player.getLocation()).count(NumberUtil.getRandomInRange(5, 7)).spawn();
    }

    @Override
    public void onSpellHitEntity(Player player, Entity hit) {
        if (!(hit instanceof LivingEntity)) {
            return;
        }

        LivingEntity hitMob = (LivingEntity) hit;
        drawCircle(player, hitMob.getLocation().add(0, 1, 0));

        if (hitMob.getUniqueId().equals(player.getUniqueId())) {
            return;
        }

        int spellLevel = getPlayerSpellLevel(player);


        double damage = 5.5 * spellLevel;
        hitMob.damage(damage, player);
        hitMob.setFireTicks(60);
        displayDamage(player, damage, hitMob);
    }

    @Override
    public void onSpellHitBlock(Player player, Block block) {
        drawCircle(player, block.getLocation());
        burnBlockAbove(block, getPlayerSpellLevel(player) + 3);
    }

    protected void burnBlockAbove(Block block, int seconds) {
        Block blockAbove = block.getRelative(0, 1, 0);
        if (blockAbove.getType() == Material.AIR) {
            final Material restoreMaterial = blockAbove.getType();
            blockAbove.setType(Material.FIRE);

            new BukkitRunnable() {
                @Override
                public void run() {
                    blockAbove.setType(restoreMaterial);
                }
            }.runTaskLater(Loot.getInstance(), seconds * 20);
        }
    }

    protected void drawCircle(Player player, Location hit) {
        Wizard user = getUser(player);

        int radiusBonus = 0;

        int playerSpellLevel = getPlayerSpellLevel(player);

        if (playerSpellLevel > getDefaultMaxLevel()) {
            radiusBonus += 3;
        }

        if (playerSpellLevel > 0 && playerSpellLevel < 3) {
            radiusBonus += 1;
        } else if (playerSpellLevel >= 3 && playerSpellLevel <= getDefaultMaxLevel()) {
            radiusBonus += 2;
        }

        int radius = 2 + radiusBonus; //todo get specific value based on spells level + bonus from wand.

        List<Location> circleAroundTarget = LocationUtils.getCircle(hit, radius);

        circleAroundTarget.forEach(l -> {
//            Particle.FLAME.builder().location(l).offset(0, 0, 0).force(false).spawn();

            burnBlockAbove(l.getBlock(), getPlayerSpellLevel(player) + 3);
            ParticleBuilder flameParticle = new ParticleBuilder(Particle.FLAME)
                    .force(false)
                    .count(2);

            for (int i = 1; i < 5; i++) {
                flameParticle.location(l.add(0, i, 0)).force(false).offset(0, i, 0).spawn();
            }

            EntityUtils.getLivingEntitiesNearLocation(l, 1).forEach(le -> {
                if (le instanceof Player) { //todo build pvp check method
                    Player attackedPlayer = (Player) le;
                    if (attackedPlayer.getUniqueId().equals(player.getUniqueId())) {
                        return;
                    }
                }

                le.setFireTicks((playerSpellLevel * 20) + 5);
                SoundUtils.playSound(player, Sound.BLOCK_FIRE_AMBIENT);
                flameParticle.location(le.getEyeLocation().add(0, 0.25, 0)).count(1).force(false).spawn();
                double damage = 2.4 * playerSpellLevel;
                le.damage(damage, player);
                DamageIndicatorUtil.getInstance().spawnDamageIndicator(le, String.format("&e✯ &6%s &e✯", String.valueOf(damage)), EntityDamageEvent.DamageCause.CUSTOM, damage);
            });
        });
    }
}
