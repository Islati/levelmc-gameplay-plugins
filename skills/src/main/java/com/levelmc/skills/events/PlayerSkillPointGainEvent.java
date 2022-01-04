package com.levelmc.skills.events;

import com.levelmc.skills.SkillType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

public class PlayerSkillPointGainEvent extends CancellableSkillEvent {
    @Getter
    @Setter
    private int amount;
    public PlayerSkillPointGainEvent(Player player, SkillType skill, int amount) {
        super(player,skill);
        this.amount = amount;
    }
}
