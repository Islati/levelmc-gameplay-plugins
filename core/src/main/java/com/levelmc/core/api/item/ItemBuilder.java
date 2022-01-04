package com.levelmc.core.api.item;

import com.levelmc.core.chat.Chat;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.*;

public class ItemBuilder {
    public static ItemBuilder of(Material material) {
        return new ItemBuilder(material);
    }

    public static ItemBuilder clone(ItemStack item) {
        return new ItemBuilder(item);
    }

    private ItemStack itemStack;

    public ItemBuilder(Material material) {
        this.itemStack = new ItemStack(material);
    }

    public ItemBuilder(ItemStack item) {
        this.itemStack = item.clone();
    }

    public ItemBuilder name(String name) {
        ItemMeta meta = itemStack.getItemMeta();

        meta.setDisplayName(Chat.colorize(name));
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder durability(short amt) {
        itemStack.setDurability(amt);
        return this;
    }

    public ItemBuilder data(MaterialData data) {
        itemStack.setData(data);
        return this;
    }

    public ItemBuilder lore(String... lines) {
        ItemUtils.setLore(itemStack, Arrays.asList(lines));
        return this;
    }

    public ItemBuilder lore(List<String> lines) {
        ItemUtils.setLore(itemStack, lines);
        return this;
    }

    public ItemBuilder addLore(String... lines) {
        ItemUtils.addLore(itemStack, lines);
        return this;
    }

    public ItemBuilder addLore(List<String> lines) {
        ItemUtils.addLore(itemStack, lines);
        return this;
    }

    public ItemBuilder enchant(Enchantment enchant, int level) {
        ItemUtils.addEnchantment(itemStack, enchant, level, true);
        return this;
    }

    public ItemBuilder amount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    public ItemStack item() {
        return itemStack.clone();
    }
}
