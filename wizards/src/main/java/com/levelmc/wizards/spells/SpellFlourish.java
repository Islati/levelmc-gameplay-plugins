package com.levelmc.wizards.spells;

import com.levelmc.core.api.utils.SoundUtils;
import com.levelmc.core.api.world.BlockUtils;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class SpellFlourish extends ParticleSpell {
    public SpellFlourish() {
        super(Particle.COMPOSTER, 0.15, 450, 2.5, "flourish", MagicType.EARTH, 3, 60, 15);
    }

    @Override
    public void onSpellCast(Player player) {
        spawnParticles(player.getEyeLocation());
    }

    protected void spawnParticles(Location loc) {
        World world = loc.getWorld();

        world.spawnParticle(Particle.COMPOSTER, loc.add(0.5, 0.5, 0.75), 1);
        world.spawnParticle(Particle.COMPOSTER, loc.add(-0.5, 0.5, 0.75), 1);
        world.spawnParticle(Particle.COMPOSTER, loc.add(-0.5, -0.5, 0.75), 1);
        world.spawnParticle(Particle.COMPOSTER, loc.add(0.5, -0.5, 0.75), 1);
        world.spawnParticle(Particle.COMPOSTER, loc.add(0, 1.2, 0), 1);

        world.spawnParticle(Particle.HEART, loc.add(0, 1.8, 0), 1);
    }

    protected int getRadius(Player player, int level) {
        if (level > getDefaultMaxLevel()) {
            return 6;
        }

        switch (level) {
            default:
            case 1:
                return 3;
            case 2:
                return 4;
            case 3:
                return 5;
        }
    }

    @Override
    public void onSpellHitEntity(Player player, Entity entity) {
        /* todo temporarily put a flower pot on their head. */
    }

    @Override
    public void onSpellHitBlock(Player player, Block block) {
        int playerLevel = getPlayerSpellLevel(player);
        List<Block> blocksInRadius = BlockUtils.getCubeFilled(block.getLocation(), getRadius(player, playerLevel));

        for (Block bl : blocksInRadius) {
            BlockData data = bl.getBlockData();

            if (!(data instanceof Ageable)) {
                continue;
            }

            Ageable ageable = (Ageable) data;

            int age = ageable.getAge();
            int maxAge = ageable.getMaximumAge();

            if (age == maxAge) {
                continue;
            }

            age += playerLevel + 2;
            if (age > maxAge) {
                age = maxAge;
            }

            ageable.setAge(age);
            bl.setBlockData(ageable);
            spawnParticles(bl.getLocation());
            SoundUtils.playSoundDistant(player, bl.getLocation(), Sound.BLOCK_CHORUS_FLOWER_GROW);
        }
    }

    @Override
    public int getManaCost(Player player, int level) {
        if (level > getDefaultMaxLevel()) {
            return 13;
        }

        switch (level) {
            default:
            case 1:
                return 30;
            case 2:
                return 24;
            case 3:
                return 19;
        }
    }

    @Override
    public int getExpAward(Player player, int level) {
        if (level > getDefaultMaxLevel()) {
            return 120;
        }
        return level * 38;
    }

    @Override
    public List<String> getSpellDescription(Player player) {
        return Arrays.asList("&aGrow your crops using the earths magic.");
    }
}
