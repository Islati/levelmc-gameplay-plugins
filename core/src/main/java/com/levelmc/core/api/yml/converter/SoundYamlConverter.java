package com.levelmc.core.api.yml.converter;

import com.levelmc.core.api.yml.ConfigSection;
import com.levelmc.core.api.yml.InternalConverter;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

public class SoundYamlConverter implements Converter {
    private InternalConverter converter;

    public SoundYamlConverter(InternalConverter converter) {
        this.converter = converter;
    }

    @Override
    public Object toConfig(Class<?> type, Object obj, ParameterizedType parameterizedType) throws Exception {
        Map<String, Object> saveMap = new HashMap<>();

//        if (obj == null) {
//            saveMap.put("sound", "");
//        } else {
//        }
        Sound sound = (Sound) obj;

        saveMap.put("sound", sound.name());

        return saveMap;
    }

    @Override
    public Object fromConfig(Class<?> type, Object section, ParameterizedType parameterizedType) throws Exception {
        Map<String, Object> soundData;

        if (section instanceof Map) {
            soundData = (Map<String, Object>) section;
        } else {
            soundData = (Map<String, Object>) ((ConfigSection) section).getRawMap();
        }

        try {
            Sound sound = Sound.valueOf(((String) soundData.get("sound")));
            return sound;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> type) {
        return type.isAssignableFrom(Sound.class);
    }
}
