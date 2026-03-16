package project.team.ondo.global.controller;

import org.springframework.http.ResponseEntity;
import project.team.ondo.global.response.ApiResponse;

public abstract class BaseApiController {

    protected ResponseEntity<ApiResponse<Void>> ok(String message) {
        return ResponseEntity.ok(ApiResponse.success(message));
    }

    protected <T> ResponseEntity<ApiResponse<T>> ok(String message, T data) {
        return ResponseEntity.ok(ApiResponse.success(message, data));
    }
}