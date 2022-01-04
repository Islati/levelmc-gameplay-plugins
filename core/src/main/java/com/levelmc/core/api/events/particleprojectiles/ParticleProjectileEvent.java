package com.levelmc.core.api.events.particleprojectiles;


import com.levelmc.core.api.particle.projectiles.ParticleProjectile;
import lombok.Getter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ParticleProjectileEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    protected boolean cancelled = false;

    @Getter
    protected ParticleProjectile projectile;

    private LivingEntity source;

    public ParticleProjectileEvent(LivingEntity who, ParticleProjectile projectile) {
        this.source = who;
        this.projectile = projectile;
    }

    public LivingEntity getSourceEntity() {
        if (!this.cancelled)
            return this.source;
        return null;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
