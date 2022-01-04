package com.levelmc.core.api.yml.converter;

import com.levelmc.core.api.yml.ConfigSection;
import com.levelmc.core.api.yml.InternalConverter;
import org.bukkit.entity.EntityType;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

public class EntityTypeYamlConverter implements Converter {
    private InternalConverter converter;

    public EntityTypeYamlConverter(InternalConverter converter) {
        this.converter = converter;
    }

    @Override
    public Object toConfig(Class<?> type, Object obj, ParameterizedType parameterizedType) throws Exception {
        EntityType entityType = (EntityType) obj;
        Map<String, Object> saveMap = new HashMap<>();
        saveMap.put("entity-type", entityType.name().toLowerCase());
        return saveMap;
    }

    @Override
    public Object fromConfig(Class<?> type, Object section, ParameterizedType parameterizedType) throws Exception {
        Map<String, Object> entityData;

        if (section instanceof Map) {
            entityData = (Map<String, Object>) section;
        } else {
            entityData = (Map<String, Object>) ((ConfigSection) section).getRawMap();
        }

        EntityType entityType = EntityType.fromName((String) entityData.get("entity-type"));

        return entityType;
    }

    @Override
    public boolean supports(Class<?> type) {
        return type.isAssignableFrom(EntityType.class);
    }
}
