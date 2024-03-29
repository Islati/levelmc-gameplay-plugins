package com.levelmc.core.api.world;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.levelmc.core.api.utils.EffectUtils;
import com.levelmc.core.api.utils.NumberUtil;
import com.levelmc.core.api.utils.ListUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.material.MaterialData;

import java.util.*;
import java.util.stream.Collectors;

public class BlockUtils {

    private static final Random random = new Random();

    /**
     * Set of the item-ids which materials are hollow
     */
    private static final Set<Material> HOLLOW_MATERIALS = new HashSet<>();
    public static final HashSet<Material> TRANSPARENT_MATERIALS = new HashSet<>();

    /* Initialize the materials which are hollow */
    static {
        HOLLOW_MATERIALS.add(Material.AIR);

        //SAPPLINGS
        HOLLOW_MATERIALS.add(Material.OAK_SAPLING);
        HOLLOW_MATERIALS.add(Material.ACACIA_SAPLING);
        HOLLOW_MATERIALS.add(Material.BAMBOO_SAPLING);
        HOLLOW_MATERIALS.add(Material.BIRCH_SAPLING);
        HOLLOW_MATERIALS.add(Material.DARK_OAK_SAPLING);
        HOLLOW_MATERIALS.add(Material.JUNGLE_SAPLING);
        HOLLOW_MATERIALS.add(Material.SPRUCE_SAPLING);

        //GRASS
        HOLLOW_MATERIALS.add(Material.TALL_GRASS);
        HOLLOW_MATERIALS.add(Material.TALL_SEAGRASS);
        HOLLOW_MATERIALS.add(Material.SEAGRASS);
        HOLLOW_MATERIALS.add(Material.GRASS);

        //FLOWERS
        HOLLOW_MATERIALS.add(Material.CHORUS_FLOWER);
        HOLLOW_MATERIALS.add(Material.DANDELION);
        HOLLOW_MATERIALS.add(Material.AZURE_BLUET);
        HOLLOW_MATERIALS.add(Material.LEGACY_YELLOW_FLOWER);
        HOLLOW_MATERIALS.add(Material.LEGACY_RED_ROSE);
        HOLLOW_MATERIALS.add(Material.ROSE_BUSH);
        //SEEDS
        HOLLOW_MATERIALS.add(Material.BEETROOT_SEEDS);
        HOLLOW_MATERIALS.add(Material.MELON_SEEDS);
        HOLLOW_MATERIALS.add(Material.PUMPKIN_SEEDS);
        HOLLOW_MATERIALS.add(Material.WHEAT_SEEDS);

        //SIGNS
        HOLLOW_MATERIALS.add(Material.ACACIA_SIGN);
        HOLLOW_MATERIALS.add(Material.BIRCH_SIGN);
        HOLLOW_MATERIALS.add(Material.DARK_OAK_SIGN);
        HOLLOW_MATERIALS.add(Material.JUNGLE_SIGN);
        HOLLOW_MATERIALS.add(Material.OAK_SIGN);
        HOLLOW_MATERIALS.add(Material.SPRUCE_SIGN);

        //WALL SIGNS
        HOLLOW_MATERIALS.add(Material.ACACIA_WALL_SIGN);
        HOLLOW_MATERIALS.add(Material.BIRCH_WALL_SIGN);
        HOLLOW_MATERIALS.add(Material.OAK_WALL_SIGN);
        HOLLOW_MATERIALS.add(Material.DARK_OAK_WALL_SIGN);
        HOLLOW_MATERIALS.add(Material.JUNGLE_WALL_SIGN);
        HOLLOW_MATERIALS.add(Material.SPRUCE_WALL_SIGN);

//        //DOORS
//        HOLLOW_MATERIALS.add(Material.DARK_OAK_DOOR);
//        HOLLOW_MATERIALS.add(Material.OAK_DOOR);
//        HOLLOW_MATERIALS.add(Material.ACACIA_DOOR);
//        HOLLOW_MATERIALS.add(Material.BIRCH_DOOR);
//


        HOLLOW_MATERIALS.add(Material.POWERED_RAIL);
        HOLLOW_MATERIALS.add(Material.DETECTOR_RAIL);
        HOLLOW_MATERIALS.add(Material.DEAD_BUSH);
        HOLLOW_MATERIALS.add(Material.BROWN_MUSHROOM);
        HOLLOW_MATERIALS.add(Material.RED_MUSHROOM);
        HOLLOW_MATERIALS.add(Material.TORCH);
        HOLLOW_MATERIALS.add(Material.REDSTONE_WIRE);
        HOLLOW_MATERIALS.add(Material.LADDER);
        HOLLOW_MATERIALS.add(Material.RAIL);
        HOLLOW_MATERIALS.add(Material.LEVER);

        //PRESSURE PLATES
        HOLLOW_MATERIALS.add(Material.STONE_PRESSURE_PLATE);
        HOLLOW_MATERIALS.add(Material.ACACIA_PRESSURE_PLATE);
        HOLLOW_MATERIALS.add(Material.BIRCH_PRESSURE_PLATE);
        HOLLOW_MATERIALS.add(Material.JUNGLE_PRESSURE_PLATE);
        HOLLOW_MATERIALS.add(Material.OAK_PRESSURE_PLATE);
        HOLLOW_MATERIALS.add(Material.DARK_OAK_PRESSURE_PLATE);
        HOLLOW_MATERIALS.add(Material.HEAVY_WEIGHTED_PRESSURE_PLATE);
        HOLLOW_MATERIALS.add(Material.LIGHT_WEIGHTED_PRESSURE_PLATE);

        HOLLOW_MATERIALS.add(Material.REDSTONE_TORCH);
        HOLLOW_MATERIALS.add(Material.REDSTONE_WALL_TORCH);

        HOLLOW_MATERIALS.add(Material.SUGAR_CANE);

        HOLLOW_MATERIALS.add(Material.LEGACY_DIODE_BLOCK_ON);
        HOLLOW_MATERIALS.add(Material.LEGACY_DIODE_BLOCK_OFF);


        HOLLOW_MATERIALS.add(Material.STONE_BUTTON);
        HOLLOW_MATERIALS.add(Material.SNOW);
        HOLLOW_MATERIALS.add(Material.PUMPKIN_STEM);
        HOLLOW_MATERIALS.add(Material.MELON_STEM);
        HOLLOW_MATERIALS.add(Material.VINE);
//        HOLLOW_MATERIALS.add(Material.ACACIA_FENCE_GATE);
//        HOLLOW_MATERIALS.add(Material.BIRCH_FENCE_GATE);
//        HOLLOW_MATERIALS.add(Material.BIRCH_FENCE_GATE);
        HOLLOW_MATERIALS.add(Material.LILY_PAD);
        HOLLOW_MATERIALS.add(Material.LILY_OF_THE_VALLEY);
        HOLLOW_MATERIALS.add(Material.NETHER_WART_BLOCK);

        try {
            HOLLOW_MATERIALS.add(Material.BLACK_CARPET);
            HOLLOW_MATERIALS.add(Material.BLUE_CARPET);
            HOLLOW_MATERIALS.add(Material.RED_CARPET);
            HOLLOW_MATERIALS.add(Material.CYAN_CARPET);
            HOLLOW_MATERIALS.add(Material.BROWN_CARPET);
            HOLLOW_MATERIALS.add(Material.GRAY_CARPET);
            HOLLOW_MATERIALS.add(Material.GREEN_CARPET);
            HOLLOW_MATERIALS.add(Material.LIGHT_BLUE_CARPET);
            HOLLOW_MATERIALS.add(Material.LIGHT_GRAY_CARPET);
            HOLLOW_MATERIALS.add(Material.LIME_CARPET);
            HOLLOW_MATERIALS.add(Material.MAGENTA_CARPET);
            HOLLOW_MATERIALS.add(Material.ORANGE_CARPET);
            HOLLOW_MATERIALS.add(Material.YELLOW_CARPET);
            HOLLOW_MATERIALS.add(Material.PINK_CARPET);
            HOLLOW_MATERIALS.add(Material.PURPLE_CARPET);
            HOLLOW_MATERIALS.add(Material.WHITE_CARPET);
        } catch (NoSuchFieldError e) {
//            Chat.debug(Messages.OUTDATED_VERSION);
        }

        //All hollow materials are transparant materials
        TRANSPARENT_MATERIALS.addAll(HOLLOW_MATERIALS);

        //Water is transparent, though not hollow
        TRANSPARENT_MATERIALS.add(Material.WATER);
        TRANSPARENT_MATERIALS.add(Material.LEGACY_STATIONARY_WATER);
    }

