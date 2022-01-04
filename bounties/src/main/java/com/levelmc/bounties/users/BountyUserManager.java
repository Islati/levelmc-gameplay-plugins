package com.levelmc.bounties.users;

import com.levelmc.bounties.Bounties;
import com.levelmc.core.api.players.User;
import com.levelmc.core.api.players.UserManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BountyUserManager extends UserManager<BountyUser> {
    public BountyUserManager() {
        super(Bounties.getInstance(),BountyUser.class);
    }
}
