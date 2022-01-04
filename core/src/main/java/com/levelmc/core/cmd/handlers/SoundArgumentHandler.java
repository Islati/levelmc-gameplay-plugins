package com.levelmc.core.cmd.handlers;

import com.levelmc.core.cmd.ArgumentHandler;
import com.levelmc.core.cmd.CommandArgument;
import com.levelmc.core.cmd.TransformError;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;


public class SoundArgumentHandler extends ArgumentHandler<Sound> {
    public SoundArgumentHandler() {
        setMessage("parse_error", "There is no sound named %1");
        setMessage("include_error", "There is no sound named %1");
        setMessage("exclude_error", "There is no sound named %1");
    }

    @Override
    public Sound transform(CommandSender sender, CommandArgument argument, String value) throws TransformError {
        return Sound.valueOf(value.toUpperCase());
    }


}