    /**
     * Convert a list of locations into a list of blocks!
     *
     * @param locs locations to get the blocks for.
     * @return list of all the locations that the blocks are at.
     */
    public static List<Block> getBlocks(List<Location> locs) {
        return locs.stream().map(Location::getBlock).collect(Collectors.toList());
    }

    public static List<Block> getCubeFilled(Location center, int radius) {
        return getBlocks(LocationUtils.getCubeFilled(center,radius));
    }

    public static List<Block> getCubeOutline(Location corner1, Location corner2) {
        return getBlocks(LocationUtils.getCubeOutline(corner1,corner2,1.25));
    }

    /**
     * Get the distance between the nearest block of the given type, and the location.
     *
     * @param loc            location to begin the search from
     * @param searchMaterial material to search for
     * @param depth          depth (and radius) of how far to search
     * @return the distance between the location and the searched material, or -1 if nothing was found.
     */
    public static int getBlockTypeDistance(Location loc, Material searchMaterial, int depth) {
        World world = loc.getWorld();
        double baseX = loc.getX();
        double baseY = loc.getY();
        double baseZ = loc.getZ();

        //From 0 to the deepest of depths allowed, loop through all the blocks!
        for (int depthLevel = 0; depthLevel < depth; ++depthLevel) {

            int deepZ;
            int deepY;

            //Bottom up on Z axis & Y Axis- check if the blocks match the type we're searching for!
            for (deepZ = -depthLevel; deepZ <= depthLevel; ++deepZ) {
                for (deepY = -depthLevel; deepY <= depthLevel; ++deepY) {

                    Block blockAtPlus = getBlockAt(new Location(world, baseX + deepZ, baseY + depthLevel, baseZ + deepY));
                    Block blockAtMinus = getBlockAt(new Location(world, baseX + deepZ, baseY - depthLevel, baseZ + deepY));

                    if (blockAtPlus.getType() == searchMaterial || blockAtMinus.getType() == searchMaterial) {
                        return depthLevel;
                    }
                }
            }

            for (deepZ = -depthLevel; deepZ <= depthLevel; ++deepZ) {
                for (deepY = (-depthLevel + 1); deepY <= (depthLevel - 1); ++deepY) {
                    Block blockAtPlus = getBlockAt(new Location(world, baseX + deepZ, baseY + deepY, baseZ + depthLevel));
                    Block blockAtMinus = getBlockAt(new Location(world, baseX + deepZ, baseY + deepY, baseZ - depthLevel));

                    if (blockAtPlus.getType() == searchMaterial || blockAtMinus.getType() == searchMaterial) {
                        return depthLevel;
                    }
                }
            }

            for (deepZ = (-depthLevel + 1); deepZ < (depthLevel - 1); ++deepZ) {
                for (deepY = (-depthLevel + 1); deepY <= (depthLevel - 1); ++deepY) {
                    Block blockAtPlus = getBlockAt(new Location(world, baseX + depthLevel, baseY + deepY, baseZ + deepZ));
                    Block blockAtMinus = getBlockAt(new Location(world, baseX - depthLevel, baseY + deepY, baseZ + deepZ));

                    if (blockAtPlus.getType() == searchMaterial || blockAtMinus.getType() == searchMaterial) {
                        return depthLevel;
                    }
                }
            }
        }

        return -1;
    }


