package com.study.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.imageio.ImageIO;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class FileAsyncConfig {

    @Bean("fileProcessExecutor")
    public Executor fileProcessExecutor() {
        ThreadPoolTaskExecutor ex = new ThreadPoolTaskExecutor();
        ex.setCorePoolSize(8);     // 비디오(ffmpeg) 있으면 2~3 권장
        ex.setMaxPoolSize(16);
        ex.setQueueCapacity(1000);
        ex.setThreadNamePrefix("file-process-");

        // 중요: 큐가 꽉 찼을 때 정책
        // CallerRunsPolicy: 요청 스레드가 직접 처리 -> "거절" 대신 "속도제한(백프레셔)"
        ex.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        ex.initialize();
        return ex;
    }

    @PostConstruct
    public void init() {
        ImageIO.scanForPlugins();
    }
}
