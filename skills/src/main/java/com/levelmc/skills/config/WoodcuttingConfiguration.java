package com.levelmc.skills.config;

import com.levelmc.core.api.yml.Path;
import com.levelmc.core.api.yml.YamlConfig;
import lombok.Getter;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class WoodcuttingConfiguration extends YamlConfig {

    @Path("exp-rewards")
    @Getter
    private Map<String, Integer> expRewards = new HashMap<>();

    public WoodcuttingConfiguration() {
        expRewards.put(Material.ACACIA_LOG.name(),5);
        expRewards.put(Material.BIRCH_LOG.name(),5);
        expRewards.put(Material.DARK_OAK_LOG.name(),5);
        expRewards.put(Material.JUNGLE_LOG.name(),5);
        expRewards.put(Material.OAK_LOG.name(),5);
        expRewards.put(Material.SPRUCE_LOG.name(),5);
    }

    public int getExpReward(Material material) {
        if (!hasExpReward(material)) {
            return 0;
        }

        return expRewards.get(material.name());
    }

    public boolean hasExpReward(Material material) {
        return expRewards.containsKey(material.name());
    }


}
