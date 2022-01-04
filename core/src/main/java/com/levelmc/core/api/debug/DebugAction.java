package com.levelmc.core.api.debug;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public abstract class DebugAction {

    @Getter
    private final String id;

    public abstract void onDebug(Player player, String[] args);

    public void onEnable() {}

    public void onDisable() {}
}
