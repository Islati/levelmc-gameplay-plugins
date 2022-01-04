package com.levelmc.core.api.gadgets;

import com.levelmc.core.api.events.gadget.GadgetUseEvent;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public interface Gadget extends Listener {

    /**
     * @return The itemstack that represents this gadget
     */
    ItemStack getItem();

    /**
     * Handle when a gadget is used
     *
     * @param e
     */
    void onGadgetUse(GadgetUseEvent e);

    /**
     * Operations to perform whenever the gadget breaks for the player.
     *
     * @param p player using the gadget
     */
    default void onBreak(Player p) {

    }

    /**
     * Actions to perform whenever the player drops the gadget.
     *
     * @param player player dropping the item.
     * @param item   item that was dropped.
     */
    default void onDrop(Player player, Item item) {

    }

    /**
     * Specific invocation when the player is attempting to switch the item to their offhand.
     *
     * @param player player involved
     * @param event  event which signalled the change.
     */
    default void onPlayerSwitchHand(Player player, InventoryClickEvent event) {

    }

    /**
     * the gadget interactions inside InventoryMenus
     *
     * @param e event where the interaction took place.
     */
    default void onInventoryInteraction(InventoryClickEvent e) {

    }

    /**
     * the interaction of a gadget taking damage
     *
     * @param e event
     */
    default void onPlayerItemDamageEvent(PlayerItemDamageEvent e) {

    }

    <T extends GadgetProperties> T properties();

    /**
     * Unique identifier for the gadget.
     * <b>Each gadget must have a unique ID, otherwise the previously registered gadget will be overridden.</b>
     *
     * @return Unique identifier for the gadget.
     */
    String id();
}
