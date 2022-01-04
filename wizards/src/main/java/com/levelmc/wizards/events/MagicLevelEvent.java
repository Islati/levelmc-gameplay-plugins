package com.levelmc.wizards.events;

import lombok.Getter;
import org.bukkit.entity.Player;

public class MagicLevelEvent extends WizardEvent {

    @Getter
    private int previousLevel;
    @Getter
    private int currentLevel;

    public MagicLevelEvent(Player player, int previousLevel, int currentLevel) {
        super(player);
        this.previousLevel = previousLevel;
        this.currentLevel = currentLevel;
    }
}
