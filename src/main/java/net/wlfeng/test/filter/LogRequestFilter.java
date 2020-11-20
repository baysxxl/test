package net.wlfeng.test.filter;

import lombok.extern.slf4j.Slf4j;
import net.wlfeng.test.util.HttpRequestUtils;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author weilingfeng
 * @date 2019/10/22 12:29
 * @description
 */
@Slf4j
@Component
public class LogRequestFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        long startTime = System.currentTimeMillis();
        BodyReaderHttpServletRequestWrapper httpServletRequest = new BodyReaderHttpServletRequestWrapper((HttpServletRequest) request);
        // 打印当前的请求路径和参数
        log.info("======RequestMapping:{},param:{}", httpServletRequest.getRequestURI(), HttpRequestUtils.getParams(httpServletRequest));
        chain.doFilter(httpServletRequest, response);
        log.info("======request end,spend:{}ms======", System.currentTimeMillis() - startTime);
    }
}
