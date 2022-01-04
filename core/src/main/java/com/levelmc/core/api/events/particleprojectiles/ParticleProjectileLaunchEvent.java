package com.levelmc.core.api.events.particleprojectiles;


import com.levelmc.core.api.particle.projectiles.ParticleProjectile;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public class ParticleProjectileLaunchEvent extends ParticleProjectileEvent {
    private Location start_location;

    public ParticleProjectileLaunchEvent(LivingEntity who, Location start, ParticleProjectile projectile) {
        super(who, projectile);
        this.start_location = start;
    }

    public Location getStart() {
        if (!this.cancelled)
            return this.start_location;
        return null;
    }

    public Color getColor() {
        if (!this.cancelled)
            return this.projectile.getColor();
        return null;
    }
}
