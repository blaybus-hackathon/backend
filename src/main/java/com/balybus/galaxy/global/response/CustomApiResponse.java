package com.balybus.galaxy.global.response;

import com.balybus.galaxy.global.exception.ExceptionCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomApiResponse<T> {
    private Integer code;
    private String message;
    private ResponseStatus status;
    private T data;

    public CustomApiResponse(Integer code, ResponseStatus status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

    public CustomApiResponse(Integer code, ResponseStatus status, String message, T data) {
        this.code = code;
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> CustomApiResponse<T> SUCCESS (SuccessCode code) {
        return new CustomApiResponse<>(code.getCode(), ResponseStatus.SUCCESS, code.getMessage());
    }
    public static <T> CustomApiResponse<T> SUCCESS (SuccessCode code, T data) {
        return new CustomApiResponse<>(code.getCode(), ResponseStatus.SUCCESS, code.getMessage(), data);
    }
    public static <T> CustomApiResponse<T> ERROR (ExceptionCode code) {
        return new CustomApiResponse<>(code.getCode(), ResponseStatus.ERROR, code.getMessage());
    }
}
