package com.levelmc.skreet.users;

import com.levelmc.core.api.players.UserManager;
import com.levelmc.core.api.yml.InvalidConfigurationException;
import com.levelmc.skreet.Skreet;

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
}
