package com.levelmc.bounties;

import com.levelmc.bounties.bounties.Bounty;
import com.levelmc.bounties.bounties.BountyConfiguration;
import com.levelmc.bounties.bounties.BountyManager;
import com.levelmc.bounties.bounties.commands.BountyCommands;
import com.levelmc.bounties.bounties.gui.BountyMainGUI;
import com.levelmc.bounties.users.BountyUser;
import com.levelmc.bounties.users.BountyUserManager;
import com.levelmc.core.LevelCore;
import com.levelmc.core.api.gui.ItemMenu;
import com.levelmc.core.api.item.ItemBuilder;
import com.levelmc.core.api.item.SkullCreator;
import com.levelmc.core.api.utils.PluginUtils;
import com.levelmc.core.api.utils.SoundUtils;
import com.levelmc.core.api.world.WorldUtils;
import com.levelmc.core.api.yml.InvalidConfigurationException;
import com.levelmc.core.chat.Chat;
import com.levelmc.core.cmd.CommandHandler;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Bounties extends JavaPlugin implements Listener {

    private static Bounties instance = null;

    public static Bounties getInstance() {
        return instance;
    }

    private NamespacedKey keySkullOwnerUuid = null;

    @Getter
    private BountyConfiguration configuration = null;

    @Getter
    private BountyManager bountyManager = null;

    private BountyCommands commands;

    private static final String bountyConfigFileLoc = "config.yml";
    private static final String bountyManagerFileLoc = "bounties.yml";

    @Getter
    private NamespacedKey npcBountyMasterKey = null;

    @Getter
    private BountyUserManager userManager = null;

    @Getter
    private CommandHandler commandHandler = null;


    @Getter
    private Economy economy = null;

    @Override
    public void onLoad() {
        super.onLoad();
        instance = this;
    }

    @Override
    public void onDisable() {
        super.onDisable();

        try {
            bountyManager.save(new File(getDataFolder(), bountyManagerFileLoc));
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEnable() {
        instance = this;
        super.onEnable();

        commandHandler = new CommandHandler(this);

        keySkullOwnerUuid = new NamespacedKey(LevelCore.getInstance(), "skullOwnerUuid");

        configuration = new BountyConfiguration();

        bountyManager = new BountyManager();

        commands = new BountyCommands(this);

        PluginUtils.registerListeners(this, this);
        getLogger().info("Listeners registered");
        getCommandHandler().registerCommands(commands);
        getLogger().info("Command registered");

        try {
            configuration.init(new File(getDataFolder(), bountyConfigFileLoc));
            getLogger().info("Bounty configuration initialized");
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }

        try {
            bountyManager.init(new File(getDataFolder(), bountyManagerFileLoc));
            getLogger().info("Bounty Manager initialized");
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }

        npcBountyMasterKey = new NamespacedKey(this, configuration.getNpcIdKey());
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }


    private void dropSkull(Player dead) {

        /* Drop the players skull and give it a worth */
        ItemStack deadHead = SkullCreator.itemFromUuid(dead.getUniqueId());

        String playerKillerName = null;

        if (dead.getKiller() != null) {
            playerKillerName = dead.getKiller().getName();
            deadHead = ItemBuilder.clone(deadHead).lore(String.format("&cKilled By&7: &6%s", playerKillerName)).item();
        } else {
            return;
        }

        ItemMeta meta = deadHead.getItemMeta();
        meta.getPersistentDataContainer().set(keySkullOwnerUuid, PersistentDataType.STRING, dead.getUniqueId().toString());
        deadHead.setItemMeta(meta);

        Item item = WorldUtils.dropItem(dead.getLocation(), deadHead);
        item.setCustomNameVisible(true);
    }

    @EventHandler
    public void onEntityDeathEvent(EntityDeathEvent e) {
        LivingEntity entity = e.getEntity();

        if (!(entity instanceof Player)) {
            return;
        }

        Player dead = (Player) entity;
        Player killer = dead.getKiller();

        if (killer == null) {
            return;
        }

        Bounty deadBounty = bountyManager.getBounty(dead);

        if (deadBounty == null) {
            return;
        }

        if (configuration.isSkullDroppedOnDeath()) {
            dropSkull(dead);
            return;
        }

        BountyUser trapKiller = getUserManager().getUser(killer);

        trapKiller.incrementKillStreak();

        Chat.actionMessage(killer, "&6Active Kill Streak: &c" + trapKiller.getActiveKillStreak());
        if (trapKiller.getActiveKillStreak() > 0) {
            trapKiller.setKillRewardPoints(trapKiller.getKillRewardPoints() + 2);
            Chat.message(killer, "&7+2 Bounty Hunter Points");
            SoundUtils.playSound(killer, Sound.BLOCK_NOTE_BLOCK_PLING);
        }

        deadBounty.claim(killer);
        Chat.broadcast(String.format("&c%s &6fell to the Bounty Hunter &l%s", dead.getName(), killer.getName()));
        SoundUtils.playSound(killer, Sound.ENTITY_GHAST_SCREAM, 0.65f, 1.0f);
        Chat.format(killer, "&a&l+ &r&e$&a%s &7(Bounty issued by %s", deadBounty.getRewardMoney(), deadBounty.getPosterName());
    }

    @EventHandler
    public void onPlayerInteractBountyManager(PlayerInteractEntityEvent e) {
        Entity entity = e.getRightClicked();
        Player p = e.getPlayer();

        if (!(entity instanceof LivingEntity) || entity.getType() != EntityType.VILLAGER) {
            return;
        }

        LivingEntity le = (LivingEntity) entity;
        if (!le.getPersistentDataContainer().has(npcBountyMasterKey, PersistentDataType.INTEGER)) {
            return;
        }

        //todo do the thing.
        new BountyMainGUI(getBountyManager(), 1).openMenu(p);
    }

    public ItemMenu getMenu() {
        return new BountyMainGUI(getBountyManager(), 1); //todo add option in menu for admins to remove and refund bounties.
    }
}
