package com.base.sbc.config.aspect;

import com.base.sbc.config.annotation.DuplicationCheck;
import com.base.sbc.config.exception.OtherException;

import com.base.sbc.config.redis.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Arrays;

/**
 * 重复提交校验切面
 * @author 卞康
 * @date 2023/10/26 11:15:11
 * @mail 247967116@qq.com
 */
@Aspect
@Component
@RequiredArgsConstructor
public class DuplicationCheckAspect {

    private final RedisUtils redisUtils;

    /**
     * 重复提交校验切面
     * @param joinPoint 切点
     * @return 返回结果
     * @throws Throwable 异常
     */
    @Around("@annotation(com.base.sbc.config.annotation.DuplicationCheck)")
    public Object logMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        DuplicationCheck duplicationCheck = getDuplicationCheck(joinPoint);
        try {
            if (duplicationCheck.value()) {
                String key = generateKey(joinPoint,duplicationCheck);
                if (redisUtils.hasKey(key)) {
                    throw new OtherException(duplicationCheck.message());
                }
                redisUtils.set(key, key, duplicationCheck.time());
            }
            return joinPoint.proceed();
        } finally {
            redisUtils.del(generateKey(joinPoint,duplicationCheck));
        }
    }

    /**
     * 生成key
     *
     * @param joinPoint        切点
     * @param duplicationCheck 重复提交校验注解
     * @return key
     */
    private String generateKey(ProceedingJoinPoint joinPoint, DuplicationCheck duplicationCheck) {
        StringBuilder sb = new StringBuilder();
        sb.append(joinPoint.getTarget().getClass().getName());
        sb.append(joinPoint.getSignature().getName());

        //是否只校验请求地址
        if (duplicationCheck.type()==2 || duplicationCheck.type()==4) {
            Object[] args = joinPoint.getArgs();
            for (Object arg : args) {
                sb.append(arg.toString());
            }
        }

        return sb.toString();
    }

    /**
     * 获取方法上的注解
     * @param joinPoint  切点
     * @return 重复提交校验注解
     */
    private DuplicationCheck getDuplicationCheck(ProceedingJoinPoint joinPoint) {
Signature signature = joinPoint.getSignature();
        Annotation[] annotations = ((MethodSignature) signature).getMethod().getAnnotations();
        return Arrays.stream(annotations).filter(annotation -> annotation instanceof DuplicationCheck).map(annotation -> (DuplicationCheck) annotation).findFirst().orElse(null);

    }
}
