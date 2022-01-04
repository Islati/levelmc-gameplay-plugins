package com.levelmc.core.cmd;

import org.bukkit.command.CommandSender;

public interface ExecutableArgument {
    public Object execute(CommandSender sender, Arguments args) throws CommandError;
}
