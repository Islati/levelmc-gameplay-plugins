package com.levelmc.core.api.gui;

import com.levelmc.core.api.item.ItemBuilder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class MenuItem {
    public static final MenuItem GRAY_FILLER = new MenuItem(ItemBuilder.of(Material.GRAY_STAINED_GLASS_PANE).name("&7").item());

    @Getter
    @Setter
    private ItemMenu menu;

    @Setter
    @Getter
    private ItemStack itemStack = null;

    @Getter
    @Setter
    private MenuItemClickHandler clickHandler = null;

    public MenuItem(@NonNull ItemStack icon) {
        this.itemStack = icon.clone();

    }

    public void close(Player player) {
        getMenu().closeMenu(player);
    }

    public MenuItem onClickHandler(MenuItemClickHandler clickHandler) {
        this.clickHandler = clickHandler;
        return this;
    }

    public void onClick(Player player, ClickType clicktype) {
        if (getClickHandler() == null) {
            return;
        }

        getClickHandler().onClick(this, player, clicktype);
    }
}
