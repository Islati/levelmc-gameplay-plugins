package com.levelmc.loot.commands;

import com.levelmc.core.api.inventory.HandSlot;
import com.levelmc.core.api.players.PlayerUtils;
import com.levelmc.core.chat.Chat;
import com.levelmc.core.chat.HelpScreen;
import com.levelmc.core.cmd.Arg;
import com.levelmc.core.cmd.Command;
import com.levelmc.core.cmd.commands.CommandInfo;
import com.levelmc.loot.Loot;
import com.levelmc.loot.api.DropTable;
import com.levelmc.loot.api.abilities.Abilities;
import com.levelmc.loot.api.abilities.Ability;
import com.levelmc.loot.api.abilities.AbilityProperties;
import com.levelmc.loot.api.levels.ItemLevelManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

@CommandInfo(name = "loot", aliases = "lt", description = "Perform actions with Loot")
public class LootCommand {

    private Loot core;

    private HelpScreen help = new HelpScreen("Loot Command Help")
            .addEntry("loot", "Base command")
            .addEntry("loot gen <table-id>", "Create an item from a specific loot table")
            .addEntry("loot apply-ability <ability>", "Apply an in-game ability to the weapon")
            .addEntry("loot set-level <level>", "Set your held items level to the arg provided")
            .addEntry("loot tables", "View a list of loot tabels");

    public LootCommand(Loot core) {
        this.core = core;
    }

    @Command(identifier = "loot", description = "Loot base command", permissions = "levelcore.admin")
    public void onLootCommand(CommandSender sender) {
        help.sendTo(sender, 1, 9);
    }

    @Command(identifier = "loot tables", description = "View loot tables")
    public void onLootTables(Player player) {
        List<String> tables = core.getRegistry().getDropTables().stream().map(DropTable::getId).collect(Collectors.toList());

        Chat.msg(player, "&7==== &bLoot Tables&7 ====");
        for (String id : tables) {
            Chat.format(player, "&7- &b" + id);
        }
    }

    @Command(identifier = "loot apply-ability", permissions = "levelcore.admin")
    public void onLootApplyAbilityCommand(Player player, @Arg(name = "effectId") String abilityId) {
        Ability ability = Abilities.getById(abilityId);

        if (ability == null) {
            StringBuilder builder = new StringBuilder("&a- &7");

            for (Ability able : Abilities.getRegisteredAbilities().values()) {
                builder.append(able.getId()).append(",");
            }
            Chat.message(player, "No ability with that id. Available choices are :", builder.toString());
            return;
        }

        ItemStack handItem = PlayerUtils.getItemInHand(player, HandSlot.MAIN_HAND);

        if (handItem == null) {
            Chat.message(player, "&cAn item is required to be held.");
            return;
        }

        ability.apply(handItem, new AbilityProperties());
        Abilities.updateAbilities(handItem);
        core.getLevelManager().addItemExperience(player, handItem, 0);
        Chat.message(player, "&7(&a!&7) &eAbility &6" + abilityId + "&e has been applied to your held item.");
    }

    @Command(identifier = "loot set-level", onlyPlayers = true, permissions = "levelcore.admin", description = "Set the level of your held item to the variable provided when executing this command")
    public void onLootSetLevelCommand(Player player, @Arg(name = "level", verifiers = "max[200]") int level) {
        if (PlayerUtils.handIsEmpty(player, HandSlot.MAIN_HAND)) {
            Chat.message(player, "&cThis requires you have a command in your hand.");
            return;
        }

        ItemStack handItem = PlayerUtils.getItemInHand(player, HandSlot.MAIN_HAND);

        ItemLevelManager manager = core.getLevelManager();

        manager.setLevel(handItem, level);
        manager.addItemExperience(player, handItem, 0); //temp workaround for changing item level -- forces re-render.

        Chat.message(player, String.format("&7(&a!&7) &eSet your hand item to level &6%s", level));
    }

    @Command(identifier = "loot gen", description = "Generate loot")
    public void onLootGenCommand(Player player, @Arg(name = "table") String tableId, @Arg(name = "amount", def = "1") int amount, @Arg(name = "target", def = "?sender") Player target) {
        DropTable dropTable = core.getRegistry().getTable(tableId);

        if (dropTable == null) {
            Chat.msg(player, "&cUnable to find table with id&7: &o" + tableId);
            return;
        }

        for (int i = 0; i < amount; i++) {
            ItemStack item = dropTable.createItem();
            PlayerUtils.giveItem(target, item);
            if (target.getUniqueId().equals(player.getUniqueId())) {
                Chat.msg(player, "&l&a+ Item Generated &7(&b" + tableId + "&7)");
            } else {
                Chat.msg(player, String.format("&l&a+ Received Item &e'&r%s&r&e'", item.getItemMeta().getDisplayName()));
            }
        }

    }

}
