package com.levelmc.loot.api;

import com.levelmc.core.api.utils.ListUtils;
import com.levelmc.core.api.yml.Path;
import com.levelmc.core.api.yml.YamlConfig;
import com.levelmc.loot.Loot;
import lombok.Getter;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to assign drop tables via their id to mobs.
 */
public class MobDropData extends YamlConfig {

    @Getter
    @Path("mob")
    private EntityType entityType;

    @Path("tables")
    private List<String> dropTableIds = new ArrayList<>();

    public MobDropData(EntityType entityType) {
        this.entityType = entityType;
    }

    public MobDropData() {

    }

    public MobDropData addTable(DropTable table) {
        dropTableIds.add(table.getId());
        return this;
    }

    public DropTable getRandomTable() {

        String id = null;

        if (dropTableIds.size() == 1) {
            id = dropTableIds.get(0);
        } else {
            id = ListUtils.getRandom(dropTableIds);
        }

        return Loot.getInstance().getRegistry().getTable(ListUtils.getRandom(dropTableIds));
    }

}
