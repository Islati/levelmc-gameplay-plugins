package com.levelmc.core.api.events.particleprojectiles;


import com.levelmc.core.api.particle.projectiles.ParticleProjectile;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;

public class ParticleProjectileHitBlockEvent extends ParticleProjectileHitEvent {
    private Block block;

    public ParticleProjectileHitBlockEvent(LivingEntity who, Location start, Location end, Block what, ParticleProjectile projectile) {
        super(who, start, end, projectile);
        this.block = what;
    }

    public Block getBlock() {
        if (!this.cancelled)
            return this.block;
        return null;
    }
}