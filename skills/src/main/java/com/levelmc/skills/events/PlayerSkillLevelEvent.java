package com.levelmc.skills.events;

import com.levelmc.skills.SkillType;
import lombok.Getter;
import org.bukkit.entity.Player;

public class PlayerSkillLevelEvent extends CancellableSkillEvent {
    @Getter
    private int newLevel;

    @Getter
    private int oldLevel;
    public PlayerSkillLevelEvent(Player player, SkillType type,int oldLevel, int newLevel) {
        super(player,type);
        this.oldLevel = oldLevel;
        this.newLevel = newLevel;
    }
}
