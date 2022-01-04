package com.levelmc.loot.api.repairing;

import com.destroystokyo.paper.ParticleBuilder;
import com.levelmc.core.api.gadgets.Gadgets;
import com.levelmc.core.api.item.ItemUtils;
import com.levelmc.core.api.utils.NumberUtil;
import com.levelmc.core.api.utils.SoundUtils;
import com.levelmc.core.chat.Chat;
import com.levelmc.loot.Loot;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ItemRepairListener implements Listener {

    private Loot parent;

    public ItemRepairListener(Loot parent) {
        this.parent = parent;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        HumanEntity entity = e.getWhoClicked();

        if (!(entity instanceof Player)) {
            return;
        }

        if (e.getCursor() == null) {
            return;
        }
        ItemStack cursor = e.getCursor();

        ItemRepairScroll scroll = ItemRepairScroll.getInstance();

        if (!Gadgets.isGadget(Loot.getInstance(),cursor, scroll)) {
            return;
        }

        ItemStack current = e.getCurrentItem();

        if (current == null) {
            return;
        }

        if (!parent.getLevelManager().isItemLevelable(current)) {
            e.setResult(Event.Result.DENY);
            e.setCancelled(true);
            Chat.actionMessage((Player) entity, "&cOnly levelable items can be repaired this way.");
            return;
        }

        if (current.getType().getMaxDurability() > 0 && current.getDurability() > 0) {
            e.setCursor(ItemUtils.removeFromStack(e.getCursor(), 1));
            current.setDurability((short) 0);

            Player player = (Player) entity;
            SoundUtils.playSound(player, Sound.BLOCK_ANVIL_USE);

            new ParticleBuilder(Particle.CLOUD).location(player.getLocation()).count(NumberUtil.getRandomInRange(3,5)).spawn();
        }
    }

}
