package com.levelmc.core.api;

import com.levelmc.core.LevelCoreTest;
import org.joor.Reflect;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.Map;

public class TestApiMessages extends LevelCoreTest {

    @Test
    public void testApiMessages() {
        ApiMessages defaultMsgs = new ApiMessages(getPlugin());
        ApiMessages msgs = ApiMessages.getInstance();

        Assertions.assertNotNull(defaultMsgs._get("player-only-command"));
        Assertions.assertNotNull(msgs._get("player-only-command"));
        Assertions.assertEquals(msgs._get("player-only-command"), defaultMsgs._get("player-only-command"));

        Map<String, String> defaultMsgMap = Reflect.on(defaultMsgs).field("coreMessages").get();
        for (String key : defaultMsgMap.keySet()) {
            Assertions.assertNotNull(msgs._get(key));
        }

        Assertions.assertNull(msgs._get("not-a-real-msg"));
    }

}
