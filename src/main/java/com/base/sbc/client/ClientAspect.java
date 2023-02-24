package com.base.sbc.client;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 远程请求的切面
 * @author xiong
 *
 */
@Aspect
@Component
public class ClientAspect {
	
	/**
	 * 后置通知
	 * @param joinPoint
	 * @param keys
	 */
	@AfterReturning(value = "execution(* com.base.sbc.client.*.service.*.*(..))", returning = "keys")
	public void doAfterReturningAdvice1(JoinPoint joinPoint, Object keys) {
		System.out.println("后置通知："+keys);
//		JSONObject jsonx = JSON.parseObject((String)keys);
//		if(!jsonx.getBoolean("success")) {//如果失败
//			String data =  "";
//			if(jsonx.getJSONObject("data")!=null) {
//				data = jsonx.getJSONObject("data").toJSONString();
//			}
//			String message = jsonx.getString("message");
//			int code = jsonx.getIntValue("status");
//
////			throw new ClientException(code,message,data);
//		}
	}
}
