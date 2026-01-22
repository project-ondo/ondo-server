package project.team.ondo.global.response;


import project.team.ondo.global.error.CommonErrorCode;

public record ApiResponse<T>(
        boolean success,
        String message,
        T data
) {

    public static ApiResponse<Void> error(CommonErrorCode errorCode) {
        return new ApiResponse<>(false, errorCode.getMessage(), null);
    }

    public static ApiResponse<Void> success(String message) {
        return new ApiResponse<>(true, message, null);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }
}
