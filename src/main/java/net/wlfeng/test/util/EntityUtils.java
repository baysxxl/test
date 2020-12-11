package net.wlfeng.test.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;

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
    public static Map<String, String> entityToMap(Object object) {
        if (object == null) {
            return null;
        }
        Map<String, String> map = null;
        try {
            map = JSONObject.parseObject(JSONObject.toJSONString(object), new TypeReference<Map<String, String>>(){});
        } catch (Exception e) {
            log.error("===实体类转map异常,异常信息:{}===", e.getMessage());
        }
        return map;
    }

    /**
     * Map转实体类
     * @param map 需要初始化的数据，key字段必须与实体类的成员名字一样，否则赋值为空
     * @param clazz  需要转化成的实体类类型
     * @return
     */
    public static <T> T mapToEntity(Map<String, String> map, Class<T> clazz) {
        if (map == null) {
            return null;
        }
        T obj = null;
        try {
            obj = JSONObject.parseObject(JSONObject.toJSONString(map), clazz);
        } catch (Exception e) {
            log.error("===map转实体类异常,异常信息:{}===", e.getMessage());
        }
        return obj;
    }

}
