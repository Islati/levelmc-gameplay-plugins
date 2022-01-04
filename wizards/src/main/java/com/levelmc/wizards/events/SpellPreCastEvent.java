package com.levelmc.wizards.events;

import com.levelmc.wizards.spells.Spell;
import org.bukkit.entity.Player;

/**
 * Executed before the player has actually cast the spell.
 */
public class SpellPreCastEvent extends CancellableSpellEvent {
    public SpellPreCastEvent(Player player, Spell spell) {
        super(player, spell);
    }
}
