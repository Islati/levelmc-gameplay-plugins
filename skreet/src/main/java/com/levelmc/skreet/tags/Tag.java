package com.levelmc.skreet.tags;

import com.levelmc.core.api.yml.Path;
import com.levelmc.core.api.yml.YamlConfig;
import lombok.Getter;
import lombok.Setter;

public class Tag extends YamlConfig {
    @Path("name")
    @Getter
    private final String name;

    @Path("tag")
    @Getter
    @Setter
    private String tag;

    public Tag(String name, String tag) {
        this.name = name;
        this.tag = tag;
    }
}
