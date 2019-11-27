package net.wlfeng.test.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author weilingfeng
 * @date 2019/11/27 19:22
 * @description 实体类工具集合
 */
@Slf4j
public class EntityUtils {
    /**
     * 实体类转Map
     * @param object
     * @return
     */
    public static Map<String, Object> entityToMap(Object object) {
        Map<String, Object> map = new HashMap();
        try {
            for (Field field : object.getClass().getDeclaredFields()){
                boolean flag = field.isAccessible();
                field.setAccessible(true);
                Object o = field.get(object);
                map.put(field.getName(), o);
                field.setAccessible(flag);
            }
        } catch (Exception e) {
            log.error("===实体类转map异常,异常信息:{}===", e.getMessage());
        }
        return map;
    }

    /**
     * Map转实体类
     * @param map 需要初始化的数据，key字段必须与实体类的成员名字一样，否则赋值为空
     * @param entity  需要转化成的实体类
     * @return
     */
    public static <T> T mapToEntity(Map<String, Object> map, Class<T> entity) {
        T t = null;
        try {
            t = entity.newInstance();
            for(Field field : entity.getDeclaredFields()) {
                if (map.containsKey(field.getName())) {
                    boolean flag = field.isAccessible();
                    field.setAccessible(true);
                    Object object = map.get(field.getName());
                    if (object!= null && field.getType().isAssignableFrom(object.getClass())) {
                        field.set(t, object);
                    }
                    field.setAccessible(flag);
                }
            }
            return t;
        } catch (IllegalAccessException e) {
            log.error("===map转实体类异常,异常信息:{}===", e.getMessage());
        } catch (InstantiationException e) {
            log.error("===map转实体类异常,异常信息:{}===", e.getMessage());
        }
        return t;
    }
}
