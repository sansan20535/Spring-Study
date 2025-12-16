package com.study.util.sanitizer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ImageSanitizer {

    private final FfmpegRunner ffmpegRunner;

    public byte[] sanitize(byte[] input, String lowerName) {

        if (lowerName.endsWith(".webp")) {
            return sanitizeWebpWithFfmpeg(input);
        }

        String format = detectFormat(lowerName);

        try (ByteArrayInputStream in = new ByteArrayInputStream(input)) {
            BufferedImage image = ImageIO.read(in);
            if (image == null) throw new IllegalArgumentException("이미지 디코딩 실패: " + lowerName);

            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                boolean ok = ImageIO.write(image, format, out);
                if (!ok) throw new IllegalStateException("이미지 인코딩 실패(format=" + format + ")");
                return out.toByteArray();
            }
        } catch (IOException e) {
            throw new RuntimeException("이미지 메타데이터 제거 처리 오류", e);
        }
    }

    private byte[] sanitizeWebpWithFfmpeg(byte[] input) {
        Path inFile = null;
        Path outFile = null;

        try {
            inFile = Files.createTempFile("webp-in-", ".webp");
            outFile = Files.createTempFile("webp-out-", ".webp");
            Files.write(inFile, input);

            ffmpegRunner.run(List.of(
                    "-y",
                    "-i", inFile.toString(),
                    "-map_metadata", "-1",
                    outFile.toString()
            ));

            return Files.readAllBytes(outFile);
        } catch (Exception e) {
            throw new RuntimeException("WEBP 메타데이터 제거 실패", e);
        } finally {
            if (inFile != null) try {
                Files.deleteIfExists(inFile);
            } catch (IOException ignored) {
            }
            if (outFile != null) try {
                Files.deleteIfExists(outFile);
            } catch (IOException ignored) {
            }
        }
    }


    private String detectFormat(String lowerName) {
        if (lowerName.endsWith(".png")) {
            return "png";
        }
        if (lowerName.endsWith(".webp")) {
            return "webp";
        }
        if (lowerName.endsWith(".jpg")) {
            return "jpg";
        }
        if (lowerName.endsWith(".jpeg")) {
            return "jpeg";
        }
        throw new RuntimeException("잘못된 파일 확장자입니다.");
    }
}
