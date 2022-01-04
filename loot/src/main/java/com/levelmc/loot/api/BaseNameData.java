package com.levelmc.loot.api;


import com.levelmc.loot.api.abilities.AbilityProperties;

public class BaseNameData extends NameData {
    public BaseNameData(String name, int chance, int measure) {
        super(NameSlot.BASE, name, chance, measure, "", AbilityProperties.create());
    }

    public BaseNameData(String name, int chance, int measure, String abilityId, AbilityProperties abilityProperties) {
        super(NameSlot.BASE, name, chance, measure, abilityId, abilityProperties);
    }
}
