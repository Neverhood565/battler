package org.battler.urils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

/**
 * Created by romanivanov on 14.09.2022
 */
public class JsonUtils {

    public static final ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    public static String toJson(Object object) {
        return mapper.writeValueAsString(object);
    }

    @SneakyThrows
    public static <T> T toObject(String json, Class<T> type) {
        return mapper.readValue(json, type);
    }
}
