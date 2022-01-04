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

public class MaxManaIncreaseArmorAbility extends Ability {

    private static MaxManaIncreaseArmorAbility instance = null;

    public static MaxManaIncreaseArmorAbility getInstance() {
        if (instance == null) {
            instance = new MaxManaIncreaseArmorAbility();
        }

        return instance;
    }

    protected MaxManaIncreaseArmorAbility() {
        super("max_mana");
    }

    public int getManaIncrease(ItemStack item) {
        if (!hasAbility(item)) {
            return 0;
        }

        return item.getItemMeta().getPersistentDataContainer().get(getKey(String.format("mana-increase-%s", getId())), PersistentDataType.INTEGER);

    }
    @Override
    public void apply(ItemStack item, AbilityProperties properties) {
        if (hasAbility(item)) {
            return;
        }

        applyAbilityId(item);

        int manaIncrease = Integer.parseInt(properties.get("mana-increase", "5"));

        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();

        if (lore == null) {
            lore = new ArrayList<>();
        }

        meta.getPersistentDataContainer().set(getKey(String.format("mana-increase-%s", getId())), PersistentDataType.INTEGER, manaIncrease);

        lore.add(Chat.colorize(String.format("&b+ %s Maximum Mana", manaIncrease)));
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    @Override
    public void handlePlayerAttackTarget(Player player, LivingEntity target, double damage) {

    }
}
