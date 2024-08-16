package com.base.sbc.config.async;

import com.alibaba.ttl.threadpool.TtlExecutors;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@EnableScheduling
@Configuration
public class SchedulingConfig implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        //设置taskScheduler
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(5);
        taskScheduler.setThreadNamePrefix("pdmTaskScheduler");
        taskScheduler.initialize();
        taskRegistrar.setScheduler(TtlExecutors.getTtlScheduledExecutorService(taskScheduler.getScheduledExecutor()));
    }
}
