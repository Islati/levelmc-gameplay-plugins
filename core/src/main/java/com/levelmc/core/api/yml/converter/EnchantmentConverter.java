package com.levelmc.core.api.yml.converter;

import com.levelmc.core.LevelCore;
import com.levelmc.core.api.yml.ConfigSection;
import com.levelmc.core.api.yml.InternalConverter;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

public class EnchantmentConverter implements Converter {
    private InternalConverter converter;

    public EnchantmentConverter(InternalConverter converter) {
        this.converter = converter;
    }

    @Override
    public Object toConfig(Class<?> type, Object obj, ParameterizedType parameterizedType) throws Exception {
        Map<String, Object> saveMap = new HashMap<>();

//        if (obj == null) {
//            saveMap.put("sound", "");
//        } else {
//        }
        Enchantment enchant = (Enchantment) obj;

        saveMap.put("enchantment", enchant.getKey().getKey());

        return saveMap;
    }

    @Override
    public Object fromConfig(Class<?> type, Object section, ParameterizedType parameterizedType) throws Exception {
        Map<String, Object> enchantment;

        if (section instanceof Map) {
            enchantment = (Map<String, Object>) section;
        } else {
            enchantment = (Map<String, Object>) ((ConfigSection) section).getRawMap();
        }

        String name = (String)enchantment.get("enchantment");

        LevelCore.getInstance().getLogger().info("Translating enchantment: " + name);

        try {
            Enchantment enchant = Enchantment.getByKey(NamespacedKey.minecraft((String)enchantment.get("enchantment")));
            return enchant;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> type) {
        return type.isAssignableFrom(Enchantment.class);
    }
}
