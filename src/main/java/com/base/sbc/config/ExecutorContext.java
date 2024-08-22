package com.base.sbc.config;

import cn.hutool.core.thread.ExecutorBuilder;
import com.alibaba.ttl.threadpool.TtlExecutors;
import com.base.sbc.config.exception.OtherException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * {@code 描述：通用MapStruct计算转化}
 * @author KC
 * @since 2024/1/9
 * @CopyRight @ 广州尚捷科技有限公司
 */
@Component
@Slf4j
public class ExecutorContext {
    
    public static ThreadPoolTaskExecutor asyncExecutor;
    public static ExecutorService moreLanguageListenerExecutor;
    public static ExecutorService imageExecutor;
    public static ExecutorService baseExecutor;

    @Autowired(required = false)
    @Qualifier("asyncExecutor")
    public void setAsyncExecutor(ThreadPoolTaskExecutor asyncExecutor) {
        ExecutorContext.asyncExecutor = asyncExecutor;
    }

    public static Thread.UncaughtExceptionHandler uncaughtExceptionHandler() {
        return (Thread t, Throwable e) -> {
            if (e != null) {
                log.error(e.getMessage());
                throw new OtherException(e.getMessage());
            }
        };
    }

    public static ThreadFactory threadFactory(String name) {
        return r -> {
            Thread thread = new Thread(r,name);
            thread.setUncaughtExceptionHandler(uncaughtExceptionHandler());
            return thread;
        };
    }

    @PostConstruct
    public void initThread(){
        moreLanguageListenerExecutor = TtlExecutors.getTtlExecutorService(new ThreadPoolExecutor(8, 8,
                0L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(16), threadFactory("多语言导入")));
        imageExecutor = TtlExecutors.getTtlExecutorService(ExecutorBuilder.create()
                .setCorePoolSize(8)
                .setMaxPoolSize(16)
                .setThreadFactory(threadFactory("图片处理"))
                .build());
        baseExecutor = TtlExecutors.getTtlExecutorService(new ThreadPoolExecutor(5, 5,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>() , threadFactory("基础")));
    }

}
