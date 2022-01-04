package com.levelmc.core.api.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public interface MenuItemClickHandler {
    void onClick(MenuItem item, Player player, ClickType type);
}
