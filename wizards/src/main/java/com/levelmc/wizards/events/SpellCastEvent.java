package com.levelmc.wizards.events;

import com.levelmc.wizards.spells.Spell;
import org.bukkit.entity.Player;

public class SpellCastEvent extends SpellEvent {
    public SpellCastEvent(Player player, Spell spell) {
        super(player, spell);
    }
}
