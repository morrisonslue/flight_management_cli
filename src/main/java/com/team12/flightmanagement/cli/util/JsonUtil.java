package com.team12.flightmanagement.cli.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> T fromJson(String json, Class<T> clazz) throws Exception {
        return mapper.readValue(json, clazz);
    }

    public static <T> T[] fromJsonArray(String json, Class<T[]> clazz) throws Exception {
        return mapper.readValue(json, clazz);
    }
}

