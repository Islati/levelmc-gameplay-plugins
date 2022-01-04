package com.levelmc.core.api.gadgets;

import com.levelmc.core.chat.Chat;
import com.levelmc.core.api.events.gadget.GadgetUseEvent;
import com.levelmc.core.api.inventory.HandSlot;
import com.levelmc.core.api.inventory.InventoryUtils;
import com.levelmc.core.api.utils.PluginUtils;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;

public class GadgetListener implements Listener {

    private Plugin parent;

    public GadgetListener(Plugin parent) {
        this.parent = parent;
        parent.getLogger().info("Gadgets listener has been registered");
    }


    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        //Get the inventory that's being clicked
        Inventory inventory = event.getInventory();
        InventoryType inventoryType = inventory.getType();
        Player player = (Player) event.getWhoClicked();

        boolean switchingHands = false;
        if (event.isCancelled()) {
            return;
        }

        switch (inventoryType) {
            case WORKBENCH:
                break;
            case PLAYER:
                PlayerInventory pInv = (PlayerInventory) inventory;
                InventoryAction action = event.getAction();
                int slot = event.getSlot();

                /*
                Determine if the gadget is equippable in the players offhand.
                 */

                ItemStack cursorItem = event.getCursor();

                if (cursorItem != null && cursorItem.getType() != Material.AIR) {
                    break;
                }

                if (!Gadgets.isGadget(parent, cursorItem)) {
                    return;
                }

                Gadget movingGadget = Gadgets.getGadget(parent, cursorItem);
                if (movingGadget != null && slot == InventoryUtils.PLAYER_OFF_HAND_ITEM_SLOT) {

                    if (!movingGadget.properties().isOffhandEquippable()) {
                        Chat.message(player, "&cThis is not off hand equipable");
                        event.setCancelled(true);
                    }
                    movingGadget.onPlayerSwitchHand(player, event);
                    switchingHands = true;
                }


                if (movingGadget != null && !switchingHands) {
                    movingGadget.onInventoryInteraction(event);
                }
                break;
            case CHEST:
                break;
            default:
                break;
        }

    }

    @EventHandler
    public void itemDamageEvent(PlayerItemDamageEvent e) {
        Player p = e.getPlayer();
        ItemStack item = e.getItem();

        if (!Gadgets.isGadget(parent, item)) {
            return;
        }

        Gadget gadget = Gadgets.getGadget(parent, item);
        if (!gadget.properties().isBreakable()) {
            e.setCancelled(true);
        }

        gadget.onPlayerItemDamageEvent(e);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerDropItemEvent(PlayerDropItemEvent e) {
        /*
        Handle the 'onPlayerDrop' item event.
         */
        Item item = e.getItemDrop();

        if (!Gadgets.isGadget(parent, item.getItemStack())) {
            return;
        }

        Gadget gadget = Gadgets.getGadget(parent, item.getItemStack());

        if (!gadget.properties().isDroppable()) {
            e.setCancelled(true);
        }

        gadget.onDrop(e.getPlayer(), item);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteracted(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemInHand = event.getItem();
        EquipmentSlot eSlot = event.getHand();


        if (itemInHand == null) {
            return;
        }

        if (!Gadgets.isGadget(parent, itemInHand)) {
            return;
        }

        if (eSlot == null) {
            return;
        }

        HandSlot hand = HandSlot.getSlot(eSlot);

        if (hand == null) {
            return;
        }

        Gadget gadget = Gadgets.getGadget(parent, itemInHand);
        GadgetUseEvent gadgetEvent = new GadgetUseEvent(parent, player, event.getAction(), gadget, hand);

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            gadgetEvent.setBlock(event.getClickedBlock());
        }

        PluginUtils.callEvent(gadgetEvent);
        if (!gadgetEvent.isCancelled()) {
            gadget.onGadgetUse(gadgetEvent);
        }
        event.setCancelled(true);
    }
}
