package com.levelmc.wizards.abilities;

import com.levelmc.core.chat.Chat;
import com.levelmc.loot.api.abilities.Ability;
import com.levelmc.loot.api.abilities.AbilityProperties;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class MagicWandAbility extends Ability {

    private static MagicWandAbility instance = null;

    public static MagicWandAbility getInstance() {
        if (instance == null) {
            instance = new MagicWandAbility();
        }

        return instance;
    }

    protected MagicWandAbility() {
        super("magic_wand");
    }
    @Override
    public void apply(ItemStack item, AbilityProperties properties) {
        if (hasAbility(item)) {
            return;
        }

        applyAbilityId(item);

        ItemMeta meta = item.getItemMeta();

        List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();

        lore.add(Chat.colorize("&d★ &7Magical &d★"));
        lore.add(Chat.colorize("&7(Can cast spells)"));

        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    @Override
    public void handlePlayerAttackTarget(Player player, LivingEntity target, double damage) {

    }
}
