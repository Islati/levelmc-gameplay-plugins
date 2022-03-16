package com.levelmc.skreet.commands;

import com.levelmc.core.chat.Chat;
import com.levelmc.core.chat.HelpScreen;
import com.levelmc.core.cmd.Command;
import com.levelmc.core.cmd.commands.CommandInfo;
import com.levelmc.skreet.Skreet;
import com.levelmc.skreet.gangs.Gang;
import com.levelmc.skreet.gangs.menus.GangSelectionMenu;
import com.levelmc.skreet.users.SkreetPlayer;
import org.bukkit.entity.Player;

@CommandInfo(name = "gang", aliases = "g", description = "Gang commands", usage = "/gang")
public class GangCommands {

    private HelpScreen helpMenu = new HelpScreen("Gang Commands")
            .addEntry("gang", "Base Command")
            .addEntry("gang help","Open gang help menu")
            .addEntry("gang join", "Opens the gang join menu");

    public GangCommands() {

    }

    @Command(identifier = "gang")
    public void onGangCommand(Player player) {
        SkreetPlayer user = Skreet.getInstance().getUserManager().getUser(player);
        if (user.hasGang()) {
            Gang gang = user.getGang();

            StringBuilder onlineUsersString = new StringBuilder();
            for (Player member : gang.getOnlinePlayers()) {
                onlineUsersString.append(member.getName()).append(" ");
            }
            Chat.message(player,
                    String.format("&7--------&r%s Gang&r--------", gang.getType().getDisplayName()),
                    String.format("&7Gang Kills: &r%s", gang.getGangKills().size()),
                    String.format("&7Online Members: &r%s", onlineUsersString.toString()),
                    String.format("&7Total Members: &r%s", gang.getMembersMap().size())
            );
        } else {

            helpMenu.sendTo(player, 1, 10);
        }
    }

    @Command(identifier = "gang help")
    public void onGangHelp(Player player) {
        helpMenu.sendTo(player, 1, 10);
    }

    @Command(identifier = "gang join")
    public void onGangJoinCommand(Player player) {
        SkreetPlayer user = Skreet.getInstance().getUserManager().getUser(player);

        if (user.hasGang()) {
            Chat.message(player, user.getGangType().getColorPrefix() + "You are already a gang member.");
            return;
        }

        GangSelectionMenu.getInstance().openMenu(player);

    }


}
