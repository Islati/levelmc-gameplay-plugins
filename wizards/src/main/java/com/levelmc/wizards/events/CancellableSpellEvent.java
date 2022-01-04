package com.levelmc.wizards.events;

import com.levelmc.wizards.spells.Spell;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class CancellableSpellEvent extends SpellEvent implements Cancellable {
    @Getter
    @Setter
    private boolean cancelled = false;

    public CancellableSpellEvent(Player player, Spell spell) {
        super(player, spell);
    }
}
