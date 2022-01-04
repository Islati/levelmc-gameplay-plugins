package com.levelmc.wizards.abilities;

import com.levelmc.core.chat.Chat;
import com.levelmc.loot.api.abilities.Ability;
import com.levelmc.loot.api.abilities.AbilityProperties;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class ManaRegenIncreaseArmorAbility extends Ability {

    private static ManaRegenIncreaseArmorAbility instance = null;

    public static ManaRegenIncreaseArmorAbility getInstance() {
        if (instance == null) {
            instance = new ManaRegenIncreaseArmorAbility();
        }

        return instance;
    }

    protected ManaRegenIncreaseArmorAbility() {
        super("mana_regen");
    }

    @Override
    public void apply(ItemStack item, AbilityProperties properties) {
        if (hasAbility(item)) {
            return;
        }

        applyAbilityId(item);

        ItemMeta meta = item.getItemMeta();
        int regentAmount = Integer.parseInt(properties.get("mana-regen", "2"));
        meta.getPersistentDataContainer().set(getKey(String.format("manaregenbonus-%s", getId())), PersistentDataType.INTEGER, Integer.parseInt(properties.get("mana-regen", "2")));

        List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();

        if (lore == null) {
            lore = new ArrayList<>();
        }

        lore.add(Chat.colorize(String.format("&b+ %s Mana Regen Bonus", regentAmount)));
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    @Override
    public void handlePlayerAttackTarget(Player player, LivingEntity target, double damage) {

    }

    public int getManaRegenAmount(ItemStack item) {
        if (!hasAbility(item)) {
            return 0;
        }

        return item.getItemMeta().getPersistentDataContainer().get(getKey(String.format("manaregenbonus-%s", getId())), PersistentDataType.INTEGER);
    }
}
