package com.levelmc.core.api.gui;

import com.google.common.collect.Lists;
import com.levelmc.core.chat.Chat;
import com.levelmc.core.api.item.ItemUtils;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemMenu implements Menu {

    private int rows;
    private String title;
    private Inventory inventory;
    private Map<Integer, MenuItem> items = new HashMap<>();
    private boolean exitOnClickOutside = true;

    private Map<MenuAction, ArrayList<MenuBehaviour>> menuActions = new HashMap<>();

    private boolean refresh = false;

    public ItemMenu(String title, int rows) {
        this.title = Chat.colorize(title);
        this.rows = rows;
        menuActions.put(MenuAction.OPEN, Lists.newArrayList());
        menuActions.put(MenuAction.CLOSE, Lists.newArrayList());
    }

    @Override
    public Inventory getInventory() {
        if (inventory == null || refresh) {
            inventory = Bukkit.createInventory(this, rows * 9, title);
        }
        updateMenuItems();
        return inventory;
    }

    ;

    public void refresh() {

        if (inventory != null) {
            inventory.clear();
        }

        getInventory();
    }

    public ItemMenu fillEmpty(MenuItem item) {
        Inventory inv = getInventory();

        for (int i = 0; i < inv.getSize(); i++) {
            if (items.containsKey(i)) {
                continue;
            }

            ItemStack invItem = inv.getItem(i);
            if (invItem == null || ItemUtils.isAir(invItem)) {
                items.put(i, item);
            }
        }
        return this;
    }

    public ItemMenu onOpen(MenuBehaviour behaviour) {
        return behaviour(MenuAction.OPEN, behaviour);
    }

    public ItemMenu onClose(MenuBehaviour behaviour) {
        return behaviour(MenuAction.CLOSE, behaviour);
    }

    public ItemMenu behaviour(MenuAction type, MenuBehaviour behaviour) {
        menuActions.get(type).add(behaviour);
        return this;
    }

    public ItemMenu item(int index, @NonNull MenuItem item) {
        item.setMenu(this);
        this.items.put(index, item);
        getInventory().setItem(index, item.getItemStack());
        return this;
    }

    public ItemMenu exitOnClickOutside(boolean exit) {
        exitOnClickOutside = exit;
        return this;
    }

    public List<MenuBehaviour> getBehaviours(MenuAction type) {
        return menuActions.get(type);
    }

    @Override
    public boolean exitOnClickOutside() {
        return exitOnClickOutside;
    }

    private void updateMenuItems() {
        inventory.clear();

        for (Map.Entry<Integer, MenuItem> menuItem : items.entrySet()) {
            inventory.setItem(menuItem.getKey(), menuItem.getValue().getItemStack());
        }
    }

    public boolean removeItem(int index) {
        ItemStack slot = inventory.getItem(index);

        if (slot == null) {
            return false;
        }

        inventory.setItem(index, null);
        items.remove(index);
        return true;
    }

    public void selectMenuItem(Player player, int index, ClickType type) {
        if (!items.containsKey(index)) {
            return;
        }

        MenuItem item = items.get(index);
        item.onClick(player, type);
    }

    public boolean isViewing(Player player) {
        return getViewers().contains((HumanEntity) player);
    }

    public void closeMenu() {
        Inventory inv = getInventory();

        List<MenuBehaviour> closeActions = getBehaviours(MenuAction.CLOSE);
        if (closeActions.size() > 0) {
            for (HumanEntity viewer : getViewers()) {
                if (!(viewer instanceof Player)) {
                    continue;
                }

                Player p = (Player) viewer;

                p.closeInventory();

                for (MenuBehaviour onClose : closeActions) {
                    onClose.doAction(this, p);
                }
            }
            return;
        }

        for (HumanEntity viewer : getViewers()) {
            if (!(viewer instanceof Player)) {
                continue;
            }

            viewer.closeInventory();
        }
    }

    public void switchMenu(Player player, Menu toMenu) {
        Menus.switchMenu(player, this, toMenu);
    }

    public MenuItem getItem(int index) {
        return items.get(index);
    }
}
