package com.levelmc.core.api.raycast;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Particle;

/**
 * Renders a single particle at the given location.h
 */
public class DefaultRenderer extends AbstractParticleRender {

    @Getter
    @Setter
    private double offsetX = 0;

    @Getter
    @Setter
    private double offsetY = 0;

    @Getter
    @Setter
    private double offsetZ = 0;


    public DefaultRenderer() {

    }

    @Override
    public DefaultRenderer particle(Particle particle) {
        this.setParticleType(particle);
        return this;
    }

    public DefaultRenderer offset(double x, double y, double z) {
        setOffsetX(x);
        setOffsetY(y);
        setOffsetZ(z);
        return this;
    }

    @Override
    public void render(Location loc) {
        loc.getWorld().spawnParticle(getParticleType(), loc.getX(), loc.getY(), loc.getZ(), 1, offsetX, offsetY, offsetZ);
    }
}
