package com.levelmc.bounties.bounties.commands;

import com.levelmc.bounties.Bounties;
import com.levelmc.core.chat.Chat;
import com.levelmc.core.chat.HelpScreen;
import com.levelmc.core.cmd.Arg;
import com.levelmc.core.cmd.Command;
import com.levelmc.core.cmd.commands.CommandInfo;
import com.levelmc.core.api.entities.CreatureBuilder;
import com.levelmc.core.api.entities.EntityUtils;
import com.levelmc.core.api.gui.*;
import com.levelmc.bounties.bounties.BountyManager;
import com.levelmc.bounties.bounties.gui.BountyMainGUI;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.util.Set;

@CommandInfo(name = "bounty", aliases = {"bo"}, description = "Bounty Hunter is a viable career choice")
public class BountyCommands {

    private HelpScreen helpMenu = new HelpScreen("Bounty Hunter Commands")
            .addEntry("bounty", "This help menu")
            .addEntry("bounty help", "This help menu")
            .addEntry("bounty place <name> <reward>", "Place a bounty on the given player")
            .addEntry("bounty add <name> <reward>", "Force a bounty to be added to a player", "trap.admin")
            .addEntry("bounty remove <name>", "Remove the active bounty on the players head", "trap.admin")
            .addEntry("bounty gui [player]", "Open the bounty hunter GUI for a specific player.")
            .addEntry("bounty npc set-spawn", "Set the Bounty Masters spawn location", "trap.admin")
            .addEntry("bounty npc respawn", "Respawn the bounty master at their location", "trap.admin");

    private Bounties core;
    private Economy eco;

    public BountyCommands(Bounties core) {
        this.eco = core.getEconomy();
    }

    @Command(identifier = "bounty", description = "Bounty hunter base command", onlyPlayers = false)
    public void onBountyBaseCommand(CommandSender player) {
        helpMenu.sendTo(player, 1, 8);
    }

    @Command(identifier = "bounty add", permissions = "trap.admin")
    public void onBountyAddCommand(Player player, @Arg(name = "name") String playerName, @Arg(name = "reward") double reward) {
        OfflinePlayer offPlayer = Bukkit.getOfflinePlayer(playerName);

        if (!offPlayer.hasPlayedBefore()) {
            Chat.message(player, "&cThey've not played on our server before");
            return;
        }

        BountyManager manager = Bounties.getInstance().getBountyManager();

        if (manager.isTarget(offPlayer.getUniqueId())) {
            Chat.message(player, "&e" + playerName + "&c already has a bounty on their head.");
            return;
        }

        if (!offPlayer.isOnline()) {
            Chat.message(player, "&e" + playerName + "&c must be online to place a bounty");
            return;
        }

        manager.addBounty(player, offPlayer.getPlayer(), reward);
        Chat.broadcast("&aBounty placed on &c" + playerName + "&a for &e$" + reward);
    }


    @Command(identifier = "bounty place")
    public void onBountyPlaceCommand(Player player, @Arg(name = "name") String playerName, @Arg(name = "reward") double reward) {
        OfflinePlayer offPlayer = Bukkit.getOfflinePlayer(playerName);

        if (!offPlayer.hasPlayedBefore()) {
            Chat.message(player, "&cThey've not played on our server before");
            return;
        }

        BountyManager manager = Bounties.getInstance().getBountyManager();

        if (manager.isTarget(offPlayer.getUniqueId())) {
            Chat.message(player, "&e" + playerName + "&c already has a bounty on their head.");
            return;
        }

        if (!offPlayer.isOnline()) {
            Chat.message(player, "&e" + playerName + "&c must be online to place a bounty");
            return;
        }

        if (!eco.has(player, reward)) {
            Chat.message(player, "&cInsufficient Funds");
            return;
        }

        Player target = offPlayer.getPlayer();

        ConfirmationMenu.of("Place Bounty on " + playerName + "?").onConfirm(new ConfirmationMenu.Action() {
            @Override
            public void perform(Menu menu, Player player) {
                EconomyResponse resp = eco.withdrawPlayer(player, reward);
                if (!resp.transactionSuccess()) {
                    Chat.message(player, "&cUnable to complete the request at this time.");
                    menu.closeMenu(player);
                    return;
                }
                manager.addBounty(player, target, reward);
                Chat.broadcast("&aBounty placed on &c" + playerName + "&a for &e$" + reward);
                menu.closeMenu(player);
            }
        }).openMenu(player);
    }

    @Command(identifier = "bounty remove", permissions = "trap.admin")
    public void onBountyRemoveCommand(Player player, @Arg(name = "target") String playerName) {
        OfflinePlayer offPlayer = Bukkit.getOfflinePlayer(playerName);

        if (!offPlayer.hasPlayedBefore()) {
            Chat.message(player, "&cThey've not played on our server before");
            return;
        }

        BountyManager manager = Bounties.getInstance().getBountyManager();

        if (!manager.isTarget(offPlayer.getUniqueId())) {
            Chat.message(player, "&e" + playerName + "&c has no bounty on their head.");
            return;
        }

        manager.removeBounty(offPlayer.getUniqueId());
    }

    @Command(identifier = "bounty npc set-spawn", permissions = "trap.admin")
    public void onBountyNpcSetSpawn(Player player) {
        //todo implement removal of previous npc
        Bounties.getInstance().getConfiguration().setNpcSpawnLocation(player.getLocation());
        Chat.message(player, "&aBounty Masters location has been updated");
    }

    @Command(identifier = "bounty npc respawn", permissions = "trap.admin")
    public void onBountyNpcRespawn(Player player) {
        Location loc = Bounties.getInstance().getConfiguration().getNpcSpawnLocation();
        String name = Bounties.getInstance().getConfiguration().getName();

        Set<LivingEntity> entities = EntityUtils.getLivingEntitiesNearLocation(loc, 3);

        for (LivingEntity entity : entities) {
            if (entity.getType() != EntityType.VILLAGER) {
                continue;
            }

            if (!entity.getPersistentDataContainer().has(Bounties.getInstance().getNpcBountyMasterKey(), PersistentDataType.INTEGER)) {
                continue;
            }

            entity.remove();
            Chat.message(player, "&7Removed previous Bounty Master");
        }

        LivingEntity mob = CreatureBuilder.of(EntityType.VILLAGER)
                .name(name)
                .spawn(loc);

        mob.getPersistentDataContainer().set(Bounties.getInstance().getNpcBountyMasterKey(), PersistentDataType.INTEGER, 1);
        mob.setCustomNameVisible(true);
        mob.setAI(false);
        Chat.message(player, Bounties.getInstance().getConfiguration().getName() + " &ahas spawned");
    }

    @Command(identifier = "bounty gui", onlyPlayers = false)
    public void onBountyGuiCommand(CommandSender player, @Arg(name = "target", def = "?sender") Player target) {
        new BountyMainGUI(Bounties.getInstance().getBountyManager(), 1).openMenu(target);
    }
}
