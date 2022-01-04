package com.levelmc.core.api.events.particleprojectiles;

import com.levelmc.core.api.particle.projectiles.ParticleProjectile;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public class ParticleProjectileHitEvent extends ParticleProjectileLaunchEvent {
    private Location end_location;

    public ParticleProjectileHitEvent(LivingEntity who, Location start, Location end, ParticleProjectile projectile) {
        super(who, start, projectile);
        this.end_location = end;
    }

    public Location getEnd() {
        if (!this.cancelled)
            return this.end_location;
        return null;
    }
}