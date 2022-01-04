package com.levelmc.core.api.players;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.levelmc.core.LevelCore;
import com.levelmc.core.api.entities.EntityUtils;
import com.levelmc.core.api.inventory.*;
import com.levelmc.core.api.world.WorldUtils;
import com.levelmc.core.api.gadgets.Gadget;
import com.levelmc.core.api.gadgets.Gadgets;
import com.levelmc.core.api.item.ItemUtils;
import com.levelmc.core.api.utils.ListUtils;
import com.levelmc.core.api.utils.SoundUtils;
import com.levelmc.core.api.world.BlockUtils;
import com.levelmc.core.api.world.Direction;
import com.levelmc.core.api.world.LocationUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlayerUtils {

    public static final int DEPTH_EQUALIZE_NUMBER = 63;
    private static final int MAX_BLOCK_TARGET_DISTANCE = 30;

    public static Set<CommandSender> getPlayersWithPermission(String... permission) {
        return stream().filter(p -> {
            boolean pass = true;
            for (String s : permission) {
                //todo check if vault enabled.
                if (!p.hasPermission(s)) {
                    pass = false;
                    break;
                }
            }
            return pass;
        }).map(p -> (CommandSender) p).collect(Collectors.toSet());
    }

    /**
     * Teleports the player to a location
     *
     * @param player   player to teleport
     * @param location location to teleport the player to
     * @since 1.0
     */
    public static void teleport(Player player, Location location) {
        Validate.notNull(player);
        Validate.notNull(location);

        float preYaw = player.getLocation().getYaw();
        float prePitch = player.getLocation().getPitch();

        player.teleport(new Location(location.getWorld(), location.getX(), location.getY(), location.getZ(), preYaw, prePitch), PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    /**
     * Clear the players entire inventory, armor slots included.
     *
     * @param player player to clear inventory of
     * @see #clearInventory(Player, boolean)
     * @since 1.0
     */
    public static void clearInventory(Player player) {
        clearInventory(player, true);
    }

    /**
     * Set the contents of the players inventory.
     *
     * @param player         the player to set the inventory on.
     * @param itemMap        map of indices and the items to set to said index.
     * @param clearInventory whether or not to clear the players inventory. If not, current items may be overriden.
     */
    public static void setInventory(Player player, Map<Integer, ItemStack> itemMap, boolean clearInventory) {
        if (clearInventory) {
            clearInventory(player, true);
        }

        PlayerInventory inventory = player.getInventory();
        for (Map.Entry<Integer, ItemStack> itemEntry : itemMap.entrySet()) {
            inventory.setItem(itemEntry.getKey(), itemEntry.getValue());
        }
    }

    /**
     * Clear the players entire inventory, and optionally their armor slots.
     *
     * @param player     player to clear inventory of
     * @param clearArmor whether or not to clear the players armor slots
     * @since 1.0
     */
    public static void clearInventory(Player player, boolean clearArmor) {
        player.getInventory().clear();
        if (clearArmor) {
            player.getInventory().setArmorContents(new ItemStack[]{null, null, null, null});
        }

        /* Update the players inventory, to prevent any errors or item misplacement. */
        player.updateInventory();
    }

    /**
     * Update the players inventory on the next game tick.
     *
     * @param player player who's inventory shall be updated.
     */
    public static void updateInventory(Player player) {

    }

    /**
     * Force the player to drop their InventoryUtils contents.
     *
     * @param player player to drop the inventory of.
     */
    public static void dropInventory(Player player) {
        ItemStack[] inventoryContents = player.getInventory().getContents();
        PlayerUtils.clearInventory(player);

        for (ItemStack item : inventoryContents) {
            if (item == null) {
                continue;
            }
            WorldUtils.dropItemNaturally(player, item);
        }
    }

    /**
     * Places an item into the players inventory but does NOT call an update to their inventory
     *
     * @param player    player to give an item to
     * @param itemStack itemstack to give to the player
     * @since 1.0
     */
    public static void giveItem(Player player, ItemStack itemStack) {
        player.getInventory().addItem(itemStack);
    }

    /**
     * Give the player an item, optionally dropping it if they have no free room in their inventory.
     *
     * @param player    player to give the item to.
     * @param itemStack item to give to the player.,
     * @param drop      whether or not to drop the item if there's no free space.
     * @return true if the player received the item, false if there was no free space and the item wasn't dropped.
     */
    public static boolean giveItem(Player player, ItemStack itemStack, boolean drop) {
        PlayerInventory inventory = player.getInventory();
        if (inventory.firstEmpty() == -1) {
            if (drop) {
                WorldUtils.dropItem(player, itemStack, false);
                return true;
            }
            return false;
        }
        inventory.addItem(itemStack);
        return true;
    }

    /**
     * Get an item at a specific slot in the players inventory.
     *
     * @param player player to get the item from.
     * @param slot   slot to get the item in.
     * @return the item that's at the given slot in the players inventory, potentially null or Material.AIR
     */
    @Nullable
    public static ItemStack getItem(Player player, int slot) {
        return player.getInventory().getItem(slot);
    }

    /**
     * Set the item at a specific slot in the players inventory.
     *
     * @param player player to operate on.
     * @param slot   slot to assign the item to.
     * @param item   item to put in the given slot.
     */
    public static void setItem(Player player, int slot, ItemStack item) {
        InventoryUtils.setItem(player.getInventory(), slot, item);
        player.updateInventory();
    }

    /**
     * Changes the active slot a player has selected on their hotbar. (Between 0 & 8)
     *
     * @param player player to switch slots of
     * @param slot   slot to switch the player to.
     */
    public static void setHotbarSelection(Player player, int slot) {
        if (slot > 8) {
            return;
        }

        player.getInventory().setHeldItemSlot(slot);
    }

    /**
     * Sets an item in the players hotbar to the item given
     *
     * @param player player to give the item to
     * @param item   item to set in slot
     * @param slot   slot to change
     */
    public static void setHotbarItem(Player player, ItemStack item, int slot) {
        if (slot > 8) {
            return;
        }

        setItem(player, slot, item);
    }

    /**
     * Set the contents of a players hotbar.
     *
     * @param player player to change the hotbar of.
     * @param items  items to set the players hotbar to.
     */
    public static void setHotbarContents(Player player, ItemStack... items) {
        for (int i = 0; i < items.length; i++) {
            setHotbarItem(player, items[i], i);
        }
    }

    /**
     * Set the contents of a players hotbar
     *
     * @param player player to change the hotbar of.
     * @param hotbar hotbar to set for the player.
     */
    public static void setHotbar(Player player, Hotbar hotbar) {
        hotbar.assign(player);
    }

    /**
     * Places items into the players inventory without calling an update method
     *
     * @param player player to give the items to
     * @param items  items to give the player
     * @see #giveItem(Player, ItemStack)
     * @since 1.0
     */
    public static void giveItem(Player player, ItemStack... items) {
        for (ItemStack itemStack : items) {
            giveItem(player, itemStack, true);
        }
    }

    /**
     * Set the armor in a specific slot for the given player.
     *
     * @param player    player to change the armor of.
     * @param armorSlot slot to assign the armor to.
     * @param itemStack item to assign as armor in the given slot.
     */
    public static void setArmor(Player player, ArmorSlot armorSlot, ItemStack itemStack) {
        if (itemStack == null || armorSlot == null) {
            return;
        }

        PlayerInventory inventory = player.getInventory();
        switch (armorSlot) {
            case HELMET:
                inventory.setHelmet(itemStack);
                break;
            case CHEST:
                inventory.setChestplate(itemStack);
                break;
            case LEGGINGS:
                inventory.setLeggings(itemStack);
                break;
            case BOOTS:
                inventory.setBoots(itemStack);
                break;
            case MAIN_HAND:
                inventory.setItemInMainHand(itemStack);
                break;
            case OFF_HAND:
                inventory.setItemInOffHand(itemStack);
                break;
            default:
                break;
        }
    }

    /**
     * Get the items a player has equipped.
     *
     * @param player player to get the armor of
     * @return the items a player has equipped.
     */
    public static ItemStack[] getArmor(Player player) {
        return player.getInventory().getArmorContents();
    }

    /**
     * Get the equipped armor of a player in a specific slot.
     *
     * @param player    player to get the armor of.
     * @param armorSlot which armor slot to get the armor from.
     * @return the item equipped in the given slot, or null if none is equipped.
     */
    public static ItemStack getArmor(Player player, ArmorSlot armorSlot) {
        PlayerInventory playerInventory = player.getInventory();
        ItemStack itemStack = null;
        switch (armorSlot) {
            case HELMET:
                itemStack = playerInventory.getHelmet();
                break;
            case CHEST:
                itemStack = playerInventory.getChestplate();
                break;
            case LEGGINGS:
                itemStack = playerInventory.getLeggings();
                break;
            case BOOTS:
                itemStack = playerInventory.getBoots();
                break;
            case MAIN_HAND:
                itemStack = playerInventory.getItemInMainHand();
                break;
            case OFF_HAND:
                itemStack = playerInventory.getItemInOffHand();
                break;
            default:
                break;
        }
        return itemStack;
    }

    /**
     * Sets the players armor.
     *
     * @param player player to set armor on
     * @param armor  itemstack array of the armor we're equipping the player with
     * @since 1.0
     */
    public static void setArmor(Player player, ItemStack[] armor) {
        player.getInventory().setArmorContents(armor);
    }

    /**
     * Equip the player with armor.
     *
     * @param player player to parent.
     * @param armor  armor-inventory to assign to the player
     */
    public static void setArmor(Player player, ArmorInventory armor) {
        for (Map.Entry<ArmorSlot, ItemStack> entry : armor.getArmor().entrySet()) {
            setArmor(player, entry.getKey(), entry.getValue());
        }
    }

    /**
     * Removes all the potion effects from the player
     *
     * @param player player to remove the potion effects from
     * @since 1.0
     */
    public static void removePotionEffects(Player player) {
        EntityUtils.removePotionEffects(player);
    }

    /**
     * Give a player a potion effect of the given type
     *
     * @param player       player to give the potion effect to
     * @param potionEffect the potion effect in which to give the player
     * @since 1.0
     */
    public static void addPotionEffect(Player player, PotionEffect potionEffect) {
        EntityUtils.addPotionEffect(player, potionEffect);
    }

    /**
     * @return amount of players that are currently online
     * @since 1.0
     */
    public static int getOnlineCount() {
        return allPlayers().size();
    }

    public static Set<Player> allPlayers() {
        return Sets.newHashSet(Bukkit.getOnlinePlayers());
    }

    /**
     * @return Lambda stream of all the currently online players.
     */
    public static Stream<Player> stream() {
        return allPlayers().stream();
    }

    /**
     * Get a set of all the online operators.
     *
     * @return a hashset of all the currently online operators.
     */
    public static Set<Player> onlineOperators() {
        Set<Player> players = new HashSet<>();
        for (Player player : allPlayers()) {
            if (player.isOp()) {
                players.add(player);
            }
        }
        return players;
    }


    /**
     * Retrieve a random player of those currently online.
     *
     * @return a random online player.
     */
    public static Player getRandomPlayer() {
        return ListUtils.getRandom(Lists.newArrayList(allPlayers()));
    }

    /**
     * Retrieve all the players in a specific world.
     *
     * @param world world to search for players in.
     * @return a collection of all players in the given world.
     */
    public static Collection<Player> allPlayers(World world) {
        return world.getEntitiesByClass(Player.class);
    }

    /**
     * Get a collection of players by their UUIDs.
     *
     * @param ids ids of players ot retrieve
     * @return a set of players whos is one of the specified ids.
     */
    public static Set<Player> getPlayers(Collection<UUID> ids) {
        Set<Player> players = new HashSet<>();
        for (UUID id : ids) {
            if (!isOnline(id)) {
                continue;
            }
            players.add(Bukkit.getPlayer(id));
        }
        return players;
    }

    /**
     * Get all the online players excluding those who are to be excluded
     *
     * @param excludedPlayers names of the players to exclude from the set
     * @return set of all players
     */
    @SuppressWarnings("deprecation")
    public static Set<Player> allPlayersExcept(String... excludedPlayers) {
        Set<Player> players = new HashSet<>();
        Set<String> names = Sets.newHashSet(excludedPlayers);
        for (Player player : allPlayers()) {
            if (!names.contains(player.getName())) {
                players.add(player);
            }
        }
        return players;
    }

    /**
     * Retrieve a set of players except those who's UUID matches one of those specified.
     *
     * @param playerIds id's of players to exclude.
     * @return all players except those with excluded uuids.
     */
    public static Set<Player> allPlayersExcept(UUID... playerIds) {
        Set<Player> players = Sets.newHashSet(allPlayers());
        Set<UUID> uniqueIds = Sets.newHashSet(playerIds);
        for (Player player : allPlayers()) {
            if (uniqueIds.contains(player.getUniqueId())) {
                players.remove(player);
            }
        }
        return players;
    }

    /**
     * Check if there's atleast the given amount of players online
     *
     * @param amount Amount to check against
     * @return true if amount is greater or equal to the amount of players online, false otherwise
     * @since 1.0
     */
    public static boolean isOnline(int amount) {
        return getOnlineCount() >= amount;
    }

    public static boolean isOnline(UUID id) {
        return Bukkit.getPlayer(id) != null;
    }

    /**
     * Gets the players depth on the y-axis
     *
     * @param player player to get the depth of
     * @return the players block-level depth
     * @since 1.0
     */
    public static int getDepth(Player player) {
        return player.getLocation().getBlockY();
    }

    /**
     * Gets the players depth on a 0-based number
     *
     * @param player player to get the depth of
     * @return 0-based y-axis position
     * @since 1.0
     */
    private static int getEqualizedDepth(Player player) {
        return getDepth(player) - DEPTH_EQUALIZE_NUMBER;
    }

    /**
     * Checks whether or not the player is above sea level
     *
     * @param player player to check the depth of
     * @return true if they're above sea level, false if they're at or below sea level
     * @since 1.0
     */
    public static boolean isAboveSeaLevel(Player player) {
        return getEqualizedDepth(player) > 0;
    }

    /**
     * Checks whether or not the player is below sea level
     *
     * @param player player to check the depth of
     * @return true if the player is below sea level, false if they're at or above sea level
     * @since 1.0
     */
    public static boolean isBelowSeaLevel(Player player) {
        return getEqualizedDepth(player) < 0;
    }

    /**
     * Check whether or not the player is at sea level
     *
     * @param player player to check the depth of
     * @return true if they're at sea level, false if they're above or below sea level
     * @since 1.0
     */
    public static boolean isAtSeaLevel(Player player) {
        return getEqualizedDepth(player) == 0;
    }

    /**
     * Replenishes a players food level to the amount given.
     * <p>
     * Sets saturation to 10 and exhaustion to 0.
     * </p>
     *
     * @param player player to feed
     * @param amount amount of hunger to restore
     * @since 1.0
     */
    public static void feed(Player player, int amount) {
        player.setFoodLevel(amount);
        player.setSaturation(10);
        player.setExhaustion(0);
    }

    /**
     * Replenishes the players food level to full (20).
     * <p>
     * The players exhaustion will also be set to 0, along with
     * saturation set to 10
     * </p>
     *
     * @param player player to feed
     * @see #feed(Player, int)
     * @since 1.0
     */
    public static void feed(Player player) {
        feed(player, 20);
    }

    public static void decreaseHunger(Player player, int amount) {
        int hungerLevel = player.getFoodLevel();
        hungerLevel -= amount;
        if (hungerLevel <= 0) {
            hungerLevel = 0;
        }

        player.setFoodLevel(hungerLevel);
    }

    /**
     * Restore the players health by the given amount. Will not exceed the players maximum health.
     *
     * @param p      player to restore health for.
     * @param amount amount of health to restore on the player.
     */
    public static void restoreHealth(Player p, int amount) {
        double currentHealth = p.getHealth();
        double maxHealth = p.getMaxHealth();

        if (currentHealth >= maxHealth) {
            return;
        }

        double newHealth = currentHealth + amount;
        if (newHealth > maxHealth) {
            newHealth = maxHealth;
        }

        p.setHealth(newHealth);
    }

    /**
     * Restore the players health by the given amount. Will not exceed the players maximum health.
     *
     * @param p      player to restore health for.
     * @param amount amount of health to restore on the player.
     */
    public static void restoreHealth(Player p, double amount) {
        double currentHealth = p.getHealth();
        double maxHealth = p.getMaxHealth();

        if (currentHealth >= maxHealth) {
            return;
        }

        double newHealth = currentHealth + amount;
        if (newHealth > maxHealth) {
            newHealth = maxHealth;
        }

        p.setHealth(newHealth);
    }

    /**
     * Heal the player to full health, remove any potion effects they have, and clear them from all
     * ticking damages.
     *
     * @param player player to heal
     */
    public static void heal(Player player) {
        PlayerUtils.removePotionEffects(player);
        player.setHealth(player.getMaxHealth());
        player.setFireTicks(0);
    }

    /**
     * Repair all the items in a players inventory to full durability, excluding their armor.
     *
     * @param player player to repair the items of
     * @see #repairItems(Player, boolean)
     * @since 1.0
     */
    public static void repairItems(Player player) {
        repairItems(player, false);
    }

    /**
     * Repair all the items in a player inventory to full durability.
     * <p>
     * If {@param repairArmor} is true, then the players armor will also
     * be repaired to full.
     * </p>
     *
     * @param player      player to repair the items of
     * @param repairArmor whether or not to repair the armor of the player aswell
     * @since 1.0
     */
    public static void repairItems(Player player, boolean repairArmor) {
        PlayerInventory inventory = player.getInventory();
        ItemUtils.repairItems(inventory.getContents());
        if (repairArmor) {
            ItemUtils.repairItems(inventory.getArmorContents());
        }
    }

    /**
     * Check whether or not the player has an item in either of their hands (Main, or Offhand).
     *
     * @param player player to check for items
     * @return true if the player has an item in either their main or offhand slot, otherwise false.
     */
    public static boolean hasItemInHand(Player player) {
        PlayerInventory playerInv = player.getInventory();
        ItemStack mainHand = playerInv.getItemInMainHand();
        ItemStack offHand = playerInv.getItemInOffHand();

        return (mainHand != null && mainHand.getType() != Material.AIR) || (offHand != null && offHand.getType() != Material.AIR);
    }

    /**
     * Check whether or not the player has an item in the specified hand
     *
     * @param player player to check the hand of.
     * @param slot   hand (slot) to check
     * @return true if the specified slot has an item in it, false otherwise.
     */
    public static boolean hasItemInHand(Player player, HandSlot slot) {
        switch (slot) {
            case MAIN_HAND:
                ItemStack mainHand = player.getInventory().getItemInMainHand();
                return mainHand != null && mainHand.getType() != Material.AIR;
            case OFF_HAND:
                ItemStack offHand = player.getInventory().getItemInOffHand();
                return offHand != null && offHand.getType() != Material.AIR;
            default:
                return false;
        }
    }

    /**
     * Retrieve the item in the players specified hand (slot).
     *
     * @param player player to retrieve the item from
     * @param slot   hand (slot) to get the item in
     * @return item in the players specified hand slot.
     */
    public static ItemStack getItemInHand(Player player, HandSlot slot) {
        switch (slot) {
            case MAIN_HAND:
                return player.getInventory().getItemInMainHand();
            case OFF_HAND:
                return player.getInventory().getItemInOffHand();
            default:
                return null;
        }
    }

    /**
     * Change the item in the players hand (slot) to that specified.
     *
     * @param player player to change the hand item of.
     * @param stack  stack to set in the players hand
     * @param slot   hand (slot) to set the items in.
     */
    public static void setItemInHand(Player player, ItemStack stack, HandSlot slot) {
        switch (slot) {
            case MAIN_HAND:
                player.getInventory().setItemInMainHand(stack);
                break;
            case OFF_HAND:
                player.getInventory().setItemInOffHand(stack);
                break;
            default:
                break;
        }
    }


    /**
     * Check whether a player has an item similar to the compare item in either their main or offhand.
     *
     * @param player  player to check items for
     * @param compare item to check for in the players hands
     * @return true if the player has an item similar to the comparing item in main or offhand, false otherwise.
     */
    @Deprecated
    public static boolean hasItemInHand(Player player, ItemStack compare) {
        PlayerInventory inv = player.getInventory();

        ItemStack mainHand = inv.getItemInMainHand();
        ItemStack offHand = inv.getItemInOffHand();


        return mainHand.isSimilar(compare) || offHand.isSimilar(compare);
    }

    /**
     * Check whether or not the player has a specific item in a specific hand slot (main or offhand)
     *
     * @param player  player to check the hands of
     * @param compare item to check for in players hand
     * @param slot    which hand (slot) to check for the item in.
     * @return true if the player has the desired item in their hand, false otherwise.
     */
    public static boolean hasItemInHand(Player player, ItemStack compare, HandSlot slot) {
        return hasItemInHand(player, slot) && getItemInHand(player, slot).isSimilar(compare);
    }

    /**
     * Check if the player is holding any items.
     *
     * @param player player to check.
     * @return true if the player has nothing in their hand, false otherwise.
     */
    @Deprecated
    public static boolean handIsEmpty(Player player) {
        return !hasItemInHand(player);
    }

    /**
     * Check if both of the players hands are empty.
     *
     * @param player player to check the hands of
     * @return true if the player has no item in either their main or off-hand, false otherwise.
     */
    public static boolean handsAreEmpty(Player player) {
        return !hasItemInHand(player, HandSlot.MAIN_HAND) && !hasItemInHand(player, HandSlot.OFF_HAND);
    }

    /**
     * Check if the players hand (slot) is empty.
     *
     * @param player player to check the hand of
     * @param slot   hand (slot) to check
     * @return true if the players hand is empty, false otherwise
     */
    public static boolean handIsEmpty(Player player, HandSlot slot) {
        return !hasItemInHand(player, slot);
    }

    /**
     * Clear the items in each of the players hands.
     *
     * @param player player to clear the hands of.
     */
    public static void clearHands(Player player) {
        PlayerInventory inv = player.getInventory();

        inv.setItemInMainHand(null);
        inv.setItemInOffHand(null);
    }

    /**
     * Clear the items in the players specific hand slot .
     *
     * @param player player to clear the hand of.
     * @param slot   hand (slot) to clear the items from in the players hand.
     */
    public static void clearHand(Player player, HandSlot slot) {
        switch (slot) {
            case MAIN_HAND:
                player.getInventory().setItemInMainHand(null);
                break;
            case OFF_HAND:
                player.getInventory().setItemInOffHand(null);
                break;
            default:
                break;
        }
    }

    /**
     * Remove a specific amount of items from the stack the player's holding in their main hand.
     *
     * @param player player to take the items from.
     * @param amount amount of items take from the stack.
     */
    @Deprecated
    public static void removeFromHand(Player player, int amount) {
        removeFromHand(player, amount, HandSlot.MAIN_HAND);
    }

    /**
     * Remove an amount of items from the players specified hand.
     *
     * @param player player to remove the items from.
     * @param amount amount of items to remove from the players hand
     * @param slot   which hand to remove the items from
     */
    public static void removeFromHand(Player player, int amount, HandSlot slot) {
        if (!hasItemInHand(player, slot)) {
            return;
        }

        ItemStack handItem = ItemUtils.removeFromStack(getItemInHand(player, slot), amount);

        switch (slot) {
            case MAIN_HAND:
                player.getInventory().setItemInMainHand(handItem);
                break;
            case OFF_HAND:
                player.getInventory().setItemInOffHand(handItem);
                break;
            default:
                break;
        }

        updateInventory(player);
    }

    /**
     * Check the players inventory for an item with a specific material and name
     * Uses a fuzzy search to determine if the item is in their inventory
     *
     * @param player   player who's inventory we're checking
     * @param material The material type were checking for
     * @param name     The name we're doing a fuzzy search against for
     * @return true if they have the item, false otherwise
     * @see InventoryUtils#contains(Inventory, Material)
     */
    public static boolean hasItem(Player player, Material material, String name) {
        return InventoryUtils.contains(player.getInventory(), material, name);
    }

    /**
     * Check if the player has any items in their inventory matching the given item.
     *
     * @param player player to check.
     * @param item   item to search for.
     * @return true if the player has an item matching the given item, false otherwise.
     */
    public static boolean hasItem(Player player, ItemStack item) {
        return InventoryUtils.contains(player.getInventory(), item);
    }

    /**
     * Check if the player has any items of the given material in their inventory.
     *
     * @param player   player to check.
     * @param material material to search for.
     * @return true if any items match the given material, false otherwise.
     */
    public static boolean hasItem(Player player, Material material) {
        return InventoryUtils.contains(player.getInventory(), material);
    }

    /**
     * Check if the player has the gadget in their inventory.
     *
     * @param player player to check.
     * @param gadget gadget to search for on the player.
     * @return true if the player has the gadget in their inventory, false otherwise.
     */
    public static boolean hasGadget(Player player, Gadget gadget) {
        return PlayerUtils.hasItem(player, gadget.getItem());
    }

    /**
     * Gets the players targeted location (on their cursor) of up-to 30 blocks in distance
     *
     * @param player player to get the target-location of
     * @return location that the player's targeting with their cursor; If greater than 30 blocks
     * in distance, then returns the location at the cursor 30 blocks away
     * @since 1.0
     */
    public static Location getTargetLocation(Player player) {
        return getTargetLocation(player, MAX_BLOCK_TARGET_DISTANCE);
    }

    /**
     * Gets the players target location (on their cursor) up to the distance given.
     *
     * @param player   player to get the target location for.
     * @param distance furthest distance to retrieve the location at.
     * @return location that the player's targeting with their cursor; If it's greater than the given distance,
     * the location at the distance given is returned.
     */
    public static Location getTargetLocation(Player player, int distance) {
        return LocationUtils.getNormalizedLocation(player.getTargetBlock(BlockUtils.TRANSPARENT_MATERIALS, distance).getLocation());
    }


    /**
     * Play a sound for all online players with a specific volume and pitch.
     *
     * @param sound  sound to play.
     * @param volume volume to play the sound at.
     * @param pitch  pitch to play the sound at.
     */
    public static void playSoundAll(Sound sound, float volume, float pitch) {
        for (Player p : allPlayers()) {
            SoundUtils.playSound(p, sound, volume, pitch);
        }
    }

    /**
     * Play the sound at the given volume for all online players.
     *
     * @param sound  sound to play.
     * @param volume volume to play the sound at.
     */
    public static void playSoundAll(Sound sound, int volume) {
        playSoundAll(sound, volume, 1.0f);
    }

    /**
     * Get the cardinal compass direction of a player.
     *
     * @param player player to get the direction of.
     * @return The direction the player's facing. (North(e,w),South(e,w),East, West)
     */
    public static Direction getCardinalDirection(Player player) {
        double rot = (player.getLocation().getYaw() - 90) % 360;
        if (rot < 0) {
            rot += 360.0;
        }
        return getDirection(rot);
    }

    /**
     * Retrieve the direction the player is facing. Does <b>not</b> return the cardinal direction.
     *
     * @param player player to get the direction of.
     * @return Direction the player's facing (North/South/East/West)
     */
    public static Direction getDirection(Player player) {
        int dirCode = Math.round(player.getLocation().getYaw() / 90f);
        switch (dirCode) {
            case 0:
                return Direction.SOUTH;
            case 1:
                return Direction.WEST;
            case 2:
                return Direction.NORTH;
            case 3:
                return Direction.EAST;
            default:
                return Direction.SOUTH;
        }
    }

    /**
     * Converts a rotation to a cardinal direction.
     *
     * @param rot
     * @return
     */
    private static Direction getDirection(double rot) {
        if (0 <= rot && rot < 22.5) {
            return Direction.NORTH;
        } else if (22.5 <= rot && rot < 67.5) {
            return Direction.NORTHEAST;
        } else if (67.5 <= rot && rot < 112.5) {
            return Direction.EAST;
        } else if (112.5 <= rot && rot < 157.5) {
            return Direction.SOUTHEAST;
        } else if (157.5 <= rot && rot < 202.5) {
            return Direction.SOUTH;
        } else if (202.5 <= rot && rot < 247.5) {
            return Direction.SOUTHWEST;
        } else if (247.5 <= rot && rot < 292.5) {
            return Direction.WEST;
        } else if (292.5 <= rot && rot < 337.5) {
            return Direction.NORTHWEST;
        } else if (337.5 <= rot && rot < 360.0) {
            return Direction.NORTH;
        } else {
            return null;
        }
    }
}