    public static Material getBlockMaterial(Block block) {
        Material itemMaterial = block.getType();
        switch (itemMaterial) {
            case REDSTONE_WIRE:
                itemMaterial = Material.REDSTONE;
                break;
            case FIRE:
                itemMaterial = Material.AIR;
                break;
            case PUMPKIN_STEM:
                itemMaterial = Material.PUMPKIN_SEEDS;
                break;
            case MELON_STEM:
                itemMaterial = Material.MELON_SEEDS;
                break;
        }
        return itemMaterial;
    }

    /**
     * Gets the material id for the block passed
     *
     * @param block block to get the id for
     * @return id of the block
     * @see #getBlockId(Block, boolean)
     */
    public static int getBlockId(Block block) {
        return getBlockId(block, true);
    }

    /**
     * Gets either the block id, or material id based on the parameters.s
     *
     * @param block   block to get the id for
     * @param itemsId whether or not to retrieve the item-stack id, or the actual block material id
     * @return integer for the item id requested (either block or material)
     */
    public static int getBlockId(Block block, boolean itemsId) {
        return itemsId ? getBlockMaterial(block).getId() : block.getType().getId();
    }

    /**
     * Check whether or not a block at a specific location is a solid or not.
     *
     * @param loc location to check if the block is solid.
     * @return true if the block is a solid block, false if it's hollow, air, or liquid (Not solid).
     */
    public static boolean isSolid(Location loc) {
        return getBlockAt(loc).getType().isSolid();
    }

