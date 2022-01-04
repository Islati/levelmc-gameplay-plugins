package com.levelmc.wizards.spells;

import com.levelmc.core.api.particle.projectiles.ParticleProjectile;
import com.levelmc.core.api.utils.PluginUtils;
import com.levelmc.wizards.events.SpellCastEvent;
import com.levelmc.wizards.events.SpellHitBlockEvent;
import com.levelmc.wizards.events.SpellHitEntityEvent;
import com.levelmc.wizards.events.SpellPreCastEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public abstract class ParticleSpell extends ParticleProjectile implements Spell {
    private String id;

    protected int defaultMaxLevel = 5;

    private MagicType type;

    private int defaultCooldown = 0;

    private int cooldownReductionPerLevel = 0;

    @Setter @Getter
    private Material menuMaterial = Material.PAPER;

    public ParticleSpell(Particle particle, double size, double velocity, double lifespan, String spellId, MagicType type, int defaultMaxLevel, int defaultCooldown, int cooldownReductionPerLevel) {
        super(particle, size, velocity, lifespan);
        this.id = spellId;
        this.defaultMaxLevel = defaultMaxLevel;
        this.type = type;
        this.defaultCooldown = defaultCooldown;
        this.cooldownReductionPerLevel = cooldownReductionPerLevel;
    }

    @Override
    public String id() {
        return id;
    }

    protected int getDefaultMaxLevel() {
        return defaultMaxLevel;
    }

    public int getMaxLevel(Player player) {
        return defaultMaxLevel + getUser(player).getExtraMaxLevel(id());
    }

    @Override
    public int getCooldown(Player player, int level) {
        int resolvedCooldown = defaultCooldown - (cooldownReductionPerLevel * level);

        return Math.max(resolvedCooldown, 0);
    }

    @Override
    public void onCast(Player player) {

        SpellPreCastEvent preCastEvent = new SpellPreCastEvent(player, this);
        PluginUtils.callEvent(preCastEvent);

        if (preCastEvent.isCancelled()) {
            return;
        }

        launch(player, player.getWorld(), Vector.getRandom().subtract(Vector.getRandom()).multiply(4.0), true);

        SpellCastEvent event = new SpellCastEvent(player, this);
        PluginUtils.callEvent(event);
    }

    @Override
    public MagicType getType() {
        return type;
    }


    @Override
    public void onHitEntity(Player player, Entity hit) {
        SpellHitEntityEvent event = new SpellHitEntityEvent(player, this, hit);
        PluginUtils.callEvent(event);

        if (event.isCancelled()) {
            return;
        }

        onSpellHitEntity(player, hit);
    }

    @Override
    public void onHitBlock(Player player, Block block) {
        SpellHitBlockEvent event = new SpellHitBlockEvent(player, this, block);
        PluginUtils.callEvent(event);

        if (event.isCancelled()) {
            return;
        }

        onSpellHitBlock(player, block);
    }

    public abstract void onSpellCast(Player player);

    public abstract void onSpellHitEntity(Player player, Entity entity);

    public abstract void onSpellHitBlock(Player player, Block block);
}
