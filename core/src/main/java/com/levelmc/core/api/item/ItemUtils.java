package com.levelmc.core.api.item;

import com.google.common.collect.Sets;
import com.levelmc.core.chat.Chat;
import com.levelmc.core.api.world.BlockUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.stream.Collectors;

public class ItemUtils {

    private static Set<Material> armorMaterials = Sets.newHashSet(
            Material.LEATHER_BOOTS,
            Material.LEATHER_CHESTPLATE,
            Material.LEATHER_HELMET,
            Material.LEATHER_LEGGINGS,
            Material.GOLDEN_BOOTS,
            Material.GOLDEN_CHESTPLATE,
            Material.GOLDEN_HELMET,
            Material.GOLDEN_LEGGINGS,
            Material.IRON_BOOTS,
            Material.IRON_CHESTPLATE,
            Material.IRON_HELMET,
            Material.IRON_LEGGINGS,
            Material.DIAMOND_BOOTS,
            Material.DIAMOND_CHESTPLATE,
            Material.DIAMOND_HELMET,
            Material.DIAMOND_LEGGINGS
    );

    static {
        armorMaterials = Sets.newHashSet();
        Collections.addAll(armorMaterials);
    }

    /**
     * Check whether or not an item has enchantments.
     *
     * @param itemStack itemstack to check for enchantments on.
     * @return true if the item has any enchantments, false otherwise.
     */
    public static boolean hasEnchantments(ItemStack itemStack) {
        return hasMetadata(itemStack) && itemStack.getItemMeta().hasEnchants();
    }

    /**
     * Check if an item has a specific enchantment
     * @param item item to check
     * @param enchant enchantment to look for
     * @return whether or not the item contains given enchantment
     */
    public static boolean hasEnchantment(ItemStack item, Enchantment enchant) {
        Map<Enchantment, Integer> enchantMap = item.getEnchantments();
        return enchantMap.containsKey(enchant);
    }

    /**
     * Check if the item has an enchantment at the given level/
     * @param item item to check
     * @param enchantment enchant we're looking for
     * @param level level to check for on enchantment
     * @return whether or not the item has the enchantment at the given level.
     */
    public static boolean hasEnchantment(ItemStack item, Enchantment enchantment, Integer level) {
        Map<Enchantment, Integer> enchantMap = item.getEnchantments();
        if (!enchantMap.containsKey(enchantment)) {
            return false;
        }

        return Objects.equals(enchantMap.get(enchantment), level);
    }

    /**
     * Check whether or not an item has metadata
     *
     * @param itemStack itemstack to check for metadata on.
     * @return true if the item has metadata, false otherwise
     */
    public static boolean hasMetadata(ItemStack itemStack) {
        if (itemStack == null) {
            return false;
        }

        return itemStack.hasItemMeta();
    }

    public static boolean hasNamespacedKey(ItemStack item, NamespacedKey key, PersistentDataType dataType) {
        return hasMetadata(item) && item.getItemMeta().getPersistentDataContainer().has(key, dataType);
    }

    /**
     * Set the MetaData on an Item Stack
     *
     * @param itemStack item stack to set the metadata on
     * @param itemMeta  The metadata to set on our item
     * @return The itemstack passed, but with its metadata changed.
     */
    public static void setMetadata(ItemStack itemStack, ItemMeta itemMeta) {
        itemStack.setItemMeta(itemMeta);
    }

    /**
     * Clear existing lore on an item.
     *
     * @param item item to remove the lore from.
     */
    public static void clearLore(ItemStack item) {
        setLore(item, Arrays.asList());
    }

    /**
     * Get lore of item at specific line
     *
     * @param itemStack Item to get lore of
     * @param line      Index of lore to get
     * @return String of lore if it exists, otherwise null
     */
    public static String getLore(ItemStack itemStack, int line) {
        if (!hasLore(itemStack)) {
            return null;
        }
        try {
            return getLore(itemStack).get(line);
        } catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Add lines of lore to an item
     *
     * @param itemStack item stack to add lore on
     * @param loreLines the lore lines to add to the item
     * @return itemstack with the new lore lines added
     */
    public static void addLore(ItemStack itemStack, String... loreLines) {
        addLore(itemStack, Arrays.asList(loreLines));
    }

    public static void addLore(ItemMeta meta, List<String> lore) {
        //Boolean statements; Woo! Our newItemLore = the current items lore, if it has lore; otherwise a blank arraylist
        List<String> newItemLore = hasLore(meta) ? getLore(meta) : new ArrayList<>();
        //Add all the lines passed to this method to the items current lore
        for (String line : lore) {
            if (line == null) {
                continue;
            }

            newItemLore.add(Chat.colorize(line));
        }
        setLore(meta, newItemLore);
    }

    /**
     * Add lines of lore to an item
     *
     * @param itemStack item stack to add lore on
     * @param loreLines the lore lines to add to the item
     * @return itemstack with the new lore lines added
     */
    public static void addLore(ItemStack itemStack, List<String> loreLines) {
        //Get the metadata for our item
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            itemMeta = Bukkit.getItemFactory().getItemMeta(itemStack.getType());
        }
        addLore(itemMeta, loreLines);
    }

