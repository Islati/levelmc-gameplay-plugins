package com.levelmc.wizards.spells;

import lombok.Getter;
import org.bukkit.ChatColor;

public enum MagicType {
    FIRE("&4&l", "Fire Magic"),
    WATER("&2&l", "Water Magic"),
    EARTH("&a&l", "Earth Magic"),
    AIR("&b&l", "Air Magic"),
    DARK(ChatColor.DARK_PURPLE + "&l", "Dark Magic"),
    WHITE("&f&l", "White Magic"),
    AURAS(ChatColor.GOLD + "&l","Aura's");
//    CURSE("&c&l", "Curses"),
//    AURA("&d&l", "Auras");

    private String magicName;

    @Getter
    private String namePrefix;

    MagicType(String prefix, String name) {
        this.namePrefix = prefix;
        this.magicName = name;
    }

    public String getMagicName() {
        return String.format("%s%s", namePrefix, magicName);
    }
}
