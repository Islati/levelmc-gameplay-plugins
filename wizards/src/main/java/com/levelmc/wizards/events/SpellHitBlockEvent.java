package com.levelmc.wizards.events;

import com.levelmc.wizards.spells.Spell;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class SpellHitBlockEvent extends CancellableSpellEvent {

    private static HandlerList handlers = new HandlerList();

    private Spell spell;


    private Block block;

    public SpellHitBlockEvent(@NotNull Player who, Spell spell, Block block) {
        super(who, spell);
        this.block = block;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
