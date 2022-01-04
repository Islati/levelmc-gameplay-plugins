package com.levelmc.skreet.events;

import com.levelmc.skreet.gangs.GangType;
import lombok.Getter;
import org.bukkit.event.Event;

public abstract class GangEvent extends Event {

    @Getter
    private GangType gangType;

    public GangEvent(GangType gang) {
        this.gangType = gang;
    }
}
