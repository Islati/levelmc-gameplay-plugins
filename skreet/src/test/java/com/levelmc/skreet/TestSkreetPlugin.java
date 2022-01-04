package com.levelmc.skreet;

import org.joor.Reflect;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.io.File;

public class TestSkreetPlugin extends SkreetTest {

    @Test
    public void testInitialization() {
        Assertions.assertNotNull(Reflect.on(getPlugin()).field("tagManager").get());
        Assertions.assertNotNull(Reflect.on(getPlugin()).field("userManager").get());
        Assertions.assertNotNull(Reflect.on(getPlugin()).field("gangListener").get());
        Assertions.assertNotNull(Reflect.on(getPlugin()).field("gangManager").get());
    }
}