    /**
     * Check whether or not a block is hollow
     *
     * @param block block to check
     * @return true if the block is hollow (meaning it can't be stood on / walked over without falling through), false otherwise
     */
    public static boolean isHollowBlock(Block block) {
        return HOLLOW_MATERIALS.contains(getBlockId(block, false));
    }

    /**
     * Check whether or not a block is transparent
     *
     * @param block block to check
     * @return true if the block is hollow (meaning it can't be stood on / walked over without falling through), false otherwise
     */
    public static boolean isTransparentBlock(Block block) {
        return TRANSPARENT_MATERIALS.contains((byte) getBlockId(block, false));
    }

    /**
     * Returns the block at a specific location
     *
     * @param blockLocation location of the block
     * @return Block that was at the given location, or null if none was there
     */
    public static Block getBlockAt(Location blockLocation) {
        return blockLocation.getWorld().getBlockAt(blockLocation);
    }

    /**
     * Breaks the block at specific location without showing the block-break effect
     *
     * @param blockLocation location to break the block
     * @param natural       whether or not to break naturally
     * @return true if the block was broken, false if it doesn't exist or wasn't broken
     * @see #breakBlock(Block, boolean, boolean)
     */
    public static boolean breakBlockAt(Location blockLocation, boolean natural) {
        return breakBlockAt(blockLocation, natural, false);
    }

    /**
     * Breaks the block at a specific location
     *
     * @param blockLocation location to break the block
     * @param natural       whether or not to break naturally
     * @param playeffect    whether or not to play the block-break effect
     * @return true if the block was broken, false if it wasn't broken, or no block is at the location
     * @see #breakBlock(Block, boolean)
     */
    public static boolean breakBlockAt(Location blockLocation, boolean natural, boolean playeffect) {
        Block block = getBlockAt(blockLocation);
        if (block != null) {
            return breakBlock(block, natural, playeffect);
        }
        return false;
    }

