package com.levelmc.wizards.events;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

/**
 * Executed whenever a player regenerates mana.
 */
public class ManaRegenEvent extends WizardEvent implements Cancellable {

    @Getter
    @Setter
    private int amount;

    @Getter @Setter
    private boolean cancelled = false;

    public ManaRegenEvent(Player player, int amount) {
        super(player);
        this.amount = amount;
    }
}
