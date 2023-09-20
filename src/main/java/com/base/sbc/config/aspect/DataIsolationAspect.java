package com.base.sbc.config.aspect;


import com.base.sbc.client.amc.service.DataPermissionsService;
import com.base.sbc.config.common.annotation.DataIsolation;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.redis.RedisUtils;
import com.base.sbc.config.utils.SpringContextHolder;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author lxf
 * @date 2023/9/19 9:53:36
 * @mail 123456789@qq.com
 * 数据隔离切面
 */
@Aspect
@Component
@RequiredArgsConstructor
public class DataIsolationAspect {
    @Resource
    private HttpServletRequest httpServletRequest;
    @Pointcut("@annotation(com.base.sbc.config.common.annotation.DataIsolation)")
    public void servicePointcut() {
        System.out.println("Pointcut: 不会被执行");
    }
    @Before(value = "servicePointcut()")
    public void checkPermission(JoinPoint jointPoint) throws Exception {
        // 获取当前访问的class类及类名
        Class<?> clazz = jointPoint.getTarget().getClass();
//        String clazzName = jointPoint.getTarget().getClass().getName();
        // 获取访问的方法名
        String methodName = jointPoint.getSignature().getName();
        // 获取方法所有参数及其类型
//        Object[] args = jointPoint.getArgs();
        Class[] argClz = ((MethodSignature) jointPoint.getSignature()).getParameterTypes();
        // 获取访问的方法对象
        Method method = clazz.getDeclaredMethod(methodName, argClz);

        // 判断当前访问的方法是否存在指定注解
        if (method.isAnnotationPresent(DataIsolation.class)) {
            String userId = httpServletRequest.getHeader("userId");
            String usercompany = httpServletRequest.getHeader("Usercompany");
            String authorization = httpServletRequest.getHeader("Authorization");
            DataIsolation dataIsolation = method.getAnnotation(DataIsolation.class);
            if(!StringUtils.isBlank(usercompany) && !StringUtils.isBlank(userId) && !StringUtils.isBlank(authorization) && dataIsolation.state() && StringUtils.isNotBlank(dataIsolation.authority())){
                // 获取注解标识值与注解描述
                String operateType = dataIsolation.operateType()?"read":"write";
                RedisUtils redisUtils = SpringContextHolder.getBean("redisUtils");
                String dataPermissionsKey = "USERISOLATION:"+usercompany+":"+userId+":";

                Map<String,Object> entity=null;
                Boolean authorityState=false;
                DataPermissionsService dataPermissionsService = SpringContextHolder.getBean("dataPermissionsService");
                entity= dataPermissionsService.getDataPermissionsForQw(dataIsolation.authority(),operateType,null,dataIsolation.authorityFields(),dataIsolation.isAssignFields(),dataPermissionsKey);
                authorityState=entity.containsKey("authorityState")?(Boolean)entity.get("authorityState"):false;
                if(!authorityState){
                    redisUtils.set(dataPermissionsKey +operateType+"@" + dataIsolation.authority()+":authorityState", authorityState, 60 * 3);//如数据的隔离3分钟更新一次
                    throw new OtherException("无权限");
                }
            }
        }
    }
}
