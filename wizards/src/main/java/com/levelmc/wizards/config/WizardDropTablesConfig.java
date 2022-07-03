package com.levelmc.wizards.config;

import com.levelmc.core.api.yml.Path;
import com.levelmc.core.api.yml.YamlConfig;
import com.levelmc.loot.api.ChancedMaterial;
import com.levelmc.loot.api.DropTable;
import com.levelmc.loot.api.Name;
import com.levelmc.loot.api.abilities.AbilityProperties;
import com.levelmc.wizards.abilities.MagicWandAbility;
import lombok.Getter;
import org.bukkit.Material;

public class WizardDropTablesConfig extends YamlConfig {

    @Path("drop-table.wands")
    @Getter
    private DropTable wandsDropTable = new DropTable("wands", 100, 100);

    public WizardDropTablesConfig() {
        wandsDropTable.addChancedMaterials(new ChancedMaterial(Material.STICK, 100, 100));
        wandsDropTable.addChancedMaterials(new ChancedMaterial(Material.BLAZE_ROD, 20, 100));

        wandsDropTable.add(Name.prefix("&dEnchantress'", 20, 100))
                .add(Name.prefix("&eJesters", 20, 100))
                .add(Name.prefix("&bFairy's", 20, 100))
                .add(Name.prefix("&aElven", 20, 100))
                .add(Name.prefix("&cChaotic", 5, 100))
                .add(Name.prefix("", 100, 100))
                .add(Name.base("Wand", 100, 100, MagicWandAbility.getInstance(), new AbilityProperties()));

    }
}
