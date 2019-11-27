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

    public CommonResponse(boolean success, int code, String msg, T data) {
        this.success = success;
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static CommonResponse success(String message) {
        return new CommonResponse(true, 200, message, null);
    }

    public static CommonResponse fail(String message) {
        return new CommonResponse(false, 500, message, null);
    }
}
