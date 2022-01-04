package com.levelmc.skills.config;

import com.levelmc.core.api.yml.Path;
import com.levelmc.core.api.yml.YamlConfig;
import lombok.Getter;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration specific to the mining skill.
 */
public class MiningConfiguration extends YamlConfig {

    @Path("exp-rewards")
    @Getter
    private Map<String, Integer> expRewards = new HashMap<>();

    public MiningConfiguration() {
        expRewards.put(Material.STONE.name(), 1);
        expRewards.put(Material.COAL_ORE.name(), 10);
        expRewards.put(Material.IRON_ORE.name(), 20);
        expRewards.put(Material.GOLD_ORE.name(), 45);
        expRewards.put(Material.LAPIS_ORE.name(), 15);
        expRewards.put(Material.NETHER_QUARTZ_ORE.name(), 1);
        expRewards.put(Material.REDSTONE_ORE.name(), 15);
        expRewards.put(Material.EMERALD_ORE.name(), 100);
        expRewards.put(Material.DIAMOND_ORE.name(), 100);
        expRewards.put(Material.NETHER_GOLD_ORE.name(), 20);
        expRewards.put(Material.COPPER_ORE.name(),10);
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
