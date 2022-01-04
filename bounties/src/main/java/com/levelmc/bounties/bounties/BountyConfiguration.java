package com.levelmc.bounties.bounties;

import com.levelmc.core.api.yml.Comments;
import com.levelmc.core.api.yml.Path;
import com.levelmc.core.api.yml.YamlConfig;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

public class BountyConfiguration extends YamlConfig {

    @Path("skull.drop-on-death")
    @Getter
    private boolean skullDroppedOnDeath = false;

    @Path("skull.bleed-time")
    @Getter
    private double bleedTimeSeconds = 4;

    @Path("npc.spawn-location")
    @Getter
    @Setter
    private Location npcSpawnLocation = null;

    @Path("npc.name")
    @Getter
    @Setter
    private String name = "&cBounty Master";

    @Path("npc.key")
    @Comments({"Used to identify the bounty master","If changed you may have a left-over npc spawned somewhere"})
    @Getter
    private String npcIdKey = "bountyHunterMaster";

    public BountyConfiguration() {

    }
}
