package com.levelmc.wizards.users;

import com.levelmc.core.api.players.PlayerUtils;
import com.levelmc.core.api.players.User;
import com.levelmc.core.api.utils.LevelExpUtil;
import com.levelmc.core.api.utils.TimeType;
import com.levelmc.core.api.utils.TimeUtils;
import com.levelmc.core.api.yml.Path;
import com.levelmc.core.api.yml.Skip;
import com.levelmc.wizards.Wizards;
import com.levelmc.wizards.abilities.ManaRegenIncreaseArmorAbility;
import com.levelmc.wizards.abilities.MaxManaIncreaseArmorAbility;
import com.levelmc.wizards.spells.Spell;
import com.levelmc.wizards.spells.SpellBindSlot;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Wizard extends User {

    @Path("experience")
    @Getter
    private int experience;

    @Path("mana")
    @Getter
    @Setter
    private int mana = 30;

    @Path("max-mana")
    @Setter
    private int maxMana = 40;

    @Skip
    private static int manaRegenCooldownTimeMs = 2850;

    @Getter
    @Path("spell.binds")
    private Map<String, String> spellBindSlotsMap = new HashMap<>();

    @Getter
    @Path("spell.levels")
    private Map<String, Integer> spellLevelsMap = new HashMap<>();

    @Path("spell.extra-max-levels")
    @Getter
    private Map<String, Integer> extraMaxLevelsMap = new HashMap<>();

    @Skip
    private Map<String, Long> spellCooldownTimestamps = new HashMap<>();

    @Skip
    @Getter
    private long lastRegenTime = 0L;

    public Wizard(Player player) {
        super(player);
    }


    public int getWizardingLevel() {
        return LevelExpUtil.getLevelAtExperience(experience);
    }

    public int getNextLevelExperience() {
        return LevelExpUtil.getExperienceAtLevel(getWizardingLevel() + 1);
    }

    public void addExperience(int amount) {
        experience += amount;
    }

    /**
     * Retrieve the level of a spell.
     *
     * @param id spell id to get level of
     * @return level of the spell. -1 will be returned if there is no valid entry.
     */
    public int getSpellLevel(String id) {
        if (!spellLevelsMap.containsKey(id)) {
            return -1;
        }

        return spellLevelsMap.get(id);
    }

    public boolean hasSpellUnlocked(String id) {
        return getSpellLevel(id) > 0;
    }

    public int getSpellLevel(Spell spell) {
        return getSpellLevel(spell.id());
    }

    public boolean isOnCooldown(Spell spell) {
        String id = spell.id();

        if (!spellCooldownTimestamps.containsKey(id)) {
            return false;
        }

        Long cooldownTimestamp = spellCooldownTimestamps.get(id);

        long time = System.currentTimeMillis();
        return time < cooldownTimestamp;
    }

    public void setOnCooldown(Player player, Spell spell) {

        //todo apply wand bonus

        int spellLevel = getSpellLevel(spell.id());

        int cooldownSeconds = spell.getCooldown(player, spellLevel);

        long timestampCooldown = Long.sum(System.currentTimeMillis(), TimeUtils.getTimeInMilles(cooldownSeconds, TimeType.SECOND));

        spellCooldownTimestamps.put(spell.id(), timestampCooldown);
    }

    public long getRemainingCooldownTime(Spell spell) {
        if (!isOnCooldown(spell)) {
            return 0;
        }

        long cooldownTimestamp = getCooldownTimestamp(spell);
        long diff = cooldownTimestamp - System.currentTimeMillis();

        return diff;
    }

    public String getCooldownBreakdown(Spell spell) {
        return TimeUtils.getDurationBreakdown(getRemainingCooldownTime(spell));
    }

    public long getCooldownTimestamp(Spell spell) {
        return spellCooldownTimestamps.get(spell.id());
    }

    public String getSpellBindIdAtSlot(SpellBindSlot e) {
        return spellBindSlotsMap.get(e.getSlotName());
    }

    public Spell getSpellAtSlot(SpellBindSlot e) {
        if (!hasSpellBoundAtSlot(e)) {
            return null;
        }

        return Wizards.getInstance().getSpellManager().getSpell(spellBindSlotsMap.get(e.getSlotName()));
    }

    public boolean hasSpellBoundAtSlot(SpellBindSlot e) {
        return getSpellBindIdAtSlot(e) != null;
    }

    public void setSpellBoundAtSlot(SpellBindSlot e, Spell spell) {
        spellBindSlotsMap.put(e.getSlotName(), spell.id());
    }

    public List<Spell> getSpells() {
        return spellLevelsMap.keySet().stream().map(key -> Wizards.getInstance().getSpellManager().getSpell(key)).collect(Collectors.toList());
    }

    public void setSpellLevel(Spell spell, int level) {
        spellLevelsMap.put(spell.id(), level);
    }

    public void setSpellLevel(String id, int level) {
        spellLevelsMap.put(id, level);
    }

    public void addExtraMaxLevel(Spell spell, int amount) {
        addExtraMaxLevel(spell.id(), amount);
    }

    public void addExtraMaxLevel(String id, int amount) {
        if (!extraMaxLevelsMap.containsKey(id)) {
            extraMaxLevelsMap.put(id, amount);
            return;
        }

        extraMaxLevelsMap.put(id, extraMaxLevelsMap.get(id) + amount);
    }

    public boolean hasExtraMaxLevel(String id) {
        return extraMaxLevelsMap.containsKey(id);
    }

    public int getExtraMaxLevel(String id) {
        if (!extraMaxLevelsMap.containsKey(id)) {
            return 0;
        }

        return extraMaxLevelsMap.get(id);
    }

    public boolean hasSpellEquipped(String id) {
        return getSpellBindSlot(id) != null;
    }

    public SpellBindSlot getSpellBindSlot(String id) {
        SpellBindSlot slot = null;

        for (Map.Entry<String, String> bind : spellBindSlotsMap.entrySet()) {
            if (bind.getValue().equals(id)) {
                slot = SpellBindSlot.getSpellBindSlot(bind.getKey());
                break;
            }
        }

        return slot;
    }

    public boolean isOnRegenCooldown() {
        return System.currentTimeMillis() < Long.sum(lastRegenTime, manaRegenCooldownTimeMs);
    }

    public void setOnRegenCooldown() {
        lastRegenTime = System.currentTimeMillis();
    }

    public int getMaxMana(Player player) {
        ItemStack[] armor = PlayerUtils.getArmor(player);

        int maxMana = this.maxMana;

        for (ItemStack item : armor) {
            if (item == null || item.getType() == Material.AIR) {
                continue;
            }

            if (!MaxManaIncreaseArmorAbility.getInstance().hasAbility(item)) {
                continue;
            }

            maxMana += MaxManaIncreaseArmorAbility.getInstance().getManaIncrease(item);
        }

        return maxMana;
    }

    public int getManaRegenAmount(Player player) {
        ItemStack[] armor = PlayerUtils.getArmor(player);

        int regenAmount = 5;

        for (ItemStack item : armor) {
            if (item == null || item.getType() == Material.AIR) {
                continue;
            }

            if (!ManaRegenIncreaseArmorAbility.getInstance().hasAbility(item)) {
                continue;
            }

            regenAmount += ManaRegenIncreaseArmorAbility.getInstance().getManaRegenAmount(item);
        }

        //todo calculate based on players equipment + bonuses & region effect iff any
        return regenAmount;
    }

}
