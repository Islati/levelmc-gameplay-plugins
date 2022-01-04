package com.levelmc.core.api.gadgets;

import com.google.common.collect.Lists;
import com.levelmc.core.LevelCore;
import com.levelmc.core.api.world.WorldUtils;
import com.levelmc.core.api.item.ItemUtils;
import com.levelmc.core.api.utils.PluginUtils;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class Gadgets {

    private static final Random random = new Random();

    //todo Implement methods to get first free int for gadget registration
    private static final Map<String, Gadget> gadgets = new LinkedHashMap<>();

    //todo implement int method to return the registered gadgets id
    //todo Move Gadgets class to a manager for each plugin made with commons

    public static void init(Plugin plugin) {
        PluginUtils.registerListeners(plugin,new GadgetListener(plugin));
    }

    /**
     * Register the given gadget, enabling it's effects to be utilized whenever the associated item is used.
     * Note: Every gadget must have a unique id, if not, the previously registered gadget will be overwritten.
     *
     * @param gadget gadget to register.
     */
    public static void registerGadget(Plugin plugin, Gadget gadget) {
        gadgets.put(gadget.id(), gadget);
        PluginUtils.registerListeners(plugin, gadget); //todo implement unhooking the listeners
        plugin.getLogger().info("Registered gadget class " + gadget.getClass().getSimpleName() + " with id " + gadget.id());
    }

    /**
     * Check whether or not the given itemstack is a gadget / has gadget data associated with it.
     *
     * @param item item to check
     * @return true if the item is a gadget, false otherwisse.
     */
    public static boolean isGadget(Plugin plugin, ItemStack item) {
        return getGadget(plugin,item) != null;
    }

    /**
     * Check whether or not a gadget with the given id exists.
     *
     * @param id id of the gadget to check for
     * @return true if a gadget exists with the given id, false otherwise.
     */
    public static boolean isGadget(String id) {
        return gadgets.containsKey(id);
    }

    public static boolean isGadget(Plugin plugin, ItemStack item, Gadget gadget) {
        return isGadget(plugin,item) && getId(plugin,item).equalsIgnoreCase(gadget.id());
    }

    /**
     * Retrieve a gadget its associated item stack.
     *
     * @param item item to get the gadget container for.
     * @return the gadget associated with the given itemstack, or null if no gadget is associated.
     */
    public static Gadget getGadget(Plugin plugin, ItemStack item) {
        if (item == null) {
            return null;
        }

        if (!ItemUtils.hasMetadata(item)) {
            return null;
        }

        ItemMeta meta = item.getItemMeta();
        NamespacedKey gadgetIdKey = new NamespacedKey(plugin, "gadgetId");
        if (!meta.getPersistentDataContainer().has(gadgetIdKey, PersistentDataType.STRING)) {
            return null;
        }

        String gadgetId = meta.getPersistentDataContainer().get(gadgetIdKey, PersistentDataType.STRING);

        for (Gadget gadget : gadgets.values()) {
            if (gadget.id().equals(gadgetId)) {
                return gadget;
            }

        }
        return null;
    }

    /**
     * Retrieve a gadget by its registered id.
     *
     * @param id id of the gadget to get
     * @return gadget registered with the given id, or null if none are registered with the given id.
     */
    public static Gadget getGadget(String id) {
        return gadgets.get(id);
    }

    /**
     * Spawn the gadget at the given location.
     *
     * @param gadget   gadget to drop at the location
     * @param location location to spawn the gadget at
     */
    public static void spawnGadget(Gadget gadget, Location location) {
        WorldUtils.dropItem(location, gadget.getItem());
    }

    /**
     * Retrieve a random gadget from the list of currently registered gadgets
     *
     * @return random gadget of the registered gadgets
     */
    public static Gadget getRandomGadget() {
        List<Gadget> gadgetList = Lists.newArrayList(gadgets.values());
        return gadgetList.get(random.nextInt(gadgetList.size()));
    }

    /**
     * @return a collection of all the currently registered gadgets
     */
    public static Collection<Gadget> getAllGadgets() {
        return gadgets.values();
    }

    /**
     * @return the amount of registered gadgets
     */
    public static int getGadgetCount() {
        return gadgets.size();
    }

    /**
     * Determine whether or not the gadget has been registered already.
     *
     * @param gadget gadget
     * @return
     */
    public static boolean hasBeenRegistered(Gadget gadget) {
        return gadgets.containsKey(gadget.id());
    }

    public static String getId(Plugin plugin, ItemStack item) {
        if (!isGadget(plugin, item)) {
            return null;
        }

        ItemMeta meta = item.getItemMeta();
        NamespacedKey gadgetIdKey = new NamespacedKey(plugin, "gadgetId");
        if (!meta.getPersistentDataContainer().has(gadgetIdKey, PersistentDataType.STRING)) {
            return null;
        }

        String gadgetId = meta.getPersistentDataContainer().get(gadgetIdKey, PersistentDataType.STRING);
        return gadgetId;
    }
}
