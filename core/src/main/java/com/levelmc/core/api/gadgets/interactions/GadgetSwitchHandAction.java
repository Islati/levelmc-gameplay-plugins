package com.levelmc.core.api.gadgets.interactions;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public interface GadgetSwitchHandAction extends GadgetAction {
    void onSwitchHand(Player player, InventoryClickEvent event);
}
