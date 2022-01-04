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

import java.util.Arrays;
import java.util.List;

public class SpellFireBreath extends BaseSpell {

    /*
    todo:
        - range increase per level.
        - lower starting range.
     */

    public SpellFireBreath() {
        super("fire-breath", MagicType.FIRE, 3, 0, 0);
        setHitboxSize(1.5);
        setParticleRenderer(
                new ParticleRenderer() {
                    @Override
                    public void render(Location loc) {
                        /* Render effects */
                        World w = loc.getWorld();
                        Particle spawnParticle = Particle.FALLING_LAVA;
                        w.spawnParticle(Particle.FLAME, loc, 1, 0, 0, 0);
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

    @Override
    public void onSpellHitEntity(Player player, Entity entity) {
        if (!(entity instanceof LivingEntity)) {
            return;
        }

        LivingEntity le = (LivingEntity) entity;

        int fireTime = le.getFireTicks();
        if (fireTime < 0) {
            fireTime = 0;
        }


        boolean burning = le.getFireTicks() > 0;
        le.setFireTicks(fireTime + ((getPlayerSpellLevel(player) + 1) * 35));
        int playerSpellLevel = getPlayerSpellLevel(player);
        double damage = playerSpellLevel + 0.5 * 1.77;
        if (burning) {
            damage = damage * 1.8;
        }
        le.damage(damage);
        DamageIndicatorUtil.getInstance().spawnDamageIndicatorDelayed(le, String.format("&c☀ &7%s &c☀", damage), EntityDamageEvent.DamageCause.CUSTOM, damage, NumberUtil.getRandomInRange(10, 15));
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
        return Arrays.asList("&cEngorge your enemies in &oDragons Breath",
                "&cDamage to those already &oon &r&cfire");
    }

    @Override
    public void onSpellCast(Player player) {

    }
}
