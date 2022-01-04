package com.levelmc.loot.api.levels;

import com.google.common.collect.Lists;
import com.levelmc.core.api.utils.PluginUtils;
import com.levelmc.core.api.utils.ProgressBarUtil;
import com.levelmc.core.api.yml.Comments;
import com.levelmc.core.api.yml.Path;
import com.levelmc.core.api.yml.Skip;
import com.levelmc.core.api.yml.YamlConfig;
import com.levelmc.core.chat.Chat;
import com.levelmc.loot.Loot;
import com.levelmc.loot.api.abilities.Abilities;
import com.levelmc.loot.api.levels.events.ItemGainExpEvent;
import com.levelmc.loot.api.levels.events.ItemLevelEvent;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages the levels of items, and their progression.
 */
public class ItemLevelManager extends YamlConfig {

    @Path("levels.item-description")
    private List<String> itemDescription = Lists.newArrayList("&7", "&e✘ &7Level &b{level}", "&7&l[&r{progress_bar}&r&l&7]", "&7{exp}/{exp_to_lvl} &7&lExp");

    @Comments({"Forumula is (level * value) + (outcome * tax) = experience required."})
    @Path("levels.exp-per-level")
    @Getter
    private int expPerLevel = 420;

    @Path("levels.tax-per-level")
    @Getter
    private int taxPerLevel = 150;

    @Path("levels.level-cap")
    @Getter
    private int maxItemLevel = 200;

    @Path("levels.experience-rewards.mobs")
    @Getter
    private Map<String, Integer> mobExperienceRewards = new HashMap<>();

    @Path("levels.experience-rewards.ores")
    private Map<String, Integer> oreExperienceTypes = new HashMap<>();

    @Skip
    private Map<Integer, Integer> levelUpExpCache = new HashMap<>();

    @Skip
    private NamespacedKey keyItemLevelable = null;

    @Skip
    private NamespacedKey keyItemExperience = null;

    @Skip
    private NamespacedKey keyItemLoreEndIndex = null;

