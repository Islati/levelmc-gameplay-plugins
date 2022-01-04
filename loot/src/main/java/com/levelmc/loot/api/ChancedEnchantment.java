package com.levelmc.loot.api;

import com.levelmc.core.api.Chanceable;
import com.levelmc.core.api.utils.NumberUtil;
import com.levelmc.core.api.yml.Path;
import com.levelmc.core.api.yml.YamlConfig;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.enchantments.Enchantment;

public class ChancedEnchantment extends YamlConfig implements Chanceable {

    @Path("enchantment")
    @Getter
    private Enchantment enchantment;

    @Path("level.min")
    @Getter
    @Setter
    private int levelMin;
    @Path("level.max")
    @Getter
    @Setter
    private int levelMax;
    @Path("chance")
    @Getter @Setter
    private int chance;
    @Path("measure")
    @Getter
    @Setter
    private int measure;

    public ChancedEnchantment(Enchantment enchantment, int levelMin, int levelMax, int chance, int measure) {
        this.enchantment = enchantment;
        this.levelMin = levelMin;
        this.levelMax = levelMax;
        this.chance = chance;
        this.measure = measure;
    }

    public ChancedEnchantment() {

    }

    @Override
    public int getChance() {
        return chance;
    }

    @Override
    public int getMeasure() {
        return measure;
    }

    public int getLevelInRange() {
        if (levelMin == levelMax) {
            return levelMin;
        }

        return NumberUtil.getRandomInRange(levelMin,levelMax);
    }
}
