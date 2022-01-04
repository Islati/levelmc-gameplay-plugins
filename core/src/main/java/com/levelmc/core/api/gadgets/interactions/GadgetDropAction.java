package com.levelmc.core.api.gadgets.interactions;

import com.levelmc.core.api.gadgets.Gadget;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

/**
 * When a player drops a gadget
 */
public interface GadgetDropAction extends GadgetAction {
    void onDrop(Player player, Gadget gadget, Item item);
}
