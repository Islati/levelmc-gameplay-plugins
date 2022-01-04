package com.levelmc.skreet.gangs;

import lombok.Getter;

public enum GangType {
    CRIPS("Crips", "&bCrips"),
    BLOODS("Bloods", "&cBloods");

    private String name;

    @Getter
    private String displayName;

    GangType(String name, String displayName) {
        this.name = name;
        this.displayName = displayName;
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