    /**
     * Check if an item has lore
     *
     * @param itemStack itemstack to check
     * @return true if the itemstack has lore, false otherwise
     */
    public static boolean hasLore(ItemStack itemStack) {
        return hasMetadata(itemStack) && itemStack.getItemMeta().hasLore();
    }

    /**
     * Check if an item has lore
     *
     * @param itemMeta Metadata to check for lore
     * @return true if the metadata has lore, false otherwise
     */
    public static boolean hasLore(ItemMeta itemMeta) {
        return itemMeta.hasLore();
    }

    /**
     * Retrieve the lore of an item if available.
     *
     * @param itemStack itemstack to get the lore of.
     * @return null if the item has no lore, otherwise a(n) list of lore-lines.
     */
    public static List<String> getLore(ItemStack itemStack) {
        if (!hasLore(itemStack)) {
            return null;
        }
        return itemStack.getItemMeta().getLore();
    }

    /**
     * Retrieve the lore off of an ItemMeta object.
     *
     * @param itemMeta meta to get the lore of.
     * @return lore of the ItemMeta object,
     */
    public static List<String> getLore(ItemMeta itemMeta) {
        return itemMeta.getLore();
    }

    /**
     * Check if the item has lore available at a specific line. 0-based index.
     *
     * @param item item to get the lore of.
     * @param line line to get the lore at.
     * @return true if the item has a non-empty line of at the given index, false otherwise.
     */
    public static boolean hasLoreAtLine(ItemStack item, int line) {
        List<String> loreLines = getLore(item);

        if (loreLines == null) {
            return false;
        }

        if (loreLines.size() > line) {
            return StringUtils.isNotEmpty(loreLines.get(line));
        }

        return false;
    }

    /**
     * Retrieve the line of text in the items lore where it contains a specific string.
     *
     * @param item item to search the lore on.
     * @param text text to search for in the items lore.
     * @return The line of text containing the search variable, if available; Otherwise null is returned.
     */
    public static String getLoreLineContaining(ItemStack item, String text) {
        if (!hasLore(item)) {
            return null;
        }

        List<String> lore = getLore(item);

        String cLine = null;

        for (String line : lore) {
            if (!ChatColor.stripColor(line.toLowerCase()).contains(ChatColor.stripColor(text.toLowerCase()))) {
                continue;
            }

            cLine = line;
            break;
        }

        return cLine;
    }

    /**
     * Get the line number which contains the given text in an items lore.
     *
     * @param item item to search the lore on.
     * @param text text to search for within the lore.
     * @return line number which the text resides if it exists, if it doesn't exist -1 is returned.
     */
    public static int getLoreLineNumberContaining(ItemStack item, String text) {
        if (!hasLore(item)) {
            return -1;
        }

        List<String> lore = getLore(item);

        for (int i = 0; i < lore.size(); i++) {
            String loreLine = ChatColor.stripColor(lore.get(i));

            if (!loreLine.toLowerCase().contains(ChatColor.stripColor(text.toLowerCase()))) {
                continue;
            }

            return i;
        }

        return -1;
    }

    /**
     * Retrieve all the lines of lore on an item which contain a specific piece of text. (Future: Match against regex)
     *
     * @param item item to retrieve the lore from.
     * @param text text to search for in each line of lore.
     * @return List of all the lore lines which contained the desired text.
     */
    public static List<String> getLoreLinesContaining(ItemStack item, String text) {
        List<String> lines = new ArrayList<>();

        if (!hasLore(item)) {
            return lines;
        }

        List<String> lore = getLore(item);

        if (lore == null) {
            return lines;
        }

        for (String line : lore) {
            if (ChatColor.stripColor(line).toLowerCase().contains(ChatColor.stripColor(text.toLowerCase()))) {
                lines.add(line);
            }
        }

        return lines;
    }

