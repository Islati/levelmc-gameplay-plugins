package com.levelmc.skills.perks.woodcutting;

import com.google.common.collect.Lists;
import com.levelmc.core.api.ApiMessages;
import com.levelmc.skills.SkillType;
import com.levelmc.skills.perks.BasePerk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;
import java.util.List;

public class TreeFell extends BasePerk {

    private static TreeFell instance = null;

    public static TreeFell getInstance() {
        if (instance == null) {
            instance = new TreeFell();
        }

        return instance;
    }

    protected TreeFell() {
        super("tree-fell", "Tree-Capitation", SkillType.WOODCUUTTING, 5);
        setDefaultDescription(Lists.newArrayList(
                ApiMessages.get("skill-point-menu-level-desc"),
                "&a✚ {chance}% chance to knock the whole tree down",
                ApiMessages.get("perk-is-toggleable")
        ));
    }

    @Override
    public double getActivationChance(int level) {
        if (level > 0 && level < 3) {
            return level * 2;
        }

        return level * 2.2;
    }


    protected List<Block> getSurrounding(Location origin, String type) {
        List<Block> blocks = new ArrayList<>();
        for (int x = -1; x < 2; x++) {
            for (int y = 0; y < 2; y++) {
                for (int z = -1; z < 2; z++) {
                    Location loc = origin.clone().add(x, y, z);
                    Block block = loc.getBlock();
                    if (!origin.equals(loc) && block.getType().name().endsWith(type)) {
                        blocks.add(block);
                    }
                }

            }

        }
        return blocks;
    }

    private static final int MAX_DISTANCE = 250;

    public List<Block> getLogs(Location origin, String type) {
        List<Block> logs = new ArrayList<>();
        List<Block> next = getSurrounding(origin, type);
        int distance = 0;
        while (!next.isEmpty()) {
            List<Block> nextNext = new ArrayList<>();
            for (Block log : next) {
                if (!logs.contains(log)) {
                    logs.add(log);
                    nextNext.addAll(getSurrounding(log.getLocation(), type));
                }
            }
            next = nextNext;
        }
        return logs;
    }

    /*
    Handle the block break logic for players who have tree fell active.
     */
    public void onBlockBreakEvent(BlockBreakEvent event) {
        Block block = event.getBlock();

        String blockTypeName = block.getType().name();

        if (!blockTypeName.endsWith("_LOG")) {
            return;
        }

        if (blockTypeName.startsWith("STRIPPED_")) {
            blockTypeName = blockTypeName.substring(9);
        }

        List<Block> surroundingLogs = getLogs(block.getLocation(), blockTypeName);
        surroundingLogs.remove(block);

        for (Block b : surroundingLogs) {
            b.breakNaturally();
        }
    }

}
