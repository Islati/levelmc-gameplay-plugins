package com.levelmc.wizards.threads;

import com.levelmc.core.api.utils.PluginUtils;
import com.levelmc.wizards.Wizards;
import com.levelmc.wizards.events.ManaRegenEvent;
import com.levelmc.wizards.users.Wizard;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ManaRegenThread extends BukkitRunnable {

    public ManaRegenThread() {

    }

    @Override
    public void run() {
        StringBuilder names = new StringBuilder();

        if (Bukkit.getOnlinePlayers().size() == 0) {
            return;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {

            Wizard user = Wizards.getInstance().getUserManager().getUser(player);

            if (player.getGameMode() == GameMode.CREATIVE) {
                ManaRegenEvent event = new ManaRegenEvent(user.getPlayer(), user.getMaxMana(player));
                PluginUtils.callEvent(event);
                continue;
            }

            if (!user.isOnRegenCooldown()) {
                ManaRegenEvent event = new ManaRegenEvent(user.getPlayer(), user.getManaRegenAmount(player));
                PluginUtils.callEvent(event);
                user.setOnRegenCooldown();
            }
        }

    }
}
