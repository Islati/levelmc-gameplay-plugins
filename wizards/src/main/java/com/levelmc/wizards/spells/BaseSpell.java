package com.levelmc.wizards.spells;

import com.levelmc.core.api.raycast.DefaultRenderer;
import com.levelmc.core.api.raycast.Raycast;
import com.levelmc.core.api.raycast.ParticleRenderer;
import com.levelmc.core.api.utils.PluginUtils;
import com.levelmc.wizards.events.SpellCastEvent;
import com.levelmc.wizards.events.SpellHitBlockEvent;
import com.levelmc.wizards.events.SpellHitEntityEvent;
import com.levelmc.wizards.events.SpellPreCastEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * Abstraction implementation of the {@link Spell} interface.
 */
public abstract class BaseSpell implements Spell {

    @Getter
    @Setter
    private ParticleRenderer particleRenderer = new DefaultRenderer().particle(Particle.SPELL_INSTANT);

    private String id;

    private MagicType type;

    @Getter
    private int defaultMaxLevel;

    private int defaultCooldown;
    private int cooldownReductionPerLevel;

    @Getter @Setter
    private double hitboxSize = 0.2;

    public BaseSpell(String id, MagicType type, int defaultMaxLevel, int defaultCooldown, int cooldownReductionPerLevel) {
        this.id = id;
        this.type = type;
        this.defaultMaxLevel = defaultMaxLevel;
        this.defaultCooldown = defaultCooldown;
        this.cooldownReductionPerLevel = cooldownReductionPerLevel;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public int getCooldown(Player player, int level) {
        int resolvedCooldown = defaultCooldown - (cooldownReductionPerLevel * level);

        return Math.max(resolvedCooldown, 0);
    }

    @Override
    public int getMaxLevel(Player player) {
        return defaultMaxLevel + getUser(player).getExtraMaxLevel(id());
    }

    @Override
    public MagicType getType() {
        return type;
    }

    @Override
    public void onCast(Player player) {
        /* Raycast no animation + physics on it. */

        Raycast raycast = new Raycast(player.getEyeLocation(), 30);
        raycast.setOwner(player);
        raycast.setHitboxSize(getHitboxSize());
        raycast.setShowRayCast(true);
        raycast.setParticleRenderer(particleRenderer);

        /* Todo handle mana */
        SpellPreCastEvent preCastEvent = new SpellPreCastEvent(player, this);
        PluginUtils.callEvent(preCastEvent);

        if (preCastEvent.isCancelled()) {
            return;
        }

        onSpellCast(player);

        SpellCastEvent event = new SpellCastEvent(player, this);
        PluginUtils.callEvent(event);

        if (raycast.compute(Raycast.RaycastType.ENTITY_AND_BLOCK)) {

            if (raycast.hasBlock()) {
                onHitBlock(player, raycast.getHurtBlock());
                return;
            }

            if (raycast.hasEntity()) {
                onHitEntity(player, raycast.getHurtEntity());
                return;
            }
        }
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

    public abstract void onSpellHitBlock(Player player, Block block);

    public abstract void onSpellHitEntity(Player player, Entity entity);

    public abstract void onSpellCast(Player player);

    /* todo add methods to grab wand bonuses from player */
}
