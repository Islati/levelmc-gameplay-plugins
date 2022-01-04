package com.levelmc.skills;

import com.google.common.collect.Lists;
import lombok.Getter;
import org.bukkit.Material;

import java.util.List;

public enum SkillType {
    WOODCUUTTING("woodcutting", Lists.newArrayList("&7Go n' smack some tinder"), Material.STONE_AXE),
    ARCHERY("archery",Lists.newArrayList("&7Progress your marksmanship"), Material.BOW),
    SWORDS("swords",Lists.newArrayList("&7Hack & Slash to Victory"), Material.STONE_SWORD),
    UNARMED("unarmed",Lists.newArrayList("&7Martial Arts"), Material.BELL),
    //    FISHING("fishing"),
    MINING("mining",Lists.newArrayList("&7Become a more proficient miner"), Material.STONE_PICKAXE);

    @Getter
    String skillName;

    @Getter
    List<String> description;

    @Getter
    Material menuIconMaterial;

    SkillType(String skillName,List<String> description, Material material) {
        this.skillName = skillName;
        this.description = description;
        this.menuIconMaterial = material;
    }

}
