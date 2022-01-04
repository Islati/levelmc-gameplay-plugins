package com.levelmc.core.item;

import com.levelmc.core.LevelCoreTest;
import com.levelmc.core.api.item.ItemBuilder;
import com.levelmc.core.api.item.ItemUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class TestItemBuilder extends LevelCoreTest {

    @Test
    public void testItemBuilderBasic() {
        ItemStack dirt = ItemBuilder.of(Material.DIRT).item();
        Assertions.assertNotNull(dirt);
        Assertions.assertEquals(dirt.getType(), Material.DIRT);
    }

    @Test
    public void testItemBuilderNames() {
        ItemStack namedItem = ItemBuilder.of(Material.WOODEN_SWORD)
                .name("Test Item").item();

        Assertions.assertTrue(ItemUtils.hasName(namedItem));
        Assertions.assertTrue(ItemUtils.nameContains(namedItem, "Test Item"));

        ItemStack colouredNamedItem = ItemBuilder.of(Material.WOODEN_AXE)
                .name("&7Gray Axe").item();

        Assertions.assertTrue(ItemUtils.hasName(colouredNamedItem));
        Assertions.assertTrue(ItemUtils.nameContains(colouredNamedItem, "Gray Axe"));
    }

    @Test
    public void testItemBuilderLore() {
        ItemStack loreItem = ItemBuilder.of(Material.WOODEN_SWORD)
                .name("Test Item")
                .lore("Lore Line 1", "Lore Line 2")
                .item();

        Assertions.assertTrue(ItemUtils.hasLore(loreItem));
        Assertions.assertTrue(ItemUtils.loreContains(loreItem, "Lore Line 1"));
        Assertions.assertTrue(ItemUtils.loreContains(loreItem, "Lore Line 2"));
        Assertions.assertTrue(ItemUtils.hasLoreAtLine(loreItem, 0));
        Assertions.assertTrue(ItemUtils.hasLoreAtLine(loreItem, 1));
    }

    @Test
    public void testItemBuilderEnchantments() {
        ItemStack itemEnchanted = ItemBuilder.of(Material.WOODEN_SWORD)
                .enchant(Enchantment.DAMAGE_ALL, 1)
                .item();

        Assertions.assertTrue(ItemUtils.hasEnchantments(itemEnchanted));
        Assertions.assertTrue(ItemUtils.hasEnchantment(itemEnchanted, Enchantment.DAMAGE_ALL, 1));
        Assertions.assertFalse(ItemUtils.hasEnchantment(itemEnchanted, Enchantment.DAMAGE_ALL, 2));
    }

}
