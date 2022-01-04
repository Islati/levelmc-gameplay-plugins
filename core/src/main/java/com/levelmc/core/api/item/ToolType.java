package com.levelmc.core.api.item;

import com.google.common.collect.Sets;
import org.bukkit.Material;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public enum ToolType {

    HOE(Material.WOODEN_HOE, Material.STONE_HOE, Material.IRON_HOE, Material.GOLDEN_HOE, Material.DIAMOND_HOE,Material.NETHERITE_HOE),
    AXE(Material.WOODEN_AXE, Material.STONE_AXE, Material.IRON_AXE, Material.GOLDEN_AXE, Material.DIAMOND_AXE,Material.NETHERITE_AXE),
    SHOVEL(Material.WOODEN_SHOVEL, Material.STONE_SHOVEL, Material.IRON_SHOVEL, Material.GOLDEN_SHOVEL, Material.DIAMOND_SHOVEL,Material.NETHERITE_SHOVEL),
    PICK_AXE(Material.WOODEN_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLDEN_PICKAXE, Material.DIAMOND_PICKAXE,Material.NETHERITE_PICKAXE),
    FIRE_STARTER(Material.FLINT_AND_STEEL),
    UTILITY(Material.BUCKET, Material.FISHING_ROD, Material.CARROT_ON_A_STICK, Material.SHEARS);

    private static Set<Material> validTools = Sets.newHashSet();

    private static Map<ToolType, Set<Material>> toolIndexedMaterials = new HashMap<>();

    static {
        for (ToolType toolType : EnumSet.allOf(ToolType.class)) {
            Set<Material> materials = toolType.getMaterialTypes();
            /* All all the materials of the tool type to the set of valid tools */
            validTools.addAll(materials);

            /*
			Populate the tool-indexed map of materials; Used for static
            retrieval and checking of types for materials!
             */
            toolIndexedMaterials.put(toolType, materials);
        }
    }

    private Set<Material> toolTypes;

    ToolType(Material... types) {
        toolTypes = Sets.newHashSet(types);
    }

    public Set<Material> getMaterialTypes() {
        return toolTypes;
    }

    public boolean isType(Material material) {
        return toolTypes.contains(material);
    }

    public static boolean isTool(Material material) {
        return validTools.contains(material);
    }

    /**
     * Get the tool type of the given material.
     *
     * @param mat material to get the tool type for
     * @return tooltype for the given material if it's a tool, if it's not a tool null is returned.
     */
    public static ToolType getType(Material mat) {
        if (!isTool(mat)) {
            return null;
        }

        for (ToolType type : toolIndexedMaterials.keySet()) {
            if (!type.isType(mat)) {
                continue;
            }

            return type;
        }

        return null;
    }

    public static boolean isType(ToolType toolType, Material material) {
        return toolType.isType(material);
    }
}
