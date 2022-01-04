package com.levelmc.bounties.users;

import com.levelmc.core.api.players.User;
import com.levelmc.core.api.yml.Path;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

public class BountyUser extends User {

    @Getter
    @Setter
    @Path("heads-collected")
    private int headsCollected = 0;

    @Getter
    @Path("kills.active-streak")
    private int activeKillStreak = 0;

    @Getter
    @Path("kills.highest-streak")
    private int highestKillStreak = 0;

    @Getter
    @Setter
    @Path("kills.reward-points")
    private int killRewardPoints = 0;

    public BountyUser(Player player) {
        super(player);
    }

    public void incrementKillStreak() {
        activeKillStreak += 1;

        if (activeKillStreak >= highestKillStreak) {
            highestKillStreak = activeKillStreak;
        }
    }
}
