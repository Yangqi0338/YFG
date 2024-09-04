package com.base.sbc.config.aspect;

import com.base.sbc.config.annotation.DistributedLock;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.utils.SpElParseUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁
 */
@Aspect
@Component
public class DistributedLockAspect {

    @Autowired
    private RedissonClient redissonClient;
    private final String prefix = "DistributedLock:";

    @Around("@annotation(distributedLock)")
    public Object around(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {
        String key = distributedLock.key();
        String lockKey= SpElParseUtil.generateKeyBySpEL(key, joinPoint);
        if (StringUtils.isEmpty(lockKey)) {
            // 如果key为空，尝试从方法签名中获取
            key = joinPoint.getSignature().getName();
        }else {
            key =lockKey;
        }
        RLock lock = redissonClient.getLock(prefix+key);
        try {
            // 尝试获取锁，如果在waitTime时间内未获取到，则放弃
            if (lock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), TimeUnit.SECONDS)) {
                return joinPoint.proceed();
            } else {
                throw new OtherException(distributedLock.errorMsg());
            }
        } finally {
            // 方法执行完毕后释放锁
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}