package com.levelmc.core.api.gui;

import com.levelmc.core.LevelCore;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Menus {
    private static int[] ROW_PLACEMENTS = new int[54];

    static {
        for (int i = 0; i < ROW_PLACEMENTS.length; i++) {
            ROW_PLACEMENTS[i] = getRows(i);
        }
    }

    public static int getRows(int itemCount) {
        return ((int) Math.ceil(itemCount / 9.0D));
    }

    /**
     * Get the amount of rows required to place an item at the given
     * index of a menus.
     *
     * @param index index to get the rows for.
     * @return amount of rows an inventory will require to place an item at the given index.
     */
    public static int getRowsForIndex(int index) {
        if (index < 0) {
            index = 0;
        }

        if (index >= ROW_PLACEMENTS.length) {
            index = 53;
        }

        return ROW_PLACEMENTS[index];
    }

    public static void switchMenu(final Player player, Menu fromMenu, Menu toMenu) {
        fromMenu.closeMenu(player);

        new BukkitRunnable() {
            @Override
            public void run() {
                toMenu.openMenu(player);
            }
        }.runTaskLater(LevelCore.getInstance(), 2);
    }
}
