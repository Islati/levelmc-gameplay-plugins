package com.levelmc.core.cmd.handlers;

import com.levelmc.core.cmd.CommandArgument;
import com.levelmc.core.cmd.TransformError;
import org.bukkit.command.CommandSender;

public class IntegerArgumentHandler extends NumberArgumentHandler<Integer> {
    public IntegerArgumentHandler() {
        setMessage("parse_error", "The parameter [%p] is not an integer");
    }

    @Override
    public Integer transform(CommandSender sender, CommandArgument argument, String value) throws TransformError {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new TransformError(argument.getMessage("parse_error"));
        }
    }
}
