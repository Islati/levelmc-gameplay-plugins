package com.levelmc.wizards.gui;

import com.levelmc.core.api.gui.ItemMenu;
import com.levelmc.core.api.gui.Menu;
import com.levelmc.core.api.gui.MenuItem;
import com.levelmc.core.api.gui.MenuItemClickHandler;
import com.levelmc.core.api.item.ItemBuilder;
import com.levelmc.core.api.item.SkullCreator;
import com.levelmc.core.api.utils.LevelExpUtil;
import com.levelmc.core.api.utils.ProgressBarUtil;
import com.levelmc.wizards.Wizards;
import com.levelmc.wizards.config.MenuConfig;
import com.levelmc.wizards.spells.Spell;
import com.levelmc.wizards.spells.SpellBindSlot;
import com.levelmc.wizards.users.Wizard;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class PlayerWizardGUI extends ItemMenu {
    public PlayerWizardGUI(Wizard wizard) {
        super("Wizardry", 1);

        MenuConfig menuConfig = Wizards.getInstance().getMenuConfig();

        ItemStack playerDetailsItem = SkullCreator.itemFromUuid(wizard.getPlayer().getUniqueId());
        playerDetailsItem = ItemBuilder.clone(playerDetailsItem).lore(
                String.format("&6%s", wizard.getName()),
                "",
                String.format("► &7Level &b%s", wizard.getWizardingLevel()),
                "&7%s/%s EXP".formatted(wizard.getExperience(), wizard.getNextLevelExperience()),
                String.format("%s", ProgressBarUtil.renderProgressBar(wizard.getExperience()))
        ).name("&a&l%s".formatted(wizard.getName())).item();
        MenuItem playerDetailsMenuItem = new MenuItem(SkullCreator.itemFromUuid(wizard.getPlayer().getUniqueId())).onClickHandler(new MenuItemClickHandler() {
            @Override
            public void onClick(MenuItem item, Player player, ClickType type) {

            }
        });
        item(menuConfig.getSlotPlayerIcon(), playerDetailsMenuItem);

        ItemBuilder wandDetailsItem = ItemBuilder.of(Material.BLAZE_ROD).name("&cSpell Binds");
        for (SpellBindSlot slot : SpellBindSlot.values()) {
            Spell spell = wizard.getSpellAtSlot(slot);
            wandDetailsItem.lore("&7> ► %s: %s", slot.getSlotName(), spell == null ? "&7None" : "&e%s".formatted(spell.getSpellName(wizard.getPlayer(), wizard.getWizardingLevel())));
        }

        item(menuConfig.getSlotSpellBinds(),new MenuItem(ItemBuilder.of(Material.BLAZE_ROD).name("&a&lSpell Binds").lore("&7Configure your wand").item()).onClickHandler(new MenuItemClickHandler() {
            @Override
            public void onClick(MenuItem item, Player player, ClickType type) {
                switchMenu(player, new SpellBindsGUI(wizard));
            }
        }));

        item(menuConfig.getSlotSpellTomes(), new MenuItem(ItemBuilder.of(Material.ENCHANTED_BOOK).name("&c&lSpell Book").lore("&7Click to for more info.").item()).onClickHandler(new MenuItemClickHandler() {
            @Override
            public void onClick(MenuItem item, Player player, ClickType type) {
                switchMenu(player, new WizardSpellsGUI(wizard));
            }
        }));
    }
}
