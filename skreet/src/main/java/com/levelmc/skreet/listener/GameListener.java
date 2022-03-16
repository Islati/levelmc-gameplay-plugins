package com.levelmc.skreet.listener;

import com.levelmc.skreet.Skreet;
import com.levelmc.skreet.users.SkreetPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Core mechanics
 * - Hook & Extend
 */
public class GameListener implements Listener {

    private static GameListener instance = null;


    protected GameListener() {

    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        SkreetPlayer user = Skreet.getInstance().getUserManager().getUser(e.getPlayer());

        if (!user.hasGang()) {
            player.performCommand("gang join");
        }
    }
}
