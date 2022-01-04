package com.levelmc.skills.perks.woodcutting;

import com.levelmc.core.api.inventory.HandSlot;
import com.levelmc.core.api.item.ItemUtils;
import com.levelmc.core.api.players.PlayerUtils;
import com.levelmc.core.api.utils.NumberUtil;
import com.levelmc.core.api.world.WorldUtils;
import com.levelmc.skills.SkillType;
import com.levelmc.skills.perks.BasePerk;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class WoodChipper extends BasePerk {
    private static WoodChipper instance = null;

    public static WoodChipper getInstance() {
        if (instance == null) {
            instance = new WoodChipper();
        }

        return instance;
    }

    protected WoodChipper() {
        super("wood-chipper", "Wood Chipper", SkillType.WOODCUUTTING, 10, 10);
    }

    public void handle(BlockBreakEvent event) {
        //Todo drop the wood equivelent to the log they fell.

        Material woodType = Material.valueOf(event.getBlock().getType().name().replace("_LOG", "_PLANKS"));

        ItemStack woodStack = new ItemStack(woodType, 4);

        Player player = event.getPlayer();
        if (!NumberUtil.percentCheck(getLevel(player))) {
            ItemUtils.damageItem(PlayerUtils.getItemInHand(player, HandSlot.MAIN_HAND), 1);
        }

        event.setDropItems(false);
        WorldUtils.dropItem(event.getBlock().getLocation(), woodStack);
    }
}
