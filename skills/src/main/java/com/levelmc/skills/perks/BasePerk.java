package com.levelmc.skills.perks;

import com.google.common.collect.Lists;
import com.levelmc.core.api.ApiMessages;
import com.levelmc.skills.SkillType;
import com.levelmc.skills.Skills;
import com.levelmc.skills.users.SkillUser;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BasePerk implements SkillPerk {
    @Getter
    private String perkId;

    @Getter
    private SkillType skillType;

    @Getter
    private int maxLevel;

    @Getter
    private Map<String, Integer> preRequirements = new HashMap<>();

    @Getter
    private String name;

    @Getter
    @Setter
    private List<String> defaultDescription = new ArrayList<>();

    @Getter
    @Setter
    private double activationChancePerLevel = 5;

    @Getter @Setter
    private boolean toggleable = false;

    public BasePerk(String id, String name, SkillType parentSkill, int maxLevel) {
        this.perkId = id;
        this.skillType = parentSkill;
        this.name = name;
        this.maxLevel = maxLevel;
        this.defaultDescription = Lists.newArrayList(ApiMessages.get("skill-point-menu-level-desc"));
    }

    public BasePerk(String id, String name, SkillType parentSkill, int maxLevel, double activationChancePerLevel) {
        this(id, name, parentSkill, maxLevel);
        this.activationChancePerLevel = activationChancePerLevel;
    }

    @Override
    public String getPerkName() {
        return name;
    }

    @Override
    public List<String> getPerkDescription(Player player) {
        /*
        Placeholders:
            - {skill-level}
            - {max-level}
            - {name}
         */

        List<String> parsedDescription = new ArrayList<>();

        SkillUser user = getUser(player);

        if (!user.hasPerk(getPerkId())) {
            return Lists.newArrayList(ApiMessages.get("skill-locked"));
        }

        for (String s : defaultDescription) {
            parsedDescription.add(s
                    .replace("{skill-level}", String.valueOf(user.getPerkLevel(getPerkId())))
                    .replace("{max-level}", String.valueOf(getMaxLevel()))
                    .replace("{name}", getName())
                    .replace("{chance}", String.valueOf(getActivationChancePerLevel() * user.getPerkLevel(getPerkId())))
            );
        }



        return parsedDescription;
    }

    @Override
    public double getActivationChance(int level) {
        return level * activationChancePerLevel;
    }

    /**
     * Set a perk to have other perks required.
     *
     * @param perk  perk to require
     * @param level level of perk required
     * @return this
     */
    public BasePerk setRequirement(SkillPerk perk, int level) {
        return setRequirement(perk.getPerkId(), level);
    }

    public BasePerk setRequirement(String id, int level) {
        if (!getPerkId().equalsIgnoreCase(id)) {
            this.preRequirements.put(id.toLowerCase(), level);
        }
        return this;
    }

    public int getLevel(Player player) {
        return Skills.getInstance().getUserManager().getUser(player).getPerkLevel(getPerkId());
    }
}
