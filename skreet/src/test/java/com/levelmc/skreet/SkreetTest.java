package com.levelmc.skreet;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import lombok.Getter;
import org.junit.After;
import org.junit.Before;

public class SkreetTest {

    @Getter
    protected Skreet plugin;
    @Getter
    protected ServerMock server;

    @Before
    public void setup() {
        this.server = MockBukkit.mock();
        this.plugin = MockBukkit.load(Skreet.class);
    }

    @After
    public void teardown() {
        MockBukkit.unmock();
    }

}
