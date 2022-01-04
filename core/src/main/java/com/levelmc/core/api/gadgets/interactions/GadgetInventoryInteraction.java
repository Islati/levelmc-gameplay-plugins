package com.levelmc.core.api.gadgets.interactions;

import com.levelmc.core.api.gadgets.Gadget;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Interactions with a gadget from inside an inventory interface.
 */
public interface GadgetInventoryInteraction extends GadgetAction {
    void onInventoryClick(Gadget gadget, InventoryClickEvent e);
}
