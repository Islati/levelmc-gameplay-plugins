package com.levelmc.wizards.spells;

import com.levelmc.core.api.raycast.ParticleRenderer;
import com.levelmc.core.api.utils.NumberUtil;
import com.levelmc.loot.api.utils.DamageIndicatorUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

public class SpellIceBeam extends BaseSpell {
    public SpellIceBeam() {
        super("ice-beam", MagicType.WATER, 3, 0, 0);

        setHitboxSize(1.5);
        setParticleRenderer(
                new ParticleRenderer() {
                    @Override
                    public void render(Location loc) {
                        /* Render effects */
                        World w = loc.getWorld();
                        Particle spawnParticle = Particle.CLOUD;
                        w.spawnParticle(Particle.WATER_BUBBLE, loc, 1, 0, 0, 0);
                        w.spawnParticle(spawnParticle, loc, 1, 0, 0, 0.5);
                        w.spawnParticle(spawnParticle, loc, 1, 0, 0, -0.5);
                        w.spawnParticle(spawnParticle, loc, 1, 0.5, 0, -0.5);
                        w.spawnParticle(spawnParticle, loc, 1, -0.5, 0, -0.5);
                        w.spawnParticle(spawnParticle, loc, 1, -0.5, 0, 0);
                        w.spawnParticle(spawnParticle, loc, 1, 0.5, 0, 0);
                    }
                }
        );
    }

    @Override
    public void onSpellHitBlock(Player player, Block block) {

    }

    protected int getEffectLevel(Player player) {
        int spellLevel = getPlayerSpellLevel(player);

        if (spellLevel > getDefaultMaxLevel()) {
            return 4;
        }

        switch (spellLevel) {
            default:
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
        }
    }

    @Override
    public void onSpellHitEntity(Player player, Entity entity) {
        //slow entity
        if (!(entity instanceof LivingEntity)) {
            return;
        }

        LivingEntity le = (LivingEntity) entity;

        int fireTime = le.getFireTicks();
        if (fireTime < 0) {
            fireTime = 0;
        }


        int ticks = 0;
        boolean slowAlready = false;
        if (le.hasPotionEffect(PotionEffectType.SLOW)) {
            ticks = le.getPotionEffect(PotionEffectType.SLOW).getDuration();
            slowAlready = true;
        } else {
            ticks = ticks + getPlayerSpellLevel(player) + 1 * 35;
        }

        le.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, ticks, getEffectLevel(player)));
        int playerSpellLevel = getPlayerSpellLevel(player);
        double damage = playerSpellLevel + 0.5 * 1.77;
        if (slowAlready) {
            damage = damage * 1.8;
        }
        le.damage(damage);
        DamageIndicatorUtil.getInstance().spawnDamageIndicatorDelayed(le, String.format("&b☀ &7%s &b☀", damage), EntityDamageEvent.DamageCause.CUSTOM, damage, NumberUtil.getRandomInRange(10, 15));
    }

    @Override
    public void onSpellCast(Player player) {

    }

    @Override
    public int getManaCost(Player player, int level) {
        if (level > 3) {
            return 3;
        }

        switch (level) {
            case 1:
            default:
                return 7;
            case 2:
                return 5;
            case 3:
                return 4;
        }
    }

    @Override
    public int getExpAward(Player player, int level) {
        if (level > 3) {
            return 6;
        }

        switch (level) {
            case 1:
            default:
                return 2;
            case 2:
                return 4;
            case 3:
                return 5;
        }
    }

    @Override
    public List<String> getSpellDescription(Player player) {
        return Arrays.asList("&bHarness your powers and freeze enemies");
    }
}
