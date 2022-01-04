package com.levelmc.skills;

import com.levelmc.core.api.utils.PluginUtils;
import com.levelmc.core.api.yml.InvalidConfigurationException;
import com.levelmc.core.cmd.CommandHandler;
import com.levelmc.skills.command.SkillsCommand;
import com.levelmc.skills.config.SkillsConfiguration;
import com.levelmc.skills.perks.SkillPerk;
import com.levelmc.skills.perks.mining.MiningMasteryPerk;
import com.levelmc.skills.perks.woodcutting.TreeFell;
import com.levelmc.skills.perks.woodcutting.WoodChipper;
import com.levelmc.skills.users.SkillUserManager;
import lombok.Getter;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Skills extends JavaPlugin {
    private static Skills instance = null;

    public static Skills getInstance() {
        if (instance == null) {
            instance = new Skills();
        }

        return instance;
    }

    private CommandHandler commandHandler;

    /* Registered perks */
    private LinkedHashMap<String, SkillPerk> registeredPerks = new LinkedHashMap<>();

    @Getter
    private SkillsListener skillsListener = null;

    @Getter
    private SkillsConfiguration skillsConfig = null;

    @Getter
    private SkillUserManager userManager = null;

    public Skills() {
        super();
        instance = this;
    }

    public Skills(@NotNull JavaPluginLoader loader, @NotNull PluginDescriptionFile description, @NotNull File dataFolder, @NotNull File file) {
        super(loader, description, dataFolder, file);
        instance = this;
    }

    @Override
    public void onLoad() {
        super.onLoad();

        instance = this;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        commandHandler = new CommandHandler(this);


        skillsConfig = new SkillsConfiguration();

        userManager = new SkillUserManager();
        PluginUtils.registerListeners(this,userManager);
        getLogger().info("User Manager Registered");

        try {
            skillsConfig.init(new File(getDataFolder(),"skills-config.yml"));
            getLogger().info("Initialized skills-config.yml");
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
            return;
        }

        getLogger().info("  - Mining Config - ");
        for (Map.Entry<String, Integer> entry : skillsConfig.getMiningConfig().getExpRewards().entrySet()) {
            getLogger().info(String.format("    * %s %s xp", entry.getKey(), entry.getValue()));
        }

        getLogger().info("  - WoodCutting Config - ");
        for (Map.Entry<String, Integer> entry : skillsConfig.getWoodcuttingConfiguration().getExpRewards().entrySet()) {
            getLogger().info(String.format("    * %s %s xp", entry.getKey(), entry.getValue()));
        }

        skillsListener = new SkillsListener(this);
        PluginUtils.registerListeners(this, skillsListener);
        getLogger().info("Skills Listener Registered");

        commandHandler.registerCommand(new SkillsCommand());
        getLogger().info("Registered skills command");

        getLogger().info("Registering Perks");

        registerPerk(
                MiningMasteryPerk.getInstance(),
                TreeFell.getInstance(),
                WoodChipper.getInstance()
        );

    }

    @Override
    public void onDisable() {
        super.onDisable();
    }


    public boolean hasPerks(SkillType type) {
        boolean hasPerk = false;

        for (SkillPerk perk : registeredPerks.values()) {
            if (perk.getSkillType() == type) {
                hasPerk = true;
                break;
            }
        }

        return hasPerk;
    }

    public List<SkillPerk> getPerks(SkillType type) {
        if (!hasPerks(type)) {
            return null;
        }

        List<SkillPerk> perks = new ArrayList<>();

        for (Map.Entry<String, SkillPerk> entry : this.registeredPerks.entrySet()) {
            SkillPerk perk = entry.getValue();

            if (perk.getSkillType() != type) {
                continue;
            }

            perks.add(perk);
        }

        return perks;
    }

    public SkillPerk getPerk(String id) {
        return registeredPerks.get(id.toLowerCase());
    }

    public void registerPerk(SkillPerk... perks) {
        for (SkillPerk perk : perks) {
            registeredPerks.put(perk.getPerkId().toLowerCase(), perk);
            getLogger().info(String.format("Registered perk %s (%s)", perk.getPerkName(), perk.getPerkId()));
        }
    }
}
