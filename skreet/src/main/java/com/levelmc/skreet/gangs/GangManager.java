package com.levelmc.skreet.gangs;

import com.levelmc.core.api.yml.Path;
import com.levelmc.core.api.yml.Skip;
import com.levelmc.core.api.yml.YamlConfig;

import java.util.HashMap;
import java.util.Map;

public class GangManager extends YamlConfig {

    @Skip
    private static GangManager instance = null;

    @Path("gangs")
    private Map<String, Gang> gangMap = new HashMap<>();

    public static GangManager get() {
        if (instance == null) {
            instance = new GangManager();
        }

        return instance;
    }

    public GangManager() {
        for(GangType type : GangType.values()) {
            gangMap.put(type.toString(),new Gang(type));
        }
    }

    public Gang getGang(String name) {
        return gangMap.get(name);
    }

    public Gang getGang(GangType type) {
        return getGang(type.toString());
    }

}
