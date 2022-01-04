package com.levelmc.skreet.tags;

import com.levelmc.skreet.SkreetTest;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class TestTagManager extends SkreetTest {

    @Test
    public void testTagManager() {
        TagManager tagManager = getPlugin().getTagManager();

        Tag bloodTag = tagManager.createTag("blood", "&cBlood");
        Assertions.assertNotNull(bloodTag);
        Assertions.assertTrue(tagManager.isTag("blood"));

        Assertions.assertEquals(tagManager.getTag("blood").getTag(), "&cBlood");
    }
}
