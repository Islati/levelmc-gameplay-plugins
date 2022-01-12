package com.levelmc.skreet;

import com.levelmc.core.api.gadgets.Gadgets;
import com.levelmc.core.api.utils.PluginUtils;
import com.levelmc.core.api.yml.InvalidConfigurationException;
import com.levelmc.core.cmd.CommandHandler;
import com.levelmc.skreet.commands.GangCommands;
import com.levelmc.skreet.gangs.GangManager;
import com.levelmc.skreet.listener.GangRelatedListener;
import com.levelmc.skreet.listener.PvpListener;
import com.levelmc.skreet.tags.TagManager;
import com.levelmc.skreet.users.SkreetPlayers;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class Skreet extends JavaPlugin {
    private static Skreet instance;

    @Getter
    @Setter
    private TagManager tagManager = null;

    @Getter
    private SkreetPlayers userManager = null;

    @Getter
    private GangManager gangManager = null;

    @Getter
    private GangRelatedListener gangListener = null;

    @Getter
    private PvpListener pvpListener = null;

    private CommandHandler commandHandler = null;


    public static Skreet getInstance() {
        return instance;
    }

    public Skreet() {
        super();
        instance = this;
    }

    protected Skreet(@NotNull JavaPluginLoader loader, @NotNull PluginDescriptionFile description, @NotNull File dataFolder, @NotNull File file) {
        super(loader, description, dataFolder, file);
        instance = this;
    }


    @Override
    public void onLoad() {
        super.onLoad();
        instance = this;
    }

    @Override
    public void onDisable() {
        super.onDisable();

        saveConfig();
        getLogger().info("Saved tags.yml, and gangs.yml");
    }

    @Override
    public void onEnable() {
        super.onEnable();
        instance = this;

        initComponents();

        try {
            initConfig();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }

        initListeners();

    }

    public void initComponents() {
        tagManager = new TagManager();
        userManager = SkreetPlayers.getInstance();
        gangManager = GangManager.get();

        commandHandler = new CommandHandler(this);
        commandHandler.registerCommands(new GangCommands());
    }

    public void initListeners() {
        gangListener = new GangRelatedListener(Skreet.getInstance());
        pvpListener = new PvpListener();
        PluginUtils.registerListeners(this, userManager, gangListener, pvpListener);

        Gadgets.init(this);
    }

    public void initConfig() throws InvalidConfigurationException {
        File tagsFile = new File(getDataFolder(), "tags.yml");
        File gangsFile = new File(getDataFolder(), "gangs.yml");

        File usersFolder = new File(getDataFolder(),"users/");
        if (!usersFolder.exists()) {
            usersFolder.mkdirs();
        }

        tagManager.init(tagsFile);
        gangManager.init(gangsFile);
    }

    public void saveConfig() {
        File tagsFile = new File(getDataFolder(), "tags.yml");
        File gangsFile = new File(getDataFolder(), "gangs.yml");

        try {
            tagManager.save(tagsFile);
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }

        try {
            gangManager.save(gangsFile);
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}