    public ItemLevelManager(Loot core) {

        keyItemLevelable = new NamespacedKey(core, "isItemLevelable");
        keyItemExperience = new NamespacedKey(core, "itemExp");
        keyItemLoreEndIndex = new NamespacedKey(core, "loreIndexEnd");

        setMobExpReward(EntityType.SHEEP, 10)
                .setMobExpReward(EntityType.PIG, 10)
                .setMobExpReward(EntityType.COW, 10)
                .setMobExpReward(EntityType.VILLAGER, 20)
                .setMobExpReward(EntityType.ZOMBIFIED_PIGLIN, 50)
                .setMobExpReward(EntityType.ENDERMAN, 50)
                .setMobExpReward(EntityType.BLAZE, 80)
                .setMobExpReward(EntityType.CREEPER, 50)
                .setMobExpReward(EntityType.WITCH, 115)
                .setMobExpReward(EntityType.SKELETON, 45)
                .setMobExpReward(EntityType.WITHER_SKELETON, 60)
                .setMobExpReward(EntityType.EVOKER, 100)
                .setMobExpReward(EntityType.SPIDER, 40)
                .setMobExpReward(EntityType.CAVE_SPIDER, 65)
                .setMobExpReward(EntityType.BAT, 10)
                .setMobExpReward(EntityType.COD, 5)
                .setMobExpReward(EntityType.ENDER_DRAGON, 4000)
                .setMobExpReward(EntityType.GHAST, 90)
                .setMobExpReward(EntityType.HUSK, 65)
                .setMobExpReward(EntityType.MUSHROOM_COW, 10)
                .setMobExpReward(EntityType.FOX, 10)
                .setMobExpReward(EntityType.BEE, 10)
                .setMobExpReward(EntityType.SLIME, 15)
                .setMobExpReward(EntityType.RABBIT, 5)
                .setMobExpReward(EntityType.ZOMBIE, 40)
                .setMobExpReward(EntityType.CHICKEN, 5)
                .setMobExpReward(EntityType.AXOLOTL, 5)
                .setMobExpReward(EntityType.GIANT, 100)
                .setMobExpReward(EntityType.DOLPHIN, 10)
                .setMobExpReward(EntityType.DONKEY, 10)
                .setMobExpReward(EntityType.DROWNED, 30)
                .setMobExpReward(EntityType.ENDERMITE, 5)
                .setMobExpReward(EntityType.EVOKER, 100)
                .setMobExpReward(EntityType.GLOW_SQUID, 15)
                .setMobExpReward(EntityType.GUARDIAN, 100)
                .setMobExpReward(EntityType.GOAT, 25)
                .setMobExpReward(EntityType.HOGLIN, 25)
                .setMobExpReward(EntityType.ILLUSIONER, 50)
                .setMobExpReward(EntityType.MAGMA_CUBE, 15)
                .setMobExpReward(EntityType.LLAMA, 15)
                .setMobExpReward(EntityType.PANDA, 25)
                .setMobExpReward(EntityType.PARROT, 15)
                .setMobExpReward(EntityType.OCELOT, 5)
                .setMobExpReward(EntityType.HORSE, 20)
                .setMobExpReward(EntityType.SALMON, 5)
                .setMobExpReward(EntityType.SILVERFISH, 5)
                .setMobExpReward(EntityType.MULE, 15)
                .setMobExpReward(EntityType.PHANTOM, 50)
                .setMobExpReward(EntityType.POLAR_BEAR, 35)
                .setMobExpReward(EntityType.PUFFERFISH, 35)
                .setMobExpReward(EntityType.PLAYER, 85)
                .setMobExpReward(EntityType.RAVAGER, 35)
                .setMobExpReward(EntityType.ZOMBIFIED_PIGLIN, 25)
                .setMobExpReward(EntityType.STRAY, 5)
                .setMobExpReward(EntityType.PIGLIN_BRUTE, 25)
                .setMobExpReward(EntityType.SHULKER, 100)
                .setMobExpReward(EntityType.SNOWMAN, 5)
                .setMobExpReward(EntityType.STRIDER, 20)
                .setMobExpReward(EntityType.VEX, 50)
                .setMobExpReward(EntityType.TURTLE, 15)
                .setMobExpReward(EntityType.TRADER_LLAMA, 15)
                .setMobExpReward(EntityType.ZOMBIE_VILLAGER, 25)
                .setMobExpReward(EntityType.ZOMBIE_HORSE, 25)
                .setMobExpReward(EntityType.SKELETON_HORSE, 25)
                .setMobExpReward(EntityType.WANDERING_TRADER, 25)
                .setMobExpReward(EntityType.ZOGLIN, 25);

        /* TODO FILL ORE EXP REWARDS AND TEST */

        for (int i = 1; i <= maxItemLevel; i++) {
            levelUpExpCache.put(i, expPerLevel * i + (expPerLevel * (taxPerLevel / 100)));
        }

    }

    public int getExpForLevel(int level) {
        if (level >= maxItemLevel) {
            return levelUpExpCache.get(maxItemLevel);
        }

        return levelUpExpCache.get(level);
    }

    /**
     * Based on the experience provided, determine what level the item is.
     *
     * @param exp experience points
     * @return level of the item based on the experience provided.
     */
    public int getLevelFromExp(int exp) {
        if (exp == -1) {
            return -1;
        }

        for (Map.Entry<Integer, Integer> levelExp : levelUpExpCache.entrySet()) {
            int level = levelExp.getKey();
            int expForLevel = levelExp.getValue();

            if (level >= maxItemLevel) {
                return level;
            }


            if (exp <= expForLevel && exp < getExpForLevel(level + 1)) {
                return level;
            }
        }

        return -1;
    }

    public ItemLevelManager setMobExpReward(EntityType type, int expReward) {
        mobExperienceRewards.put(type.name().toLowerCase(), expReward);
        return this;
    }

    public ItemLevelManager setOreExpReward(Material material, int expReward) {
        oreExperienceTypes.put(material.name().toLowerCase(), expReward);
        return this;
    }

    public int getExpReward(EntityType type) {
        if (!mobExperienceRewards.containsKey(type.name().toLowerCase())) {
            return 0;
        }

        return mobExperienceRewards.get(type.name().toLowerCase());
    }

    public boolean isItemLevelable(ItemStack item) {
        if (!item.hasItemMeta()) {
            return false;
        }

        return item.getItemMeta().getPersistentDataContainer().has(keyItemLevelable, PersistentDataType.INTEGER);
    }

    public int getItemLevel(ItemStack item) {
        return getLevelFromExp((int) getItemExp(item));
    }

