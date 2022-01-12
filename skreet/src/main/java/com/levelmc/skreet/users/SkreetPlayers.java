package com.levelmc.skreet.users;

import com.levelmc.core.api.players.UserManager;
import com.levelmc.core.api.yml.InvalidConfigurationException;
import com.levelmc.skreet.Skreet;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;
import java.util.UUID;

public class SkreetPlayers extends UserManager<SkreetPlayer> {
    private static SkreetPlayers instance = null;

    public static SkreetPlayers getInstance() {
        if (instance == null) {
            instance = new SkreetPlayers();
        }
        return instance;
    }

    public SkreetPlayers() {
        super(Skreet.getInstance(), SkreetPlayer.class);
    }

    private void initUserFile(SkreetPlayer user) {
        File userFile = new File(getParent().getDataFolder(), String.format("users/%s.yml", user.getId().toString()));
        if (!userFile.exists()) {
            try {
                user.init(userFile);
            } catch (InvalidConfigurationException e) {
                e.printStackTrace();
            }
        } else {
            try {
                user.load(userFile);
            } catch (InvalidConfigurationException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void addUser(SkreetPlayer user) {
        initUserFile(user);
        getUsers().put(user.getId(), user);
    }

    @Override
    public boolean removeUser(UUID id) {
        SkreetPlayer user = getUser(id);
        File userFile = new File(getParent().getDataFolder(), String.format("users/%s.yml", id.toString()));

        try {
            user.save(userFile);
            getUsers().remove(id);
            return true;
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent e) {
        addUser(new SkreetPlayer(e.getPlayer()));
    }

    @Override
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuit(PlayerQuitEvent e) {
        removeUser(e.getPlayer().getUniqueId());
    }

    @Override
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerKick(PlayerKickEvent e) {
        removeUser(e.getPlayer().getUniqueId());
    }
}
