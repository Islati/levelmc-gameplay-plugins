package com.levelmc.loot.api;

import com.levelmc.loot.LootTest;
import org.bukkit.enchantments.Enchantment;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class TestChancedEnchantment extends LootTest {

    @Test
    public void testChancedEnchantment() {
        ChancedEnchantment enchantment = new ChancedEnchantment(Enchantment.ARROW_DAMAGE, 1, 3, 0, 100);
        Assertions.assertTrue(enchantment.getLevelInRange() >= 1, "Level was not within the range");
        Assertions.assertFalse(enchantment.chanceCheck());
        enchantment.setChance(100);
        Assertions.assertTrue(enchantment.chanceCheck());
        Assertions.assertEquals(100, enchantment.getChance());
    }
}
