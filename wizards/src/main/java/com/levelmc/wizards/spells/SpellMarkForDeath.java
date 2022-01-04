package com.levelmc.wizards.spells;

import com.levelmc.core.chat.Chat;
import com.levelmc.core.api.particle.imagerenderer.Axis;
import com.levelmc.core.api.particle.imagerenderer.ParticleImageRenderer;
import com.levelmc.core.api.thread.IterationLimitedRunnable;
import com.levelmc.core.api.world.LocationUtils;
import com.levelmc.wizards.Wizards;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class SpellMarkForDeath extends ParticleSpell {
    private static final String skullLocation = "plugins/LevelCore/imgs/skull.png";

    public SpellMarkForDeath() {
        super(Particle.CLOUD, 0.15, 550, 2.1, "mark-for-death", MagicType.AURAS, 3, 50, 15);
    }

    @Override
    public void onSpellCast(Player player) {

    }

    protected class SkullMarkEffect extends IterationLimitedRunnable {

        private UUID pid;
        private LivingEntity entity;

        public SkullMarkEffect(Player player, LivingEntity entity) {
            super(10);

            this.pid = player.getUniqueId();
            this.entity = entity;
        }

        @Override
        public void onRun() {


        }
    }

    @Override
    public void onSpellHitEntity(Player player, Entity entity) {

        /* implement aoe at level 3 */

        if (!(entity instanceof LivingEntity)) {
            return;
        }

        LivingEntity le = (LivingEntity) entity;

        try {
            Location loc = le.getLocation().add(0, 5, 0);
            Vector directionalOffset = loc.getDirection();
            directionalOffset.setY(0);
            loc.add(directionalOffset);

            ParticleImageRenderer imageRenderer = new ParticleImageRenderer(new File(skullLocation), loc);

            imageRenderer.rotate(Axis.X, 90).rotate(Axis.Z, player.getLocation().getYaw());

            new IterationLimitedRunnable(10) {

                @Override
                public void onRun() {
                    if (!player.isOnline()) {
                        cancel();
                        return;
                    }

                    if (!entity.isValid() || entity.isDead()) {
                        cancel();
                        return;
                    }
                    imageRenderer.renderImage(LocationUtils.getPlayersInRadius(loc, 50));
                }
            }.runTaskTimer(Wizards.getInstance(), 5L, 10L);

        } catch (IOException e) {
            e.printStackTrace();
            Chat.msg(player, "&cError displaying skull file");
        }
    }

    @Override
    public void onSpellHitBlock(Player player, Block block) {
    }

    @Override
    public int getManaCost(Player player, int level) {
        if (level > getDefaultMaxLevel()) {
            return 10;
        }

        switch (level) {
            default:
            case 1:
                return 20;
            case 2:
                return 16;
            case 3:
                return 11;
        }
    }

    @Override
    public int getExpAward(Player player, int level) {
        if (level > getDefaultMaxLevel()) {
            return 110;
        }


        return level * 34;
    }

    @Override
    public List<String> getSpellDescription(Player player) {
        return Arrays.asList("&cEnemies marked for death take more damage");
    }
}