    public int getItemExp(ItemStack item) {
        if (!isItemLevelable(item)) {
            return -1;
        }
        int itemExp = item.getItemMeta().getPersistentDataContainer().get(keyItemExperience, PersistentDataType.INTEGER);
        return itemExp;
    }

    public void setItemExp(ItemStack item, int amount) {
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(keyItemExperience, PersistentDataType.INTEGER, amount);
        item.setItemMeta(meta);
    }

    /**
     * Apply the given level to the item.
     *
     * @param item  item to give level to
     * @param level level to give the item.
     */
    public void setLevel(ItemStack item, int level) {
        ItemMeta meta = null;

        if (item.hasItemMeta()) {
            meta = item.getItemMeta();
        } else {
            meta = Bukkit.getItemFactory().getItemMeta(item.getType());
        }

        //Give the key to show the item is actually levelable
        int startingExp = level <= 1 ? 0 : getExpForLevel(level);
        int expForLevelUp = getExpForLevel(Math.max(level, 1));
        meta.getPersistentDataContainer().set(keyItemLevelable, PersistentDataType.INTEGER, level);
        //Store the experience.
        meta.getPersistentDataContainer().set(keyItemExperience, PersistentDataType.INTEGER, startingExp);

        List<String> placeholderUpdatedLore = new ArrayList<>();
        if (meta.hasLore()) {
            placeholderUpdatedLore = new ArrayList<>(meta.getLore());
        }

        Abilities.updateAbilities(item);

        for (String lore : itemDescription) {
            placeholderUpdatedLore.add(Chat.colorize(lore.replace("{level}", String.valueOf(level)).replace("{exp}", String.valueOf(startingExp)).replace("{exp_to_lvl}", String.valueOf(expForLevelUp)).replace("{progress_bar}", ProgressBarUtil.renderProgressBar("&a", "&c", ProgressBarUtil.DEFAULT_BAR, 0))));
        }

        meta.setLore(placeholderUpdatedLore);
        item.setItemMeta(meta);
    }

    /**
     * Add exp to the item and update its lore all in one go (aesthetics).
     *
     * @param item      item to add experience to.
     * @param expAmount amount of experience to add to item
     */
    public void addItemExperience(Player player, ItemStack item, int expAmount) {

        if (!isItemLevelable(item)) {
            setLevel(item, 1);
        }

        ItemMeta meta = item.getItemMeta();

        meta.setLore(new ArrayList<>());

        item.setItemMeta(meta);

        Abilities.updateAbilities(item);

        meta = item.getItemMeta();

        List<String> placeholderUpdatedLore = meta.getLore();

        if (placeholderUpdatedLore == null) {
            placeholderUpdatedLore = new ArrayList<>();
        }

        int currentItemExp = getItemExp(item);
        int currentLevel = getLevelFromExp((int) currentItemExp);

        double newItemExp = currentItemExp + expAmount;

        double itemLevel = getLevelFromExp((int) newItemExp);
        double expForLevel = getExpForLevel((int) itemLevel);

        ItemGainExpEvent event = new ItemGainExpEvent(player, item, expAmount);
        PluginUtils.callEvent(event);

        if (event.isCancelled()) {
            return;
        }

        //Increase items experience by amount given back from the event above.
        setItemExp(item, currentItemExp + event.getAmount());
        meta = item.getItemMeta();

        double progressPercent = (newItemExp / expForLevel) * 100;

        for (String line : this.itemDescription) {
            String updatedLine = line.replace("{level}", String.valueOf((int) itemLevel));
            updatedLine = updatedLine.replace("{exp}", String.valueOf((int) newItemExp));
            updatedLine = updatedLine.replace("{exp_to_lvl}", String.valueOf((int) expForLevel));
            updatedLine = updatedLine.replace("{progress_bar}", ProgressBarUtil.renderProgressBar("&a", "&c", ProgressBarUtil.DEFAULT_BAR, progressPercent));

            placeholderUpdatedLore.add(Chat.colorize(updatedLine));
        }

        meta.setLore(placeholderUpdatedLore);
        item.setItemMeta(meta);

        /*
        Check if the user has had an item level up.
         */
        if (itemLevel > currentLevel) {
            ItemLevelEvent itemLevelEvent = new ItemLevelEvent(player, item, (int) itemLevel);
            PluginUtils.callEvent(itemLevelEvent);
        }
    }
}
