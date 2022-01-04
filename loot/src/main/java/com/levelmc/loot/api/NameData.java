package com.levelmc.loot.api;

import com.levelmc.core.api.yml.Path;
import com.levelmc.core.api.yml.YamlConfig;
import com.levelmc.loot.api.abilities.Abilities;
import com.levelmc.loot.api.abilities.Ability;
import com.levelmc.loot.api.abilities.AbilityProperties;
import lombok.Getter;

public class NameData extends YamlConfig implements Name {
    @Path("name")
    private String name;
    @Path("slot")
    private String slotName;

    @Path("chance")
    private int chance;
    @Path("measure")
    private int measure;

    @Path("ability-id")
    @Getter
    private String abilityId = null;

    @Path("ability-data")
    @Getter
    private AbilityProperties abilityProperties = new AbilityProperties();

    public NameData(NameSlot slot, String name, int chance, int measure, String associatedAbilityId, AbilityProperties abilityProperties) {
        this.name = name;
        this.slotName = slot.name().toLowerCase();
        this.chance = chance;
        this.measure = measure;
        this.abilityId = associatedAbilityId;
        this.abilityProperties = abilityProperties;
    }

    public NameData() {

    }

    @Override
    public NameSlot getSlot() {
        return NameSlot.valueOf(slotName.toUpperCase());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getChance() {
        return chance;
    }

    @Override
    public int getMeasure() {
        return measure;
    }

    public Ability getAbility() {
        if (abilityId == null) {
            return null;
        }
        return Abilities.getById(getAbilityId());
    }
}