    /**
     * Breaks the block either naturally, or un-naturally
     *
     * @param block   block to "break" / remove
     * @param natural whether or not to break naturally
     * @return true if the block has been broken, false otherwise (Normally followed by a bukkit exception)
     * @see #breakBlock(Block, boolean, boolean)
     */
    public static boolean breakBlock(Block block, boolean natural) {
        //If it's supposed to be natural, return the bukkit call for breakNaturally, otherwise return our methods return
        return natural ? block.breakNaturally() : breakBlock(block, false, false);
    }

    /**
     * Breaks the block
     *
     * @param block      block to break
     * @param natural    whether or not to break naturally
     * @param playEffect whether or not to play the block-break effect
     * @return true if the block was broken, false otherwise
     */
    public static boolean breakBlock(Block block, boolean natural, boolean playEffect) {
        if (natural) {
            //Return bukkits breakNaturally method on a block
            return block.breakNaturally();
        } else {
            //Change the material of the block to air
            setBlock(block, Material.AIR);
            //if the effect is to be played, play it!
            if (playEffect) {
                EffectUtils.playBlockBreakEffect(block.getLocation(), 4, block.getType());
            }
            return true;
        }
    }

    /**
     * Changes the block to the data contained by {@param blockData}
     *
     * @param block     block to change
     * @param blockData material data used to update the block
     */
    public static void setBlock(Block block, MaterialData blockData) {
        //Update the blocks material data
        block.getState().setData(blockData);
        //Update the type
        block.setType(blockData.getItemType());
        //Update the byte-data (Positioning, etc)
//        block.setData(block.getData());
        //todo implement directional changes
        //Update the block state
        block.getState().update(true);
    }

    /**
     * Change the block at the given location to the data contained by {@param data}
     *
     * @param loc  location of the block to modify.
     * @param data materialdata to assign to the block.
     */
    public static void setBlock(Location loc, MaterialData data) {
        setBlock(loc.getBlock(), data);
    }

    /**
     * Change the material of a block
     *
     * @param block          block to change
     * @param changeMaterial material to set the block to
     */
    public static void setBlock(Block block, Material changeMaterial) {
        block.setType(changeMaterial);
        block.getState().setType(changeMaterial);
        block.getState().update(true);
    }

    /**
     * Change the material of a block at a specific location.
     *
     * @param location location to change the block at.
     * @param material material to assign to the block
     */
    public static void setBlock(Location location, Material material) {
        setBlock(getBlockAt(location), material);
    }

    /**
     * Check whether or not a block is an ore.
     *
     * @param block block to check
     * @return true if the material is the ore-block of coal, iron, diamond, emerald, redstone, gold, or lapis
     */
    public static boolean isOre(Block block) {
        return isOre(block.getType());
    }

    /**
     * Check whether or not a material is an ore.
     *
     * @param material material to check
     * @return true if the material is the ore-block of coal, iron, diamond, emerald, redstone, gold, or lapis
     */
    public static boolean isOre(Material material) {
        switch (material) {
            case COAL_ORE:
            case IRON_ORE:
            case DIAMOND_ORE:
            case EMERALD_ORE:
            case REDSTONE_ORE:
            case GOLD_ORE:
            case LAPIS_ORE:
                return true;
            default:
                return false;
        }
    }

    /**
     * Check if a block's type matches any of those passed by the types parameter.
     *
     * @param block block to check.
     * @param types type(s) to match against.
     * @return
     */
    public static boolean isOfAnyType(Block block, Material... types) {
        Set<Material> mats = Sets.newHashSet(types);
        return mats.contains(block.getType());
    }

    /**
     * Spawn primed tnt at a specific location
     *
     * @param location location to spawn tnt
     */
    public static TNTPrimed spawnTNT(Location location) {
        return (TNTPrimed) location.getWorld().spawnEntity(location, EntityType.PRIMED_TNT);
    }

    /**
     * Spawn a specific amount of primed tnt at a specific location
     *
     * @param location location to spawn tnt
     * @param amount   amount of tnt to spawn
     */
    public static void spawnTNT(Location location, int amount) {
        for (int i = 0; i < amount; i++) {
            spawnTNT(location);
        }
    }

