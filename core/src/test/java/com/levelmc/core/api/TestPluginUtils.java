package com.levelmc.core.api;

import com.levelmc.core.LevelCoreTest;
import com.levelmc.core.api.utils.PluginUtils;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class TestPluginUtils extends LevelCoreTest {

    @Test
    public void onTestServerMock() {
        Assertions.assertTrue(PluginUtils.isServerMock());
    }
}
