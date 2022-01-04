package com.levelmc.skreet.listener;

import com.levelmc.core.chat.Chat;
import com.levelmc.skreet.users.SkreetPlayer;
import com.levelmc.skreet.users.SkreetPlayers;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PvpListener implements Listener {

    @EventHandler
    public void onPlayerDamagePlayer(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity damaged = event.getEntity();

        if (!(damaged instanceof Player)) {
            return;
        }

        Player attacked = (Player) damaged;
        Player attacker = null;

        if (!(damager instanceof Player)) {
            if (damager instanceof Projectile) {
                Projectile shot = (Projectile) damager;

                if (!(shot.getShooter() instanceof Player)) {
                    return;
                }

                attacker = (Player) shot.getShooter();
            }
        }

        if (attacker == null) {
            return;
        }

        SkreetPlayer userAttacking = SkreetPlayers.getInstance().getUser(attacker);
        SkreetPlayer userAttacked = SkreetPlayers.getInstance().getUser(attacked);

        if (userAttacking.getGangType() == userAttacked.getGangType()) {
            Chat.actionMessage(attacker,"&cYou can't attack gang members.");
            event.setCancelled(true);
        }
    }
}
