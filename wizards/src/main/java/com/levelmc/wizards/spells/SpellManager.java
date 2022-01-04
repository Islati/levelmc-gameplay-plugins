package com.levelmc.wizards.spells;

import com.levelmc.wizards.Wizards;
import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Manages the spells. Responsible for handling registration, and querying.
 */
public class SpellManager {

    @Getter
    private LinkedHashMap<String, Spell> spells = new LinkedHashMap<>();

    private Wizards component = null;

    public SpellManager(Wizards parent) {
        this.component = parent;
    }

    public Spell getSpell(String id) {
        return spells.get(id);
    }

    public void registerSpells(Spell... spells) {
        for (Spell spell : spells) {
            this.spells.put(spell.id(), spell);
        }
    }

    public List<Spell> getSpells(MagicType type) {
        List<Spell> spells = new ArrayList<>();

        for(Spell sp : this.spells.values()) {
            if (sp.getType() == type) {
                spells.add(sp);
            }
        }

        return spells;
    }
}
