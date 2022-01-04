package com.levelmc.core.api.debug.actions;

import com.levelmc.core.api.debug.DebugAction;
import com.levelmc.core.api.item.SkullCreator;
import com.levelmc.core.api.players.PlayerUtils;
import com.levelmc.core.chat.Chat;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DebugSkullCreator extends DebugAction {

    private static DebugSkullCreator instance = null;

    public static DebugSkullCreator getInstance() {
        if (instance == null) {
            instance = new DebugSkullCreator();
        }

        return instance;
    }

    protected DebugSkullCreator() {
        super("tc-skull-creator");
    }

    @Override
    public void onDebug(Player player, String[] args) {
        if (args.length == 0) {
            Chat.message(player,"&cYou must include a players name.");
            return;
        }

        String name = args[0];

        ItemStack skull = SkullCreator.itemFromName(name);
        PlayerUtils.giveItem(player,skull);
        Chat.msg(player,"&cYou now have the skull of &7: " + name);
    }
}
