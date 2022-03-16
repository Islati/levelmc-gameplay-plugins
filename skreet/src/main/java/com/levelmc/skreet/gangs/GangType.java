package com.levelmc.skreet.gangs;

import lombok.Getter;

public enum GangType {
    CRIPS("Crips", "&bCrips", "&b"),
    BLOODS("Bloods", "&cBloods", "&c"),
    BACCAS("Baccas", "&0Baccas", "&0"),
    ANGELS("Angels", "&6Angel", "&6"),
    PAGANS("Pagans", "&aPagan", "&a");

    private String name;

    @Getter
    private String displayName;

    @Getter
    private String colorPrefix;

    GangType(String name, String displayName, String colorPrefix) {
        this.name = name;
        this.displayName = displayName;
        this.colorPrefix = colorPrefix;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public Gang get() {
        return GangManager.get().getGang(this.name);
    }

    public static GangType getByName(String name) {
        switch (name.toLowerCase()) {
            case "crips":
                return GangType.CRIPS;
            case "bloods":
                return GangType.BLOODS;
            case "baccas":
                return GangType.BACCAS;
            case "angels":
                return GangType.ANGELS;
            case "pagans":
                return GangType.PAGANS;
            default:
                return null;
        }
    }
}
