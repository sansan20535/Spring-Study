package com.study.util;

import com.study.util.sanitizer.ImageSanitizer;
import com.study.util.sanitizer.Mp3Sanitizer;
import com.study.util.sanitizer.VideoSanitizer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class MetaDataRemover {

    private final ImageSanitizer imageSanitizer;
    private final VideoSanitizer videoSanitizer;
    private final Mp3Sanitizer mp3Sanitizer;

    public byte[] remove(byte[] input, String contentType, String originalName) {
        String name = originalName.toLowerCase(Locale.ROOT);

        // 이미지
        if (name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png") || name.endsWith(".webp") || name.endsWith(".dng")
                || (contentType != null && contentType.startsWith("image/"))) {
            return imageSanitizer.sanitize(input, name);
        }

        if (name.endsWith(".mp4") || name.endsWith(".mov")
                || "video/mp4".equalsIgnoreCase(contentType)
                || "video/quicktime".equalsIgnoreCase(contentType)
                || (contentType != null && contentType.startsWith("video/"))) {
            return videoSanitizer.sanitize(input, name);
        }

        if (name.endsWith(".mp3")
                || "audio/mpeg".equalsIgnoreCase(contentType)
                || (contentType != null && contentType.startsWith("audio/"))) {
            return mp3Sanitizer.sanitize(input);
        }

        throw new IllegalArgumentException("지원하지 않는 파일 형식입니다: " + originalName);

    }
}
