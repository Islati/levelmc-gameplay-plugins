package com.levelmc.bounties.bounties;

import com.levelmc.core.api.yml.Path;
import com.levelmc.core.api.yml.YamlConfig;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Stores all information for active bounties, statistics on bounties, and more.
 */
public class BountyManager extends YamlConfig {

    @Path("bounties")
    private List<Bounty> allBounties = new ArrayList<>();

    public BountyManager() {

    }

    public void addBounty(Player owner, Player target, double rewardMoney) {
        Bounty bounty = new Bounty(owner, target, rewardMoney);
        allBounties.add(bounty);
    }

    public boolean isTarget(Player player) {
        return getBounty(player) != null;
    }

    public boolean isTarget(UUID id) {
        return getBounty(id) != null;
    }

    public Bounty getBounty(Player target) {
        return getBounty(target.getUniqueId());
    }

    public Bounty getBounty(UUID targetId) {
        for (Bounty b : allBounties) {
            if (!b.isClaimed() && b.getTargetUuid().equals(targetId)) {
                return b;
            }
        }
        return null;
    }

    public void removeBounty(UUID target) {
        List<Bounty> newBountiesList = new ArrayList<>();

        for (Bounty b : allBounties) {
            if (b.getTargetUuid().equals(target)) {
                continue;
            }

            newBountiesList.add(b);
        }

        this.allBounties = newBountiesList;
    }

    public boolean hasBounties() {
        return !allBounties.isEmpty();
    }

    public List<Bounty> getAllBounties() {
        //todo sort by online then offline
        return allBounties.stream().sorted(new Comparator<Bounty>() {
            @Override
            public int compare(Bounty bounty, Bounty t1) {
                if (bounty.isClaimed()) {
                    return -1;
                }

                if (bounty.isTargetOnline()) {
                    if (t1.isTargetOnline()) {
                        return 0;
                    }

                    return 1;
                }

                return -1;
            }
        }).collect(Collectors.toList());
    }

    public List<Bounty> getOnlinePlayerBounties() {
        return getAllBounties().stream().filter(Bounty::isTargetOnline).collect(Collectors.toList());
    }
}
