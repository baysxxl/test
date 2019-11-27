package net.wlfeng.test.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.http.HttpMethod;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.net.URLDecoder;
import java.util.Map;

/**
 * @author weilingfeng
 * @date 2019/10/22 15:54
 * @description
 */
public class BodyReaderHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private final String body;

    public BodyReaderHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader reader = request.getReader();
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        body = sb.toString();
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream bais = new ByteArrayInputStream(body.getBytes());
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener listener) {

            }

            @Override
            public int read() throws IOException {
                return bais.read();
            }
        };
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

    public String getBody() {
        return body;
    }

    public String getParams() throws UnsupportedEncodingException {
        String params = "";
        if (HttpMethod.GET.matches(this.getMethod())) {
            // params = this.getQueryString();
            Map map = this.getParameterMap();
            JSONObject jsonObject = new JSONObject();
            for (Object key : map.keySet()) {
                jsonObject.put(key.toString(), URLDecoder.decode(this.getParameter(key.toString()), "utf-8"));
            }
            params = jsonObject.toJSONString(map);
        }
        if (HttpMethod.POST.matches(this.getMethod())) {
            if (StringUtils.isNotBlank(this.getBody())) {
                // 转换一遍去除json中的空格
                params = JSON.parseObject(this.getBody()).toJSONString();
            }
        }
        return params;
    }
}
