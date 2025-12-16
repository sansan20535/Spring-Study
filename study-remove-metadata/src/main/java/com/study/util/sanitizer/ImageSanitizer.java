package com.study.util.sanitizer;

import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Component
public class ImageSanitizer {
    public byte[] sanitize(byte[] input, String lowerName) {
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
