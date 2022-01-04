package com.levelmc.core.config;

import com.levelmc.core.api.yml.Comments;
import com.levelmc.core.api.yml.Path;
import com.levelmc.core.api.yml.YamlConfig;
import lombok.Getter;

public class LevelCoreConfig extends YamlConfig {

    @Path("api-only")
    @Comments({"When enabled only the API will be initialized,", "saving extra features that would normally be in the live game."})
    @Getter
    private boolean apiOnly = false;

    @Path("server.name")
    @Getter
    @Comments({"Very important to be different for each server", "as this value is the key identifier in some database queries"})
    private String serverName = "";

    public LevelCoreConfig() {

    }

}
