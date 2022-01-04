package com.levelmc.loot.api.abilities;

import com.levelmc.core.api.inventory.HandSlot;
import com.levelmc.core.api.players.PlayerUtils;
import com.levelmc.loot.Loot;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

import java.util.Set;

public class AbilityListener implements Listener {


    private Loot parent;
    public AbilityListener(Loot parent) {
        this.parent = parent;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDamageEntity(EntityDamageByEntityEvent e) {
        Entity damager = e.getDamager();

        Player attacker = null;

        if (damager instanceof Projectile) {
            Projectile projectile = (Projectile) damager;
            ProjectileSource source = projectile.getShooter();
            if (!(source instanceof Player)) {
                return;
            }

            attacker = (Player) source;
        }

        if (damager instanceof Player) {
            attacker = (Player) damager;
        }

        if (attacker == null) {
            return;
        }

        ItemStack playerHand = PlayerUtils.getItemInHand(attacker, HandSlot.MAIN_HAND);

        if (playerHand == null) {
            return;
        }

        if (!Abilities.hasAbility(playerHand)) {
            return;
        }

        Entity attacked = e.getEntity();


        if (!(attacked instanceof LivingEntity)) {
            return;
        }

        Set<Ability> abilities = Abilities.getAbilities(playerHand);

        for (Ability ability : abilities) {
            ability.handlePlayerAttackTarget(attacker, (LivingEntity) attacked,e.getDamage());
        }
    }
}
