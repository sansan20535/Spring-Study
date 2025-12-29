package com.study.util.sanitizer;

import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class FfmpegRunner {

    // 실제 배포 환경이면 배포 환경에 맞게 조절 필요
    private final String ffmpegPath = "/opt/homebrew/bin/ffmpeg";

    public void run(List<String> args) {
        List<String> command = new ArrayList<>();
        command.add(ffmpegPath);
        command.addAll(args);

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);

        try {
            Process p = pb.start();

            String output;
            try (InputStream is = p.getInputStream()) {
                output = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            }

            int exitCode = p.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("ffmpeg 실패 (code=" + exitCode + ")\n" + output);
            }

        } catch (Exception e) {
            throw new RuntimeException("ffmpeg 실행 실패", e);
        }
    }
}
