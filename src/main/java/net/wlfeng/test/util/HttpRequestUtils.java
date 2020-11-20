package net.wlfeng.test.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.wlfeng.test.filter.BodyReaderHttpServletRequestWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
public class HttpRequestUtils {

    public static final String _255 = "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";

    public static final Pattern pattern = Pattern.compile("^(?:" + _255 + "\\.){3}" + _255 + "$");

    public static String longToIpV4(long longIp) {
        int octet3 = (int) ((longIp >> 24) % 256);
        int octet2 = (int) ((longIp >> 16) % 256);
        int octet1 = (int) ((longIp >> 8) % 256);
        int octet0 = (int) ((longIp) % 256);
        return octet3 + "." + octet2 + "." + octet1 + "." + octet0;

    }

    public static long ipV4ToLong(String ip) {
        String[] octets = ip.split("\\.");
        return (Long.parseLong(octets[0]) << 24) + (Integer.parseInt(octets[1]) << 16) + (Integer.parseInt(octets[2]) << 8) + Integer.parseInt(octets[3]);

    }

    public static boolean isIPv4Valid(String ip) {
        return pattern.matcher(ip).matches();
    }

    /**
     * * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址;
     * <p>
     * *
     * <p>
     * * @param request
     * <p>
     * * @return
     * <p>
     */
    public final static String getIpAddress() {
        log.info("HttpRequestUtils.getIpAddress is start");
        HttpServletRequest request = getRequest();
        if (request == null) {
            log.warn("HttpRequestUtils.getIpAddress request is null");
            return null;
        }
        String headerType = "X-Forwarded-For";
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                headerType = "Proxy-Client-IP";
                ip = request.getHeader(headerType);
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                headerType = "WL-Proxy-Client-IP";
                ip = request.getHeader(headerType);
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                headerType = "HTTP_CLIENT_IP";
                ip = request.getHeader(headerType);
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                headerType = "HTTP_X_FORWARDED_FOR";
                ip = request.getHeader(headerType);
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                headerType = "getRemoteAddr";
                ip = request.getRemoteAddr();
                if (ip.equals("127.0.0.1")) {
                    String localIp = getLocalIpAddress();
                    if (StringUtils.isNotBlank(localIp)) {
                        ip = localIp;
                    }
                }
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (int index = 0; index < ips.length; index++) {
                String strIp = ips[index];
                if (!("unknown".equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        log.info("HttpRequestUtils.getIpAddress {} ip = {}", headerType, ip);
        if (isIPv4Valid(ip)) {
            return ip;
        }
        return null;
    }

    /**
     * 获取本机网卡绑定的ip地址
     * @return
     */
    public final static String getLocalIpAddress() {
        log.info("HttpRequestUtils.getLocalIpAddress is start");
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.error("HttpRequestUtils.getLocalIpAddress error:{}", e);
        }
        return null;
    }

    public static HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }
        return requestAttributes.getRequest();
    }

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

    public static <T> String getParamMd5Str(T param) {
        Map<String, String> paramMap = EntityUtils.entityToMap(param);
        if (paramMap.isEmpty()) {
            return null;
        }
        String[] sortedKeys = paramMap.keySet().toArray(new String[]{});
        Arrays.sort(sortedKeys);
        StringBuilder sb = new StringBuilder();
        for (String key : sortedKeys) {
            sb.append(key).append("=").append(paramMap.get(key)).append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
        return MD5Util.string2MD5(sb.toString());
    }

}
