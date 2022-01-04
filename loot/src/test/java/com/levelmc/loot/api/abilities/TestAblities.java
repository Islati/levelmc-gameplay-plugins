package com.levelmc.loot.api.abilities;

import com.levelmc.loot.LootTest;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class TestAblities extends LootTest {

    @Test
    public void testAbilitiesRegistration() {
        Assertions.assertNotNull(Abilities.getById(Abilities.BLEED.getId()) );
    }

    @Test
    public void testAbilityApply() {
        ItemStack sword = new ItemStack(Material.WOODEN_SWORD);

        Abilities.BLEED.apply(sword, new Abilities.BleedProperties(1.5,15,2,3,1,3));

        Assertions.assertTrue(Abilities.hasAbility(sword));
        Assertions.assertEquals(1,Abilities.getAbilities(sword).size());
    }
}
