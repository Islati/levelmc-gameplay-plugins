package com.levelmc.loot.api;

import com.levelmc.core.api.Chanceable;
import com.levelmc.loot.api.abilities.Ability;
import com.levelmc.loot.api.abilities.AbilityProperties;

/**
 * Used in the selection of a name for custom loot generation.
 */
public interface Name extends Chanceable {
    static NameData prefix(String name, int chance, int measure) {
        return new PrefixNameData(name, chance, measure);
    }

    static NameData prefix(String name, int chance, int measure, Ability ability, AbilityProperties props) {
        return new PrefixNameData(name, chance, measure, ability.getId(), props);
    }

    static NameData suffix(String name, int chance, int measure) {
        return new SuffixNameData(name, chance, measure);
    }

    static NameData suffix(String name, int chance, int measure, Ability ability, AbilityProperties props) {
        return new SuffixNameData(name, chance, measure, ability.getId(), props);
    }

    static NameData base(String name, int chance, int measure) {
        return new BaseNameData(name, chance, measure);
    }

    static NameData base(String name, int chance, int measure, Ability ability, AbilityProperties props) {
        return new BaseNameData(name, chance, measure, ability.getId(), props);
    }

    NameSlot getSlot();

    String getName();

    default boolean hasAbility() {
        return getAbility() != null;
    }

    default Ability getAbility() {
        return null;
    }

    default AbilityProperties getAbilityProperties() {
        return null;
    }

    default boolean hasAbilityProperties() {
        return getAbilityProperties() != null;
    }
}
