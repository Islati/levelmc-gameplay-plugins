package com.levelmc.wizards.spells;

import com.levelmc.core.api.entities.EntityUtils;
import com.levelmc.core.api.utils.NumberUtil;
import com.levelmc.core.api.world.LocationUtils;
import com.levelmc.loot.api.utils.DamageIndicatorUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Arrays;
import java.util.List;

public class SpellHealingPool extends ParticleSpell {
    public SpellHealingPool() {
        super(Particle.HEART, 0.15, 555, 2.25, "healing-pool", MagicType.WHITE, 5, 8, 1);
    }

    @Override
    public void onSpellCast(Player player) {
        /* Draw healing circle around players head. */
        drawAroundEye(player);

        //todo implement spell cast.
    }

    protected void drawAroundEye(LivingEntity entity) {
        List<Location> headCircle = LocationUtils.getParticlesCircle(entity.getEyeLocation(), 2.5f, 0.2f);
        for (Location loc : headCircle) {
            loc.getWorld().spawnParticle(Particle.HEART, loc, 1);
        }
    }

    @Override
    public void onSpellHitEntity(Player player, Entity entity) {
        if (!(entity instanceof Player)) {
            return;
        }

        healRadiusOnCast(player, entity.getLocation());
    }

    public void healRadiusOnCast(Player player, Location loc) {

        int radius = getRadius(player, getPlayerSpellLevel(player));

        List<Location> radiusAround = LocationUtils.getCircle(loc, radius);

        radiusAround.forEach(l -> {
            l.getWorld().spawnParticle(Particle.HEART, l, 2,NumberUtil.randomDouble(0.2,0.5),NumberUtil.randomDouble(0.2,0.5),NumberUtil.randomDouble(0.2,0.5));
        });

        EntityUtils.getLivingEntitiesNearLocation(loc, getRadius(player, getPlayerSpellLevel(player))).forEach(
                living -> {

                    if (!(living instanceof Player)) {
                        return;
                    }

                    int healAmount = getPercentHealed(player, getPlayerSpellLevel(player));

                    double healthRestore = living.getHealth() * (healAmount / 100);
                    healthRestore = NumberUtil.round(healthRestore, 1);
                    double playerHealth = living.getHealth();

                    double newHealth = playerHealth + healthRestore;

                    if (newHealth > living.getMaxHealth()) {
                        newHealth = living.getMaxHealth();
                    }

                    living.setHealth(newHealth);
                    drawAroundEye(living);
                    DamageIndicatorUtil.getInstance().spawnDamageIndicatorDelayed(living, String.format("&a+ %s ♥", healthRestore), EntityDamageEvent.DamageCause.CUSTOM, healthRestore, NumberUtil.getRandomInRange(5, 8));
                }
        );


    }

    @Override
    public void onSpellHitBlock(Player player, Block block) {
        healRadiusOnCast(player, block.getLocation());
    }

    @Override
    public int getManaCost(Player player, int level) {
        if (level > getDefaultMaxLevel()) {
            return 15;
        }

        switch (level) {
            default:
            case 1:
                return 25;
            case 2:
                return 23;
            case 3:
                return 21;
            case 4:
                return 20;
            case 5:
                return 18;
        }
    }

    @Override
    public int getExpAward(Player player, int level) {
        if (level > getDefaultMaxLevel()) {
            return 155;
        }

        return level * 28;
    }

    public int getPercentHealed(Player player, int level) {
        if (level > getDefaultMaxLevel()) {
            return 48;
        }

        switch (level) {
            default:
            case 1:
                return 15;
            case 2:
                return 19;
            case 3:
                return 24;
            case 4:
                return 29;
            case 5:
                return 34;
        }
    }

    public int getRadius(Player player, int level) {
        if (level > getDefaultMaxLevel()) {
            return 7;
        }

        switch (level) {
            default:
            case 1:
                return 2;
            case 2:
                return 3;
            case 3:
                return 4;
            case 4:
                return 5;
            case 5:
                return 6;
        }
    }

    @Override
    public List<String> getSpellDescription(Player player) {
        /* todo: Include radius, and heal %" */
        int radius = getRadius(player, getPlayerSpellLevel(player));
        int percentage = getPercentHealed(player, getPlayerSpellLevel(player));
        return Arrays.asList(
                "&aHeal players in a {radius} block radius".replace("{radius}", String.valueOf(radius)),
                "&aof the hit location for {percentage}% health".replace("{percentage}", String.valueOf(percentage))
        );
    }
}
