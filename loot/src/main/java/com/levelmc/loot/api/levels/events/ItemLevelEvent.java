package com.levelmc.loot.api.levels.events;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemLevelEvent extends PlayerEvent {
    private static HandlerList handlerList = new HandlerList();

    @Getter
    private ItemStack item;
    @Getter
    private int level;
    public ItemLevelEvent(@NotNull Player who, ItemStack item, int level) {
        super(who);

        this.item = item;
        this.level = level;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
