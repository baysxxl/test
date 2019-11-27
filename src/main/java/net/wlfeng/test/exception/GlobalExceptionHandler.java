package net.wlfeng.test.exception;

import lombok.extern.slf4j.Slf4j;
import net.wlfeng.test.dto.CommonResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author weilingfeng
 * @date 2019/11/27 16:31
 * @description 全局异常处理器
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    CommonResponse handleException(Exception e){
        log.error("===系统异常,异常信息:{}===", e.getMessage());
        return CommonResponse.fail(e.getMessage());
    }

    /**
     * 处理所有接口数据验证异常
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    CommonResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        log.error("===数据校验异常,异常信息:{}===", e.getMessage());
        return CommonResponse.fail(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }
}
