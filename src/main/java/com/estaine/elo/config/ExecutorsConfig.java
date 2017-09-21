package com.estaine.elo.config;

import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;


@Configuration
@EnableScheduling
@EnableAsync
public class ExecutorsConfig implements SchedulingConfigurer, AsyncConfigurer {

    private static final int SCHEDULER_POOL_SIZE = 5;
    private static final int ASYNC_POOL_SIZE = 10;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(SCHEDULER_POOL_SIZE);
        taskScheduler.setThreadNamePrefix("ScheduledExecutor-");
        taskScheduler.initialize();

        taskRegistrar.setTaskScheduler(taskScheduler);
    }

    @Bean(name = "asyncExecutor")
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(ASYNC_POOL_SIZE);
        executor.setThreadNamePrefix("AsyncExecutor-");
        executor.initialize();

        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, objects) -> LoggerFactory.getLogger("Async").error("Async error", throwable); // ?
    }

}
