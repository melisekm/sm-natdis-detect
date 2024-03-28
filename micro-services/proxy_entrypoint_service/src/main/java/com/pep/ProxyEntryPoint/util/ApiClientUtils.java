package com.pep.ProxyEntryPoint.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.LinkedHashMap;
import java.util.List;

public class ApiClientUtils {

    public static <T> T convertLinkedHashMapToObject(List<LinkedHashMap<String, Object>> linkedHashMapList, Class<T> targetType) throws Exception {
        if (linkedHashMapList == null || linkedHashMapList.isEmpty()) {
            throw new IllegalArgumentException("List is null or empty");
        }
        LinkedHashMap<String, Object> firstElement = linkedHashMapList.get(0);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(firstElement);

        return objectMapper.readValue(json, targetType);
    }
}
