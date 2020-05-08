package net.wlfeng.test.exception;

/**
 * @author weilingfeng
 * @date 2020/5/6 16:01
 * @description 自定义业务异常
 */
public class BizException extends RuntimeException {
    private Integer code;
    private Object data;

    public BizException(Integer code, String message) {
        this(code, message, (Throwable)null);
    }

    public BizException(Integer code, String message, Object data) {
        super(message);
        this.code = code;
        this.data = data;
    }

    public BizException(Integer code, String message, Throwable cause) {
        this(code, message, (Object)null, cause);
    }

    public BizException(Integer code, String message, Object data, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.data = data;
    }

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
