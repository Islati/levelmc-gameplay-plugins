package com.levelmc.wizards.commands;

import com.levelmc.core.chat.Chat;
import com.levelmc.core.chat.HelpScreen;
import com.levelmc.core.cmd.Arg;
import com.levelmc.core.cmd.Command;
import com.levelmc.core.cmd.commands.CommandInfo;
import com.levelmc.core.api.players.PlayerUtils;
import com.levelmc.loot.Loot;
import com.levelmc.wizards.Wizards;
import com.levelmc.wizards.gui.SpellBindsGUI;
import com.levelmc.wizards.spells.Spell;
import com.levelmc.wizards.spells.SpellManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandInfo(name = "wizadmin", aliases = {"wa"})
public class WizAdminCommand {

    private Wizards core;

    private HelpScreen help = new HelpScreen("Wiz Admin Commands")
            .addEntry("wizadmin <page>", "Open the help screen")
            .addEntry("wizadmin binds", "Binds Menu (Temp)")
            .addEntry("wizadmin gentome <spell> <level>", "Generate a tome");

    public WizAdminCommand(Wizards core) {
        this.core = core;
    }

    @Command(identifier = "wizadmin", permissions = "levelcore.admin",onlyPlayers = false)
    public void onWizAdminCommand(CommandSender player, @Arg(name = "page", def = "1") int page) {
        help.sendTo(player, page, 8);
    }

    @Command(identifier = "wizadmin binds", permissions = "levelcore.admin",onlyPlayers = false)
    public void onWizAdminBindsCommand(CommandSender sender, @Arg(name="player")Player player) {
        SpellBindsGUI gui = new SpellBindsGUI(core.getUserManager().getUser(player));
        gui.openMenu(player);
    }

    @Command(identifier = "wizadmin gentome", permissions = "levelcore.admin")
    public void onWizAdminGenTome(Player player, @Arg(name = "spell") String spellId, @Arg(name = "level") int level) {
        SpellManager spellManager = core.getSpellManager();

        Spell spell = spellManager.getSpell(spellId);

        if (spell == null) {
            Chat.format(player, "&cThe spell '&e%s&c' could not be found", spellId);
            return;
        }

        ItemStack tomeItem = core.getWizardItemUtils().createTome(player, spell, level);
        PlayerUtils.giveItem(player, tomeItem);
        Chat.format(player, "&aYou have received: &r" + tomeItem.getItemMeta().getDisplayName());
    }

    @Command(identifier = "wizadmin genwand", permissions = "levelcore.admin")
    public void onWizAdminGenWand(Player player) {
        ItemStack item = Loot.getInstance().getRegistry().getTable("wands").createItem();
        PlayerUtils.giveItem(player, item);
        Chat.actionMessage(player, "&6Enjoy this new wand.");
    }

    @Command(identifier = "wizadmin spells",permissions="levelcore.admin")
    public void onWizadminSpellsCommand(Player player) {
        Chat.msg(player,"&6Magical Spells Available&7:");
        for(Spell spell : core.getSpellManager().getSpells().values()) {
            Chat.msg(player, String.format("&7- &b%s", spell.id()));
        }
    }
}
