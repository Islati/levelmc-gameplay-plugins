package com.levelmc.loot.api.levels;

import com.destroystokyo.paper.ParticleBuilder;
import com.levelmc.core.api.inventory.HandSlot;
import com.levelmc.core.api.item.WeaponType;
import com.levelmc.core.api.players.PlayerUtils;
import com.levelmc.core.chat.Chat;
import com.levelmc.loot.Loot;
import com.levelmc.loot.api.levels.events.ItemLevelEvent;
import com.levelmc.loot.api.utils.DamageIndicatorUtil;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class ItemLevelListener implements Listener {

    private ItemLevelManager manager;

    private Loot core;
    public ItemLevelListener(Loot core) {
        this.core = core;
        this.manager = core.getLevelManager();
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        LivingEntity dead = e.getEntity();

        Player killer = dead.getKiller();

        if (killer == null) {
            return;
        }

        if (PlayerUtils.handIsEmpty(killer, HandSlot.MAIN_HAND)) {
            return;
        }

        ItemStack killerHand = PlayerUtils.getItemInHand(killer, HandSlot.MAIN_HAND);

        if (!manager.isItemLevelable(killerHand) && WeaponType.isItemWeapon(killerHand)) {
            manager.setLevel(killerHand, 1); //Make the item in their hand levelable.
        }

        int expReward = manager.getExpReward(dead.getType());
        if (expReward == 0) {
            return;
        }

        manager.addItemExperience(killer, killerHand, expReward);
        DamageIndicatorUtil.getInstance().spawnDamageIndicator(dead, String.format("&l&a+ &b%s &aItem XP", expReward), EntityDamageEvent.DamageCause.CUSTOM, 0);
    }

    @EventHandler
    public void onItemLevelEvent(ItemLevelEvent event) {
        Chat.sendTitle(event.getPlayer(), "&aItem Level Up", event.getItem().getItemMeta().getDisplayName(), 20, 10, 20);
        Chat.actionMessage(event.getPlayer(), "&6Now level &l&e" + manager.getItemLevel(event.getItem()));

        new ParticleBuilder(Particle.ENCHANTMENT_TABLE).location(event.getPlayer().getLocation()).count(10).spawn();
    }
}
