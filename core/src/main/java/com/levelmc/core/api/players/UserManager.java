package com.levelmc.core.api.players;

import com.levelmc.core.api.yml.Path;
import com.levelmc.core.api.yml.Skip;
import com.levelmc.core.api.yml.YamlConfig;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.joor.Reflect;

import java.util.*;

public class UserManager<T extends User> extends YamlConfig implements Listener {

    @Getter
    @Path("users")
    private Map<UUID, T> users = new HashMap<>();

    @Getter
    @Skip
    private JavaPlugin parent;

    @Skip
    private Class<? extends User> userClass;

    public UserManager(JavaPlugin parent, Class<? extends User> userClass) {
        this.parent = parent;
        this.userClass = userClass;
    }

    public UserManager() {

    }

    public boolean hasData(Player player) {
        return users.containsKey(player.getUniqueId());
    }

    public T getUser(Player player) {
        return users.get(player.getUniqueId());
    }

    public T getUser(UUID userId) {
        return users.get(userId);
    }

    public Collection<T> allUsers() {
        return users.values();
    }

    public void addUser(T user) {
        users.put(user.getId(), user);
    }

    public boolean removeUser(UUID id) {
        return users.remove(id) != null;
    }

    public boolean removeUser(Player player) {
        return removeUser(player.getUniqueId());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        addUser(e.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        removeUser(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent e) {
        removeUser(e.getPlayer().getUniqueId());
    }

    public void addUser(Player p) {
        users.put(p.getUniqueId(), Reflect.onClass(userClass).create(p).get());
    }
}
