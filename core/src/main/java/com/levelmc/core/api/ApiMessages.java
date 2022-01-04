package com.levelmc.core.api;

import com.levelmc.core.LevelCore;
import com.levelmc.core.api.yml.Path;
import com.levelmc.core.api.yml.SerializeOptions;
import com.levelmc.core.api.yml.Skip;
import com.levelmc.core.api.yml.YamlConfig;

import java.util.HashMap;
import java.util.Map;

@SerializeOptions(configHeader = {"All messages for the core can be customized in this config"})
public class ApiMessages extends YamlConfig {

    @Skip
    private static ApiMessages instance = null;

    public static String get(String key) {
        return getInstance()._get(key);
    }

    public static String getFormatted(String key, Object... args) {
        return String.format(get(key), args);
    }

    public static ApiMessages getInstance() {
        if (instance == null) {
            instance = new ApiMessages(LevelCore.getInstance());
        }

        return instance;
    }

    @Skip
    private LevelCore parent = null;

    @Path("messages")
    private Map<String, String> coreMessages = new HashMap<>();

    public ApiMessages(LevelCore plugin) {
        parent = plugin;
        coreMessages.put("player-only-command", "You cannot use this command as console.");
        coreMessages.put("world-not-found", "World '%s' was not found");
        coreMessages.put("main-hand-item-required", "This requires you have an item in your main hand");
        coreMessages.put("off-hand-item-required", "This requires you have an item in your off-hand");
        coreMessages.put("debug-id-invalid", "Invalid id provided");
        coreMessages.put("debug-prefix", "&7&l[&bDebug&7&l]");
        coreMessages.put("debug-enabled", "&7► &l&6Debug Mode Enabled");
        coreMessages.put("debug-disabled", "&7► &l&6Debug Mode Disabled");
        coreMessages.put("skill-point-gained", "&6&l✪ &a&lGained %s %s Skill Point");
        coreMessages.put("skill-exp-gained", "&a&l✚ %s %s XP");
        coreMessages.put("skill-point-assign-confirm", "&a&l✚ &6✪ &ato %s?");
        coreMessages.put("skill-point-menu-level-desc", "&6Lvl ✦ &c{skill-level}&7/&c{max-level} &6✦");
        coreMessages.put("skill-point-menu-pre-req-for-many", "&7&o(Pre-Req for many other perks)");
        coreMessages.put("skill-point-unavailable", "&c&l&6✪&c&lNo skill points available &6✪");
        coreMessages.put("skill-locked","&7&lLocked (&r&7Use &6✪ &7to Unlock)");
        coreMessages.put("skill-leveled","&a&lLevel Up &r&7► &r&b%s &7(%s)");
        coreMessages.put("perk-cannot-level", "&cUnable to level the perk %s.");
        coreMessages.put("perk-activated", "&a&lActivated Perk&7: &6%s");
        coreMessages.put("perk-deactivated", "&e&lDeactivated Perk: &6%s");
        coreMessages.put("perk-is-toggleable", "&7(&oToggleable with &r&l&7Right Click&r&7)");
    }

    protected String _get(String key) {
        return coreMessages.get(key);
    }

}
