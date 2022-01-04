package com.levelmc.loot.api.levels.events;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemGainExpEvent extends PlayerEvent implements Cancellable {
    private static HandlerList handlerList = new HandlerList();

    @Getter @Setter
    private boolean cancelled;

    @Getter
    private ItemStack item;

    @Getter @Setter
    private int amount;

    public ItemGainExpEvent(@NotNull Player who, ItemStack item, int amount) {
        super(who);
        this.item = item;
        this.amount = amount;
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
