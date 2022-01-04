package com.levelmc.core.api.raycast;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Particle;

public abstract class AbstractParticleRender implements ParticleRenderer {

    @Getter
    @Setter
    private Particle particleType = Particle.CLOUD;

    public AbstractParticleRender() {

    }

    public AbstractParticleRender(Particle particle) {
        this.particleType = particle;
    }

    public AbstractParticleRender particle(Particle particle) {
        setParticleType(particle);
        return this;
    }

    @Override
    public abstract void render(Location loc);

}
