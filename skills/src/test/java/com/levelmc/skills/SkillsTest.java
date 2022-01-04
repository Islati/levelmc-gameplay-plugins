package com.levelmc.skills;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import lombok.Getter;
import org.junit.After;
import org.junit.Before;

public class SkillsTest {

    @Getter
    private Skills plugin;
    @Getter
    private ServerMock server;

    @Before
    public void setup() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(Skills.class);
    }

    @After
    public void cleanup() {
        MockBukkit.unmock();
    }
}
