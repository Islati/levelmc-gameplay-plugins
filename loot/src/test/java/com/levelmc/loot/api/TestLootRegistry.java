package com.levelmc.loot.api;

import com.levelmc.core.api.utils.ListUtils;
import com.levelmc.loot.LootTest;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class TestLootRegistry extends LootTest {

    @Test
    public void testLootRegistry() {
        LootRegistry lr = getPlugin().getRegistry();

        Assertions.assertTrue(lr.hasTable("example"));

        Assertions.assertNotNull(ListUtils.getRandom(lr.getDropTables()).createItem());
    }
}
