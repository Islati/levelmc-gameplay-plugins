package com.levelmc.wizards.events;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

public class MagicExpGainEvent extends CancellableWizardEvent {

    @Getter
    @Setter
    private int amount;

    public MagicExpGainEvent(Player player, int amount) {
        super(player);
        this.amount = amount;
    }
}
