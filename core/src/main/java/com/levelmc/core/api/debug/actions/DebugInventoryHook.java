package com.levelmc.core.api.debug.actions;

import com.levelmc.core.LevelCore;
import com.levelmc.core.api.debug.DebugAction;
import com.levelmc.core.api.utils.PluginUtils;
import com.levelmc.core.chat.Chat;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class DebugInventoryHook extends DebugAction implements Listener {
    private static Set<UUID> playersDebuggingInventories = new HashSet<>();

    private static DebugInventoryHook instance = null;

    public static DebugInventoryHook getInstance() {
        if (instance == null) {
            instance = new DebugInventoryHook();
        }

        return instance;
    }

    public DebugInventoryHook() {
        super("inv-hook");
    }

    @Override
    public void onDebug(Player player, String[] args) {
        if (playersDebuggingInventories.contains(player.getUniqueId())) {
            playersDebuggingInventories.remove(player.getUniqueId());
            Chat.message(player, "&7No longer debugging inventory actions");
            return;
        }


        playersDebuggingInventories.add(player.getUniqueId());
        Chat.message(player, "&aDebugging inventory actions");
    }

    @Override
    public void onEnable() {
        PluginUtils.registerListeners(LevelCore.getInstance(),this);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        HumanEntity entity = e.getWhoClicked();

        if (!(entity instanceof Player)) {
            return;
        }

        Player p = (Player)entity;
        if (playersDebuggingInventories.contains(p.getUniqueId())) {
            Chat.actionMessage(p, String.format("Slot %s // Raw Slot %s", e.getSlot(),e.getRawSlot()));
            Chat.message(p, String.format("Slot %s // Raw Slot %s", e.getSlot(),e.getRawSlot()));
            //todo implement as option to see in chat / action bar.
        }
    }
}
