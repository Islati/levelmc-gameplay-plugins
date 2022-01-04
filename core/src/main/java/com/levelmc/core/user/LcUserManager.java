package com.levelmc.core.user;

import com.levelmc.core.LevelCore;
import com.levelmc.core.api.players.UserManager;
import org.bukkit.entity.Player;

public class LcUserManager extends UserManager<LcUser> {
    public LcUserManager() {
        super(LevelCore.getInstance(), LcUser.class);
    }

    @Override
    public void addUser(Player p) {
        addUser(new LcUser(p));
    }
}
