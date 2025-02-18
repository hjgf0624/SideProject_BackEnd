package com.github.hjgf0624.sideproject.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.LinkedHashMap;
import java.util.Map;

// 250218
@Getter
@Builder
@ToString
public class BaseResponseDTO<T> {
    private final boolean success;
    private final Map<String, Object> responseData;

    public static <T> BaseResponseDTO<T> success(T data, String keyName) {
        Map<String, Object> responseData = new LinkedHashMap<>();
        responseData.put(keyName, data);

        return BaseResponseDTO.<T>builder()
                .success(true)
                .responseData(responseData)
                .build();
    }

    public BaseResponseDTO<T> addField(String key, Object value) {
        Map<String, Object> newData = new LinkedHashMap<>();
        newData.put(key, value);
        newData.putAll(this.responseData);

        return BaseResponseDTO.<T>builder()
                .success(this.success)
                .responseData(newData)
                .build();
    }

    @JsonAnyGetter
    public Map<String, Object> getResponseData() {
        return responseData;
    }
}
