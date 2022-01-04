package com.levelmc.skills.users;

import com.levelmc.core.api.players.User;
import com.levelmc.core.api.utils.LevelExpUtil;
import com.levelmc.core.api.yml.Comments;
import com.levelmc.core.api.yml.Path;
import com.levelmc.skills.SkillType;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkillUser extends User {

    @Path("experience")
    private Map<String, Integer> experience = new HashMap<>();


    @Path("available-points")
    @Comments({"Each skill and their available amount of skill", "points for allocation"})
    private Map<String, Integer> skillPointsAvailable = new HashMap<>();

    @Path("perks.levels")
    @Comments({"Levels for each of the players perks determined", "by the number of allocated skill points."})
    private Map<String, Integer> perkLevels = new HashMap<>();

    @Path("perks.active")
    @Comments({"Shows which perks are toggled active by the player.", "Can be ignored."})
    @Getter
    private List<String> activePerks = new ArrayList<>();

    public SkillUser(Player player) {
        super(player);

        for (SkillType type : SkillType.values()) {
            experience.put(type.getSkillName(), LevelExpUtil.getExperienceAtLevel(1));
        }
    }


    public void addExperience(SkillType type, int experience) {
        if (!this.experience.containsKey(type.getSkillName())) {
            this.experience.put(type.getSkillName(), LevelExpUtil.getExperienceAtLevel(1));
        }

        int _newExp = this.experience.get(type.getSkillName()) + experience;
        this.experience.put(type.getSkillName(), _newExp);
    }

    public int getExperience(SkillType type) {
        return experience.get(type.getSkillName());
    }

    public int getLevel(SkillType type) {
        return LevelExpUtil.getLevelAtExperience(getExperience(type));
    }

    public boolean hasPerk(String id) {
        return getPerkLevel(id) > 0;
    }

    /**
     * Get the level for a perk.
     *
     * @param id
     * @return
     */
    public int getPerkLevel(String id) {
        if (!perkLevels.containsKey(id.toLowerCase())) {
            return 0;
        }

        return perkLevels.get(id.toLowerCase());
    }

    /**
     * Add levels to the perk.
     *
     * @param id    perk to set level of
     * @param level level to set skill.
     */
    public void setPerkLevel(String id, int level) {
        perkLevels.put(id.toLowerCase(), level);
    }

    /**
     * Add levels to the given perk.
     *
     * @param id    id of the perk to add level to.
     * @param level count of levels to add
     */
    public void addPerkLevel(String id, int level) {
        if (!hasPerk(id)) {
            setPerkLevel(id.toLowerCase(), level);
        } else {
            int amount = getPerkLevel(id.toLowerCase());
            amount += level;
            setPerkLevel(id, amount);
        }

    }

    /**
     * Get the amount of skill points a player has in the skill/
     *
     * @param type Skill to check amount of points for
     * @return amount of skill points.
     */
    public int getSkillPointsAvailable(SkillType type) {
        if (!skillPointsAvailable.containsKey(type.getSkillName())) {
            skillPointsAvailable.put(type.getSkillName(), 0);
        }

        return skillPointsAvailable.get(type.getSkillName());
    }

    /**
     * Add skill points for the given skill to be redeemed on perks
     *
     * @param type   skill that will be receiving the skill points
     * @param amount amount of skill points to add
     * @return total skill points
     */
    public int addSkillPoint(SkillType type, int amount) {
        int currentAmount = getSkillPointsAvailable(type);

        currentAmount += amount;
        skillPointsAvailable.put(type.getSkillName(), currentAmount);

        return currentAmount;
    }

    public int subtractSkillPoint(SkillType type, int amount) {
        int skillPoints = getSkillPointsAvailable(type);
        skillPoints -= amount;
        skillPointsAvailable.put(type.getSkillName(), skillPoints);
        return skillPoints;
    }

    public boolean isPerkActive(String perkId) {
        if (activePerks == null) {
            activePerks = new ArrayList<>();
        }
        return activePerks.contains(perkId);
    }

    /**
     * Set the status of a perk. This will be reflected in the players skill menu.
     *
     * @param perkId
     * @param status
     */
    public void setPerkActive(String perkId, boolean status) {
        if (!status) {
            activePerks.remove(perkId);
        } else {
            activePerks.add(perkId);
        }
    }
}
