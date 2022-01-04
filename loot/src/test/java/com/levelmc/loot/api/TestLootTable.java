package com.levelmc.loot.api;

import com.google.common.collect.Sets;
import com.levelmc.core.api.item.ItemUtils;
import com.levelmc.loot.LootTest;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.Map;
import java.util.Set;

public class TestLootTable extends LootTest {


    @Test
    public void testLootTable() {
        Set<String> baseNames = Sets.newHashSet("Tester","Debugger","Equip");
        Set<String> prefixNames = Sets.newHashSet("Ultimate","Noobs","Devs");
        Set<String> suffixNames = Sets.newHashSet("of Testing","of Development");
        Set<Material> lootMaterials = Sets.newHashSet(Material.WOODEN_SWORD, Material.WOODEN_SHOVEL, Material.WOODEN_AXE, Material.WOODEN_HOE, Material.WOODEN_PICKAXE, Material.IRON_AXE);
        Set<Enchantment> enchantments = Sets.newHashSet(Enchantment.KNOCKBACK,Enchantment.DAMAGE_ALL);

        DropTable table = new DropTable("test-table",100,100);

        Assertions.assertEquals(100,table.getChance());
        Assertions.assertEquals(100,table.getMeasure());

        for(Material mat : lootMaterials) {
            table.addChancedMaterials(new ChancedMaterial(mat,25,100));
        }

        for(Enchantment enchant : enchantments) {
            table.addChancedEnchantment(new ChancedEnchantment(enchant,1,1,10,100));
        }

        baseNames.forEach(name -> {
            table.add(Name.base(name,10,100));
        });

        prefixNames.forEach(name -> {
            table.add(Name.prefix(name,10,100));
        });

        suffixNames.forEach(name -> {
            table.add(Name.suffix(name,10,100));
        });

        for(int i = 0; i < 10; i++) {
            ItemStack generated = table.createItem();
            Assertions.assertNotNull(generated);

            if (ItemUtils.hasEnchantments(generated)) {
                Map<Enchantment, Integer> enchants = generated.getEnchantments();

                for(Map.Entry<Enchantment, Integer> entry : enchants.entrySet()) {
                    Assertions.assertTrue(table.hasEnchantment(entry.getKey()),"Item has an enchantment not on the LootTable");
                }
            }

            Assertions.assertTrue(lootMaterials.contains(generated.getType()));
        }
    }
}
