package com.levelmc.core.cmd;

public interface HelpHandler {
    public String[] getHelpMessage(RegisteredCommand command);

    public String getUsage(RegisteredCommand command);
}
