package com.levelmc.wizards.events;

import com.levelmc.wizards.spells.Spell;
import lombok.Getter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class SpellHitEntityEvent extends CancellableSpellEvent {
    @Getter
    private Entity entity;
    public SpellHitEntityEvent(Player player, Spell spell, Entity entity) {
        super(player, spell);
    }

    public boolean isLivingEntity() {
        return entity instanceof LivingEntity;
    }
}
