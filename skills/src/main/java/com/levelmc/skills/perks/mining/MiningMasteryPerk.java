package com.levelmc.skills.perks.mining;

import com.google.common.collect.Lists;
import com.levelmc.core.api.ApiMessages;
import com.levelmc.skills.SkillType;
import com.levelmc.skills.perks.BasePerk;

public class MiningMasteryPerk extends BasePerk {
    private static MiningMasteryPerk instance = null;

    public static MiningMasteryPerk getInstance() {
        if (instance == null) {
            instance = new MiningMasteryPerk();
        }

        return instance;
    }

    protected MiningMasteryPerk() {
        super("mining-mastery", "Mining Mastery", SkillType.MINING, 20, 0.65);
        setDefaultDescription(Lists.newArrayList(
                ApiMessages.get("skill-point-menu-level-desc"),
                "&a✚ {chance}% chance for double drops.",
                "&7Prove your mastery as a Miner.",
                ApiMessages.get("skill-point-menu-pre-req-for-many")
        ));
    }
}
