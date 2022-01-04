package com.levelmc.skills.menus;

import com.levelmc.core.api.ApiMessages;
import com.levelmc.core.api.gui.*;
import com.levelmc.core.chat.Chat;
import com.levelmc.core.api.utils.PluginUtils;
import com.levelmc.skills.SkillType;
import com.levelmc.skills.Skills;
import com.levelmc.skills.perks.SkillPerk;
import com.levelmc.skills.events.PlayerSkillPointAssignEvent;
import com.levelmc.skills.users.SkillUser;
import org.apache.commons.lang.WordUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.List;

public class PerksMenus extends ItemMenu {
    public PerksMenus(SkillUser user, SkillType skillType) {
        super(String.format("&l%s &r&7► &r&lPerks", WordUtils.capitalize(skillType.getSkillName())), Menus.getRows(Skills.getInstance().getPerks(skillType).size()));

        List<SkillPerk> perks = Skills.getInstance().getPerks(skillType);

        if (perks == null || perks.size() == 0) {
            return;
        }

        int index = 0;

        Player player = user.getPlayer();

        for (SkillPerk perk : perks) {
            MenuItem perkMenuItem = new MenuItem(perk.getMenuIcon(player))
                    .onClickHandler(new MenuItemClickHandler() {
                        @Override
                        public void onClick(MenuItem item, Player player, ClickType type) {

                            /*
                            If they have the perk active, check if they're doing a special click (right click) to activate said perk.
                             */
                            if (user.hasPerk(perk.getPerkId()) && type == ClickType.RIGHT && Skills.getInstance().getPerk(perk.getPerkId()).isToggleable()) {
                                user.setPerkActive(perk.getPerkId(), user.isPerkActive(perk.getPerkId()));
                                Chat.message(player, ApiMessages.get(user.isPerkActive(perk.getPerkId()) ? "perk-activated" : "perk-deactivated"));
                                Menus.switchMenu(player, item.getMenu(), new PerksMenus(user, skillType));
                                return;
                            }

                            if (user.getSkillPointsAvailable(skillType) == 0) {
                                Chat.message(player, ApiMessages.get("skill-point-unavailable"));
                                return;
                            }

                            if (!perk.canLevel(player)) {
                                Chat.message(player, ApiMessages.getFormatted("perk-cannot-level", perk.getPerkName()));
                                return;
                            }

                            if (user.getPerkLevel(perk.getPerkId()) >= perk.getMaxLevel()) {
                                return;
                            }

                            ConfirmationMenu addSkillPointConfirmation = ConfirmationMenu.of(ApiMessages.getFormatted("skill-point-assign-confirm", skillType.getSkillName()))
                                    .onConfirm(new ConfirmationMenu.Action() {
                                        @Override
                                        public void perform(Menu menu, Player player) {
                                            PlayerSkillPointAssignEvent skillPointAssignEvent = new PlayerSkillPointAssignEvent(player, skillType, perk, 1);
                                            PluginUtils.callEvent(skillPointAssignEvent);
                                            Menus.switchMenu(player, menu, new PerksMenus(user, skillType));
                                        }
                                    }).denyOnClose();

                            Menus.switchMenu(player, item.getMenu(), addSkillPointConfirmation);
                        }
                    });
            item(index++, perkMenuItem);
        }
    }
}
