package com.levelmc.core.api.events.particleprojectiles;

import com.levelmc.core.api.particle.projectiles.ParticleProjectile;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public class ParticleProjectilePenetrateEntityEvent extends ParticleProjectileHitEntityEvent {
    public ParticleProjectilePenetrateEntityEvent(LivingEntity who, Location start, Location end, Entity what, ParticleProjectile projectile) {
        super(who, start, end, what, projectile);
    }
}
