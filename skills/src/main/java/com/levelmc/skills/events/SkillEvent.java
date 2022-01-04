package com.levelmc.skills.events;

import com.levelmc.skills.SkillType;
import lombok.Getter;
import org.bukkit.entity.Player;

public abstract class SkillEvent extends SkillUserEvent {
    @Getter
    private SkillType skillType;
    public SkillEvent(Player player, SkillType skill) {
        super(player);
        this.skillType = skill;
    }
}
