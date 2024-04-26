package com.base.sbc.config.async;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

@EnableAsync
@Configuration
public class AsyncConfig extends AsyncConfigurerSupport {

    public class ContextAwareCallable<T> implements Callable<T> {
        private Callable<T> task;
        private RequestAttributes context;

        public ContextAwareCallable(Callable<T> task, RequestAttributes context) {
            this.task = task;
            this.context = context;
        }

        @Override
        public T call() throws Exception {
            if (context != null) {
                RequestContextHolder.setRequestAttributes(context);
            }
            try {
                return task.call();
            } finally {
                RequestContextHolder.resetRequestAttributes();
            }
        }
    }

    public class ContextAwarePoolExecutor extends ThreadPoolTaskExecutor {
        @Override
        public <T> Future<T> submit(Callable<T> task) {
            return super.submit(new ContextAwareCallable(task, RequestContextHolder.currentRequestAttributes()));
        }

        @Override
        public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
            return super.submitListenable(new ContextAwareCallable(task, RequestContextHolder.currentRequestAttributes()));
        }
    }


    @Override
    @Bean("asyncExecutor")
    public Executor getAsyncExecutor() {
        return new ContextAwarePoolExecutor();
    }


}

