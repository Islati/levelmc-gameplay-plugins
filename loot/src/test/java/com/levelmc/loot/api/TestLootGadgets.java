package com.levelmc.loot.api;

import com.levelmc.core.api.gadgets.Gadgets;
import com.levelmc.loot.Loot;
import com.levelmc.loot.LootTest;
import com.levelmc.loot.api.repairing.ItemRepairScroll;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class TestLootGadgets extends LootTest {

    @Test
    public void testLootComponentSetup() {

        Assertions.assertTrue(Gadgets.hasBeenRegistered(ItemRepairScroll.getInstance()));
        Assertions.assertTrue(Gadgets.isGadget(Loot.getInstance(),ItemRepairScroll.getInstance().getItem()));
    }
}
