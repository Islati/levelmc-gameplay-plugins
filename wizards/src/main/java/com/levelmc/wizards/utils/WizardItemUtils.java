package com.levelmc.wizards.utils;

import com.levelmc.core.api.item.ItemBuilder;
import com.levelmc.core.api.item.ItemUtils;
import com.levelmc.wizards.Wizards;
import com.levelmc.wizards.abilities.MagicWandAbility;
import com.levelmc.wizards.spells.Spell;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

/**
 * Used to handle the wand-specific interop with ItemStacks
 */
public class WizardItemUtils {

    private NamespacedKey keyWizardsWand;

    private NamespacedKey keyTomeSpell;
    private NamespacedKey keyTomeLevel;

    private Wizards core;

    public WizardItemUtils(Wizards core) {
        this.core = core;

        keyWizardsWand = new NamespacedKey(core, "wizardsWand");
        keyTomeSpell = new NamespacedKey(core, "tomeSpell");
        keyTomeLevel = new NamespacedKey(core, "tomeLevel");
    }


    /**
     * Check whether or not the item is a wand.
     *
     * @param item item to check
     * @return true or false for wand status.
     */
    public boolean isWand(ItemStack item) {
        return MagicWandAbility.getInstance().hasAbility(item);
    }

    public boolean isTome(ItemStack item) {
        return ItemUtils.hasNamespacedKey(item, keyTomeSpell, PersistentDataType.STRING);
    }

    public String getTomeSpellId(ItemStack item) {
        if (!isTome(item)) {
            return null;
        }

        return item.getItemMeta().getPersistentDataContainer().get(keyTomeSpell, PersistentDataType.STRING);
    }

    public int getTomeLevel(ItemStack item) {
        if (!isTome(item)) {
            return -1;
        }
        ItemMeta meta = item.getItemMeta();

        return meta.getPersistentDataContainer().get(keyTomeLevel, PersistentDataType.INTEGER);
    }

    public ItemStack createTome(Player player, Spell spell, int level) {
        /* Todo implement tome stats */
        ItemStack tome = ItemBuilder.of(Material.PAPER).name(spell.getSpellName(player, level)).lore("&d► &7Use to unlock the spell.").item();

        ItemMeta meta = tome.getItemMeta();

        meta.getPersistentDataContainer().set(keyTomeSpell, PersistentDataType.STRING, spell.id());
        meta.getPersistentDataContainer().set(keyTomeLevel, PersistentDataType.INTEGER, level);

        tome.setItemMeta(meta);
        return tome;
    }
}
