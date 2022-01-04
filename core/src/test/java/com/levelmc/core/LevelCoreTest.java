package com.levelmc.core;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import lombok.Getter;
import org.bukkit.command.Command;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

/**
 * Base implementation for LevelCore tests. Provides the ServerMock, and LevelCore
 */
public abstract class LevelCoreTest {
    // Test Server
    @Getter
    private ServerMock serverMock = null;

    // Plugin being tested
    @Getter
    private LevelCore plugin = null;

    @Before
    public void setUp() {
        serverMock = MockBukkit.mock();
        plugin = MockBukkit.load(LevelCore.class);
    }

    @After
    public void tearDown() {
        MockBukkit.unmock();
    }
}
