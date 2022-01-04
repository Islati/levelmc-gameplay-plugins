package com.levelmc.core.api.raycast;

import org.bukkit.Location;

/**
 * Applied render effects along the raycasts traversed locations.
 */
public interface ParticleRenderer {
    void render(Location loc);
}
