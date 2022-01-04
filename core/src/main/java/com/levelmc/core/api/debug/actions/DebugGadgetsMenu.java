package com.levelmc.core.api.debug.actions;

import com.levelmc.core.api.debug.DebugAction;
import com.levelmc.core.api.menus.GadgetsMenu;
import org.bukkit.entity.Player;

public class DebugGadgetsMenu extends DebugAction {
    private static DebugGadgetsMenu instance = null;

    public static DebugGadgetsMenu getInstance() {
        if (instance == null) {
            instance = new DebugGadgetsMenu();
        }

        return instance;
    }

    protected DebugGadgetsMenu() {
        super("tc-gadgets-menu");
    }

    @Override
    public void onDebug(Player player, String[] args) {
        GadgetsMenu.getInstance().openMenu(player);
    }
}
