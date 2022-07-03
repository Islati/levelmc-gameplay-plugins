package com.levelmc.loot.api;

import com.levelmc.core.api.item.ItemBuilder;
import com.levelmc.core.api.item.ItemUtils;
import com.levelmc.core.api.item.ToolType;
import com.levelmc.core.api.utils.ListUtils;
import com.levelmc.core.api.yml.Path;
import com.levelmc.core.api.yml.YamlConfig;
import com.levelmc.loot.Loot;
import com.levelmc.loot.api.abilities.Ability;
import com.levelmc.loot.api.levels.ItemLevelManager;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class LootTable extends YamlConfig {

    @Path("id")
    @Getter
    private String id = "example";

    @Path("prefixes")
    @Getter
    private List<NameData> prefixes = new ArrayList<>();

    @Path("bases")
    @Getter
    private List<NameData> baseNames = new ArrayList<>();

    @Path("suffixes")
    @Getter
    private List<NameData> suffix = new ArrayList<>();

    @Path("materials")
    @Getter
    private List<ChancedMaterial> materials = new ArrayList<>();

    @Path("enchants")
    @Getter
    private List<ChancedEnchantment> enchantments = new ArrayList<>();


    public LootTable(String id) {
        this.id = id;

        //Load table into cash.
//        tableRegistry.put(id, this);
    }

    public LootTable() {

    }

    public boolean hasNameData(NameSlot slot, String name) {
        List<NameData> list = null;

        switch (slot) {
            case BASE:
                list = baseNames;
                break;
            case PREFIX:
                list = prefixes;
                break;
            case SUFFIX:
                list = suffix;
                break;
            default:
                break;
        }

        if (list == null) {
            return false;
        }

        return list.stream().anyMatch(nameData -> nameData.getName().equals(name));
    }

    public boolean hasEnchantment(Enchantment enchant) {
        return enchantments.stream().anyMatch(en -> en.getEnchantment().equals(enchant));
    }

    public boolean hasMaterial(Material material) {
        return materials.stream().anyMatch(mat -> mat.getMaterial() == material);
    }

    public LootTable add(NameData data) {
        switch (data.getSlot()) {
            case PREFIX:
                prefixes.add(data);
                break;
            case BASE:
                baseNames.add(data);
                break;
            case SUFFIX:
                suffix.add(data);
                break;
        }

        return this;
    }

    public void addChancedMaterials(ChancedMaterial material) {
        materials.add(material);
    }

    public void addChancedEnchantment(ChancedEnchantment enchantment) {
        enchantments.add(enchantment);
    }

    public ChancedMaterial getRandomMaterial() {
        if (materials.size() == 1) {
            return materials.get(0);
        }

        return ListUtils.getRandom(materials);
    }

    public NameData getRandomNameData(NameSlot name) {
        List<NameData> nameData = null;
        switch (name) {
            case PREFIX:
                nameData = getPrefixes();
                break;
            case BASE:
                nameData = baseNames;
                break;
            case SUFFIX:
                nameData = suffix;
                break;
        }

        if (nameData == null) {
            return null;
        }

        if (nameData.size() == 1) {
            return nameData.get(0);
        }

        return ListUtils.getRandom(nameData);
    }

    public ChancedEnchantment getRandomEnchantment() {
        if (enchantments.size() == 1) {
            return enchantments.get(0);
        }

        return ListUtils.getRandom(enchantments);
    }

    public ItemStack createItem() {
        ChancedMaterial materialBase = getRandomMaterial();

        while (!materialBase.chanceCheck()) {
            materialBase = getRandomMaterial();
        }

        ItemBuilder itemBuilder = new ItemBuilder(materialBase.getMaterial());


        StringBuilder nameBuilder = new StringBuilder();

        NameData prefixData = null;

        if (prefixes.size() > 0) {

            prefixData = getRandomNameData(NameSlot.PREFIX);

            while (!prefixData.chanceCheck()) {
                prefixData = getRandomNameData(NameSlot.PREFIX);
            }

            nameBuilder.append(prefixData.getName()).append(" ");
        }

        NameData baseName = null;

        if (baseNames.isEmpty()) {
            nameBuilder.append(StringUtils.capitalize(materialBase.getMaterial().name().replace("_", " ").toLowerCase()));
        } else {
            baseName = getRandomNameData(NameSlot.BASE);

            while (!baseName.chanceCheck()) {
                baseName = getRandomNameData(NameSlot.BASE);
            }
            nameBuilder.append(baseName.getName());
        }


        NameData suffixData = null;
        if (suffix.size() > 0) {
            suffixData = getRandomNameData(NameSlot.SUFFIX);

            while (!suffixData.chanceCheck()) {
                suffixData = getRandomNameData(NameSlot.SUFFIX);
            }

            nameBuilder.append(" ").append(suffixData.getName());
        }

        itemBuilder.name(nameBuilder.toString());

        if (enchantments.size() > 0) {
            ChancedEnchantment enchantment = getRandomEnchantment();

            while (!enchantment.chanceCheck()) {
                enchantment = getRandomEnchantment();
            }

            itemBuilder.enchant(enchantment.getEnchantment(), enchantment.getLevelInRange());
        }

        ItemStack baseItem = itemBuilder.item();

        if (prefixData != null && prefixData.hasAbility() && prefixData.hasAbilityProperties()) {
            Ability ability = prefixData.getAbility();

            if (ability != null && !ability.hasAbility(baseItem)) {
                ability.apply(baseItem, prefixData.getAbilityProperties());
            }
        }

        if (baseName != null && baseName.hasAbility() && baseName.hasAbilityProperties()) {
            Ability ability = baseName.getAbility();

            if (ability != null && !ability.hasAbility(baseItem)) {
                ability.apply(baseItem, baseName.getAbilityProperties());
            }
        }

        if (suffixData != null && suffixData.hasAbility() && suffixData.hasAbilityProperties()) {
            Ability ability = suffixData.getAbility();

            if (ability != null && !ability.hasAbility(baseItem)) {
                ability.apply(baseItem, suffixData.getAbilityProperties());
            }
        }

        /* Apply item Leveling */
        if (ItemUtils.isWeapon(baseItem) || ItemUtils.isTool(baseItem, ToolType.PICK_AXE) || ItemUtils.isTool(baseItem, ToolType.HOE) || ItemUtils.isTool(baseItem, ToolType.SHOVEL)) {
            ItemLevelManager manager = Loot.getInstance().getLevelManager();
            manager.setLevel(baseItem, 1);
        }

        return baseItem;
    }


}
