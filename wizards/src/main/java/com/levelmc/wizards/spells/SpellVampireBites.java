package com.levelmc.wizards.spells;

import com.destroystokyo.paper.ParticleBuilder;
import com.levelmc.core.api.entities.EntityUtils;
import com.levelmc.core.api.thread.IterationLimitedRunnable;
import com.levelmc.core.api.utils.NumberUtil;
import com.levelmc.core.api.world.LocationUtils;
import com.levelmc.loot.api.utils.DamageIndicatorUtil;
import com.levelmc.wizards.Wizards;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class SpellVampireBites extends ParticleSpell {

    public SpellVampireBites() {
        super(Particle.SQUID_INK, 0.20, 500, 2.2, "vampire-bites", MagicType.DARK, 5, 8, 2);
    }

    @Override
    public void onSpellCast(Player player) {

    }

    protected class DamageEffectsRunnable extends IterationLimitedRunnable {
        private LivingEntity entity;

        private UUID pid;

        public DamageEffectsRunnable(Player caster, LivingEntity anchor) {
            super(3);
            this.pid = caster.getUniqueId();
            this.entity = anchor;
        }

        @Override
        public void onRun() {
            Player p = Bukkit.getPlayer(pid);

            if (p == null) {
                cancel();
                return;
            }

            if (!entity.isValid() || entity.isDead()) {
                cancel();
                return;
            }

            try {
                handleDamageEffects(p, entity);
            } catch (Exception e) {
                //wildcard cancel
                cancel();
            }
        }
    }

    protected void spawnDamageEffects(Location target) {
        World world = target.getWorld();
        Particle particle = Particle.SQUID_INK;
        int amount = 1;
        ParticleBuilder pb = new ParticleBuilder(Particle.SQUID_INK)
                .count(1);
        for (int i = 0; i < 5; i += 1) {
            pb.offset(Math.cos(amount * i), i, Math.sin(i)).location(target).count(1).force(false).spawn();
        }
    }

    protected double getDamage(Player player, int level) {
        int spellLevel = getPlayerSpellLevel(player);
        return NumberUtil.round(NumberUtil.randomDouble((level * 0.25) + spellLevel, (level * 0.4) * 1.1212 + spellLevel), 1);
    }

    protected void handleDamageEffects(Player player, LivingEntity e) {
        double damage = getDamage(player, getPlayerSpellLevel(player));
        spawnDamageEffects(e.getEyeLocation());
        e.damage(damage, player);
        DamageIndicatorUtil.getInstance().spawnDamageIndicatorDelayed(e, String.format("&c❣ -%s ❣", damage), EntityDamageEvent.DamageCause.CUSTOM, damage, NumberUtil.getRandomInRange(5, 7));
    }

    protected void handleDamage(Player player, LivingEntity e) {
        new DamageEffectsRunnable(player, e).runTaskTimer(Wizards.getInstance(), 3l, 25L);
    }


    @Override
    public void onSpellHitEntity(Player player, Entity entity) {
        if (!(entity instanceof LivingEntity)) {
            return;
        }

        if (entity instanceof Player) {
            return;
        }

        int playerSpellLevel = getPlayerSpellLevel(player);
        List<Location> circleOfImpact = LocationUtils.getCircle(((LivingEntity) entity).getEyeLocation(), 3);

        for (Location location : circleOfImpact) {
            location.getWorld().spawnParticle(Particle.SQUID_INK, location.add(0, 3, 0), 4);
        }

        LivingEntity le = (LivingEntity) entity;
        handleDamage(player, le);

        EntityUtils.getLivingEntitiesNearLocation(le.getLocation(), playerSpellLevel + 1.5)
                .forEach(e -> {
                    /* Todo check for allies, friends, non pvp players, etc */

                    if (e instanceof Player) {
                        return;
                    }

                    handleDamage(player, e);
                });
    }

    @Override
    public void onSpellHitBlock(Player player, Block block) {

        int playerSpellLevel = getPlayerSpellLevel(player);

        List<Location> circleOfImpact = LocationUtils.getCircle(block.getLocation(), playerSpellLevel + 2);

        for (Location location : circleOfImpact) {
            location.getWorld().spawnParticle(Particle.SQUID_INK, location.add(0, 3, 0), 4);
        }

        EntityUtils.getLivingEntitiesNearLocation(block.getLocation(), playerSpellLevel)
                .forEach(e -> {
                    /* Todo check for allies, friends, non pvp players, etc */

                    if (e instanceof Player) {
                        return;
                    }

                    handleDamage(player, e);
                });

    }

    @Override
    public int getManaCost(Player player, int level) {
        if (level > getDefaultMaxLevel()) {
            return 7;
        }

        switch (level) {
            default:
            case 1:
                return 18;
            case 2:
                return 15;
            case 3:
                return 13;
            case 4:
                return 11;
            case 5:
                return 8;
        }
    }

    @Override
    public int getExpAward(Player player, int level) {
        if (level > getDefaultMaxLevel()) {
            return 135;
        }

        return level * 25;
    }

    protected int getRadius(Player player, int level) {
        if (level > getDefaultMaxLevel()) {
            return 8;
        }

        return level + 2;
    }

    @Override
    public List<String> getSpellDescription(Player player) {
        return Arrays.asList("&eDrain the life from all enemies", "&earound the impact area");
    }
}
