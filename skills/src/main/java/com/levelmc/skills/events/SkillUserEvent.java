package com.levelmc.skills.events;

import com.levelmc.skills.Skills;
import com.levelmc.skills.users.SkillUser;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class SkillUserEvent extends PlayerEvent {
    private static HandlerList handlers = new HandlerList();

    @Getter
    private SkillUser user = null;
    public SkillUserEvent(@NotNull Player who) {
        super(who);
        this.user = Skills.getInstance().getUserManager().getUser(who);
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
