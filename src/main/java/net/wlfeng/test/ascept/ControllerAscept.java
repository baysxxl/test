package net.wlfeng.test.ascept;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Slf4j
@Component
public class ControllerAscept {

	// 用来记录请求进入的时间，防止多线程时出错，这里用了ThreadLocal
	ThreadLocal<Long> startTime = new ThreadLocal<>();

	@Pointcut("execution(* net.wlfeng.test.controller..*.*(..))")
	public void pointCut() {}
	
	@Before("pointCut()")
	public void before(JoinPoint joinPoint) {
		startTime.set(System.currentTimeMillis());
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = servletRequestAttributes.getRequest();
		// 打印当前的请求路径和参数，如果需要打印其他的信息可以到request中去拿
		log.info("===Request,path:{},param:{}===",request.getRequestURI(), Arrays.toString(joinPoint.getArgs()));
	}
	
	@After("pointCut()")
	public void after() {
		// log.info("===execute after===");
	}
	
	@AfterReturning(pointcut = "pointCut()", returning = "response")
	public void afterReturning(Object response) {
		if (log.isDebugEnabled()) {
			// 打印返回值信息
			log.debug("======Response:[{}]",response );
		}
		// 打印请求耗时
		log.info("======Request end, spend times : [{}ms]======", System.currentTimeMillis() - startTime.get());
	}
	
	@AfterThrowing("pointCut()")
	public void afterThrowing() {
		// log.info("===execute afterThrowing===");
	}
	
	/*@Around("pointCut()")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		try {
			Object result = joinPoint.proceed();
			return result;
		} catch (Exception e) {
			return "exception";
		}
	}*/
}
