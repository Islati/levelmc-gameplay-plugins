package com.levelmc.loot.api.levels;

import com.levelmc.loot.LootTest;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.Map;

import static org.bukkit.Material.STONE_SWORD;

public class TestItemLevelManager extends LootTest {

    private ItemLevelManager itemLevelManager = null;

    @Before
    public void setupItemLevelManagerTest() {
        itemLevelManager = getPlugin().getLevelManager();
    }


    @Test
    public void testItemExpForLevel() {
        for (int i = 1; i < itemLevelManager.getMaxItemLevel(); i++) {
            int expForLevel = itemLevelManager.getExpForLevel(i);
            Assertions.assertEquals(expForLevel, itemLevelManager.getExpPerLevel() * i + (itemLevelManager.getExpPerLevel() * (itemLevelManager.getTaxPerLevel() / 100)));
        }
    }

    @Test
    public void testMobExperienceRewards() {
        Map<String, Integer> mobExperienceRewardsMap = itemLevelManager.getMobExperienceRewards();

        for (Map.Entry<String, Integer> entry : mobExperienceRewardsMap.entrySet()) {
            EntityType type = EntityType.valueOf(entry.getKey().toUpperCase());
            Assertions.assertNotNull(type);

            Assertions.assertTrue(itemLevelManager.getExpReward(type) > 0);
        }
    }

    @Test
    public void testIsItemLevelable() {
        ItemStack item = new ItemStack(STONE_SWORD);
        itemLevelManager.setLevel(item, 1);
        Assertions.assertTrue(itemLevelManager.isItemLevelable(item));
        Assertions.assertEquals(1, itemLevelManager.getItemLevel(item));
        Assertions.assertEquals(0,itemLevelManager.getItemExp(item));
    }
}
