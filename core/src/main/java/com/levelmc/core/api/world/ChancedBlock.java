package com.levelmc.core.api.world;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import com.levelmc.core.api.utils.NumberUtil;
import org.bukkit.Material;

@RequiredArgsConstructor(staticName = "of", access = AccessLevel.PUBLIC)
public class ChancedBlock {
    @Getter
    private final Material material;
    @Getter
    private final int chance;


    public boolean pass() {
        return NumberUtil.percentCheck(chance);
    }
}
