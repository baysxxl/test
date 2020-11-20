package net.wlfeng.test.annotation;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author weilingfeng
 * @date 2020/8/12 10:35
 * @description 枚举值校验注解
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {EnumValid.EnumValidtor.class})
@Documented
public @interface EnumValid {

    String message() default "值不在范围内";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<?>[] target() default {};

    public class EnumValidtor implements ConstraintValidator<EnumValid, Object> {

        Class<?>[] cls; //枚举类

        @Override
        public void initialize(EnumValid constraintAnnotation) {
            cls = constraintAnnotation.target();
        }

        @Override
        public boolean isValid(Object value, ConstraintValidatorContext context) {
            if (Objects.isNull(value)) {
                return false;
            }
            if (cls.length > 0) {
                for (Class<?> cl : cls) {
                    try {
                        if (cl.isEnum()) {
                            //枚举类验证
                            Object[] objs = cl.getEnumConstants();
                            Method method = cl.getMethod("getCode");
                            for (Object obj : objs) {
                                Object code = method.invoke(obj);
                                if (value.equals(code)) {
                                    return true;
                                }
                            }
                        }
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
            return false;
        }
    }
}