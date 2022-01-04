package com.levelmc.core.api.events.particleprojectiles;

import com.levelmc.core.api.particle.projectiles.ParticleProjectile;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public class ParticleProjectileHitEntityEvent extends ParticleProjectileHitEvent {
    private Entity entity;

    public ParticleProjectileHitEntityEvent(LivingEntity who, Location start, Location end, Entity what, ParticleProjectile projectile) {
        super(who, start, end, projectile);
        this.entity = what;
    }

    public Entity getEntity() {
        if (!this.cancelled)
            return this.entity;
        return null;
    }
}
