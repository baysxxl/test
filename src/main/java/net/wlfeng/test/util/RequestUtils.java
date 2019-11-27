package net.wlfeng.test.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.wlfeng.test.filter.BodyReaderHttpServletRequestWrapper;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Map;

/**
 * @author weilingfeng
 * @date 2019/11/2 12:00
 * @description
 */
public class RequestUtils {

    public static String getParams(BodyReaderHttpServletRequestWrapper request) throws IOException {
        JSONObject result = new JSONObject();

        if ("application/x-www-form-urlencoded".equals(request.getContentType())) {
            Map map = request.getParameterMap();
            for (Object key : map.keySet()) {
                result.put(key.toString(), URLDecoder.decode(request.getParameter(key.toString()), "utf-8"));
            }
        }

        if ("application/json".equals(request.getContentType())) {
            result = JSON.parseObject(request.getParams());
        }
        return result.toJSONString();
    }

}
