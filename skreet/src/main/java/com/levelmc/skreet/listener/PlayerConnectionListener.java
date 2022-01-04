package com.levelmc.skreet.listener;

import com.levelmc.skreet.Skreet;
import com.levelmc.skreet.gangs.menus.GangSelectionMenu;
import com.levelmc.skreet.users.SkreetPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerConnectionListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        SkreetPlayer user = Skreet.getInstance().getUserManager().getUser(player);
        if (!user.hasGang()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    GangSelectionMenu.getInstance().openMenu(player);
                }
            }.runTaskLater(Skreet.getInstance(), 10l);
        }
    }
}
