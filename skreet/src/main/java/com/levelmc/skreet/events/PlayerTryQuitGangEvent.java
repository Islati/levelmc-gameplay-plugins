package com.levelmc.skreet.events;

import com.levelmc.skreet.gangs.GangType;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerTryQuitGangEvent extends PlayerGangEvent {
    private static HandlerList handlerList = new HandlerList();

    public PlayerTryQuitGangEvent(@NotNull Player who, GangType gang) {
        super(who, gang);
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
