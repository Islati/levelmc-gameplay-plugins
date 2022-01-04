package com.levelmc.core.config;

import com.google.common.collect.Sets;
import com.levelmc.core.api.yml.Path;
import com.levelmc.core.api.yml.Skip;
import com.levelmc.core.api.yml.YamlConfig;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.material.Bed;

import java.util.*;

import static org.bukkit.Material.*;

public class ArmorEquipEventConfig extends YamlConfig {
    @Skip
    private static Set<String> searchText = Sets.newHashSet(
            "_BED",
            "_TABLE",
            "_GATE",
            "_DOOR",
            "_FENCE",
            "_SIGN",
            "_ON",
            "_BOX",
            "_BUTTON",
            "_TRAPDOOR"
    );

    @Path("blocked")
    @Getter
    private Set<Material> blockedMaterials = new HashSet<>();

    public ArmorEquipEventConfig() {

        /* Filter all the blocks that have multiple & are ignored */
        for (Material material : Material.values()) {
            for (String s : searchText) {
                if (material.name().contains(s)) {
                    blockedMaterials.add(material);
                }
            }
        }

        /* Add the remaining blocks to ignore */
        Set<Material> ignores = Sets.newHashSet(
                FURNACE,
                CHEST,
                TRAPPED_CHEST,
                BEACON,
                DISPENSER,
                DROPPER,
                HOPPER,
                CRAFTING_TABLE,
                ENCHANTING_TABLE,
                ENDER_CHEST,
                ANVIL,
                COMPARATOR,
                COMPASS,
                BREWING_STAND,
                CAULDRON,
                LEVER,
                DAYLIGHT_DETECTOR,
                BARREL,
                BLAST_FURNACE,
                SMOKER,
                CARTOGRAPHY_TABLE,
                COMPOSTER,
                GRINDSTONE,
                LECTERN,
                LOOM,
                STONECUTTER,
                BELL
        );

        for(Material ignore : ignores) {
            blockedMaterials.add(ignore);
        }
    }
}
