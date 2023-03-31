package com.base.sbc.config.aspect;

import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.utils.UserCompanyUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


/**
 *  在执行所有需要令牌的方法之前，将用户信息放进去，后面直接通过当前线程的threadLocal使用
 * 	 GetCurUserInfoAspect.companyUserInfo.get()
 * 	 即可获取到用户信息
 * @author Youkehai
 * @date 2021/12/16
 */
@Aspect
@Component
public class GetCurUserInfoAspect {

    @Autowired
    private UserCompanyUtils userCompanyUtils;

    /**
     * 当前线程的用户信息
     */
    public static ThreadLocal<UserCompany> companyUserInfo=new ThreadLocal<>();

    /**
     * 前置通知
     */
    @Before(value = "execution(* com.base.sbc.module.*.controller..*.*(..))")
    public void doSaasBefore() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        //当前登录者账号
        String username=authentication.getPrincipal().toString();
        UserCompany companyUser = userCompanyUtils.getCompanyUser();
        if(companyUser==null){
            companyUser=new UserCompany();
        }
        //设置账号信息
        companyUser.setUsername(username);
        companyUserInfo.set(companyUser);
    }

    /**
     * 后置通知
     * @param joinPoint
     * @param keys
     */
    @AfterReturning(value = "execution(* com.base.sbc.module.*.controller..*.*(..))", returning = "keys")
    public void doAfterReturningAdvice1(JoinPoint joinPoint, Object keys) {
        companyUserInfo.remove();
    }
}

