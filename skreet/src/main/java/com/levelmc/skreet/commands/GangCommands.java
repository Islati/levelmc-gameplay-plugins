package com.levelmc.skreet.commands;

import com.levelmc.core.chat.HelpScreen;
import com.levelmc.core.cmd.Command;
import com.levelmc.core.cmd.commands.CommandInfo;
import com.levelmc.skreet.gangs.menus.GangSelectionMenu;
import org.bukkit.entity.Player;

@CommandInfo(name = "gang", aliases = "g", description = "Gang commands", usage = "/gang")
public class GangCommands {

    private HelpScreen helpMenu = new HelpScreen("Gang Commands")
            .addEntry("gang", "Base Command")
            .addEntry("gang join", "Opens the gang join menu");

    public GangCommands() {

    }

    @Command(identifier = "gang")
    public void onGangCommand(Player player) {
        helpMenu.sendTo(player, 1, 10);
    }

    @Command(identifier = "gang join")
    public void onGangJoinCommand(Player player) {
        GangSelectionMenu.getInstance().openMenu(player);
    }


}
