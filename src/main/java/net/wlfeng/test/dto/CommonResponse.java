package net.wlfeng.test.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author weilingfeng
 * @date 2019/10/29 14:22
 * @description
 */
@Data
public class CommonResponse<T> implements Serializable {
    private boolean success;
    private int code;
    private String msg;
    private T data;

    public static final Integer SUCCESS_CODE = 200;

    public static final Integer FAILED_CODE = 500;

    public CommonResponse(boolean success, int code, String msg, T data) {
        this.success = success;
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static CommonResponse success(String message) {
        return success(message, null);
    }

    public static <T> CommonResponse success(T data) {
        return success(null, data);
    }

    public static <T> CommonResponse success(String message, T data) {
        return new CommonResponse(true, SUCCESS_CODE, message, data);
    }

    public static CommonResponse fail(String message) {
        return fail(FAILED_CODE, message);
    }

    public static CommonResponse fail(Integer code, String message) {
        return new CommonResponse(false, code, message, null);
    }
}
