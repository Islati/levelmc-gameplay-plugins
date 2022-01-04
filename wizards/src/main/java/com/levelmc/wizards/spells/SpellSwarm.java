package com.levelmc.wizards.spells;

import com.levelmc.core.api.entities.EntityUtils;
import com.levelmc.core.api.utils.NumberUtil;
import com.levelmc.core.api.utils.SoundUtils;
import com.levelmc.core.api.world.LocationUtils;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public class SpellSwarm extends ParticleSpell {
    public SpellSwarm() {
        super(Particle.CAMPFIRE_COSY_SMOKE, 0.3, 600, 2, "swarm", MagicType.WHITE, 3,20,8);
    }

    @Override
    public void onSpellCast(Player player) {

    }

    @Override
    public void onSpellHitEntity(Player player, Entity entity) {
        handleTeleportToCaster(player,entity.getLocation());
    }

    @Override
    public void onSpellHitBlock(Player player, Block block) {
        handleTeleportToCaster(player,block.getLocation());
    }

    protected double getRadius(Player player, int lvl) {
        if (lvl < getDefaultMaxLevel()) {
            return 6.5;
        }

        switch (lvl) {
            default:
            case 1:
                return 2.5;
            case 2:
                return 3.8;
            case 3:
                return 4.9;
        }
    }

    protected void sendEffect(Location loc) {
        for(int i = 0; i < 5; i++) {
            loc.getWorld().spawnParticle(Particle.COMPOSTER,loc.add(NumberUtil.randomDouble(0.1,1.1),NumberUtil.randomDouble(0.1,1.1),NumberUtil.randomDouble(0.1,1.1)),1);
        }
    }

    protected void handleTeleportToCaster(Player player, Location regionCenter) {
        int playerLvl = getPlayerSpellLevel(player);
        double radius = getRadius(player,playerLvl);
        List<Location> effectSpiral = LocationUtils.getSpiral(regionCenter,0.69f,getRadius(player,playerLvl),new BigDecimal(radius).floatValue(),0.2f);
        for(Location loc : effectSpiral) {
            loc.getWorld().spawnParticle(Particle.SQUID_INK,loc,1);
        }

        Set<LivingEntity> nearbyEnemies = EntityUtils.getLivingEntitiesNearLocation(regionCenter,getRadius(player,getPlayerSpellLevel(player)));

        for(LivingEntity entity : nearbyEnemies) {
            if (entity instanceof Player) {
                continue;
            }

            sendEffect(regionCenter);
            entity.teleport(player);
            SoundUtils.playSoundDistant(player,entity.getEyeLocation().add(0,1,0), Sound.ITEM_CHORUS_FRUIT_TELEPORT);
        }

    }

    @Override
    public int getManaCost(Player player, int level) {
        return 0;
    }

    @Override
    public int getExpAward(Player player, int level) {
        return 0;
    }

    @Override
    public List<String> getSpellDescription(Player player) {
        return null;
    }
}
