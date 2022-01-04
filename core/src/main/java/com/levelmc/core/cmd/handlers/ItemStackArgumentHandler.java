package com.levelmc.core.cmd.handlers;

import com.levelmc.core.api.ApiMessages;
import com.levelmc.core.cmd.*;
import com.levelmc.core.api.inventory.HandSlot;
import com.levelmc.core.api.players.PlayerUtils;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemStackArgumentHandler extends ArgumentHandler<ItemStack> {
    public ItemStackArgumentHandler() {
        //Add the sender variable,
        addVariable("hand", "item in the hand of the command executor", ItemStackHandArgumentVariable.getInstance());
        addVariable("offhand", "item in the off-hand of the command executor", ItemStackHandArgumentVariable.getInstance());
        addVariable("sender", "item in the hand of the command executor", ItemStackHandArgumentVariable.getInstance());

        for(Material material : Material.values()) {
//            Bukkit.getLogger().info(String.format("Created ItemStackArgumentHandler %s", material.name().toLowerCase()));
            try {
                addVariable(material.name().toLowerCase(),material.name().toLowerCase(), ItemStackArgumentVariable.getInstance());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public ItemStack transform(CommandSender sender, CommandArgument argument, String value) throws TransformError {
        if (value.equalsIgnoreCase("0") || value.equalsIgnoreCase("air")) {
            return null;
        }

        try {
            return new ItemStack(Material.matchMaterial(value));
        } catch (Exception e) {
            throw new TransformError(e.getMessage());
        }

    }

    private static class ItemStackArgumentVariable implements ArgumentVariable<ItemStack> {

        private static ItemStackArgumentVariable instance;

        public static ItemStackArgumentVariable getInstance() {
            if (instance == null) {
                instance = new ItemStackArgumentVariable();
            }
            return instance;
        }

        private ItemStackArgumentVariable() {
        }

        @Override
        public ItemStack var(CommandSender sender, CommandArgument argument, String varName) throws CommandError {
            if (varName.equalsIgnoreCase("0") || varName.equalsIgnoreCase("air")) {
                return null;
            }

            try {
                return new ItemStack(Material.matchMaterial(varName));
            } catch (Exception e){
                throw new TransformError(e.getMessage());
            }

        }
    }

    private static class ItemStackHandArgumentVariable implements ArgumentVariable<ItemStack> {
        private static ItemStackHandArgumentVariable instance;

        public static ItemStackHandArgumentVariable getInstance() {
            if (instance == null) {
                instance = new ItemStackHandArgumentVariable();
            }
            return instance;
        }

        @Override
        public ItemStack var(CommandSender sender, CommandArgument argument, String varName) throws CommandError {
            if (!(sender instanceof Player)) {
                throw new CommandError(ApiMessages.get("player-only-command"));
            }

            if (varName.equalsIgnoreCase("offhand")) {
                Player player = (Player) sender;

                if (PlayerUtils.handIsEmpty(player, HandSlot.OFF_HAND)) {
                    throw new CommandError(ApiMessages.get("off-hand-item-required"));
                }

                return PlayerUtils.getItemInHand(player, HandSlot.OFF_HAND);
            }

            Player player = (Player) sender;
            if (PlayerUtils.handIsEmpty(player, HandSlot.MAIN_HAND)) {
                throw new CommandError(ApiMessages.get("main-hand-item-required"));
            }

            return PlayerUtils.getItemInHand(player, HandSlot.MAIN_HAND);
        }
    }
}
