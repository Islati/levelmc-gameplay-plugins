package com.levelmc.wizards.commands;

import com.levelmc.core.chat.HelpScreen;
import com.levelmc.core.cmd.Arg;
import com.levelmc.core.cmd.Command;
import com.levelmc.core.cmd.commands.CommandInfo;
import com.levelmc.wizards.Wizards;
import com.levelmc.wizards.gui.PlayerWizardGUI;
import com.levelmc.wizards.gui.SpellBindsGUI;
import org.bukkit.entity.Player;

@CommandInfo(name = "magic",aliases = "m", description = "Commands to manage your magical spells")
public class MagicCommand {
    private HelpScreen helpScreen = new HelpScreen("Magic Command Help")
            .addEntry("magic","Open the overview menu.")
            .addEntry("magic help [page]","This help screen")
            .addEntry("magic binds","Manage your wands spellbinds");


    public MagicCommand() {

    }

    @Command(identifier = "magic",description = "Magic Command")
    public void onMagicCommand(Player sender) {
        new PlayerWizardGUI(Wizards.getInstance().getUserManager().getUser(sender));
    }

    @Command(identifier = "magic help",description = "Help menu for the magic command",onlyPlayers = false)
    public void onMagicHelpCommand(Player player,@Arg(def = "1", description = "Page number", name = "page") int page) {
        helpScreen.sendTo(player,page,8);
    }

    @Command(identifier = "magic binds",description = "Open the spell binds menu")
    public void onMagicBindsCommand(Player player) {
        SpellBindsGUI gui = new SpellBindsGUI(Wizards.getInstance().getUserManager().getUser(player));
        gui.openMenu(player);
    }
}
