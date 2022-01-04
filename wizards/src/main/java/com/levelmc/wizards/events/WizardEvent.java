package com.levelmc.wizards.events;

import com.levelmc.wizards.Wizards;
import com.levelmc.wizards.users.Wizard;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class WizardEvent extends PlayerEvent {
    private static HandlerList handlers = new HandlerList();

    @Getter
    private Wizard user;
    public WizardEvent(@NotNull Player who) {
        super(who);
        this.user = Wizards.getInstance().getUserManager().getUser(who);
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
