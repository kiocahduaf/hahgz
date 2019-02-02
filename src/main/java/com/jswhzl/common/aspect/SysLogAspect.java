package com.jswhzl.common.aspect;

import java.lang.reflect.Method;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.jswhzl.api.entity.Log;
import com.jswhzl.api.service.TokenService;
import com.jswhzl.api.service.impl.LogServiceImpl;
import com.jswhzl.common.annotation.SysLog;
import com.jswhzl.common.util.HttpContextUtils;
import com.jswhzl.common.util.IPUtils;

/**
 * 系统日志，切面处理类
 *
 * @author xuchao
 * @email 1009470059@qq.com
 * @date 2019-01-26 10:33
 */
@Aspect
@Component
public class SysLogAspect {
	@Autowired
	private LogServiceImpl sysLogService;
	@Autowired
    TokenService tokenService;
	
	@Pointcut("@annotation(com.jswhzl.common.annotation.SysLog)")
	public void logPointCut() { 
		
	}

	@Around("logPointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		long beginTime = System.currentTimeMillis();
		//执行方法
		Object result = point.proceed();
		//执行时长(毫秒)
		long time = System.currentTimeMillis() - beginTime;

		//保存日志
		saveSysLog(point, time);

		return result;
	}

	@AfterReturning("logPointCut()")
	public void  after(JoinPoint point) throws Throwable{
		MethodSignature signature = (MethodSignature) point.getSignature();
		Method method = signature.getMethod();
		SysLog syslog = method.getAnnotation(SysLog.class);
		if(syslog != null){

		}
		String className = point.getTarget().getClass().getName();
		String methodName = signature.getName();
		System.out.println(className + "." + methodName + "()" + syslog.value());
	}

	private void saveSysLog(ProceedingJoinPoint joinPoint, long time) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();

		Log sysLog = new Log();
		SysLog log = method.getAnnotation(SysLog.class);
		if(log != null){
			//注解上的描述
			sysLog.setOperation(log.value());
		}

		//请求的方法名
		String className = joinPoint.getTarget().getClass().getName();
		String methodName = signature.getName();
		sysLog.setMethod(className + "." + methodName + "()");

		//请求的参数
		Object[] args = joinPoint.getArgs();
		try{
			String params = new Gson().toJson(args[0]);
			sysLog.setParams(params);
		}catch (Exception e){

		}

		//获取request
		HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
		//设置IP地址
		sysLog.setIp(IPUtils.getIpAddr(request));

		//用户名
		String username = "管理员";
		sysLog.setUsername(username);

		sysLog.setTime(time);
		sysLog.setCreateDate(new Date());
		//保存系统日志
		sysLogService.save(sysLog);
	}
}
