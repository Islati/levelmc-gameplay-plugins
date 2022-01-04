package com.levelmc.loot.api.abilities;

import com.levelmc.core.api.yml.Path;
import com.levelmc.core.api.yml.Skip;
import com.levelmc.core.api.yml.YamlConfig;

import java.util.HashMap;
import java.util.Map;

public class AbilityProperties extends YamlConfig {

    @Skip
    public static final String DAMAGE_MIN = "damage_min";
    @Skip
    public static final String DAMAGE_MAX = "damage_max";
    @Skip
    public static final String CHANCE = "chance";
    @Skip
    public static final String TIME_MIN = "time_min";

    @Skip
    public static final String TIME_MAX = "time_max";

    @Path("properties")
    private Map<String, String> values = new HashMap<>();

    public static AbilityProperties create() {
        return new AbilityProperties();
    }

    public AbilityProperties() {

    }

    public AbilityProperties set(String key, String value) {
        this.values.put(key, value);
        return this;
    }

    public boolean has(String key) {
        return values.containsKey(key);
    }

    public String get(String key, String defaultValue) {
        if (!has(key)) {
            set(key,defaultValue);
            return defaultValue;
        }
        return values.get(key);
    }
}
