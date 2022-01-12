package com.levelmc.skreet.gangs;

import com.levelmc.core.api.utils.PluginUtils;
import com.levelmc.core.api.yml.Path;
import com.levelmc.core.api.yml.YamlConfig;
import com.levelmc.skreet.events.PlayerJoinGangEvent;
import com.levelmc.skreet.users.SkreetPlayer;
import com.levelmc.skreet.users.SkreetPlayers;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Gang Instance. One instance per gang.
 */
public class Gang extends YamlConfig {
    @Getter
    @Path("name")
    private String name;

    @Getter
    @Path("type")
    private GangType type;

    @Path("members")
    @Getter
    private Map<UUID, String> membersMap = new HashMap<>();

    @Path("kills")
    @Getter
    private List<GangKill> gangKills = new ArrayList<>();

    public Gang() {

    }

    public Gang(GangType type) {
        this.name = type.toString();
        this.type = type;
    }

    public Gang(String name, Map<UUID, String> membersMap, List<GangKill> gangKills) {
        this.name = name;
        this.type = GangType.getByName(name);
        this.membersMap = membersMap;
        this.gangKills = gangKills;
    }

    /**
     * Add a player to the gang (internally).
     *
     * @param player
     */
    public boolean addMember(SkreetPlayer player) {
        if (isMember(player)) {
            return false;
        }

        if (player.hasGang()) {
            return false;
        }


        membersMap.put(player.getId(), player.getName());
        player.setGang(getType());

        var playerJoinGangEvent = new PlayerJoinGangEvent(player.getPlayer(), getType());
        PluginUtils.callEvent(playerJoinGangEvent);

        if (playerJoinGangEvent.isCancelled()) {
            membersMap.remove(player.getId());
            player.setGang(null);
            return false;
        }

        return true;
    }

    public boolean isMember(SkreetPlayer player) {
        return membersMap.containsKey(player.getId());
    }

    public boolean isMember(Player player) {
        return membersMap.containsKey(player.getUniqueId());
    }

    public boolean isMember(UUID id) {
        return membersMap.containsKey(id);
    }

    public String getDisplayName() {
        return getType().getDisplayName();
    }


    public void addKill(SkreetPlayer killer, SkreetPlayer killed, String reason) {
        gangKills.add(new GangKill(killer.getName(), killer.getId(), killed.getName(), killed.getId(), reason, killer.getGangType().toString()));
    }

    public Set<Player> getOnlinePlayers() {
        Set<Player> players = new HashSet<>();

        for (SkreetPlayer pl : SkreetPlayers.getInstance().getUsers().values()) {
            if (pl.getGangType() == getType()) {
                players.add(pl.getPlayer());
            }
        }

        return players;
    }
}
