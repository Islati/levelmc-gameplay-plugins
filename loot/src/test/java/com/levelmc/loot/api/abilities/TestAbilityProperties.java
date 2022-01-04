package com.levelmc.loot.api.abilities;

import com.levelmc.loot.LootTest;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class TestAbilityProperties extends LootTest {

    @Test
    public void onTestAbilityProperties() {
        AbilityProperties props = AbilityProperties.create();
        props.set(AbilityProperties.CHANCE,"10");
        props.set(AbilityProperties.DAMAGE_MIN,"1");
        props.set(AbilityProperties.DAMAGE_MAX,"10");

        Assertions.assertTrue(props.has(AbilityProperties.CHANCE));
        Assertions.assertTrue(props.has(AbilityProperties.DAMAGE_MIN));
        Assertions.assertTrue(props.has(AbilityProperties.DAMAGE_MAX));

        Assertions.assertEquals("10",props.get(AbilityProperties.CHANCE,"10"));
        Assertions.assertEquals("12",props.get("Test-Prop","12"));
        Assertions.assertEquals("1",props.get(AbilityProperties.DAMAGE_MIN,"1"));
        Assertions.assertEquals("10",props.get(AbilityProperties.DAMAGE_MAX,"10"));
    }
}
