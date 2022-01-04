package com.levelmc.loot.api;

import com.google.common.collect.Sets;
import com.levelmc.core.api.yml.Path;
import com.levelmc.core.api.yml.YamlConfig;
import com.levelmc.loot.api.abilities.Abilities;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Defines the loot tables use in various aspects of the server
 */
public class LootRegistry extends YamlConfig {

    @Path("drops.default-table")
    @Getter
    private String defaultTableId = "none";

    @Path("drops.tables")
    @Getter
    private List<DropTable> dropTables = new ArrayList<>();

    @Path("mob-drops")
    private List<MobDropData> mobDropData = new ArrayList<>();

    public LootRegistry() {

        DropTable armorTable = new DropTable("armor", 100, 100);
        armorTable.addChancedMaterials(new ChancedMaterial(Material.LEATHER_CHESTPLATE, 100, 100));
        armorTable.addChancedMaterials(new ChancedMaterial(Material.LEATHER_BOOTS, 100, 100));
        armorTable.addChancedMaterials(new ChancedMaterial(Material.LEATHER_HELMET, 100, 100));
        armorTable.addChancedMaterials(new ChancedMaterial(Material.LEATHER_LEGGINGS, 100, 100));

//        armorTable.add(Name.prefix("&bWizards", 100, 100, Abilities.ARMOR_MANA_REGEN, AbilityProperties.create().set("mana-regen", "3")));
//        armorTable.add(Name.suffix("of Manasprings", 100, 100, Abilities.ARMOR_MAX_MANA, AbilityProperties.create().set("mana-increase", "5")));

        dropTables.add(armorTable);

        DropTable table = new DropTable("example", 100, 100);
        table.addChancedMaterials(new ChancedMaterial(Material.WOODEN_SWORD, 100, 100));
        table.addChancedMaterials(new ChancedMaterial(Material.STONE_SWORD, 70, 100));
        table.addChancedMaterials(new ChancedMaterial(Material.IRON_SWORD, 30, 100));

        table.add(Name.prefix("&7Damaged", 100, 100))
                .add(Name.prefix("&7Cracked", 100, 100))
                .add(Name.prefix("&7Torn", 100, 100))
                .add(Name.prefix("&7Weak", 100, 100))
                .add(Name.prefix("&aPoisoned", 20, 100, Abilities.WEAKNESS, new Abilities.WeaknessProperties(1, 1, 2, 4, 10, 35, 1, 1)))
                .add(Name.prefix("&cBlood Sucking", 5, 100, Abilities.VAMPIRISM, new Abilities.VampirismProperties(10, 25, 10, 25)))
                .add(Name.prefix("&cBone Crushing", 10, 100, Abilities.BLEED, new Abilities.BleedProperties(5, 20, 15, 30, 1, 3)))
                .add(Name.base("Sword", 100, 100))
                .add(Name.base("Rapier", 100, 100, Abilities.BLEED, new Abilities.BleedProperties(5.5, 20.5, 25, 35, 1, 2)))
                .add(Name.base("Short Sword", 100, 100))
                .add(Name.base("Long Sword", 100, 100))
                .add(Name.suffix("", 100, 100))
                .add(Name.suffix("of Soldiers", 10, 100, Abilities.INCREASED_DAMAGE, new Abilities.IncreasedDamageProperties(1.0, 2, 2.1, 3.5, 10, 20)))
                .add(Name.suffix("of Warriors", 8, 100, Abilities.INCREASED_DAMAGE, new Abilities.IncreasedDamageProperties(1.2, 2.1, 2.5, 4.0, 15, 25)))
                .add(Name.suffix("of Vampires", 5, 100, Abilities.VAMPIRISM, new Abilities.VampirismProperties(20, 30, 20, 25)));


        dropTables.add(table);

        Set<EntityType> hostiles = Sets.newHashSet(EntityType.ZOMBIE, EntityType.ZOMBIFIED_PIGLIN, EntityType.GHAST, EntityType.CREEPER, EntityType.HUSK, EntityType.SKELETON, EntityType.SPIDER, EntityType.IRON_GOLEM
                , EntityType.SHULKER, EntityType.ENDERMAN, EntityType.BLAZE, EntityType.WITHER_SKELETON, EntityType.WITHER, EntityType.WITCH, EntityType.CAVE_SPIDER, EntityType.SLIME, EntityType.PILLAGER, EntityType.SILVERFISH, EntityType.MAGMA_CUBE, EntityType.ZOMBIE_VILLAGER, EntityType.DROWNED, EntityType.EVOKER, EntityType.VEX, EntityType.RAVAGER, EntityType.GUARDIAN, EntityType.SKELETON_HORSE, EntityType.PHANTOM, EntityType.STRAY, EntityType.ELDER_GUARDIAN);

        for (EntityType hostile : hostiles) {
            MobDropData data = new MobDropData(hostile);
            data.addTable(table);

            mobDropData.add(data);
        }

        defaultTableId = "example";
    }

    public void addDropTable(DropTable... tables) {
        dropTables.addAll(Arrays.asList(tables));
    }

    public boolean hasTable(String id) {
        for (DropTable table : dropTables) {
            if (table.getId().equalsIgnoreCase(id)) {
                return true;
            }
        }

        return false;
    }

    public DropTable getTable(String id) {
        for (DropTable table : dropTables) {
            if (table.getId().equalsIgnoreCase(id)) {
                return table;
            }
        }

        return null;
    }

    public boolean hasDefaultTable() {

        return !defaultTableId.equalsIgnoreCase("none") && hasTable(getDefaultTableId());
    }

    public MobDropData getDropData(EntityType type) {
        for (MobDropData data : mobDropData) {
            if (data.getEntityType() == type) {
                return data;
            }
        }

        return null;
    }

    public void addMobDropData(MobDropData data) {
        mobDropData.add(data);
    }
}
