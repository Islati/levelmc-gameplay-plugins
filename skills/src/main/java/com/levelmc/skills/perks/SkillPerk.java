package com.levelmc.skills.perks;

import com.levelmc.core.api.ApiMessages;
import com.levelmc.core.api.item.ItemBuilder;
import com.levelmc.core.api.utils.NumberUtil;
import com.levelmc.skills.Skills;
import com.levelmc.skills.users.SkillUser;
import com.levelmc.skills.SkillType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

/**
 * Defines a skill perks identifying properties.
 */
public interface SkillPerk {

    String getPerkId();

    String getPerkName();

    List<String> getPerkDescription(Player player);

    SkillType getSkillType();

    int getMaxLevel();

    double getActivationChance(int level);

    boolean isToggleable();

    /**
     * Map of requirements (perks & level) required for the player to
     */
    Map<String, Integer> getPreRequirements();

    default boolean canLevel(Player player) {
        SkillUser user = getUser(player);

        Map<String, Integer> preReqs = getPreRequirements();
        if (preReqs.isEmpty()) {
            return true;
        }

        for (Map.Entry<String, Integer> requirement : getPreRequirements().entrySet()) {
            //todo check level.
            if (!user.hasPerk(requirement.getKey())) {
                return false;
            }

            if (user.getPerkLevel(requirement.getKey()) >= requirement.getValue()) {
                return false;
            }
        }

        int currentLevel = user.getPerkLevel(getPerkId());
        return currentLevel < getMaxLevel();
    }

    default SkillUser getUser(Player player) {
        return Skills.getInstance().getUserManager().getUser(player);
    }

    default ItemStack getMenuIcon(Player player) {
        ItemBuilder menuIcon = ItemBuilder.of(Material.PAPER).name(String.format("&a%s", getPerkName())).lore(getPerkDescription(player));

        SkillUser user = getUser(player);
        if (isToggleable()) {
            menuIcon.addLore(user.isPerkActive(getPerkId()) ? "&7Activated" : "&7Inactive", ApiMessages.get("perk-is-toggleable"));
        }

        return menuIcon.item();
    }

    default boolean activate(Player player) {
        SkillUser user = getUser(player);

        return NumberUtil.percentCheck(getActivationChance(user.getPerkLevel(getPerkId())));
    }
}

