package com.levelmc.skreet.gangs;

import lombok.Getter;

public enum GangType {
    CRIPS("Crips", "&bCrips","&b"),
    BLOODS("Bloods", "&cBloods","&c");

    private String name;

    @Getter
    private String displayName;

    @Getter
    private String colorPrefix;

    GangType(String name, String displayName,String colorPrefix) {
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
            default:
                return null;
        }
    }
}
