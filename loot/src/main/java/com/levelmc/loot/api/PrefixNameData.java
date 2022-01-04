package com.levelmc.loot.api;

import com.levelmc.loot.api.abilities.AbilityProperties;

public class PrefixNameData extends NameData {
    public PrefixNameData(String name, int chance, int measure) {
        super(NameSlot.PREFIX, name, chance, measure, "", AbilityProperties.create());
    }

    public PrefixNameData(String name, int chance, int measure, String abilityId, AbilityProperties properties) {
        super(NameSlot.PREFIX, name, chance, measure, abilityId, properties);
    }

}


