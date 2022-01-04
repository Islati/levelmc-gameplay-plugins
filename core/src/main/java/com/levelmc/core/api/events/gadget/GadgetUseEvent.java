package com.levelmc.core.api.events.gadget;

import com.levelmc.core.api.gadgets.Gadget;
import com.levelmc.core.api.gadgets.Gadgets;
import lombok.Getter;
import lombok.Setter;
import com.levelmc.core.api.inventory.HandSlot;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

/**
 * An event that's called whenever a(n) {@link Gadget}, or any deriving class, is used / interacted with.
 */
public class GadgetUseEvent extends Event implements Cancellable {
    public static final HandlerList handler = new HandlerList();

    private Action action;
    private HandSlot hand;

    private boolean cancelled = false;

    private Player player;
    private Gadget gadget;

    private Plugin plugin;

    @Getter
    @Setter
    private Block block = null;

    public GadgetUseEvent(Plugin plugin, Player player, Action action, ItemStack item, HandSlot hand) {
        this(plugin, player, action, Gadgets.getGadget(plugin, item), hand);
    }


    public GadgetUseEvent(Plugin plugin, Player player, Action action, Gadget gadget, HandSlot hand) {
        this.plugin = plugin;
        this.player = player;
        this.gadget = gadget;
        this.action = action;
        this.hand = hand;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

    @Override
    public HandlerList getHandlers() {
        return handler;
    }

    public static HandlerList getHandlerList() {
        return handler;
    }

    /**
     * @return the player using the gadget.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @return gadget being used by the player.
     */
    public Gadget getGadget() {
        return gadget;
    }

    /**
     * @return action that was performed / used to call the event.
     */
    public Action getAction() {
        return action;
    }

    public boolean isRightClick() {
        return action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK;
    }

    public boolean isLeftClick() {
        return action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK;
    }

    public HandSlot getHand() {
        return hand;
    }

    public boolean hasBlock() {
        return getBlock() != null;
    }
}
