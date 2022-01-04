package com.levelmc.loot;

import com.levelmc.core.api.gadgets.Gadgets;
import com.levelmc.core.api.utils.PluginUtils;
import com.levelmc.core.api.yml.InvalidConfigurationException;
import com.levelmc.core.cmd.CommandHandler;
import com.levelmc.loot.api.LootRegistry;
import com.levelmc.loot.api.MobDropListener;
import com.levelmc.loot.api.abilities.Abilities;
import com.levelmc.loot.api.abilities.AbilityListener;
import com.levelmc.loot.commands.LootCommand;
import com.levelmc.loot.api.repairing.ItemRepairListener;
import com.levelmc.loot.api.repairing.ItemRepairScroll;
import lombok.Getter;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import com.levelmc.loot.api.levels.*;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class Loot extends JavaPlugin {

    private static Loot instance = null;

    public static Loot getInstance() {
        return instance;
    }

    private CommandHandler commandHandler;

    @Getter
    private LootRegistry registry = new LootRegistry();

    @Getter
    private String registryFileLocation = "loot.yml";

    @Getter
    private String levelManagerFileLocation = "levels.yml";

    @Getter
    private ItemLevelManager levelManager;

    public Loot() {
        super();
        instance = this;
    }

    protected Loot(@NotNull JavaPluginLoader loader, @NotNull PluginDescriptionFile description, @NotNull File dataFolder, @NotNull File file) {
        super(loader, description, dataFolder, file);
        instance = this;
    }

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        commandHandler = new CommandHandler(this);

        commandHandler.registerCommands(new LootCommand(this));
        getLogger().info("Registered /loot command");

        Gadgets.registerGadget(this, ItemRepairScroll.getInstance());
        getLogger().info("Registered Item Repair Scroll (Gadget)");


        getLogger().info("Initializing LootRegistry");

        File registryFile = new File(getDataFolder(),registryFileLocation);

        if (!registryFile.exists()) {
            try {
                registry.init(registryFile);
                getLogger().info("Created " + registryFile.getAbsolutePath() + " with default values");
            } catch (InvalidConfigurationException e) {
                e.printStackTrace();
                registryFile.delete();
            }
        } else {
            try {
                registry.init(registryFile);
                getLogger().info("Initialized " + registryFile.getAbsolutePath() + " and loaded data");
            } catch (InvalidConfigurationException e) {
                e.printStackTrace();
                registryFile.delete();
            }
        }

        File levelManagerFile = new File(getDataFolder(),levelManagerFileLocation);

        levelManager = new ItemLevelManager(this);

        if (!levelManagerFile.exists()) {
            try {
                levelManager.init(levelManagerFile);
                getLogger().info("Created item level manager file @ " + levelManagerFileLocation);
            } catch (InvalidConfigurationException e) {
                e.printStackTrace();
            }
        } else {

            try {
                levelManager.init(levelManagerFile);
                getLogger().info("Initialized item level manager file @ " + levelManagerFileLocation);
            } catch (InvalidConfigurationException e) {
                e.printStackTrace();
            }
        }

        loadAbilities();
        PluginUtils.registerListeners(this, new AbilityListener(this), new ItemLevelListener(this), new ItemRepairListener(this));

    }

    protected void loadAbilities() {
        Abilities.registerAbilities(
                Abilities.BLEED,
                Abilities.INCREASED_DAMAGE,
                Abilities.WEAKNESS,
                Abilities.VAMPIRISM
        );
    }
}
