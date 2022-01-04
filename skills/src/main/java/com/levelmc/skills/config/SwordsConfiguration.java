package com.levelmc.skills.config;

import com.levelmc.core.api.yml.Path;
import com.levelmc.core.api.yml.YamlConfig;
import org.bukkit.entity.EntityType;

import java.util.HashMap;
import java.util.Map;

public class SwordsConfiguration extends YamlConfig {

    @Path("exp-rewards.mobs")
    private Map<String, Integer> mobKillRewards = new HashMap<>();

    @Path("exp-rewards.on-hit")
    private int expPerHit = 2;

    public SwordsConfiguration() {
        mobKillRewards.put(EntityType.BAT.name(), 5);
        mobKillRewards.put(EntityType.BEE.name(), 5);
        mobKillRewards.put(EntityType.BLAZE.name(), 15);
        mobKillRewards.put(EntityType.CAT.name(), 5);
        mobKillRewards.put(EntityType.CAVE_SPIDER.name(), 8);
        mobKillRewards.put(EntityType.CHICKEN.name(), 4);
        mobKillRewards.put(EntityType.ZOMBIFIED_PIGLIN.name(), 7);
        mobKillRewards.put(EntityType.GHAST.name(), 20);
        mobKillRewards.put(EntityType.WITCH.name(), 100);
        mobKillRewards.put(EntityType.CREEPER.name(), 10);
        mobKillRewards.put(EntityType.SKELETON.name(), 12);
        mobKillRewards.put(EntityType.SPIDER.name(), 10);
        mobKillRewards.put(EntityType.COW.name(), 4);
        mobKillRewards.put(EntityType.ENDERMAN.name(), 10);
        mobKillRewards.put(EntityType.ENDER_DRAGON.name(), 100);
        mobKillRewards.put(EntityType.EVOKER.name(), 50);
        mobKillRewards.put(EntityType.ELDER_GUARDIAN.name(), 100);
        mobKillRewards.put(EntityType.ENDERMITE.name(), 5);
        mobKillRewards.put(EntityType.FOX.name(), 5);
        mobKillRewards.put(EntityType.GIANT.name(), 30);
        mobKillRewards.put(EntityType.HOGLIN.name(), 10);
        mobKillRewards.put(EntityType.GUARDIAN.name(), 15);
        mobKillRewards.put(EntityType.HUSK.name(), 10);
        mobKillRewards.put(EntityType.ILLUSIONER.name(), 10);
        mobKillRewards.put(EntityType.MAGMA_CUBE.name(), 15);
        mobKillRewards.put(EntityType.MUSHROOM_COW.name(), 10);
        mobKillRewards.put(EntityType.MULE.name(), 10);
        mobKillRewards.put(EntityType.OCELOT.name(), 5);
        mobKillRewards.put(EntityType.PANDA.name(), 5);
        mobKillRewards.put(EntityType.RABBIT.name(), 5);
        mobKillRewards.put(EntityType.POLAR_BEAR.name(), 10);
        mobKillRewards.put(EntityType.PIG.name(), 10);
        mobKillRewards.put(EntityType.SILVERFISH.name(), 5);
        mobKillRewards.put(EntityType.PARROT.name(), 5);
        mobKillRewards.put(EntityType.SHEEP.name(), 5);
        mobKillRewards.put(EntityType.SLIME.name(), 6);
        mobKillRewards.put(EntityType.SQUID.name(), 5);
        mobKillRewards.put(EntityType.STRIDER.name(), 8);
        mobKillRewards.put(EntityType.TURTLE.name(), 5);
        mobKillRewards.put(EntityType.SHULKER.name(), 10);
        mobKillRewards.put(EntityType.VEX.name(), 50);
        mobKillRewards.put(EntityType.VINDICATOR.name(), 15);
        mobKillRewards.put(EntityType.WITHER_SKELETON.name(), 10);
        mobKillRewards.put(EntityType.WITHER.name(), 100);
        mobKillRewards.put(EntityType.WOLF.name(), 10);
        mobKillRewards.put(EntityType.ZOMBIE.name(), 5);
        mobKillRewards.put(EntityType.ZOGLIN.name(), 5);
        mobKillRewards.put(EntityType.ZOMBIE_HORSE.name(), 5);
        mobKillRewards.put(EntityType.ZOMBIE_VILLAGER.name(), 5);
    }


    public boolean hasExpReward(EntityType type) {
        return mobKillRewards.containsKey(type.name());
    }

    public int getExpReward(EntityType type) {
        if (!hasExpReward(type)) {
            return 0;
        }

        return mobKillRewards.get(type.name());
    }
}
