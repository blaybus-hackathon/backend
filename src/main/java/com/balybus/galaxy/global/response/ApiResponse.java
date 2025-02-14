package com.balybus.galaxy.global.response;

import com.balybus.galaxy.global.exception.ExceptionCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private Integer code;
    private String message;
    private ResponseStatus status;
    private T data;

    public ApiResponse(Integer code, ResponseStatus status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

    public ApiResponse(Integer code, ResponseStatus status, String message, T data) {
        this.code = code;
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> SUCCESS (SuccessCode code) {
        return new ApiResponse<>(code.getCode(), ResponseStatus.SUCCESS, code.getMessage());
    }
    public static <T> ApiResponse<T> SUCCESS (SuccessCode code, T data) {
        return new ApiResponse<>(code.getCode(), ResponseStatus.SUCCESS, code.getMessage(), data);
    }
    public static <T> ApiResponse<T> ERROR (ExceptionCode code) {
        return new ApiResponse<>(code.getCode(), ResponseStatus.ERROR, code.getMessage());
    }
}
