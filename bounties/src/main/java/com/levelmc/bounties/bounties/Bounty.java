package com.levelmc.bounties.bounties;

import com.levelmc.bounties.Bounties;
import com.levelmc.core.LevelCore;
import com.levelmc.core.api.yml.Path;
import com.levelmc.core.api.yml.YamlConfig;
import lombok.Getter;
import lombok.Setter;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Bounty extends YamlConfig {

    @Path("owner-id")
    @Getter
    private UUID posterUuid = null;

    @Path("owner-name")
    @Getter
    private String posterName = null;

    @Path("target-name")
    @Getter
    private String targetName = null;

    @Path("target-id")
    @Getter
    private UUID targetUuid = null;

    @Path("reward.money")
    @Getter
    @Setter
    private double rewardMoney = 0;

    @Path("status.claimed")
    @Getter
    @Setter
    private boolean claimed = false;

    @Path("status.killer-user-id")
    @Getter
    private UUID killerUuid = null;


    public Bounty(Player poster, Player target, double rewardMoney) {
        this.posterUuid = poster.getUniqueId();
        this.posterName = poster.getName();
        this.targetUuid = target.getUniqueId();
        this.targetName = target.getName();
        this.rewardMoney = rewardMoney;
    }

    public boolean claim(Player claiming) {
        if (claimed) {
            return false;
        }

        claimed = true;
        killerUuid = claiming.getUniqueId();

        EconomyResponse response = Bounties.getInstance().getEconomy().depositPlayer(claiming,rewardMoney);
        return response.transactionSuccess();
    }

    public boolean isTargetOnline() {
        return Bukkit.getPlayer(targetUuid) != null;
    }
}