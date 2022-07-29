/*****************************************************************
 * Copyright (c) 2017 Ecoletree. All Rights Reserved.
 * 
 * Author : dongsuk 
 * Create Date : 2016. 5. 31.
 * DESC : kr.co.ecoletree.common.aspect ETLoggingAspect.java
*****************************************************************/
package kr.co.ecoletree.common.adpect;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author dongsuk
 *
 */
@Aspect
@Component
public class ETLoggingAspect {
	
	// 패키지 경로에 맞춰서 수정할 것
	@Pointcut("execution(* kr.co.ecoletree.service..*Service.*(..))")
	private void all() {}
	
	@Around("all()")
	public Object around(ProceedingJoinPoint pjp) throws Throwable {
		Object target = pjp.getTarget();
		Signature sign = pjp.getSignature();
		String name = sign.toLongString();
		Object[] paramObj = pjp.getArgs();
		boolean noLog = false;
		
		// 특정 패키지 로그는 생략
		if (name.matches("(.*common.*)"))
			noLog = true;
		
		Logger logger = LoggerFactory.getLogger(target.getClass());
		// Level : TRACE < DEBUG < INFO < WARN < ERROR < FATAL
		if (!noLog) {
			logger.info("[======================== Method Execute ========================]");
			logger.info("실행 메소드 : " + name);
			logger.info("[======================== Method Execute ========================]");
			if (paramObj != null) {
				for (Object param : paramObj) {
					printBeans(logger, param);
				}
			}
		}
		// 메소드 실행 전
		Object result = pjp.proceed();
		// 메소드 실행 후
		if (!noLog) {
			if (result != null)
				logger.debug(sign.toShortString() + " result : " + result.getClass().getName());
			else
				logger.debug(sign.toShortString() + " result : " + result);
			if (logger.isDebugEnabled()) {
				if (result != null && result instanceof List) {
					// 결과는 간략하게 List만 찍는 것으로 변경
					logger.debug(result.getClass().getSimpleName());
					/*
					 * List<Object> resultList = (List<Object>) result;
					 * for (Object o : resultList) {
					 * printBeans(logger, o);
					 * }
					 */
				}
			}
		}
		return result;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void printBeans(Logger logger, Object param) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		if (param == null)
			return;
		Class paramClass = param.getClass();
		Package pack = paramClass.getPackage();
		
		if (pack != null && pack.getName().endsWith("vo")) {
			Field[] fields = paramClass.getDeclaredFields();
			StringBuilder sb = new StringBuilder("[");
			
			for (Field field : fields) {
				String variableName = field.getName().substring(0, 1).toUpperCase();
				if (field.getName().length() > 1) {
					variableName += field.getName().substring(1);
				}
				try {
					sb.append(field.getName());
					sb.append(" : ");
					// sb.append(paramClass.getMethod("get" + variableName, null).invoke(param, null));
					sb.append(paramClass.getMethod("get" + variableName).invoke(param, (Object[]) null));
					sb.append(",");
				} catch (Exception e) {
					// 혹 메소드를 못찾거나 오류 발생시에도 무시하고 진행
				}
			}
			sb.deleteCharAt(sb.lastIndexOf(","));
			sb.append("]");
			logger.debug(paramClass.getSimpleName() + " : " + sb);
		} else {
			logger.debug(paramClass.getSimpleName() + " : " + param);
		}
	}
	
	@AfterThrowing(pointcut = "within(kr.co.ecoletree.service..service)", throwing = "ex")
	public void throwAdvice(JoinPoint jp, Exception ex) {
		Object target = jp.getTarget();
		Logger logger = LoggerFactory.getLogger(target.getClass());
		logger.warn("[========== Exception Log ==========]", ex);
		logger.warn("오류 발생 : ", ex);
	}
}