    /**
     * Set the lore at a specific line for the given item. If there's currently no lore at the line, the operation will fail.
     *
     * @param item item to assign the lore to
     * @param line line to set the lore at
     * @param lore lore to assign at the given slot. (Note: Formats color codes)
     */
    public static void setLore(ItemStack item, int line, String lore) {
        if (!hasLoreAtLine(item, line)) {
            return;
        }

        List<String> loreLines = getLore(item);
        //todo implement option to fill with blanks.
        loreLines.set(line, Chat.colorize(lore));
        setLore(item, loreLines);
    }

    public static ItemStack setLore(ItemStack item, String... lines) {
        return setLore(item, Arrays.asList(lines));
    }

    /**
     * Set the lore on an item.
     *
     * @param itemStack item to set the lore on
     * @param loreLines lore to assign to the item. (Note: Formats color codes)
     * @return the item modified to have the lore assigned.
     */
    public static ItemStack setLore(ItemStack itemStack, List<String> loreLines) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> lore = new ArrayList<>();
        for (String line : loreLines) {
            if (line != null) {
                lore.add(Chat.colorize(line));
            }
        }
        itemMeta.setLore(lore);
        setMetadata(itemStack, itemMeta);
        return itemStack;
    }

    /**
     * Change the lore assigned to an instance of ItemMeta.
     *
     * @param itemMeta  metadata to assign the lore to.
     * @param loreLines lines of lore to assign to the item. (Note: Formats color codes)
     * @return itemmeta with the formatted lore applied.
     */
    public static void setLore(ItemMeta itemMeta, List<String> loreLines) {
        itemMeta.setLore(loreLines.stream().map(Chat::colorize).collect(Collectors.toList()));
    }

    /**
     * Checks if an items lore contains specific text
     *
     * @param itemStack Item to check
     * @param text      Text to check the item for
     * @return true if the item has the text in its lore, otherwise false.
     */
    public static boolean loreContains(ItemStack itemStack, String text) {
        if (!hasLore(itemStack)) {
            return false;
        }

        List<String> itemLore = getLore(itemStack);
        int i = 0;
        for (String s : itemLore) {
            i++;
            //If the line of lore is blank then skip it
            if (s == null || s.isEmpty()) {
                continue;
            }
            if (ChatColor.stripColor(s.toLowerCase()).contains(ChatColor.stripColor(text.toLowerCase()))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check whether or not the item has a display name.
     *
     * @param itemStack item to check
     * @return true if the item has a display name, false otherwise.
     */
    public static boolean hasName(ItemStack itemStack) {
        return (hasMetadata(itemStack) && itemStack.getItemMeta().hasDisplayName());
    }

    /**
     * Check if an items name contains a sequence of text
     *
     * @param item itemStack to check the name of
     * @param text text to check the items name for
     * @return true if the item name contains the text, false otherwise
     */
    public static boolean nameContains(ItemStack item, String text) {
        return hasName(item) && StringUtils.containsIgnoreCase(item.getItemMeta().getDisplayName(), text);
    }

    /**
     * Check if the item has the given material-data value assigned to it.
     *
     * @param item item to check.
     * @param id   material-data value to search for.
     * @return true if the item has the given data-value assigned, false otherwise.
     */
    public static boolean hasMaterialData(ItemStack item, int id) {
        return item.getData().getData() == id;
    }

    /**
     * Remove an amount of items from the given item stack.
     *
     * @param itemStack stack of items to modify
     * @param amount    amount of items to remove from the stack
     * @return the modified itemstack, amount deducted.
     */
    public static ItemStack removeFromStack(ItemStack itemStack, int amount) {
        int itemStackAmount = itemStack.getAmount();

        if (itemStackAmount <= amount) {
            return null;
        }

        itemStack.setAmount(itemStackAmount - amount);
        return itemStack;
    }

    /**
     * Set the color to a piece of leather armor
     *
     * @param itemStack itemstack
     * @param color
     * @return true if the color was set, otherwise false
     */
    public static void setColor(ItemStack itemStack, Color color) {
        switch (itemStack.getType()) {
            case LEATHER_BOOTS:
            case LEATHER_CHESTPLATE:
            case LEATHER_HELMET:
            case LEATHER_LEGGINGS:
                LeatherArmorMeta itemMeta = (LeatherArmorMeta) itemStack.getItemMeta();
                itemMeta.setColor(color);
                setMetadata(itemStack, itemMeta);
                break;
            default:
                return;
        }
    }


    /**
     * Add an enchantment to the given item
     *
     * @param itemStack          itemstack to apply the enchantment to
     * @param enchantment        enchantment to add to the item.
     * @param enchantmentLevel   level to give the enchantment
     * @param ignoreRestrictions whether or not to ignore level and item restrictions on the item (unsafe enchanting)
     * @return true if the enchantment was added to the item, false otherwise.
     */
    public static boolean addEnchantment(ItemStack itemStack, Enchantment enchantment, int enchantmentLevel, boolean ignoreRestrictions) {
        ItemMeta meta = itemStack.getItemMeta();
        boolean enchanted = meta.addEnchant(enchantment, enchantmentLevel, ignoreRestrictions);
        setMetadata(itemStack, meta);
        return enchanted;
    }

    /**
     * Compare the enchantments of 2 items and see if they match.
     *
     * @param item        item to match the enchantments against.
     * @param compareItem comparing item, to see if the enchantments match against the first.
     * @return true if both items have the same enchantments, false otherwise.
     */
    public static boolean hasSameEnchantments(ItemStack item, ItemStack compareItem) {
        if (!ItemUtils.hasEnchantments(item) || !ItemUtils.hasEnchantments(compareItem)) {
            return false;
        }

        Map<Enchantment, Integer> enchantments = item.getEnchantments();
        Map<Enchantment, Integer> checkEnchants = compareItem.getEnchantments();
        if (enchantments.size() != checkEnchants.size()) {
            return false;
        }
        for (Map.Entry<Enchantment, Integer> enchantmentEntry : checkEnchants.entrySet()) {
            Enchantment enchantment = enchantmentEntry.getKey();
            if (enchantments.containsKey(enchantment) && enchantments.get(enchantment).equals(enchantmentEntry.getValue())) {
                continue;
            }
            return false;
        }
        return true;
    }

    /**
     * Check if the ItemStack is of the given material type.
     *
     * @param itemStack item to check the type of.
     * @param material  material to compare the item to.
     * @return true if the item is of the given type, false otherwise.
     */
    public static boolean isType(ItemStack itemStack, Material material) {
        return itemStack != null && material == itemStack.getType();
    }

    /**
     * Check if the ItemStack is a piece of armor.
     *
     * @param itemStack item to check.
     * @return true if the item is a piece of armor, false otherwise.
     */
    public static boolean isArmor(ItemStack itemStack) {
        if (itemStack == null) {
            return false;
        }

        return isArmor(itemStack.getType());
    }

    /**
     * Check if the given Material is a piece of armor.
     *
     * @param material material to check.
     * @return true if the material is a piece of armor, false otherwise.
     */
    public static boolean isArmor(Material material) {
        if (material == null) {
            return false;
        }
        return armorMaterials.contains(material);
    }

    /**
     * Check if the item of the given type is a weapon or not.
     *
     * @param itemStack item to check.
     * @return true if the item is a weapon, false otherwise.
     */
    public static boolean isWeapon(ItemStack itemStack) {
        return WeaponType.isItemWeapon(itemStack);
    }

    //todo document.
    public static boolean isWeapon(ItemStack item, WeaponType type) {
        return WeaponType.isItemWeapon(item, type);
    }

    /**
     * Check if the material is the type of a weapon or not.
     *
     * @param material material to check.
     * @return true if the material is a weapon, false otherwise.
     */
    public static boolean isWeapon(Material material) {
        return WeaponType.isMaterialWeapon(material);
    }

    /**
     * Check if the Item is a tool; Hoe, Axe, Shovel, Flint & Tinder, etc!
     *
     * @param item item to check.
     * @return true if the item is that of any of the tool types, false otherwise.
     */
    public static boolean isTool(ItemStack item) {
        return isTool(item.getType());
    }

    /**
     * Check if the item is of the given tool type.
     *
     * @param item item to check.
     * @param type type to compare the item to.
     * @return true if the item is the of any of the materials matched by the given {@link ToolType}
     */
    public static boolean isTool(ItemStack item, ToolType type) {
        return isTool(item.getType(), type);
    }

    /**
     * Check if the material is of the given tool type.
     *
     * @param material material to check.
     * @param type     tooltype to compare the material to.
     * @return true if the material is of the given type, false otherwise.
     */
    public static boolean isTool(Material material, ToolType type) {
        return type.isType(material);
    }

    /**
     * Check if the material is a tool.
     *
     * @param type material to check.
     * @return true if the material is a tool, false otherwise.
     */
    public static boolean isTool(Material type) {
        return ToolType.isTool(type);
    }

    /**
     * Check whether or not an item is an ore.
     *
     * @param item Item to check if it's an ore.
     * @return true if the items material is the ore-block of coal, iron, diamond, emerald, redstone, gold, or lapis
     */
    public static boolean isOre(ItemStack item) {
        return BlockUtils.isOre(item.getType());
    }

    /**
     * Check whether or not the material is a smeltable ore.
     *
     * @param type the type of the ore to check if it's smeltable.
     * @return true if the ore is smeltable, otherwise false.
     */
    public static boolean isSmeltableOre(Material type) {
        switch (type) {
            case IRON_ORE:
            case GOLD_ORE:
                return true;
            default:
                return false;
        }
    }

    /**
     * Check whether or not the item is of a smeltable ore.
     *
     * @param item Item to check whether or not is a smeltable ore.
     * @return true if it's a smeltable ore, otherwise false.
     */
    public static boolean isSmeltableOre(ItemStack item) {
        return isSmeltableOre(item.getType());
    }

    /**
     * Get all the items in a tool set, to their max stack size.
     *
     * @param type set of tools to get a copy of.
     * @return a {@link HashSet} of {@link ItemStack} that the materials in the tool set make.
     */
    public static Set<ItemStack> getToolSet(ToolType type) {
        Set<ItemStack> items = type.getMaterialTypes().stream()
                .map(itemType -> ItemBuilder.of(itemType).amount(itemType.getMaxStackSize()).item())
                .collect(Collectors.toSet());
        return items;
    }

    /**
     * Create a collection of the Tool Set, with all items assigned the given stack size.
     *
     * @param type      type of ToolSet to create.
     * @param stackSize size to set each of the ItemStacks to.
     * @return a hashset with each item in the Tools Type
     */
    public static Set<ItemStack> getToolSet(ToolType type, int stackSize) {
        /*
        Create a set of item stacks by mapping the values of each individual material
		type to an item of the desired type, collecting it into a set!
		 */
        Set<ItemStack> items = type.getMaterialTypes().stream()
                .map(itemType -> ItemBuilder.of(itemType).amount(stackSize).item())
                .collect(Collectors.toSet());
        return items;

    }

    /**
     * Repair an items durability
     *
     * @param itemStack item to repair
     * @return true if the item was repaired; false if the item is a block, or unable to be repaired
     */
    public static boolean repairItem(ItemStack itemStack) {
        if (itemStack == null || itemStack.getDurability() == 0 || itemStack.getType().isBlock()) {
            return false;
        }
        itemStack.setDurability((short) 0);
        return true;
    }

    /**
     * Repair multiple item stacks
     *
     * @param itemStacks items to be repaired
     * @return integer of how many items were repaired
     */
    public static int repairItems(ItemStack... itemStacks) {
        int repairedItems = 0;
        for (ItemStack itemStack : itemStacks) {
            if (itemStack == null || itemStack.getType() == Material.AIR) {
                continue;
            }
            //If the item was repaired successfully, increment the repaired items count
            //Otherwise don't add anything
            repairedItems += repairItem(itemStack) ? 1 : 0;
        }
        return repairedItems;
    }

    /**
     * Check whether or not the given item is air.
     *
     * @param itemStack item to check
     * @return true if the item is null / air, false otherwise.
     */
    public static boolean isAir(ItemStack itemStack) {
        return itemStack == null || itemStack.getType() == Material.AIR;
    }


    public static boolean hasFlags(ItemStack item) {
        return item.getItemMeta().getItemFlags().size() > 0;
    }

    /**
     * Retrieve all the flags on an item.
     *
     * @param item item to get the flags for
     * @return set of all the item flags attached to the item.
     */
    public static Set<ItemFlag> getFlags(ItemStack item) {
        return item.getItemMeta().getItemFlags();
    }

    /***
     * Apply damage to an itemstacks durability.
     * @param item item to damage
     * @param amount amount to damage.
     */
    public static void damageItem(ItemStack item, int amount) {
        Damageable dmg = (Damageable)item.getItemMeta();
        dmg.setDamage(dmg.getDamage() + amount);
        item.setItemMeta((ItemMeta)dmg);
    }
}