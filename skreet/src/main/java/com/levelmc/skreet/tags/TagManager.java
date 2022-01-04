package com.levelmc.skreet.tags;

import com.levelmc.core.api.yml.Path;
import com.levelmc.core.api.yml.YamlConfig;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TagManager extends YamlConfig {

    @Path("available-tags")
    @Getter
    private Map<String, Tag> tags = new HashMap<>();

    public TagManager() {

    }

    public Tag getTag(String name) {
        return tags.get(name);
    }

    public boolean isTag(String name) {
        return tags.containsKey(name.toLowerCase());
    }

    /**
     * Register a tag in the manager for players to use / unlock.
     * @param name what the tag will be recognized by internally
     * @param tag display of the tag including &7color &bcodes
     * @return tag instance that was created and cached
     */
    public Tag createTag(String name, String tag) {
        Tag nameTag = new Tag(name,tag);
        tags.put(name,nameTag);
        return nameTag;
    }
}
