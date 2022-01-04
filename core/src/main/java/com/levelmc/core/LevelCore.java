package com.levelmc.core;

import com.google.common.collect.Lists;
import com.levelmc.core.api.gui.MenuInventoryListener;
import com.levelmc.core.cmd.CommandHandler;
import com.levelmc.core.api.debug.actions.*;
import com.levelmc.core.api.events.armor.ArmorListener;
import com.levelmc.core.api.events.armor.DispenserArmorListener;
import com.levelmc.core.config.ArmorEquipEventConfig;
import com.levelmc.core.config.LevelCoreConfig;
import com.levelmc.core.user.LcUserManager;
import lombok.Getter;
import com.levelmc.core.api.ApiMessages;
import com.levelmc.core.cmd.commands.DebuggerCommand;
import com.levelmc.core.api.debug.Debugger;
import com.levelmc.core.api.gadgets.GadgetListener;
import com.levelmc.core.api.utils.PluginUtils;
import com.levelmc.core.api.yml.InvalidConfigurationException;


import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;

/*
todo: implement support for multiple versions like 1.8, and such via reflection.
 */

public class LevelCore extends JavaPlugin {

    @Getter
    private static LevelCore instance;

    /* Provides commands */
    private CommandHandler commandHandler = null;

    /* Provides GUI menus with bound actions */
    private MenuInventoryListener guiListener = null;

    /* Provides items with bound actions (aka gadgets) */
    private GadgetListener gadgetListener = null;

    /* Provides ArmorEquipEvent */
    private static String armorEquipEventConfigLoc = "api/armor-equip-event.yml";
    private static String apiConfigLoc = "api/";

    private ArmorEquipEventConfig armorEquipEventConfig = null;
    private ArmorListener armorListener = null;
    private DispenserArmorListener dispenserArmorListener = null;
//
//    @Getter
//    private Permission perms = null;

    private static final String msgConfigLoc = "messages.yml";

    private static final String coreConfigLoc = "config.yml";

    @Getter
    private LevelCoreConfig coreConfig = null;

    @Getter
    private LcUserManager userManager = null;

    public LevelCore() {
        super();
    }

    public LevelCore(JavaPluginLoader loader, PluginDescriptionFile desc, File dataFolder, File file) {
        super(loader, desc, dataFolder, file);
    }

    @Override
    public void onEnable() {
        instance = this;

        /* Comes before the rest as our components depend on this */
        initializeApi();
    }

    @Override
    public void onDisable() {
        PluginUtils.unregisterHooks(this);
    }

    private void initializeApi() {
        initApiConfig();

        userManager = new LcUserManager();
        PluginUtils.registerListeners(this, userManager);


        PluginUtils.registerListeners(
                this,
                guiListener = new MenuInventoryListener(this),
                gadgetListener = new GadgetListener(this),
                armorListener = new ArmorListener(Lists.newArrayList(armorEquipEventConfig.getBlockedMaterials())),
                dispenserArmorListener = new DispenserArmorListener()
        );

        commandHandler = new CommandHandler(this);
        commandHandler.registerCommands(new DebuggerCommand());

//        if (!setupPermissions()) {
//            getLogger().info("Proceeding without Vault API");
//        } else {
//            Hooks.VAULT = true; //update hook.
//            getLogger().info("Setup Vault API");
//        }

        Debugger.get().registerDebugActions(
                DebugItemMenu.getInstance(),
                DebugTestGadget.getInstance(),
                DebugSkullCreator.getInstance(),
                DebugGadgetsMenu.getInstance(),
                DebugInventoryHook.getInstance()
        );

    }

    private void initApiConfig() {
        File msgConfig = new File(getDataFolder(), msgConfigLoc);
        File coreConfig = new File(getDataFolder(), coreConfigLoc);
        File armorEventConfig = new File(getDataFolder(), armorEquipEventConfigLoc);

        File apiConfigFolder = new File(getDataFolder(), apiConfigLoc);

        if (!apiConfigFolder.exists()) {
            if (apiConfigFolder.mkdirs()) {
                getLogger().info("Created api data folder @ " + apiConfigLoc);
            }
        }

        boolean armorConfigExists = armorEventConfig.exists();

        armorEquipEventConfig = new ArmorEquipEventConfig();
        try {
            armorEquipEventConfig.init(armorEventConfig);
            if (!armorConfigExists) {
                getLogger().info("Initialized default ArmorEquipEvent configuration @ " + armorEquipEventConfigLoc);
            } else {
                getLogger().info("Loaded ArmorEquipEvent configuration");
            }
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }

        try {
            ApiMessages.getInstance().init(msgConfig);

            this.coreConfig = new LevelCoreConfig();
            this.coreConfig.init(coreConfig);
            getLogger().info("Core Config & Msg Config Initialized");
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

//    private boolean setupPermissions() {
//        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
//        perms = rsp.getProvider();
//        return perms != null;
//    }

    public static class Hooks {
        /**
         * Whether or not vault has been detected on the server.
         */
        public static boolean VAULT = false;
    }

}
