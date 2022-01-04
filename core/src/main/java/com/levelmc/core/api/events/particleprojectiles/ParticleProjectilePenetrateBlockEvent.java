package com.levelmc.core.api.events.particleprojectiles;


import com.levelmc.core.api.particle.projectiles.ParticleProjectile;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;

public class ParticleProjectilePenetrateBlockEvent extends ParticleProjectileHitBlockEvent {
    public ParticleProjectilePenetrateBlockEvent(LivingEntity who, Location start, Location end, Block what, ParticleProjectile projectile) {
        super(who, start, end, what, projectile);
    }
}