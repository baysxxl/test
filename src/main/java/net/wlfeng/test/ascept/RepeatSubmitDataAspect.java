package net.wlfeng.test.ascept;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import net.wlfeng.test.annotation.RepeatSubmitDataCheck;
import net.wlfeng.test.exception.BizException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author weilingfeng
 * @date 2020/05/06 15:55
 * @description: 防重调用检查
 */
@Aspect
@Slf4j
@Component
public class RepeatSubmitDataAspect {

    @Autowired(required = false)
    private RedissonClient redissonClient;

    private RLock lock;

    /**
     * 检查重复提交的切面
     */
    @Pointcut("@annotation(net.wlfeng.test.annotation.RepeatSubmitDataCheck)")
    public void checkRepeatSubmit() {

    }

    @Around("checkRepeatSubmit()")
    public Object checkSubmit(ProceedingJoinPoint joinPoint) throws Throwable {
        if (Objects.isNull(redissonClient)) {
            return joinPoint.proceed();
        }
        String lockKey = "";
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        RepeatSubmitDataCheck dataCheck = methodSignature.getMethod().getAnnotation(RepeatSubmitDataCheck.class);
        String[] parameterNames = methodSignature.getParameterNames();
        if (dataCheck != null && parameterNames != null && parameterNames.length > 0) {
            for (int i = 0; i < parameterNames.length; i++) {
                if (parameterNames[i].equals(dataCheck.value())) {
                    try {
                        lockKey = JSON.toJSONString(joinPoint.getArgs()[i]);
                    } catch (Exception e) {
                        log.warn("参数不可序列化！");
                    }
                    break;
                }
            }
        }
        String keyPerfix = StringUtils.hasText(dataCheck.keyPerfix()) ? dataCheck.keyPerfix() : "lock:checkRepeatSubmit:";
        lock = redissonClient.getLock(keyPerfix + DigestUtils.md5DigestAsHex(lockKey.getBytes()));
        try {
            if (!lock.tryLock(1L, 10L, TimeUnit.SECONDS)) {
                throw new BizException(-1, "系统处理中，请勿重复提交！");
            }
            return joinPoint.proceed();
        } catch (Throwable throwable) {
            throw throwable;
        } finally {
            if (lock != null && lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

}
