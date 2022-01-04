package com.levelmc.skreet.users;

import com.levelmc.core.api.players.User;
import com.levelmc.core.api.yml.Path;
import com.levelmc.skreet.Skreet;
import com.levelmc.skreet.gangs.Gang;
import com.levelmc.skreet.gangs.GangType;
import com.levelmc.skreet.tags.TagManager;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SkreetPlayer extends User {

    @Path("gang-type")
    @Getter
    private GangType gangType = null;

    @Path("unlocked-tags")
    @Getter
    private List<String> unlockedTags = new ArrayList<>();


    public SkreetPlayer(Player player) {
        super(player);
    }

    public Gang getGang() {
        return gangType.get();
    }

    public boolean hasGang() {
        return gangType != null;
    }

    public void setGang(GangType type) {
        this.gangType = type;
    }

    public boolean hasTag(String name) {
        return unlockedTags.contains(name);
    }

    public boolean unlockTag(String name) {
        TagManager tagManager = Skreet.getInstance().getTagManager();
        if (!tagManager.isTag(name)) {
            return false;
        }

        if (hasTag(name)) {
            return false;
        }
        unlockedTags.add(name);
        return true;
    }
}
