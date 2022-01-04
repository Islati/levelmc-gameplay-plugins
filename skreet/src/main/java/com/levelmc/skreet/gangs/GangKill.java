package com.levelmc.skreet.gangs;

import com.levelmc.core.api.yml.Path;
import com.levelmc.core.api.yml.YamlConfig;
import lombok.Getter;

import java.util.UUID;

/**
 * Recorded game kill (Represents a record given by the API
 */
public class GangKill extends YamlConfig {
    @Getter
    @Path("killer.name")
    private String killerName = "";
    @Getter
    @Path("killer.id")
    private UUID killerId = null;

    @Getter
    @Path("killer.gang-name")
    private String killerGangName;

    @Getter
    @Path("killed.name")
    private String killedName = "";
    @Getter
    @Path("killed.id")
    private UUID killedId = null;

    @Getter
    @Path("reason")
    private String reason;

    @Getter
    @Path("timestamp")
    private long deathTime = 0l;


    public GangKill(String killerName, UUID killerId, String killedName, UUID killedId, String reason, String killerGangName) {
        this.killerName = killerName;
        this.killerId = killerId;

        this.killedName = killedName;
        this.killedId = killedId;
        this.reason = reason;
        this.killerGangName = killerGangName;
        this.deathTime = System.currentTimeMillis();
    }

    public GangKill() {

    }
}
