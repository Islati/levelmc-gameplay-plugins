package com.levelmc.core.api.yml.converter;


import com.levelmc.core.api.yml.ConfigSection;
import com.levelmc.core.api.yml.InternalConverter;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UuidYamlConverter implements Converter {
	private InternalConverter internalConverter;

	public UuidYamlConverter(InternalConverter internalConverter) {
		this.internalConverter = internalConverter;
	}

	@Override
	public Object toConfig(Class<?> type, Object obj, ParameterizedType parameterizedType) throws Exception {
		UUID id = (UUID)obj;
		Map<String, Object> saveMap = new HashMap<>();
		saveMap.put("uuid",id.toString());
		return saveMap;
	}

	@Override
	public Object fromConfig(Class type, Object section, ParameterizedType genericType) throws Exception {
		Map<String, Object> loadMap = new HashMap<>();

		if (section instanceof Map) {
			loadMap = (Map<String, Object>)section;
		} else {
			loadMap = (Map<String, Object>)((ConfigSection)section).getRawMap();
		}

		return UUID.fromString((String) loadMap.get("uuid"));
	}

	@Override
	public boolean supports(Class<?> type) {
		return type.isArray();
	}
}
