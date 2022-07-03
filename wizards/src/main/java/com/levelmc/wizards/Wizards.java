package com.levelmc.wizards;

import com.levelmc.core.api.utils.PluginUtils;
import com.levelmc.core.api.yml.InvalidConfigurationException;
import com.levelmc.core.cmd.CommandHandler;
import com.levelmc.loot.Loot;
import com.levelmc.loot.api.ChancedMaterial;
import com.levelmc.loot.api.DropTable;
import com.levelmc.loot.api.Name;
import com.levelmc.loot.api.abilities.AbilityProperties;
import com.levelmc.wizards.abilities.MagicWandAbility;
import com.levelmc.wizards.commands.MagicCommand;
import com.levelmc.wizards.commands.WizAdminCommand;
import com.levelmc.wizards.config.MenuConfig;
import com.levelmc.wizards.spells.*;
import com.levelmc.wizards.users.WizardsUserManager;
import com.levelmc.wizards.listeners.WizardingListener;
import com.levelmc.wizards.threads.ManaRegenThread;
import com.levelmc.wizards.utils.WizardItemUtils;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Players are able to unlock magical spells and bind them to wands via the use of tomes.
 * <p>
 * Tomes are created, earned, and dropped through general gameplay.
 * <p>
 * A players spell level for each spell can be upgraded by using a higher level tome, teaching them higher level magic.
 */

public class Wizards extends JavaPlugin {

    private static Wizards instance = null;

    public static Wizards getInstance() {
        return instance;
    }

    private CommandHandler commandHandler;

    @Getter
    private MenuConfig menuConfig = new MenuConfig();

    @Getter
    private SpellManager spellManager;

    private WizardingListener listener = null;

    @Getter
    private WizardItemUtils wizardItemUtils;

    private ManaRegenThread manaRegenThread = null;

    @Getter
    private WizardsUserManager userManager = null;

    @Getter
    private DropTable wandsDropTable = new DropTable("wands",100,100);

    public Wizards() {
        super();
        instance = this;
    }

    public Wizards(@NotNull JavaPluginLoader loader, @NotNull PluginDescriptionFile description, @NotNull File dataFolder, @NotNull File file) {
        super(loader, description, dataFolder, file);
        instance = this;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        instance = this;
    }

    /**
     * Initialize the configuration files for this plugin.
     */
    public void initConfig() {
        try {
            menuConfig.init(new File(getDataFolder(),"menu-config.yml"));
        } catch (InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onEnable() {
        instance = this;

        commandHandler = new CommandHandler(this);
        commandHandler.registerCommands(
                new WizAdminCommand(this),
                new MagicCommand()
        );
        spellManager = new SpellManager(this);
        listener = new WizardingListener(this, userManager);
        wizardItemUtils = new WizardItemUtils(this);
        manaRegenThread = new ManaRegenThread();

        /*
        Creates the wands drop table and registers it with the LootRegistry (API)
         */
        initDropTable();

        //todo implement javascript engine with context to create spells

        spellManager.registerSpells(
                new SpellFlameCircle(),
                new SpellFireBreath(),
                new SpellHealingPool(),
                new SpellFlourish(),
                new SpellVampireBites(),
                new SpellMarkForDeath(),
                new SpellIceBeam(),
                new SpellSwarm()
        );

        PluginUtils.registerListeners(this, listener);

        manaRegenThread.runTaskTimer(this,20L,10L);
        getLogger().info("Setup & Mana Regen Task Enabled");
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    private void initDropTable() {

        Loot.getInstance().getRegistry().addDropTable(wandsDropTable);
    }
}
