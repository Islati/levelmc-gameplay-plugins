package com.levelmc.wizards.spells;

import com.levelmc.core.chat.Chat;
import com.levelmc.core.api.item.ItemBuilder;
import com.levelmc.core.api.utils.NumberUtil;
import com.levelmc.loot.api.utils.DamageIndicatorUtil;
import com.levelmc.wizards.Wizards;
import com.levelmc.wizards.users.Wizard;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public interface Spell {
    String id();

    int getManaCost(Player player, int level);

    int getMaxLevel(Player player);

    int getCooldown(Player player, int level);

    int getExpAward(Player player, int level);

    void onCast(Player player);

    void onHitEntity(Player player, Entity hit);

    void onHitBlock(Player player, Block block);

    /**
     * Conditions aside from mana requirements.
     * @param player player casting the spell
     * @return true if they can cast the spell, false otherwise.
     */
    default boolean canCast(Player player) {
        return false;
    }

    MagicType getType();

    List<String> getSpellDescription(Player player);

    default Material getMenuMaterial() {
        return Material.PAPER;
    }

    default ItemStack getMenuItem(Player player) {
        return ItemBuilder.of(getMenuMaterial()).name(getSpellName(player))
                .lore(getMenuLore(player))
                .item();
    }

    default List<String> getMenuLore(Player player) {

        List<String> spellDescription = getSpellDescription(player);

        if (spellDescription == null) {
            spellDescription = new ArrayList<>();
        } else {
            spellDescription = new ArrayList<>(spellDescription);
        }


        if (!hasSpellUnlocked(player)) {
            spellDescription.add("");
            spellDescription.add("&c(&lLocked&r&c)");
            return spellDescription;
        }

        int spellLevel = getPlayerSpellLevel(player);
        int maxLevel = getMaxLevel(player);

        boolean maxed = spellLevel >= maxLevel;

        int manaCost = getManaCost(player, spellLevel);

        String currentLevelManaCostStr = String.format("&a%s ✧ Mana/Cast", manaCost);

        String nextLevelManaCostStr = String.format("&a%s ✧ Mana/Cast", getManaCost(player, spellLevel + 1));

        int currentLvlCooldown = getCooldown(player, spellLevel);
        int nextLvlCooldown = getCooldown(player, spellLevel + 1);
        String currentLevelCooldownStr = String.format("&eCooldown&7: &b%ss", currentLvlCooldown);
        String nextLevelCooldownStr = String.format("&eCooldown&7: &b%ss", nextLvlCooldown);

        int expReward = getExpAward(player, spellLevel);
        int nextLevelExpReward = getExpAward(player, spellLevel + 1);

        String currentLevelExpStr = String.format("&a+ %sxp/hit", expReward);
        String nextLevelExpStr = String.format("&a+ %sxp/hit", nextLevelExpReward);

        String spellLevelRoman = NumberUtil.toRoman(spellLevel);
        String maxLevelRoman = NumberUtil.toRoman(maxLevel);

        Wizard user = getUser(player);
        if (maxed) {
            spellDescription.add("&6(&7Max Lvl&6)");

            /* show extra levels added ontop of general maximum */
            if (user.hasExtraMaxLevel(id())) {
                spellDescription.add(String.format("&a+%s Extra Lvls", user.getExtraMaxLevel(id())));
            }
        } else {
            spellDescription.add("&7(Current Lvl)");
        }

        /* Current level informtion */
        spellDescription.add(currentLevelManaCostStr);
        spellDescription.add(currentLevelCooldownStr);
        spellDescription.add(currentLevelExpStr);


        if (!maxed) {
            //append next level stats.
            spellDescription.add("");
            spellDescription.add("&7(Next Lvl)");
            spellDescription.add(nextLevelManaCostStr);
            spellDescription.add(nextLevelCooldownStr);
            spellDescription.add(nextLevelExpStr);
        }

        spellDescription.add("");
        spellDescription.add(String.format("&eLevel &b%s&f/&b%s", spellLevelRoman, maxLevelRoman));
        return spellDescription;
    }

    default String getSpellName(Player player) {
        return getSpellName(player,getPlayerSpellLevel(player));
    }

    default String getSpellName(Player player, int playerLevel) {
        String additionalText = "";

        if (playerLevel == -1) {
            additionalText = "&7(&cLocked&7)";
        } else {
            additionalText = NumberUtil.toRoman(playerLevel);
        }

        return String.format("&r&f%s%s %s", getType().getNamePrefix(), StringUtils.capitalize(id().replace("-", " ")), additionalText);
    }

    /**
     * Helper method used to get the {@link Wizard} inside spell implementations.
     *
     * @param player player to retrieve data for.
     * @return user data.
     */
    default Wizard getUser(Player player) {
        return Wizards.getInstance().getUserManager().getUser(player);
    }

    /**
     * Helper method used to get the players level of the spell.
     *
     * @param player player to retrieve data for.
     * @return player spell level.
     */
    default int getPlayerSpellLevel(Player player) {
        return getUser(player).getSpellLevel(id());
    }

    default boolean hasSpellEquipped(Player player) {
        return getUser(player).hasSpellEquipped(id());
    }

    default boolean hasSpellUnlocked(Player player) {
        return getUser(player).hasSpellUnlocked(id());
    }

    default SpellBindSlot getSpellSlot(Player player) {
        return getUser(player).getSpellBindSlot(id());
    }

    default void displayDamage(Player player, double damage, LivingEntity hitMob) {
        DamageIndicatorUtil.getInstance().spawnDamageIndicatorDelayed(hitMob, Chat.colorize(String.format("&e✯ &6%s &e✯", String.valueOf(damage))), EntityDamageEvent.DamageCause.CUSTOM, damage,5L);
        Chat.actionMessage(player, String.format("&e๑ &c%s&6dmg &7(%s/%s) &e๑", Double.toString(damage), hitMob.getHealth(), hitMob.getMaxHealth()));
    }


}
