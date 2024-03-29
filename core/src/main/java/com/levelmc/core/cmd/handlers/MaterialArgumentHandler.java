package com.levelmc.core.cmd.handlers;

import com.levelmc.core.cmd.ArgumentHandler;
import com.levelmc.core.cmd.CommandArgument;
import com.levelmc.core.cmd.TransformError;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;


public class MaterialArgumentHandler extends ArgumentHandler<Material> {

    public MaterialArgumentHandler() {
        setMessage("parse_error", "The parameter [%p] is not a valid material.");
        setMessage("include_error", "There is no material named %1");
        setMessage("exclude_error", "There is no material named %1");
    }

    @Override
    public Material transform(CommandSender sender, CommandArgument argument, String value) throws TransformError {
        Material material = null;

        material = Material.matchMaterial(value);

        if (material == null) {
            throw new TransformError(argument.getMessage("parse_error"));
        }

        return material;
    }
}
