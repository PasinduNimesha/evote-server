package com.evote.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {
    private String message;
    private Object data;
    private boolean success;

    public static ApiResponse success(String message) {
        return ApiResponse.builder()
                .message(message)
                .success(true)
                .build();
    }

    public static ApiResponse success(String message, Object data) {
        return ApiResponse.builder()
                .message(message)
                .data(data)
                .success(true)
                .build();
    }

    public static ApiResponse error(String message) {
        return ApiResponse.builder()
                .message(message)
                .success(false)
                .build();
    }
}
