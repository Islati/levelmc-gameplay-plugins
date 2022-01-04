package com.levelmc.wizards.events;

import com.levelmc.wizards.spells.Spell;
import lombok.Getter;
import org.bukkit.entity.Player;

public class SpellEvent extends WizardEvent {
    @Getter
    private Spell spell;

    public SpellEvent(Player player, Spell spell) {
        super(player);
        this.spell = spell;
    }
}
