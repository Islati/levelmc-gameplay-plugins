package com.levelmc.loot.api.abilities;

import com.destroystokyo.paper.ParticleBuilder;
import com.levelmc.core.api.Chanceable;
import com.levelmc.core.api.inventory.HandSlot;
import com.levelmc.core.api.players.PlayerUtils;
import com.levelmc.core.api.utils.NumberUtil;
import com.levelmc.core.api.utils.SoundUtils;
import com.levelmc.core.api.utils.TimeType;
import com.levelmc.core.api.utils.TimeUtils;
import com.levelmc.core.chat.Chat;
import com.levelmc.loot.Loot;
import com.levelmc.loot.api.levels.ItemLevelManager;
import com.levelmc.loot.api.utils.DamageIndicatorUtil;
import lombok.Getter;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * Handler for all the loots individual abilities on items.
 * <p>
 * Each ability can be created by extending the {@link Ability} class, and calling {@link Abilities#registerAbilities(Ability...)}.
 */
public class Abilities {

    @Getter
    private static Map<String, Ability> registeredAbilities = new HashMap<>();

    /**
     * Register abilities
     *
     * @param abilities abilities to register
     */
    public static void registerAbilities(Ability... abilities) {
        for (Ability a : abilities) {
            registeredAbilities.put(a.getId(), a);
            Loot.getInstance().getLogger().info("[LOOT] Registered ability with id: " + a.getId() + " from class " + a.getClass().getSimpleName());
        }
    }

    /**
     * Retrieve an ability by its id
     *
     * @param id id of the ability to get
     * @return Ability (or null if not found)
     */
    public static Ability getById(String id) {
        if (id == null) {
            return null;
        }
        return registeredAbilities.get(id);
    }

    /**
     * Check if an item has an ability by checking each ability registered and calling {@link Ability#hasAbility(ItemStack)} method.
     *
     * @param item item to check
     * @return
     */
    public static boolean hasAbility(ItemStack item) {

        for (Ability ability : registeredAbilities.values()) {
            if (ability.hasAbility(item)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Retrieve a set of abilities attached to the item.
     *
     * @param item item to check for abilities on
     * @return a hash set of abilities registered to the item. It will be empty if there are none.
     */
    public static Set<Ability> getAbilities(ItemStack item) {
        Set<Ability> abilities = new HashSet<>();

        for (Ability a : registeredAbilities.values()) {
            if (a.hasAbility(item)) {
                abilities.add(a);
            }
        }

        return abilities;
    }

    /**
     * Items have progression and thus abilities may progress with them.
     * Calling this will re apply the describing attributes of its ability to the item, given it has an ability.
     *
     * @param item
     */
    public static void updateAbilities(ItemStack item) {
        if (!hasAbility(item)) {
            return;
        }

        Set<Ability> abilities = getAbilities(item);

        TreeMap<Integer, Ability> abilityMap = new TreeMap<>();
        for (Ability ability : abilities) {
            int abilitySlot = ability.getAbilitySlotNumber(item);

            //todo if -1 then they don't have ability so we gotta find why, but it works?

            abilityMap.put(abilitySlot, ability);
//            Loot.getInstance().getLogger().info("-- Update Abilities :: Ability " + ability.getId() + " found on " + item.getItemMeta().getDisplayName() + " in slot " + abilitySlot);
        }

        abilityMap.entrySet().stream().sorted(Map.Entry.comparingByKey())
                .forEachOrdered(abilityEntry -> {
                    Ability a = abilityEntry.getValue();
                    abilityEntry.getValue().apply(item, null);
//                    Loot.getInstance().getLogger().info("Re-applied " + a.getId() + " to " + item.getItemMeta().getDisplayName());
                });
    }

    /**
     * Used internally to retrieve the level manager without having to do to much.
     *
     * @return returns the {@link ItemLevelManager} our game plugin has registered.
     */
    protected static ItemLevelManager getLevelManager() {
        return Loot.getInstance().getLevelManager();
    }


    /**
     * Additional damage that scales with the items level.
     */
    public static Ability INCREASED_DAMAGE = new Ability("increased_damage") {

        @Override
        public void apply(ItemStack item, AbilityProperties properties) {
            if (hasAbility(item)) {
//                Loot.getInstance().getLogger().info("Attempting to re-apply increased-damage to " + item.getItemMeta().getDisplayName());

                ItemMeta meta = item.getItemMeta();

                ItemLevelManager levelManager = getLevelManager();

                double levelBonusMultiplier = 1;
                if (levelManager.isItemLevelable(item)) {
                    int level = levelManager.getItemLevel(item);
                    if (level > 0) {
                        levelBonusMultiplier = levelBonusMultiplier + (getBonusMultiplier());
                    }
                }

                levelBonusMultiplier = NumberUtil.round(levelBonusMultiplier, 1);

                double damageMin = meta.getPersistentDataContainer().get(getKey(String.format("damageMin-%s", getId())), PersistentDataType.DOUBLE);
                double damageMax = meta.getPersistentDataContainer().get(getKey(String.format("damageMax-%s", getId())), PersistentDataType.DOUBLE);
                double chance = meta.getPersistentDataContainer().get(getKey(String.format("chance-%s", getId())), PersistentDataType.DOUBLE);

                damageMin = levelBonusMultiplier * damageMin;
                damageMax = levelBonusMultiplier * damageMax;
                chance = levelBonusMultiplier * chance;

                //round em' up.

                chance = NumberUtil.round(chance, 1);
                damageMin = NumberUtil.round(damageMin, 1);
                damageMax = NumberUtil.round(damageMax, 1);
                List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();


                if (damageMin == damageMax) {
                    lore.add(Chat.colorize(String.format("&7- &e%s%% chance for +&c&c%s &edmg", chance, damageMin)));
                } else {
                    lore.add(Chat.colorize(String.format("&7- &e%s%% chance for +&c%s&7-&c%s &edmg", chance, damageMin, damageMax)));
                }

                meta.setLore(lore);
                item.setItemMeta(meta);
//                Loot.getInstance().getLogger().info("Updated Item " + meta.getDisplayName() + " with increased-damage ability");
                return;
            }

            /*
            Begin applying the ability if it doesn't already have it (as checked above).
             */
            applyAbilityId(item);


            if (!item.getItemMeta().getPersistentDataContainer().has(getAbilityIdKey(), PersistentDataType.STRING)) {
                throw new NullPointerException("Unable to find abilityId in persistent data container for itemstack upon application");
            }

            /*
            Retrieve the stored meta data, generate the range of damage min and maximum, rounding off to 2 decimal places.
             */

            double damageMinMin = Double.parseDouble(properties.get("damage_min_min", "0.8"));
            double damageMinMax = Double.parseDouble(properties.get("damage_min_max", "1.2"));

            double damageMaxMin = Double.parseDouble(properties.get("damage_max_min", "1.3"));
            double damageMaxMax = Double.parseDouble(properties.get("damage_max_max", "1.5"));

            double damageMin = 0;
            if (damageMinMin == damageMinMax) {
                damageMin = damageMinMin;
            } else {
                damageMin = NumberUtil.randomDouble(damageMinMin, damageMinMax);
            }

            damageMin = NumberUtil.round(damageMin, 1);

            double damageMax = 0;

            if (damageMaxMin == damageMaxMax) {
                damageMax = damageMaxMin;

            } else {
                damageMax = NumberUtil.randomDouble(damageMinMax, damageMaxMax);
            }

            damageMax = NumberUtil.round(damageMax, 1);

            /*
            Generate activation chance.
             */
            double activationChanceMin = Double.parseDouble(properties.get("chance_min", "4.9"));
            double activationChanceMax = Double.parseDouble(properties.get("chance_max", "25.6"));

            double activationChance = 0;
            if (activationChanceMin == activationChanceMax) {
                activationChance = activationChanceMin;
            } else {
                activationChance = NumberUtil.randomDouble(activationChanceMin, activationChanceMax);
            }

            activationChance = NumberUtil.round(activationChance, 1);

            /*
            Apply generated item values to the items meta data.
             */
            ItemMeta meta = item.getItemMeta();
            List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();


            /*
            Generate the lore that will be added as part of this ability.
             */
            if (damageMin == damageMax) {
                lore.add(Chat.colorize(String.format("&7- &e%s%% chance for +&c&c%s &edmg", activationChance, damageMin)));
            } else {
                lore.add(Chat.colorize(String.format("&7- &e%s%% chance for +&c%s&7-&c%s &edmg", activationChance, damageMin, damageMax)));
            }

            /*
            Set the items persistent key values, which is used to hold the raw data of damage min, max, activation chance and forth.
             */
            meta.getPersistentDataContainer().set(getKey(String.format("damageMin-%s", getId())), PersistentDataType.DOUBLE, damageMin);
            meta.getPersistentDataContainer().set(getKey(String.format("damageMax-%s", getId())), PersistentDataType.DOUBLE, damageMax);
            meta.getPersistentDataContainer().set(getKey(String.format("chance-%s", getId())), PersistentDataType.DOUBLE, activationChance);

            meta.setLore(lore);
            item.setItemMeta(meta);

//            log(String.format("Applied increased_damage to item %s", item.getItemMeta().getDisplayName()));
        }

        @Override
        public void handlePlayerAttackTarget(Player player, LivingEntity target, double dmg) {
            ItemStack hand = PlayerUtils.getItemInHand(player, HandSlot.MAIN_HAND);

            ItemMeta meta = hand.getItemMeta();

            double damageMin = meta.getPersistentDataContainer().get(getKey(String.format("damageMin-%s", getId())), PersistentDataType.DOUBLE);
            double damageMax = meta.getPersistentDataContainer().get(getKey(String.format("damageMax-%s", getId())), PersistentDataType.DOUBLE);
            double activationChance = meta.getPersistentDataContainer().get(getKey(String.format("chance-%s", getId())), PersistentDataType.DOUBLE);

            if (!Chanceable.check(activationChance, 100)) {
                return;
            }

            double extraDamage = 0;
            if (damageMin == damageMax) {
                extraDamage = damageMin;
            } else {
                extraDamage = NumberUtil.randomDouble(damageMin, damageMax);
            }

            extraDamage = NumberUtil.round(extraDamage, 1);

            SoundUtils.playSound(player, Sound.BLOCK_SLIME_BLOCK_HIT);

            final double extraDmg = extraDamage;
            target.damage(extraDmg, player);
            new BukkitRunnable() {

                @Override
                public void run() {
                    DamageIndicatorUtil.getInstance().spawnDamageIndicator(target, Chat.colorize(String.format("&c-%s &7(&oExtra&r&7)", extraDmg)), EntityDamageEvent.DamageCause.CUSTOM, extraDmg);
                }
            }.runTaskLater(Loot.getInstance(), 5);
            //todo implement an animation (crit / clouds / player selected animation cosmetic)
        }
    };

    public static class IncreasedDamageProperties extends AbilityProperties {
        public IncreasedDamageProperties(double damageMinMin, double damageMinMax, double damageMaxMin, double damageMaxMax, double chanceMin, double chanceMax) {
            set("damage_min_min", String.valueOf(damageMinMin));
            set("damage_min_max", String.valueOf(damageMinMax));
            set("damage_max_min", String.valueOf(damageMaxMin));
            set("damage_max_max", String.valueOf(damageMaxMax));
            set("chance_min", String.valueOf(chanceMin));
            set("chance_max", String.valueOf(chanceMax));
        }
    }

    /**
     * Ability which provides a chance to make the enemy weak (apply PotionEffectType.WEAKNESS)
     * for a duration of time, which grows stronger (scales) as the item levels.
     */
    public static Ability WEAKNESS = new Ability("weakness") {
        @Override
        public void apply(ItemStack item, AbilityProperties properties) {
            if (hasAbility(item)) {
                ItemMeta meta = item.getItemMeta();

                int minTime = meta.getPersistentDataContainer().get(getKey(String.format("minTime-%s", getId())), PersistentDataType.INTEGER);
                int maxTime = meta.getPersistentDataContainer().get(getKey(String.format("maxTime-%s", getId())), PersistentDataType.INTEGER);
                double chance = meta.getPersistentDataContainer().get(getKey(String.format("chance-%s", getId())), PersistentDataType.DOUBLE);
                int level = meta.getPersistentDataContainer().get(getKey(String.format("level-%s", getId())), PersistentDataType.INTEGER);

                int bonusTime = 0;
                double chanceBonusMultiplier = 0;

                ItemLevelManager manager = getLevelManager();
                if (manager.isItemLevelable(item)) {
                    int itemLevel = manager.getItemLevel(item);
                    if (itemLevel >= 20) {
                        for (int i = itemLevel; i > 0; i -= 20) {
                            bonusTime += 1;
                            chanceBonusMultiplier += 2;
                        }
                    }
                }

                chance = NumberUtil.round(chance + (chance * (chanceBonusMultiplier / 100)), 1);
                minTime += bonusTime;
                maxTime += bonusTime;
                List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();


                if (minTime == maxTime) {
                    lore.add(Chat.colorize(String.format("&7- &e%s%% chance of &bWeakness %s&e for %ss", chance, NumberUtil.toRoman(level), minTime)));
                } else {
                    lore.add(Chat.colorize(String.format("&7- &e%s%% chance of &bWeakness %s &efor &7&o%s &r&eto &7&o%ss", chance, NumberUtil.toRoman(level), minTime, maxTime)));
                }

                meta.setLore(lore);
                item.setItemMeta(meta);
                return;
            }

            applyAbilityId(item);

            int minTimeMin = Integer.parseInt(properties.get("min_time_min", "1"));
            int minTimeMax = Integer.parseInt(properties.get("min_time_max", "2"));
            int maxTimeMin = Integer.parseInt(properties.get("max_time_min", "3"));
            int maxTimeMax = Integer.parseInt(properties.get("max_time_max", "5"));
            double chanceMin = Double.parseDouble(properties.get("chanceMin", "10"));
            double chanceMax = Double.parseDouble(properties.get("chanceMax", "22.5"));
            int levelMin = Integer.parseInt(properties.get("level_min", "1"));
            int levelMax = Integer.parseInt(properties.get("level_max", "2"));

            int minTime = 0;
            int maxTime = 0;

            if (minTimeMin == minTimeMax) {
                minTime = minTimeMin;
            } else {
                minTime = NumberUtil.getRandomInRange(minTimeMin, minTimeMax);
            }

            if (maxTimeMin == maxTimeMax) {
                maxTime = maxTimeMin;
            } else {
                maxTime = NumberUtil.getRandomInRange(maxTimeMin, maxTimeMax);
            }

            double chance = 0;

            if (chanceMin == chanceMax) {
                chance = chanceMin;
            } else {
                chance = NumberUtil.randomDouble(chanceMin, chanceMax);
            }

            chance = NumberUtil.round(chance, 1);

            int level = 1;

            if (levelMin == levelMax) {
                level = levelMin;
            } else {
                level = NumberUtil.getRandomInRange(levelMin, levelMax);
            }

            ItemMeta meta = item.getItemMeta();
            List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();


            if (minTime == maxTime) {
                lore.add(Chat.colorize(String.format("&7- &e%s%% chance of &bWeakness %s&e for %ss", chance, NumberUtil.toRoman(level), minTime)));
            } else {
                lore.add(Chat.colorize(String.format("&7- &e%s%% chance of &bWeakness %s &efor &7&o%s &r&eto &7&o%ss", chance, NumberUtil.toRoman(level), minTime, maxTime)));
            }

            meta.setLore(lore);
//            log("Updated lore for weakness with " + ListUtils.implode(",", lore));
            meta.getPersistentDataContainer().set(getKey(String.format("minTime-%s", getId())), PersistentDataType.INTEGER, minTime);
            meta.getPersistentDataContainer().set(getKey(String.format("maxTime-%s", getId())), PersistentDataType.INTEGER, maxTime);
            meta.getPersistentDataContainer().set(getKey(String.format("chance-%s", getId())), PersistentDataType.DOUBLE, chance);
            meta.getPersistentDataContainer().set(getKey(String.format("level-%s", getId())), PersistentDataType.INTEGER, level);

            item.setItemMeta(meta);
//            log(String.format("Applied weakness to item %s", item.getItemMeta().getDisplayName()));
        }

        @Override
        public void handlePlayerAttackTarget(Player player, LivingEntity target, double dmg) {
            ItemStack item = PlayerUtils.getItemInHand(player, HandSlot.MAIN_HAND);

            ItemMeta meta = item.getItemMeta();

            int minTime = meta.getPersistentDataContainer().get(getKey(String.format("minTime-%s", getId())), PersistentDataType.INTEGER);
            int maxTime = meta.getPersistentDataContainer().get(getKey(String.format("maxTime-%s", getId())), PersistentDataType.INTEGER);
            double chance = meta.getPersistentDataContainer().get(getKey(String.format("chance-%s", getId())), PersistentDataType.DOUBLE);
            int level = meta.getPersistentDataContainer().get(getKey(String.format("level-%s", getId())), PersistentDataType.INTEGER);

            if (!Chanceable.check(chance, 100)) {
                return;
            }

            int timeInSeconds = minTime == maxTime ? minTime : NumberUtil.getRandomInRange(minTime, maxTime);

            target.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, (int) TimeUtils.getTimeInTicks(timeInSeconds, TimeType.SECOND), level));
            new BukkitRunnable() {
                @Override
                public void run() {
                    DamageIndicatorUtil.getInstance().spawnDamageIndicator(target, Chat.colorize("&c☠ &aPoisoned &c☠"), EntityDamageEvent.DamageCause.CUSTOM, 1);
                }
            }.runTaskLater(Loot.getInstance(), 10);
        }
    };

    public static class WeaknessProperties extends AbilityProperties {
        public WeaknessProperties(int minTimeMin, int minTimeMax, int maxTimeMin, int maxTimeMax, double chanceMin, double chanceMax, int levelMin, int levelMax) {
            set("min_time_min", String.valueOf(minTimeMin));
            set("min_time_max", String.valueOf(minTimeMax));
            set("max_time_min", String.valueOf(maxTimeMin));
            set("max_time_max", String.valueOf(maxTimeMax));
            set("chance_min", String.valueOf(chanceMin));
            set("chance_max", String.valueOf(chanceMax));
            set("level_max", String.valueOf(levelMin));
            set("level_max", String.valueOf(levelMax));
        }
    }

    /**
     * Ability which provides healing upon damage of an enemy, and scales with the growth of the item its attached to.
     */
    public static Ability VAMPIRISM = new Ability("vampirism") {

        @Override
        public void apply(ItemStack item, AbilityProperties properties) {
            if (hasAbility(item)) {
                ItemMeta meta = item.getItemMeta();
                double chance = meta.getPersistentDataContainer().get(getKey("chance-" + getId()), PersistentDataType.DOUBLE);
                int healAmount = meta.getPersistentDataContainer().get(getKey("healAmt-" + getId()), PersistentDataType.INTEGER);

                double chanceBonus = 0;
                int healBonus = 0;

                if (getLevelManager().isItemLevelable(item)) {
                    int abLvl = getLevelManager().getItemLevel(item);

                    if (abLvl >= 20) {
                        for (int i = abLvl; i > 0; i -= 20) {
                            chanceBonus += 0.5;
                            healBonus += 1;
                        }
                    }
                }

                chance = NumberUtil.round(chance + chanceBonus, 1);
                healAmount += healBonus;

                meta.getPersistentDataContainer().set(getKey(String.format("chance-%s", getId())), PersistentDataType.DOUBLE, chance);
                meta.getPersistentDataContainer().set(getKey(String.format("healAmt-%s", getId())), PersistentDataType.INTEGER, healAmount);
                List<String> itemLore = meta.hasLore() ? meta.getLore() : new ArrayList<>();

                itemLore.add(Chat.colorize(String.format("&7- &e%s%% chance to restore &b%s%%&e of damage as hp", chance, healAmount)));
                meta.setLore(itemLore);
                item.setItemMeta(meta);
//                log(String.format("Re-Applied %s to %s", getId(), item.getItemMeta().getDisplayName()));
                //todo update ability on item with re apply
                return;
            }

            applyAbilityId(item);

            double minChanceAmt = Double.parseDouble(properties.get("min_chance", "2.2"));
            double maxChanceAmt = Double.parseDouble(properties.get("max_chance", "6.9"));
            int minHealAmt = Integer.parseInt(properties.get("min_heal_amount", "10"));
            int maxHealAmt = Integer.parseInt(properties.get("max_heal_amount", "15"));

            double chance = minChanceAmt == maxChanceAmt ? minChanceAmt : NumberUtil.round(NumberUtil.randomDouble(minChanceAmt, maxChanceAmt), 1);
            int healAmt = minHealAmt == maxChanceAmt ? minHealAmt : NumberUtil.getRandomInRange(minHealAmt, maxHealAmt);

            ItemMeta meta = item.getItemMeta();

            meta.getPersistentDataContainer().set(getKey(String.format("chance-%s", getId())), PersistentDataType.DOUBLE, chance);
            meta.getPersistentDataContainer().set(getKey(String.format("healAmt-%s", getId())), PersistentDataType.INTEGER, healAmt);
            List<String> itemLore = meta.hasLore() ? meta.getLore() : new ArrayList<>();


            itemLore.add(Chat.colorize(String.format("&7- &e%s%% chance to restore &b%s%%&e of damage in health", chance, healAmt)));
            meta.setLore(itemLore);
            item.setItemMeta(meta);
//            log(String.format("Applied %s to %s", getId(), item.getItemMeta().getDisplayName()));
        }


        @Override
        public void handlePlayerAttackTarget(Player player, LivingEntity target, double damage) {
            ItemMeta meta = PlayerUtils.getItemInHand(player, HandSlot.MAIN_HAND).getItemMeta();
            double chance = meta.getPersistentDataContainer().get(getKey("chance-" + getId()), PersistentDataType.DOUBLE);
            int healAmount = meta.getPersistentDataContainer().get(getKey("healAmt-" + getId()), PersistentDataType.INTEGER);

            if (!Chanceable.check(chance, 100)) {
                return;
            }

            double healthRestored = NumberUtil.round(damage * ((double) healAmount / 100), 1);

            PlayerUtils.restoreHealth(player, healthRestored);
            // Spawn particle Effects.
            new ParticleBuilder(Particle.HEART).location(target.getLocation()).count(NumberUtil.getRandomInRange(5, 9)).offset(0.33, 0.33, 0.33).spawn();

            new BukkitRunnable() {
                @Override
                public void run() {
                    DamageIndicatorUtil.getInstance().spawnDamageIndicator(target, Chat.colorize(String.format("&c❤ &7(You) &a+ &l%s&r &c❤", healthRestored)), EntityDamageEvent.DamageCause.CUSTOM, 1);
                }
            }.runTaskLater(Loot.getInstance(), NumberUtil.getRandomInRange(5, 10));
        }
    };

    public static class VampirismProperties extends AbilityProperties {
        public VampirismProperties(int minChance, int maxChance, int minHealAmount, int maxHealAmount) {
            set("min_chance", String.valueOf(minChance));
            set("max_chance", String.valueOf(maxChance));
            set("min_heal_amount", String.valueOf(minHealAmount));
            set("max_heal_amount", String.valueOf(maxHealAmount));
        }
    }

    /**
     * Ability which provides damage over time to an enemy upon successful activation.
     * % Chance, % Damage, and time duration grows with the items level.
     */
    public static Ability BLEED = new Ability("bleed") {

        @Override
        public void apply(ItemStack item, AbilityProperties properties) {
            if (hasAbility(item)) {
                //reapply
                ItemMeta meta = item.getItemMeta();

                double chance = meta.getPersistentDataContainer().get(getKey(String.format("chance-%s", getId())), PersistentDataType.DOUBLE);
                double damagePercent = meta.getPersistentDataContainer().get(getKey(String.format("damagePercent-%s", getId())), PersistentDataType.DOUBLE);
                int amount = meta.getPersistentDataContainer().get(getKey(String.format("amount-%s", getId())), PersistentDataType.INTEGER);

                double bonusMultiplier = 1;

                if (getLevelManager().isItemLevelable(item)) {
                    int itemLevel = getLevelManager().getItemLevel(item);

                    if (itemLevel >= 20) {
                        for (int i = itemLevel; i > 0; i -= 20) {
                            bonusMultiplier += 0.45;
                        }
                    }

                    if (itemLevel >= 60) {
                        for (int i = itemLevel; i > 0; i -= 60) {
                            amount += 1;
                        }
                    }
                }

                chance = NumberUtil.round(chance + (chance * bonusMultiplier), 1);
                damagePercent = NumberUtil.round(damagePercent + (damagePercent * bonusMultiplier), 1);

                List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();


                lore.add(Chat.colorize(String.format("&7- &e%s%% chance for &b%s%%&c of dmg to bleed &b%sx", chance, damagePercent, amount)));
                meta.setLore(lore);
                item.setItemMeta(meta);
//                log(String.format("Re-Applied %s to %s", getId(), item.getItemMeta().getDisplayName()));
                return;
            }

            applyAbilityId(item);

            double chanceMin = Double.parseDouble(properties.get("chance_min", "5.5"));
            double chanceMax = Double.parseDouble(properties.get("chance_max", "10.5"));

            double damagePercentMin = Double.parseDouble(properties.get("damage_min", "20"));
            double damagePercentMax = Double.parseDouble(properties.get("damage_max", "45"));

            int amountMin = Integer.parseInt(properties.get("amount_min", "1"));
            int amountMax = Integer.parseInt(properties.get("amount_max", "2"));

            double chance = NumberUtil.round(NumberUtil.randomDouble(chanceMin, chanceMax), 1);
            double damagePercent = NumberUtil.round(NumberUtil.randomDouble(damagePercentMin, damagePercentMax), 1);
            int amount = amountMin == amountMax ? amountMin : NumberUtil.getRandomInRange(amountMin, amountMax);

            ItemMeta meta = item.getItemMeta();

            meta.getPersistentDataContainer().set(getKey(String.format("chance-%s", getId())), PersistentDataType.DOUBLE, chance);
            meta.getPersistentDataContainer().set(getKey(String.format("damagePercent-%s", getId())), PersistentDataType.DOUBLE, damagePercent);
            meta.getPersistentDataContainer().set(getKey(String.format("amount-%s", getId())), PersistentDataType.INTEGER, amount);

            List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();

            lore.add(Chat.colorize(String.format("&7- &e%s%% chance for &b%s%%&e of dmg to bleed &b%sx", chance, damagePercent, amount)));
            meta.setLore(lore);
            item.setItemMeta(meta);
//            log(String.format("Applied %s to %s", getId(), item.getItemMeta().getDisplayName()));
        }


        @Override
        public void handlePlayerAttackTarget(Player player, LivingEntity target, double damage) {

            ItemMeta meta = PlayerUtils.getItemInHand(player, HandSlot.MAIN_HAND).getItemMeta();

            double chance = meta.getPersistentDataContainer().get(getKey(String.format("chance-%s", getId())), PersistentDataType.DOUBLE);
            double damagePercent = meta.getPersistentDataContainer().get(getKey(String.format("damagePercent-%s", getId())), PersistentDataType.DOUBLE);

            int amount = meta.getPersistentDataContainer().get(getKey(String.format("amount-%s", getId())), PersistentDataType.INTEGER);

            if (!Chanceable.check(chance, 100)) {
                return;
            }

            double damageAmount = damage * (damagePercent / 100);

            for (int i = 0; i < amount; i++) {
                int delay = i + 1;
                delay = delay * 20;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        try {
                            target.damage(damageAmount, player);
                            DamageIndicatorUtil.getInstance().spawnDamageIndicator(target, Chat.colorize(String.format("&c☠&a -&l%s&r &cBleed", damageAmount)), EntityDamageEvent.DamageCause.CUSTOM, damageAmount);
                            new ParticleBuilder(Particle.REDSTONE).count(NumberUtil.getRandomInRange(8, 13)).location(target.getLocation()).spawn();
                            SoundUtils.playSound(player, Sound.ENTITY_SLIME_SQUISH);
                        } catch (Exception e) {
                            cancel();
                        }
                    }
                }.runTaskLater(Loot.getInstance(), delay);
            }

        }
    };

    public static class BleedProperties extends AbilityProperties {
        public BleedProperties(double chanceMin, double chanceMax, double damageMin, double damageMax, int amountMin, int amountMax) {
            set("chance_min", String.valueOf(chanceMin));
            set("chance_max", String.valueOf(chanceMax));
            set("damage_min", String.valueOf(damageMin));
            set("damage_max", String.valueOf(damageMax));
            set("amount_min", String.valueOf(amountMin));
            set("amount_max", String.valueOf(amountMax));
        }
    }

}
