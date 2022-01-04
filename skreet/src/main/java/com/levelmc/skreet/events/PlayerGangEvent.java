package com.levelmc.skreet.events;

import com.levelmc.skreet.gangs.Gang;
import com.levelmc.skreet.gangs.GangType;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class PlayerGangEvent extends GamePlayerEvent {
    @Getter
    private GangType gangType;

    public PlayerGangEvent(@NotNull Player who, GangType gang) {
        super(who);
        this.gangType = gang;
    }

    public Gang getGang() {
        return gangType.get();
    }
}

