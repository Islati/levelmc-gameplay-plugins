package com.levelmc.loot.api;

import com.levelmc.core.api.Chanceable;
import com.levelmc.core.api.yml.Path;
import com.levelmc.core.api.yml.YamlConfig;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;

public class ChancedMaterial extends YamlConfig implements Chanceable {
    @Path("material")
    @Getter
    @Setter
    private Material material;

    @Path("chance")
    @Setter
    private int chance;

    @Path("measure")
    @Setter
    private int measure;

    public ChancedMaterial(Material material, int chance, int measure) {
        this.material = material;
        this.chance = chance;
        this.measure = measure;
    }

    public ChancedMaterial() {

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
