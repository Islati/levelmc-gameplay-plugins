package com.levelmc.wizards.gui;

import com.levelmc.core.api.gui.ItemMenu;
import com.levelmc.core.api.gui.MenuItem;
import com.levelmc.core.api.gui.MenuItemClickHandler;
import com.levelmc.core.chat.Chat;
import com.levelmc.core.api.item.ItemBuilder;
import com.levelmc.wizards.Wizards;
import com.levelmc.wizards.spells.Spell;
import com.levelmc.wizards.spells.SpellBindSlot;
import com.levelmc.wizards.users.Wizard;
import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

/**
 * Collection of {@link ItemMenu} and {@link MenuItem} to provide the user with a spell binds menu,
 * choosing the spell they wish to bind and which slot (interaction wise) to use.
 */
public class SpellBindsGUI extends ItemMenu {

    public class SpellBindItem extends MenuItem {
        private SpellBindSlot slot;
        private Spell spell;

        public SpellBindItem(Player player, SpellBindSlot slot, Spell spell) {
            super(spell.getMenuItem(player));

            this.slot = slot;
            this.spell = spell;

            setClickHandler(new MenuItemClickHandler() {
                @Override
                public void onClick(MenuItem item, Player player, ClickType type) {
                    Wizard user = Wizards.getInstance().getUserManager().getUser(player);

                    user.setSpellBoundAtSlot(slot, spell);
                    Chat.format(player, "&aSpell in slot &7'&r%s&7'&a is &e%s", slot.getSlotName(), spell.id());
                    item.getMenu().closeMenu(player);
                }
            });
        }
    }

    public class SpellSelectionMenu extends ItemMenu {

        public SpellSelectionMenu(Wizard user, SpellBindSlot slot) {
            super("Spell Selection", 4);

            int i = 0;
            for (Spell spell : user.getSpells()) {
                item(i, new SpellBindItem(user.getPlayer(), slot, spell));
                i += 1;
            }
        }
    }

    public class SlotSelectionItem extends MenuItem {

        private SpellBindSlot slot;

        public SlotSelectionItem(@NonNull Wizard user, SpellBindSlot slot) {
            super(ItemBuilder.of(Material.PAPER).name(user.hasSpellBoundAtSlot(slot) ? user.getSpellBindIdAtSlot(slot) : "Nothing").lore("&7" + slot.getSlotName()).item());

            this.slot = slot;
            setClickHandler(new MenuItemClickHandler() {
                @Override
                public void onClick(MenuItem item, Player player, ClickType type) {
                    /* Todo implement spell categegory selection menu */

                    SpellSelectionMenu menu = new SpellSelectionMenu(user, slot);
                    item.getMenu().switchMenu(player, menu);
                }
            });
        }
    }

    public SpellBindsGUI(@NonNull Wizard user) {
        super("Skill Bindings", 1);

        item(1, new SlotSelectionItem(user, SpellBindSlot.LEFT_CLICK));
        item(3, new SlotSelectionItem(user, SpellBindSlot.RIGHT_CLICK));
        item(5, new SlotSelectionItem(user, SpellBindSlot.SHIFT_LEFT_CLICK));
        item(7, new SlotSelectionItem(user, SpellBindSlot.SHIFT_RIGHT_CLICK));
    }
}
