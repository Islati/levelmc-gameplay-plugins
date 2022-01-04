package com.levelmc.wizards.abilities;

import com.levelmc.core.api.utils.NumberUtil;
import com.levelmc.core.chat.Chat;
import com.levelmc.loot.api.abilities.Ability;
import com.levelmc.loot.api.abilities.AbilityProperties;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class MagicAreaOfEffectAbility extends Ability {

    public static class MagicAoeProperties extends AbilityProperties {
        public MagicAoeProperties(int rangeMin, int rangeMax) {
            set("range-min", String.valueOf(rangeMin));
            set("range-max", String.valueOf(rangeMax));
        }
    }

    @Override
    public void apply(ItemStack item, AbilityProperties properties) {
        if (!hasAbility(item)) {
            return;
        }

        applyAbilityId(item);

        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();

        int rangeIncreaseMin = Integer.parseInt(properties.get("range-min", "1"));
        int rangeIncreaseMax = Integer.parseInt(properties.get("range-max", "2"));

        int rangeIncrease = NumberUtil.getRandomInRange(rangeIncreaseMin, rangeIncreaseMax);

        lore.add(Chat.colorize(String.format("&a+ &6%s spell range", rangeIncrease)));
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    @Override
    public void handlePlayerAttackTarget(Player player, LivingEntity target, double damage) {

    }
}
