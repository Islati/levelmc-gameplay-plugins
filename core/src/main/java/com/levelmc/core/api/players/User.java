package com.levelmc.core.api.players;

import lombok.Getter;
import com.levelmc.core.api.yml.Path;
import com.levelmc.core.api.yml.YamlConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Used to store player specific information in a reliable approach.
 */
public class User extends YamlConfig {
    @Getter
    @Path("uuid")
    private UUID id;

    @Getter
    @Path("name")
    private String name;

    public User(Player player) {
        this.id = player.getUniqueId();
        this.name = player.getName();
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(id);
    }
}
