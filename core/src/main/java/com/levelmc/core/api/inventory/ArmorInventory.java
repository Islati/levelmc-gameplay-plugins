package com.levelmc.core.api.inventory;

import com.levelmc.core.api.entities.EntityUtils;
import com.levelmc.core.api.players.PlayerUtils;
import com.levelmc.core.api.yml.ConfigMode;
import com.levelmc.core.api.yml.Path;
import com.levelmc.core.api.yml.SerializeOptions;
import com.levelmc.core.api.yml.YamlConfig;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@SerializeOptions(
        configMode = ConfigMode.DEFAULT
)
public class ArmorInventory extends YamlConfig {

    @Path("main-hand")
    private ItemStack mainHandItem = null;

    @Path("off-hand")
    private ItemStack offHandItem = null;

    @Path("helmet")
    private ItemStack helmet = null;

    @Path("chest")
    private ItemStack chest = null;

    @Path("leggings")
    private ItemStack leggings = null;

    @Path("boots")
    private ItemStack boots = null;

    public ArmorInventory(File file) {
        super(file);
    }

    public ArmorInventory() {

    }

    public ArmorInventory(ItemStack[] armor) {
        for (int i = 0; i < armor.length; i++) {
            ArmorSlot slot = ArmorSlot.getSlot(i);
            ItemStack item = armor[i].clone();

            setItem(slot,item);
        }
    }

    public void setItem(ArmorSlot slot, ItemStack item) {
        switch (slot) {
            case MAIN_HAND:
                mainHandItem = item;
                break;
            case OFF_HAND:
                offHandItem = item;
                break;
            case HELMET:
                helmet = item;
                break;
            case CHEST:
                chest = item;
                break;
            case LEGGINGS:
                leggings = item;
                break;
            case BOOTS:
                boots = item;
                break;
            default:
                break;
        }
    }

    public void equip(LivingEntity entity) {
        if (entity instanceof Player) {
            PlayerUtils.setArmor((Player) entity, this);
        } else {
            EntityUtils.setEquipment(entity, this);
        }
    }

    public ItemStack getMainHand() {
        return mainHandItem;
    }

    public ItemStack getOffHand() {
        return offHandItem;
    }

    public ItemStack getHelmet() {
        return helmet;
    }

    public ItemStack getBoots() {
        return boots;
    }

    public ItemStack getLegs() {
        return leggings;
    }

    public ItemStack getChest() {
        return chest;
    }

    public Map<ArmorSlot, ItemStack> getArmor() {
        Map<ArmorSlot, ItemStack> armor = new HashMap<>();

        armor.put(ArmorSlot.HELMET,helmet);
        armor.put(ArmorSlot.BOOTS,boots);
        armor.put(ArmorSlot.LEGGINGS,leggings);
        armor.put(ArmorSlot.CHEST,chest);
        armor.put(ArmorSlot.MAIN_HAND,mainHandItem);
        armor.put(ArmorSlot.OFF_HAND,offHandItem);

        return armor;
    }
}
