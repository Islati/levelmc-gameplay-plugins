package com.levelmc.core.user;

import com.levelmc.core.api.players.User;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

public class LcUser extends User {

    @Getter
    @Setter
    private boolean debugging = false;

    public LcUser(Player player) {
        super(player);
    }
}
