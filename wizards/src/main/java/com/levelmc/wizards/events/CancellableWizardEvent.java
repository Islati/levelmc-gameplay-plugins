package com.levelmc.wizards.events;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class CancellableWizardEvent extends WizardEvent implements Cancellable {
    @Getter
    @Setter
    private boolean cancelled;

    public CancellableWizardEvent(Player player) {
        super(player);
    }
}
