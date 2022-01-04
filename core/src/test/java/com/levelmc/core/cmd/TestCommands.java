package com.levelmc.core.cmd;

import be.seeseemelk.mockbukkit.command.CommandResult;
import be.seeseemelk.mockbukkit.command.ConsoleCommandSenderMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import com.levelmc.core.LevelCoreTest;
import com.levelmc.core.api.ApiPermissions;
import org.bukkit.command.Command;
import org.bukkit.permissions.PermissionAttachment;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class TestCommands extends LevelCoreTest {
    private PlayerMock admin;
    private ConsoleCommandSenderMock consoleSender;

    private PermissionAttachment debugPerm = null;

    @Before
    public void setup() {
        admin = getServerMock().addPlayer("Islati");
        debugPerm = admin.addAttachment(getPlugin(),ApiPermissions.DEBUGGER,true);
        Assertions.assertTrue(admin.hasPermission(ApiPermissions.DEBUGGER));
    }

    @Test
    public void testCommandRegistration() {
        Command debugCommand = getServerMock().getCommandMap().getCommand("debug");
        Assertions.assertNotNull(debugCommand);
    }

    @Test
    public void testCommandExecution() {
        CommandResult cmdDebugResult = getServerMock().execute("debug",admin);
        cmdDebugResult.assertSucceeded();

        admin.removeAttachment(debugPerm);
        Assertions.assertFalse(admin.hasPermission(ApiPermissions.DEBUGGER));
    }
}
