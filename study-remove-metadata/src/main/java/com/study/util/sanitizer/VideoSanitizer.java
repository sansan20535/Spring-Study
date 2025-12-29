package com.study.util.sanitizer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Component
@RequiredArgsConstructor
public class VideoSanitizer {

    private final FfmpegRunner ffmpeg;

    public byte[] sanitize(byte[] input, String lowerName) {
        String ext = lowerName.endsWith(".mov") ? ".mov" : ".mp4";

        Path inFile = null;
        Path outFile = null;

        try {
            inFile = Files.createTempFile("in-", ext);
            outFile = Files.createTempFile("out-", ext);

            Files.write(inFile, input);

            ffmpeg.run(List.of(
                    "-y",
                    "-i", inFile.toString(),
                    "-map", "0",
                    "-c", "copy",
                    "-map_metadata", "-1",
                    "-map_chapters", "-1",
                    outFile.toString()
            ));

            return Files.readAllBytes(outFile);

        } catch (Exception e) {
            throw new RuntimeException("비디오 메타데이터 제거 실패: " + lowerName, e);
        } finally {
            if (inFile != null) try { Files.deleteIfExists(inFile); } catch (IOException ignored) {}
            if (outFile != null) try { Files.deleteIfExists(outFile); } catch (IOException ignored) {}
        }
    }
}