    /**
     * Get the block relative to the desired block face.
     *
     * @param parent base-block used to retrieve the relative block.
     * @param face   face to retrieve the block at.
     * @return block attached to the desired face of the parentBuilder.
     */
    public static Block getBlockFacing(Block parent, BlockFace face) {
        return parent.getRelative(face);
    }

    /**
     * Retrieve all the blocks that surround the parentBuilder block in all possible directions.
     *
     * @param parent parentBuilder block to retrieve the surrounding blocks from.
     * @return a {@link HashSet} of all the {@link Block} surrounding the parentBuilder block.
     */
    public static Set<Block> getBlocksSurrounding(Block parent) {
        return EnumSet.allOf(BlockFace.class).stream().map(face -> getBlockFacing(parent, face)).collect(Collectors.toSet());
    }

    /**
     * Retrieve the nearest empty space in a given radius around the base block.
     *
     * @param b         block to use as a base for the search.
     * @param maxradius maximum radius around the block to search for an empty space in.
     * @return the nearest empty space if available, otherwise null if none is present.
     */
    public static Block getNearestEmptySpace(Block b, int maxradius) {
        BlockFace[] faces = {BlockFace.UP, BlockFace.NORTH, BlockFace.EAST};
        BlockFace[][] orth = {{BlockFace.NORTH, BlockFace.EAST}, {BlockFace.UP, BlockFace.EAST}, {BlockFace.NORTH, BlockFace.UP}};
        for (int r = 0; r <= maxradius; r++) {
            for (int s = 0; s < 6; s++) {
                BlockFace f = faces[s % 3];
                BlockFace[] o = orth[s % 3];
                if (s >= 3) {
                    f = f.getOppositeFace();
                }
                Block c = b.getRelative(f, r);
                for (int x = -r; x <= r; x++) {
                    for (int y = -r; y <= r; y++) {
                        Block a = c.getRelative(o[0], x).getRelative(o[1], y);
                        if (a.getType() == Material.AIR && a.getRelative(BlockFace.UP).getType() == Material.AIR) {
                            return a;
                        }
                    }
                }
            }
        }
        return null;// no empty space within a cube of (2*(maxradius+1))^3
    }


    /**
     * Spawn a {@link FallingBlock} of the given material type
     *
     * @param loc      location to spawn the block at
     * @param material material to make the block
     * @return the falling block that was spawned
     */
    public static FallingBlock spawnFallingBlock(Location loc, Material material) {
        return spawnFallingBlock(loc, material, 0);
    }

    /**
     * Spawn a {@link FallingBlock} of the given material at the location specified.
     *
     * @param loc       location to spawn the falling block at
     * @param material  material the block will be.
     * @param dataValue data value to give the material
     * @return the falling block that was spawned.
     */
    public static FallingBlock spawnFallingBlock(Location loc, Material material, int dataValue) {
        return spawnFallingBlock(loc, new MaterialData(material, (byte) dataValue));
    }

    /**
     * Spawn a {@link FallingBlock} with the specified materialdata at the given location
     *
     * @param loc  location to spawn the fallingblock
     * @param data materialdata the block has when spawned.
     * @return the falling block that was spawned.
     */
    public static FallingBlock spawnFallingBlock(Location loc, MaterialData data) {
        World world = loc.getWorld();
        FallingBlock block = world.spawnFallingBlock(loc, data.getItemType(), data.getData());
        return block;
    }

    private static final Set<Material> GRASS_BLACKLIST = Sets.newHashSet(
            Material.ACACIA_SAPLING,
            Material.BAMBOO_SAPLING,
            Material.BIRCH_SAPLING,
            Material.DARK_OAK_SAPLING,
            Material.JUNGLE_SAPLING,
            Material.OAK_SAPLING,
            Material.TALL_GRASS,
            Material.DEAD_BUSH,
            Material.DANDELION,
            Material.ROSE_BUSH,
            Material.RED_MUSHROOM,
            Material.BROWN_MUSHROOM,
            Material.CACTUS,
            Material.VINE
    );

