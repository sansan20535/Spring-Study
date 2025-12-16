package com.study.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class FileAsyncConfig {

    @Bean("fileProcessExecutor")
    public Executor fileProcessExecutor() {
        ThreadPoolTaskExecutor ex = new ThreadPoolTaskExecutor();
        ex.setCorePoolSize(3);     // 비디오(ffmpeg) 있으면 2~3 권장
        ex.setMaxPoolSize(3);
        ex.setQueueCapacity(100);
        ex.setThreadNamePrefix("file-process-");
        ex.initialize();
        return ex;
    }
}
