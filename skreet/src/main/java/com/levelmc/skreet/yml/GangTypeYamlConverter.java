package com.levelmc.skreet.yml;

import com.levelmc.core.api.yml.ConfigSection;
import com.levelmc.core.api.yml.InternalConverter;
import com.levelmc.core.api.yml.converter.Converter;
import com.levelmc.skreet.gangs.GangType;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

public class GangTypeYamlConverter implements Converter {
    private InternalConverter converter;

    public GangTypeYamlConverter(InternalConverter converter) {
        this.converter = converter;
    }

    @Override
    public Object toConfig(Class<?> type, Object obj, ParameterizedType parameterizedType) throws Exception {
        GangType gangType = (GangType) obj;
        Map<String, Object> saveMap = new HashMap<>();
        saveMap.put("gang-type", gangType.toString());
        return saveMap;
    }

    @Override
    public Object fromConfig(Class<?> type, Object obj, ParameterizedType parameterizedType) throws Exception {
        Map<String, Object> gangTypeData;

        if (obj instanceof Map) {
            gangTypeData = (Map<String, Object>) obj;
        } else {
            gangTypeData = (Map<String, Object>) ((ConfigSection) obj).getRawMap();
        }

        GangType gangType = GangType.getByName((String) gangTypeData.get("gang-type"));
        return gangType;
    }

    @Override
    public boolean supports(Class<?> type) {
        return type.isAssignableFrom(GangType.class);
    }
}
