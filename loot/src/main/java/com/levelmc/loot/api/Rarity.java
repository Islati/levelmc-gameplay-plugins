package com.levelmc.loot.api;

import com.levelmc.core.api.Chanceable;

/**
 * Defines the rarity of an item.
 */
public interface Rarity extends Chanceable {

    /**
     * Unique identifier for this rarity. Not used on display, but in configuration.
     * @return id.
     */
    String id();

    /**
     *
     * @return
     */
    String displayName();

}
