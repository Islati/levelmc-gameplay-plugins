package com.levelmc.loot.api;

import com.levelmc.core.api.yml.Path;
import com.levelmc.core.api.yml.YamlConfig;

public class ItemRarity extends YamlConfig implements Rarity {
    @Path("id")
    private String id;
    @Path("display-name")
    private String displayName;
    @Path("chance")
    private int chance;
    @Path("measure")
    private int measure;

    public ItemRarity(String id, String displayName, int chance, int measure) {
        this.id = id;
        this.displayName = displayName;
        this.chance = chance;
        this.measure = measure;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String displayName() {
        return displayName;
    }

    @Override
    public int getChance() {
        return chance;
    }

    @Override
    public int getMeasure() {
        return measure;
    }
}
