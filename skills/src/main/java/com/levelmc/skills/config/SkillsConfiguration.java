package com.levelmc.skills.config;

import com.levelmc.core.api.yml.Path;
import com.levelmc.core.api.yml.YamlConfig;
import lombok.Getter;

public class SkillsConfiguration extends YamlConfig {

    @Path("skills.mining")
    @Getter
    private MiningConfiguration miningConfig = new MiningConfiguration();

    @Path("skills.woodcutting")
    @Getter
    private WoodcuttingConfiguration woodcuttingConfiguration = new WoodcuttingConfiguration();

    public SkillsConfiguration() {

    }

}
