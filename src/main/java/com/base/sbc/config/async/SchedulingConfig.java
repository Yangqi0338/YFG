package com.base.sbc.config.async;

import com.base.sbc.config.redis.RedisKeyConstant;
import com.base.sbc.config.redis.RedisStaticFunUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.IntervalTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@EnableScheduling
@Configuration
public class SchedulingConfig implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        //设置taskScheduler
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(5);
        taskScheduler.setThreadNamePrefix("taskScheduler");
        taskScheduler.setThreadFactory((runnable)-> {
            Thread thread = taskScheduler.newThread(runnable);
            RedisStaticFunUtils.lSet(RedisKeyConstant.JOB_THREAD_ID.build(), thread.getId());
            return thread;
        });
        taskScheduler.initialize();
        taskRegistrar.setTaskScheduler(taskScheduler);

    }
}
