package com.levelmc.core.api.menus;

import com.levelmc.core.api.gadgets.Gadget;
import com.levelmc.core.api.gadgets.Gadgets;
import com.levelmc.core.api.gui.ItemMenu;
import com.levelmc.core.api.gui.MenuItem;
import com.levelmc.core.api.players.PlayerUtils;
import com.levelmc.core.api.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class GadgetsMenu extends ItemMenu {

    public class GadgetMenuItem extends MenuItem {
        private Gadget gadget;

        public GadgetMenuItem(Gadget gadget) {
            super(gadget.getItem());
            this.gadget = gadget;
        }

        @Override
        public void onClick(Player player, ClickType clicktype) {
            PlayerUtils.giveItem(player, gadget.getItem());
        }
    }

    private static GadgetsMenu instance = null;

    public static GadgetsMenu getInstance() {
        if (instance == null) {
            instance = new GadgetsMenu();
        }

        return instance;
    }

    protected GadgetsMenu() {
        super("Registered Gadgets", 5);

        fillEmpty(new MenuItem(ItemBuilder.of(Material.GRAY_STAINED_GLASS_PANE).name("&7").item()));

        int i = 0;
        for (Gadget gadget : Gadgets.getAllGadgets()) {
            item(i++, new GadgetMenuItem(gadget));
        }
    }
}
