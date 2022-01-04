package com.levelmc.skills.events;

import com.levelmc.skills.SkillType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public abstract class CancellableSkillEvent extends SkillEvent implements Cancellable {
    @Getter
    @Setter
    private boolean cancelled;

    public CancellableSkillEvent(Player player, SkillType skill) {
        super(player, skill);
    }
}
