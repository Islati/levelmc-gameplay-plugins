package com.levelmc.skreet.events;

import com.levelmc.skreet.Skreet;
import com.levelmc.skreet.users.SkreetPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public abstract class GamePlayerEvent extends PlayerEvent {
    public GamePlayerEvent(@NotNull Player who) {
        super(who);
    }

    public SkreetPlayer getGamePlayer() {
        return Skreet.getInstance().getUserManager().getUser(getPlayer());
    }
}
