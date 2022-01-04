package com.levelmc.loot.api;

import com.levelmc.loot.LootTest;
import org.bukkit.Material;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class TestChancedMaterial extends LootTest {

    @Test
    public void testChancedMaterial() {
        ChancedMaterial material = new ChancedMaterial(Material.WOODEN_AXE,100,100);
        Assertions.assertTrue(material.chanceCheck());
        Assertions.assertEquals(material.getMaterial(),Material.WOODEN_AXE);
        Assertions.assertEquals(100,material.getChance());
    }

}
