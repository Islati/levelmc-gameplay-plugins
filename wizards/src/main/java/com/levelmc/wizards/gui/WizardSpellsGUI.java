package com.levelmc.wizards.gui;

import com.levelmc.core.api.gui.ItemMenu;
import com.levelmc.core.api.gui.MenuItem;
import com.levelmc.core.api.gui.MenuItemClickHandler;
import com.levelmc.wizards.spells.Spell;
import com.levelmc.wizards.users.Wizard;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class WizardSpellsGUI extends ItemMenu {
    public WizardSpellsGUI(Wizard wizard) {
        super("Wizardry ► Spells", 6);

        int index = 0;
        for (Spell spell : wizard.getSpells()) {
            item(index++, new MenuItem(spell.getMenuItem(wizard.getPlayer())).onClickHandler(new MenuItemClickHandler() {
                @Override
                public void onClick(MenuItem item, Player player, ClickType type) {

                }
            }));
        }
    }
}
