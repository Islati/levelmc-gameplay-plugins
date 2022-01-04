package com.levelmc.core.api.gui;

import com.levelmc.core.api.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class ConfirmationMenu extends ItemMenu {
    private ConfirmationMenuItem confirmItem = null;
    private ConfirmationMenuItem denyItem = null;

    private boolean actionPerformed = false;

    public static ConfirmationMenu of(String title) {
        return new ConfirmationMenu(title);
    }

    protected ConfirmationMenu(String title) {
        super(title, 1);
        item(MenuConfirmationOption.CONFIRM.getSlot(), confirmItem = new ConfirmationMenuItem(MenuConfirmationOption.CONFIRM, Menu::closeMenu));
		item(MenuConfirmationOption.DENY.getSlot(), denyItem = new ConfirmationMenuItem(MenuConfirmationOption.DENY, Menu::closeMenu));
    }

    public ConfirmationMenu onConfirm(Action onConfirm) {
        confirmItem = new ConfirmationMenuItem(MenuConfirmationOption.CONFIRM, onConfirm);
        item(MenuConfirmationOption.CONFIRM.getSlot(), confirmItem);
        return this;
    }

    public ConfirmationMenu onDeny(Action onDeny) {
        denyItem = new ConfirmationMenuItem(MenuConfirmationOption.DENY, onDeny);
        item(MenuConfirmationOption.DENY.getSlot(), denyItem);
        return this;
    }

    public ConfirmationMenu exitOnClickOutside(boolean exit) {
        exitOnClickOutside(exit);
        return this;
    }

    public ConfirmationMenu denyOnClose() {
        behaviour(MenuAction.CLOSE, (menu, player) -> {
            if (actionPerformed) {
                return;
            }

            denyItem.action.perform((Menu) menu, player);
        });
        return this;
    }

    public enum MenuConfirmationOption {
        CONFIRM(Material.GREEN_WOOL, 0),
        DENY(Material.RED_WOOL, 8);

        private Material icon;
        private int slot;

        MenuConfirmationOption(Material icon, int slot) {
            this.icon = icon;
            this.slot = slot;
        }

        public Material getIcon() {
            return icon;
        }

        public int getSlot() {
            return slot;
        }
    }

    public class ConfirmationMenuItem extends MenuItem {
        private Action action;

        public ConfirmationMenuItem(MenuConfirmationOption option, Action action) {
            super(ItemBuilder.of(option.getIcon()).name(option == MenuConfirmationOption.CONFIRM ? "&a&lConfirm" : "&c&lDeny").item());
            this.action = action;
        }

        @Override
        public void onClick(Player player, ClickType type) {
            actionPerformed = true;
            action.perform(getMenu(), player);
        }
    }

    @FunctionalInterface
    public interface Action {

        void perform(Menu menu, Player player);

    }
}
