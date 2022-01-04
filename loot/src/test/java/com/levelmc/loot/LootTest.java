package com.levelmc.loot;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import lombok.Getter;
import org.junit.After;
import org.junit.Before;

public class LootTest {
    @Getter
    private Loot plugin;
    @Getter
    private ServerMock server;

    @Before
    public void setup() {
        this.server = MockBukkit.mock();
        this.plugin = MockBukkit.load(Loot.class);
    }

    @After
    public void teardown() {
        MockBukkit.unmock();
    }
}
