package com.levelmc.core.api.gui;

import com.levelmc.core.api.item.ItemBuilder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class ToggleBooleanMenuItem extends MenuItem {

    private static ItemStack enabledItem = ItemBuilder.of(Material.GREEN_WOOL).name("&aEnabled").lore("&7- Click to &cDisable.").item();

    private static ItemStack disabledItem = ItemBuilder.of(Material.RED_WOOL).name("&cDisabled").lore("&7- Click to &aEnable").item();

    @Getter
    @Setter
    private boolean value = false;

    @Getter
    @Setter
    private ToggleAction callback = null;


    public ToggleBooleanMenuItem(boolean initialValue) {
        super(initialValue ? enabledItem : disabledItem);
        this.value = initialValue;
    }

    public ToggleBooleanMenuItem onToggle(ToggleAction action) {
        this.callback = action;
        return this;
    }

    @Override
    public void onClick(Player player, ClickType type) {
        this.value = !value;
        setItemStack(value ? enabledItem : disabledItem);
        getMenu().refresh();
        this.callback.onToggle(this,player,this.value);
    }

    public interface ToggleAction {
        void onToggle(MenuItem item, Player player, boolean newValue);
    }
}
