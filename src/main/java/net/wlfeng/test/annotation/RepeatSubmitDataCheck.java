package net.wlfeng.test.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author weilingfeng
 * @date 2020/05/06 15:55
 * @description: 防重调用注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RepeatSubmitDataCheck {

    String value() default "";

    String keyPerfix() default "";
}
