package com.levelmc.skreet.listener;

import com.levelmc.core.api.utils.PluginUtils;
import com.levelmc.core.chat.Chat;
import com.levelmc.skreet.Skreet;
import com.levelmc.skreet.events.PlayerJoinGangEvent;
import com.levelmc.skreet.events.PlayerTryQuitGangEvent;
import com.levelmc.skreet.gangs.Gang;
import com.levelmc.skreet.users.SkreetPlayer;
import com.nametagedit.plugin.NametagEdit;
import com.nametagedit.plugin.api.NametagAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Set;

/***
 * Handles all the gang related events.
 *
 * Quitting, Joining, etc.
 */
public class GangRelatedListener implements Listener {

    private Skreet parent;

    public GangRelatedListener(Skreet parent) {
        this.parent = parent;
    }

    @EventHandler
    public void onPlayerTryQuitGang(PlayerTryQuitGangEvent event) {
        SkreetPlayer player = event.getGamePlayer();
        Chat.broadcast(String.format("&e&l%s&r&7 tried to betray the &r&l%s", player.getName(), player.getGang().getDisplayName()));
    }

    @EventHandler
    public void onPlayerJoinGangEvent(PlayerJoinGangEvent event) {
        Gang gang = event.getGang();
        Player player = event.getPlayer();

        final SkreetPlayer user = event.getGamePlayer();
        Chat.broadcast(String.format("%s%s has joined the %s", gang.getType().getColorPrefix(), user.getName(), gang.getDisplayName()));

        /*
        If the player tries to join a gang that isn't theirs.
        */

        if (!gang.isMember(player.getUniqueId()) && user.hasGang()) {
            Chat.sendTitle(player, "", String.format("&7You'd betray the %s&7?", user.getGang().getDisplayName()), 10, 20, 10);
            PlayerTryQuitGangEvent gangQuitEvent = new PlayerTryQuitGangEvent(player, gang.getType());
            PluginUtils.callEvent(gangQuitEvent);
            return;
        }

        Set<Player> onlineGangMembers = gang.getOnlinePlayers();
        onlineGangMembers.add(player);
        for (Player p : onlineGangMembers) {
            Chat.sendTitle(player, gang.getDisplayName(), String.format("&7%s joined the gang", p.getName()), 10, 15, 15);
        }
    }
}
