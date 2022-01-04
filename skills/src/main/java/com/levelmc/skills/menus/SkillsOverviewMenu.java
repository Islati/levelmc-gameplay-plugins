package com.levelmc.skills.menus;

import com.google.common.collect.Lists;
import com.levelmc.core.api.utils.LevelExpUtil;
import com.levelmc.core.api.utils.ProgressBarUtil;
import com.levelmc.core.api.gui.*;
import com.levelmc.core.api.item.ItemBuilder;
import com.levelmc.core.api.utils.NumberUtil;
import com.levelmc.skills.SkillType;
import com.levelmc.skills.users.SkillUser;
import org.apache.commons.lang.WordUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.List;

/***
 * Provides players an overview of their skills.
 *
 * Each skill has an item, and when clicked brings the user to the perks menu for the clicked skill.
 */
public class SkillsOverviewMenu extends ItemMenu {

    protected String renderProgressBar(SkillUser user, SkillType type) {
        int currentLevel = user.getLevel(type);
        int currentExp = user.getExperience(type);
        int nextLevelExp = LevelExpUtil.getExperienceAtLevel(currentLevel + 1);

        double expPercentLeft = ((double) currentExp / (double) nextLevelExp) * 100;
        int numericPercentLeft = (int) NumberUtil.round(expPercentLeft, 1);
        return ProgressBarUtil.renderProgressBar("&a", "&c", ProgressBarUtil.DEFAULT_BAR, numericPercentLeft);
    }

    protected MenuItem generateMenuItem(SkillUser user, SkillType skillType) {

        List<String> skillsDesc = Lists.newArrayList(skillType.getDescription());

        skillsDesc.add("");
        skillsDesc.add(String.format("&7✪ &b%s &7(Skill Points)", user.getSkillPointsAvailable(skillType)));
        skillsDesc.add(String.format("&7► &cLevel &b%s", user.getLevel(skillType)));
        skillsDesc.add(renderProgressBar(user, skillType));

        return new MenuItem(ItemBuilder.of(skillType.getMenuIconMaterial()).name(String.format("&l%s", WordUtils.capitalize(skillType.getSkillName())))
                .lore(skillsDesc)
                .item()).onClickHandler(new MenuItemClickHandler() {
            @Override
            public void onClick(MenuItem item, Player player, ClickType type) {
                item.getMenu().switchMenu(player, new PerksMenus(user, skillType));
            }
        });
    }

    public SkillsOverviewMenu(SkillUser user) {
        super("&cSkills Overview", 1);

        int i = 0;
        for (SkillType type : SkillType.values()) {
            item(i++, generateMenuItem(user, type));
        }
    }
}
