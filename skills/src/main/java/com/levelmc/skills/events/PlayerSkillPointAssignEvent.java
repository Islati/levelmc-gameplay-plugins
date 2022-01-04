package com.levelmc.skills.events;

import com.levelmc.skills.SkillType;
import com.levelmc.skills.perks.SkillPerk;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

public class PlayerSkillPointAssignEvent extends CancellableSkillEvent {
    @Getter
    private SkillPerk perk;
    @Getter
    @Setter
    private int amount;

    public PlayerSkillPointAssignEvent(Player player, SkillType skill, SkillPerk perkId, int amount) {
        super(player, skill);
        this.perk = perkId;
        this.amount = amount;
    }

    public int getLevel() {
        return getUser().getPerkLevel(getPerk().getPerkId());
    }
}
