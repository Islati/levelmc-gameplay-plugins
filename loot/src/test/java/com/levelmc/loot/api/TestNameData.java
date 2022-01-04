package com.levelmc.loot.api;

import com.levelmc.loot.LootTest;
import com.levelmc.loot.api.abilities.Abilities;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class TestNameData extends LootTest {
    @Test
    public void testNameData() {
        NameData base = Name.base("Tester",100,100);
        NameData abilityBase = Name.base("Ability Tester",100,100, Abilities.INCREASED_DAMAGE, new Abilities.IncreasedDamageProperties(1.0,3,3.1,8,5,10));

        Assertions.assertEquals(NameSlot.BASE,base.getSlot());
        Assertions.assertNull(base.getAbility());
        Assertions.assertEquals("Tester",base.getName());

        Assertions.assertEquals(abilityBase.getAbilityId(),Abilities.INCREASED_DAMAGE.getId());
    }
}
