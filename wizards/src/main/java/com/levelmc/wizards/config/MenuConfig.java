package com.levelmc.wizards.config;

import com.levelmc.core.api.yml.Path;
import com.levelmc.core.api.yml.YamlConfig;
import lombok.Getter;

public class MenuConfig extends YamlConfig {
    /**
     * Configuration for the layout of the menus.
     */

    @Path("wizardry.player-icon.slot")
    @Getter
    private int slotPlayerIcon = 0;

    @Path("wizardry.spell-binds.slot")
    @Getter
    private int slotSpellBinds = 4;

    @Path("wizardry.spell-tomes.slot")
    @Getter
    private int slotSpellTomes = 8;

    public MenuConfig() {

    }
}
