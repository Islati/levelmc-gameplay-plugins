package com.levelmc.core.api.debug.actions;

import com.levelmc.core.LevelCore;
import com.levelmc.core.chat.Chat;
import com.levelmc.core.api.debug.DebugAction;
import com.levelmc.core.api.gui.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class DebugItemMenu extends DebugAction {


    public static class TestItemMenu extends ItemMenu {

        private static TestItemMenu debugItemMenu = null;

        public static TestItemMenu getInstance() {
            if (debugItemMenu == null) {
                debugItemMenu = new TestItemMenu();
            }

            return debugItemMenu;
        }

        private BukkitRunnable updateMenuItem = null;

        protected TestItemMenu() {
            super(Chat.colorize("&cDebug Menu"), 1);

            fillEmpty(new MenuItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)));

            item(0, new MenuItem(new ItemStack(Material.RED_WOOL)));

            behaviour(MenuAction.OPEN, new MenuBehaviour() {
                @Override
                public void doAction(Menu menu, Player player) {
                    if (updateMenuItem == null || updateMenuItem.isCancelled()) {

                        /* Update thread to change menu items back and forth */
                        updateMenuItem = new BukkitRunnable() {
                            private boolean isGreen = false;

                            @Override
                            public void run() {
                                TestItemMenu dMenu = (TestItemMenu) menu;

                                if (dMenu.getViewers().size() == 0) {
                                    cancel();
                                    LevelCore.getInstance().getLogger().info("Skipping debug menu refresh due to no viewers");
                                    return;
                                }

                                if (!isGreen) {
                                    dMenu.item(0, new MenuItem(new ItemStack(Material.GREEN_WOOL)));
                                } else {
                                    dMenu.item(0, new MenuItem(new ItemStack(Material.RED_WOOL)));
                                }

                                isGreen = !isGreen;
                                dMenu.updateMenu();
                                LevelCore.getInstance().getLogger().info("Ran menu update on debug menu.");
                            }
                        };

                        updateMenuItem.runTaskTimer(LevelCore.getInstance(), 20, 45);
                    }
                }
            });
        }
    }


    private static DebugItemMenu instance = null;

    public static DebugItemMenu getInstance() {
        if (instance == null) {
            instance = new DebugItemMenu();
        }

        return instance;
    }

    public DebugItemMenu() {
        super("tc-debug-menu");
    }

    @Override
    public void onDebug(Player player, String[] args) {
        TestItemMenu.getInstance().openMenu(player);
        Chat.msg(player, "&a-> &eMenu Opened");
    }
}
