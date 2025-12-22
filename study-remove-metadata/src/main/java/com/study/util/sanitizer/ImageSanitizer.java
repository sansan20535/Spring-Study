package com.study.util.sanitizer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ImageSanitizer {

    private final FfmpegRunner ffmpegRunner;

    public byte[] sanitize(byte[] input, String lowerName) {

        if (lowerName.endsWith(".webp")) {
            return sanitizeWebpWithFfmpeg(input);
        }

        String format = detectFormat(lowerName);

        try (ByteArrayInputStream in = new ByteArrayInputStream(input)) {
            BufferedImage image = ImageIO.read(in);
            if (image == null) {
                logJpegSanity(lowerName, input); // 진단
                throw new IllegalArgumentException("이미지 디코딩 실패: " + lowerName);
            }

            BufferedImage normalized = toRgb(image);

            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                boolean ok = ImageIO.write(image, format, out);
                if (!ok) {
                    throw new IllegalStateException("이미지 인코딩 실패(format=" + format + ")");
                }
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
        if (lowerName.endsWith(".dng")) {
            return "dng";
        }
        throw new RuntimeException("잘못된 파일 확장자입니다.");
    }

    private static String hex(byte[] b, int off, int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = off; i < off + len && i < b.length; i++) sb.append(String.format("%02x", b[i]));
        return sb.toString();
    }

    private static void logJpegSanity(String name, byte[] input) {
        if (input == null || input.length < 4) {
            log.warn("decode fail sanity: {} len={}", name, input == null ? null : input.length);
            return;
        }
        String head = hex(input, 0, 4);
        String tail = hex(input, Math.max(0, input.length - 2), 2);
        log.warn("decode fail sanity: {} len={} head={} tail={}", name, input.length, head, tail);
    }

    private static BufferedImage toRgb(BufferedImage src) {
        BufferedImage rgb = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = rgb.createGraphics();
        g.drawImage(src, 0, 0, null);
        g.dispose();
        return rgb;
    }
}
