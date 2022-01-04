package com.levelmc.loot.api;

import com.levelmc.loot.api.abilities.AbilityProperties;

public class SuffixNameData extends NameData {
    public SuffixNameData(String name, int chance, int measure) {
        super(NameSlot.SUFFIX, name, chance, measure,"", AbilityProperties.create());
    }

    public SuffixNameData(String name, int chance, int measure, String abilityId, AbilityProperties props) {
        super(NameSlot.SUFFIX,name,chance,measure,abilityId,props);
    }
}
