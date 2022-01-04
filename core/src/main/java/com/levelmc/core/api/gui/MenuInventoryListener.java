package com.levelmc.core.api.gui;

import com.levelmc.core.LevelCore;
import com.levelmc.core.chat.Chat;
import com.levelmc.core.api.gadgets.Gadget;
import com.levelmc.core.api.gadgets.Gadgets;
import com.levelmc.core.api.inventory.InventoryUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;

/**
 * Provides the hooks for the GUI Abstraction
 */
public class MenuInventoryListener implements Listener {

    public MenuInventoryListener(LevelCore core) {
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onMenuClick(InventoryClickEvent event) {
        //Get the inventory that's being clicked
        Inventory inventory = event.getInventory();
        Player player = (Player) event.getWhoClicked();

        InventoryHolder holder = inventory.getHolder();

        if (!(holder instanceof ItemMenu)) {
            return;
        }
        event.setCancelled(true);
        ItemMenu menu = (ItemMenu) holder;
        //If the player is clicking outside the menus, and it closes when clicking outside, then close it!

        if (event.getSlotType() == InventoryType.SlotType.OUTSIDE) {
            if (menu.exitOnClickOutside()) {
                menu.closeMenu(player);
            }
        }
        int index = event.getRawSlot();
        //if the players selecting within bounds of the inventory, then act accordingly
        if (index < inventory.getSize()) {
            menu.selectMenuItem(player, index, event.getClick());
        } else {
            //If they're gonna mess with their inventory, they don't need a menus open.
            if (menu.exitOnClickOutside()) {
                menu.closeMenu(player);
            }
        }

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        //Get the inventory that's being clicked
        Inventory inventory = event.getInventory();
        InventoryType inventoryType = inventory.getType();
        Player player = (Player) event.getWhoClicked();
        if (event.isCancelled()) {
            return;
        }

        switch (inventoryType) {
            case WORKBENCH:
                break;
            case PLAYER:
                PlayerInventory pInv = (PlayerInventory) inventory;
                InventoryAction action = event.getAction();
                int slot = event.getSlot();

                /*
                Check if the slot that was clicked is the players offhand inventory slot.
                 */
                if (slot == InventoryUtils.PLAYER_OFF_HAND_ITEM_SLOT) {
                    ItemStack cursorItem = event.getCursor();

                    if (cursorItem != null && cursorItem.getType() != Material.AIR) {
                        break;
                    }

                    if (Gadgets.isGadget(LevelCore.getInstance(), cursorItem)) {
                        Gadget movingGadget = Gadgets.getGadget(LevelCore.getInstance(), cursorItem);
                        if (!movingGadget.properties().isOffhandEquippable()) {
                            Chat.message(player, "&cThis is not off hand equipable");
                            event.setCancelled(true);
                            break;
                        }
                    }
                }
                break;
            case CHEST:
                break;
            default:
                break;
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        Inventory inventory = e.getInventory();
        InventoryHolder holder = inventory.getHolder();
        Player player = (Player) e.getPlayer();
        if (holder instanceof ItemMenu) {
            ItemMenu menu = (ItemMenu) holder;
            List<MenuBehaviour> openBehaviours = menu.getBehaviours(MenuAction.OPEN);
            if (openBehaviours != null) {
                openBehaviours.stream().filter(behaviour -> behaviour != null).forEach(behaviour -> behaviour.doAction(menu, player));
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        InventoryType inventoryType = inventory.getType();
        Player player = (Player) event.getPlayer();
        InventoryHolder holder = inventory.getHolder();
        if (holder instanceof ItemMenu) {
            ItemMenu menu = (ItemMenu) holder;
            List<MenuBehaviour> closeBehaviours = menu.getBehaviours(MenuAction.CLOSE);
            if (closeBehaviours != null) {
                closeBehaviours.stream().filter(behaviour -> behaviour != null).forEach(behaviour -> behaviour.doAction(menu, player));
            }
        }
    }
}
