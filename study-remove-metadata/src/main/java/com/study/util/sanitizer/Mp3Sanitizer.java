package com.study.util.sanitizer;

import org.jaudiotagger.audio.AudioFileIO;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class Mp3Sanitizer {

    public byte[] sanitize(byte[] input) {
        Path temp = null;

        try {
            temp = Files.createTempFile("mp3-", ".mp3");
            Files.write(temp, input);

            var audioFile = AudioFileIO.read(temp.toFile());

            // ID3v1 / ID3v2 / 커버아트 전부 삭제
            audioFile.setTag(null);
            audioFile.commit();

            return Files.readAllBytes(temp);

        } catch (Exception e) {
            throw new RuntimeException("MP3 메타데이터 제거 실패", e);
        } finally {
            if (temp != null) {
                try {
                    Files.deleteIfExists(temp);
                } catch (IOException ignored) {
                }
            }
        }
    }
}
