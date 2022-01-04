package com.levelmc.skills.events;

import com.levelmc.skills.SkillType;
import lombok.Getter;
import org.bukkit.entity.Player;

public class PlayerSkillGainedExpEvent extends CancellableSkillEvent {

    @Getter
    private int amount;

    public PlayerSkillGainedExpEvent(Player player, SkillType type, int amount) {
        super(player,type);
        this.amount = amount;
    }
}