    private static final Set<Material> GRASS_WHITELIST = Sets.newHashSet(
            Material.GRASS,
            Material.DIRT,
            Material.SOUL_SAND
    );

    //todo
    private static final List<ChancedBlock> GRASS_PATCH_BLOCKS = Lists.newArrayList(
            ChancedBlock.of(Material.TALL_GRASS, 100),
            ChancedBlock.of(Material.TALL_GRASS, 45),
            ChancedBlock.of(Material.DANDELION, 5),
            ChancedBlock.of(Material.POPPY, 5),
            ChancedBlock.of(Material.PUMPKIN, 2),
            ChancedBlock.of(Material.MELON, 2),
            ChancedBlock.of(Material.BLUE_ORCHID, 2),
            ChancedBlock.of(Material.ALLIUM, 2),
            ChancedBlock.of(Material.AZURE_BLUET, 2),
            ChancedBlock.of(Material.RED_TULIP, 2),
            ChancedBlock.of(Material.ORANGE_TULIP, 2),
            ChancedBlock.of(Material.WHITE_TULIP, 2),
            ChancedBlock.of(Material.PINK_TULIP, 2),
            ChancedBlock.of(Material.OXEYE_DAISY, 2),
            ChancedBlock.of(Material.BROWN_MUSHROOM, 1),
            ChancedBlock.of(Material.RED_MUSHROOM, 1),
            ChancedBlock.of(Material.SUNFLOWER, 5),
            ChancedBlock.of(Material.LILAC, 5),
            ChancedBlock.of(Material.TALL_GRASS, 10),
            ChancedBlock.of(Material.FERN, 10),
            ChancedBlock.of(Material.LARGE_FERN, 5),
            ChancedBlock.of(Material.ROSE_BUSH, 5),
            ChancedBlock.of(Material.PEONY, 5)
    );


    /**
     * Simulate the growth of grass & flowers; Like bonemeal!
     *
     * @param loc     center of where the growth should begin.
     * @param radius  radius around the center to grow the grass in
     * @param density Value of 1 - 100, with 1 being low density, and 100 being maximum density; Used to make the patches of grass thicker or thinner.
     */
    public static void regrowGrass(Location loc, int radius, int density) {
        if (density > 100) {
            density = 100;
        }

        Block handle;
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                handle = loc.getWorld().getHighestBlockAt((int) loc.getX() + x, (int) loc.getZ() + z);

                if (!LocationUtils.isInRadius(loc, handle.getLocation(), radius)) {
                    continue;
                }

                Block downFace = getBlockFacing(handle, BlockFace.DOWN);

                if (!GRASS_WHITELIST.contains(downFace.getType())) {
                    continue;
                }

                Material baseMat = handle.getType();

                if (GRASS_BLACKLIST.contains(baseMat)) {
                    continue;
                }

                if (!NumberUtil.percentCheck(density)) {
                    continue;
                }

                ChancedBlock replacementBlock = null;

                while (true) {
                    replacementBlock = ListUtils.getRandom(GRASS_PATCH_BLOCKS);

                    if (replacementBlock.pass()) {
                        break;
                    }
                }

                handle.setType(replacementBlock.getMaterial());

                //todo implement second check for flowers
            }
        }
//		//todo implement size check for potential brushes?
    }

    private static double getDistance(Location pos1, Location pos2) {
        double ySize = pos1.getY() - pos2.getY();
        double zSize = pos1.getZ() - pos2.getZ();
        double xSize = pos1.getX() - pos2.getX();

        double distance = Math.sqrt((xSize * xSize) + (ySize * ySize) + (zSize + zSize));
        return distance;
    }
}
