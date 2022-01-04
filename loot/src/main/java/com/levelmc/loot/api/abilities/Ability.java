package com.levelmc.loot.api.abilities;


import com.levelmc.core.api.yml.Path;
import com.levelmc.core.api.yml.Skip;
import com.levelmc.core.api.yml.YamlConfig;
import com.levelmc.loot.Loot;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Attaches to an Item for players.
 * <p>
 * Applies lore and metadata
 */
public abstract class Ability extends YamlConfig {

    @Skip
    private static final AtomicInteger idGenerator = new AtomicInteger(1);

    @Skip
    private static Map<String, NamespacedKey> createdKeys = new HashMap<>();

    @Path(value = "id")
    @Getter
    private String id;

    @Path(value = "bonus-multiplier")
    @Getter
    @Setter
    private double bonusMultiplier = 0.13;

    @Skip
    private NamespacedKey abilityIdKey = null;

    public Ability(String id) {
        this.id = id;
    }

    public Ability(String id, double bonusMultiplier) {
        this(id);
        this.bonusMultiplier = bonusMultiplier;
    }

    public Ability() {
    }

    /**
     * Apply changes to the item based on the ability properties. (Lore, etc)
     * Be sure to call {@link Ability#applyAbilityId(ItemStack)} if the item does not already have the ability id.
     * @param item item to apply changes to
     * @param properties properties of the ability.
     */
    public abstract void apply(ItemStack item, AbilityProperties properties);

    /**
     * Used to retrieve unique ability id for each instance of the class (called usually inside an abstraction)
     *
     * @return NamepacedKey specific to the instanced ability.
     */
    protected NamespacedKey getAbilityIdKey() {
        if (abilityIdKey == null) {
            String id = getId();
            abilityIdKey = new NamespacedKey(Loot.getInstance(), String.format("ability-%s", id == null ? idGenerator.getAndIncrement() : id));
            Loot.getInstance().getLogger().info("Created ability key " + abilityIdKey.getKey());
        }

        return abilityIdKey;
    }

    /**
     * Check whether or not the item has this abilities ID attached to it.
     * This is done via the persistent data container, but these methods provide a much friendler & intuitive way to access
     * this information.
     * @param item item to check
     * @return true if the ability has been attached
     */
    public boolean hasAbility(ItemStack item) {

        return item.hasItemMeta() && item.getItemMeta().getPersistentDataContainer().has(getAbilityIdKey(), PersistentDataType.STRING) && item.getItemMeta().getPersistentDataContainer().get(getAbilityIdKey(), PersistentDataType.STRING).equalsIgnoreCase(getId());
    }

    /**
     * What actions to perform when the player attacks the target living entity.
     * @param player player attacking
     * @param target entity who was attacked
     * @param damage damage that was dealt
     */
    public abstract void handlePlayerAttackTarget(Player player, LivingEntity target, double damage);

    /**
     * Applies the abilities ID to the item & updates abliity slots
     * @param item item to apply the ability to.
     */
    protected void applyAbilityId(ItemStack item) {
        ItemMeta meta = null;

        if (!item.hasItemMeta()) {
            meta = Bukkit.getItemFactory().getItemMeta(item.getType());
        } else {
            meta = item.getItemMeta();
        }

        meta.getPersistentDataContainer().set(getAbilityIdKey(), PersistentDataType.STRING, getId());

        NamespacedKey abilitySlotKey = getKey("abilitySlots");

        String abilityKeySlotData = null;
        List<String> existingAbilityIds = new ArrayList<>();
        if (meta.getPersistentDataContainer().has(abilitySlotKey, PersistentDataType.STRING)) {
            abilityKeySlotData = meta.getPersistentDataContainer().get(abilitySlotKey, PersistentDataType.STRING);

            if (abilityKeySlotData != null) {
                if (abilityKeySlotData.contains("|")) {
                    existingAbilityIds = new ArrayList<String>(Arrays.asList(StringUtils.split(abilityKeySlotData, "|")));
                } else {
                    existingAbilityIds.add(abilityKeySlotData);
                }
            }

            if (existingAbilityIds.contains(getId())) {
//                LevelCore.getInstance().getLogger().info("Item " + item.getItemMeta().getDisplayName() + " already has ability " + getId());
                return;
            }
        }

        StringBuilder abilityStringBuilder = existingAbilityIds.size() > 0 ? new StringBuilder(existingAbilityIds.remove(0)) : new StringBuilder(getId());

        if (existingAbilityIds.size() > 0) {
            for (String id : existingAbilityIds) {
                abilityStringBuilder.append("|").append(id);
            }
        }


        meta.getPersistentDataContainer().set(abilitySlotKey, PersistentDataType.STRING, abilityStringBuilder.toString());

        item.setItemMeta(meta);
//        log("Updated item " + item.getItemMeta().getDisplayName() + " ability order cache to:: " + abilityStringBuilder.toString());
    }

    /**
     * Get the slot this ability is assigned on the given item.
     *
     * @param item what item to get the slot number
     * @return slot of the ability (1+) or -1 if it's not present.
     */
    protected int getAbilitySlotNumber(ItemStack item) {
        if (!hasAbility(item)) {
            return -1;
        }

        String abilitySlots = item.getItemMeta().getPersistentDataContainer().get(getKey("abilitySlots"), PersistentDataType.STRING);

        if (abilitySlots == null) {
            return -1;
        }

        if (!abilitySlots.contains(getId())) {
            return -1;
        }

        if (abilitySlots.contains("|")) {
            String[] abilityIds = StringUtils.split(abilitySlots, "|");

            int id = 1;
            for (String abilityId : abilityIds) {
                if (abilityId.equalsIgnoreCase(getId())) {
                    break;
                }

                id += 1;
            }

            return id;

            //has multiple
        } else {
            return 1;
        }
    }

    protected NamespacedKey getKey(String name) {
        NamespacedKey key = null;
        if (!createdKeys.containsKey(name)) {
            key = new NamespacedKey(Loot.getInstance(), name);
            createdKeys.put(name, key);
        } else {
            key = createdKeys.get(name);
        }
        return key;
    }
}
