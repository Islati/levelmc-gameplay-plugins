package com.levelmc.core.api.gadgets;

import com.levelmc.core.api.events.gadget.GadgetUseEvent;
import com.levelmc.core.api.gadgets.interactions.*;
import com.levelmc.core.api.players.PlayerUtils;
import com.levelmc.core.api.item.ItemBuilder;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

/**
 * Items with actions bound to various events.
 */
public class ItemGadget implements Gadget {

    private NamespacedKey gadgetIdKey;

    private ItemStack gadgetItem;

    private GadgetProperties properties = new GadgetProperties();

    private String id = null;

    private Plugin core;

    private Map<Class<? extends GadgetAction>, GadgetAction> actions = new HashMap<>();

    public ItemGadget(Plugin plugin, String id, ItemBuilder builder) {
        this(plugin, id, builder.item());
    }

    public ItemGadget(Plugin plugin, String id, ItemStack item) {
        this.id = id;
        this.core = plugin;
        gadgetIdKey = new NamespacedKey(plugin, "gadgetId");
        setItem(item);
    }

    @Override
    public ItemStack getItem() {
        if (Gadgets.getId(core, gadgetItem) == null) {
            ItemMeta meta = gadgetItem.getItemMeta();
            meta.getPersistentDataContainer().set(gadgetIdKey, PersistentDataType.STRING, id());
            gadgetItem.setItemMeta(meta);
            core.getLogger().info(String.format("[Gadget %s] Attached gadgetId '%s' to item in %s#getItem()", getClass().getSimpleName(), id(), getClass().getSimpleName()));
        }

        return gadgetItem;
    }

    /**
     * Change the item attached to this gadget.
     *
     * @param item item to use for gadget recognition.
     */
    public void setItem(ItemStack item) {
        this.gadgetItem = item.clone();
        this.properties.breakable(item.getItemMeta().isUnbreakable());
    }

    public ItemGadget on(Class<? extends GadgetAction> event, GadgetAction action) {
        actions.put(event, action);
        return this;
    }

    public ItemGadget on(GadgetInteractionType interactionType, GadgetAction action){
        return on(interactionType.getGadgetActionClass(),action);
    }

    public boolean hasAction(Class<? extends GadgetAction> event) {
        return actions.containsKey(event);
    }

    public boolean hasAction(GadgetInteractionType type) {
        return hasAction(type.getGadgetActionClass());
    }

    @Override
    public void onGadgetUse(GadgetUseEvent e) {
        if (!hasAction(GadgetUseAction.class)) {
            return;
        }

        GadgetUseAction action = (GadgetUseAction) actions.get(GadgetUseAction.class);
        action.onGadgetUse(e);
    }

    @Override
    public void onBreak(Player p) {
        if (!hasAction(GadgetBreakAction.class)) {
            return;
        }

        GadgetBreakAction action = (GadgetBreakAction) actions.get(GadgetBreakAction.class);
        action.onBreak(this, p);
    }

    @Override
    public void onDrop(Player player, Item item) {
        if (!hasAction(GadgetDropAction.class)) {
            return;
        }

        GadgetDropAction dropAction = (GadgetDropAction) actions.get(GadgetDropAction.class);
        dropAction.onDrop(player, this, item);
    }

    @Override
    public void onPlayerSwitchHand(Player player, InventoryClickEvent event) {
        if (!hasAction(GadgetSwitchHandAction.class)) {
            return;
        }

        GadgetSwitchHandAction action = (GadgetSwitchHandAction) actions.get(GadgetSwitchHandAction.class);
        action.onSwitchHand(player, event);
    }

    @Override
    public void onInventoryInteraction(InventoryClickEvent e) {
        if (!hasAction(GadgetInventoryInteraction.class)) {
            return;
        }

        GadgetInventoryInteraction action = (GadgetInventoryInteraction) actions.get(GadgetInventoryInteraction.class);
        action.onInventoryClick(this, e);
    }

    @Override
    public void onPlayerItemDamageEvent(PlayerItemDamageEvent e) {
        if (!hasAction(GadgetItemDamageAction.class)) {
            return;
        }

        GadgetItemDamageAction action = (GadgetItemDamageAction) actions.get(GadgetItemDamageAction.class);
        action.onItemDamaged(this, e);
    }

    /**
     * Give the player a copy of the gadget.
     *
     * @param player player to give the gadget to.
     */
    public void giveTo(Player player) {
        PlayerUtils.giveItem(player, getItem());
    }

    @Override
    public GadgetProperties properties() {
        return properties;
    }

    @Override
    public String id() {
        return id;
    }
}








