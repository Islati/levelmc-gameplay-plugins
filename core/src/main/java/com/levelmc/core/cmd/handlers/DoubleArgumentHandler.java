package com.levelmc.core.cmd.handlers;

import com.levelmc.core.cmd.CommandArgument;
import com.levelmc.core.cmd.TransformError;
import org.bukkit.command.CommandSender;

public class DoubleArgumentHandler extends NumberArgumentHandler<Double> {
    public DoubleArgumentHandler() {
        setMessage("parse_error", "The parameter [%p] is not a number");
    }

    @Override
    public Double transform(CommandSender sender, CommandArgument argument, String value) throws TransformError {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new TransformError(argument.getMessage("parse_error"));
        }
    }
}
