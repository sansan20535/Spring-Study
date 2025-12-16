package com.study.service;

import com.study.util.MetaDataRemover;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileMetaDataService {

    @Value("${file.storage.path}")
    private String storagePath;

    private final MetaDataRemover metadataRemover;

    public ResponseEntity<Resource> cleanAndDownload(List<MultipartFile> files) throws IOException {

        if (files == null || files.isEmpty()) {
            throw new RuntimeException("업로드된 파일이 없습니다.");
        }

        Path dir = Paths.get(storagePath);
        Files.createDirectories(dir); // 디렉터리 없으면 생성

        for (MultipartFile file : files) {
            if (file == null || file.isEmpty()) {
                // 필요하면 여기서 continue로 스킵하도록 바꿀 수도 있음
                throw new RuntimeException("파일이 존재하지 않습니다.");
            }

            String originalFileName = file.getOriginalFilename();
            if (originalFileName == null || originalFileName.isBlank()) {
                throw new RuntimeException("잘못된 파일 이름이 포함되어 있습니다. 파일 이름을 다시 한 번 확인해주세요.");
            }

            String contentType = file.getContentType();
            if (contentType == null || contentType.isBlank()) {
                throw new RuntimeException("잘못된 파일 확장자가 포함되어 있습니다. 파일 확장자를 다시 한 번 확인해주세요.");
            }

            // 메타데이터 제거
            byte[] cleanedBytes = metadataRemover.remove(
                    file.getBytes(),
                    contentType,
                    originalFileName
            );

            // 경로 traversal 방지용으로 파일명만 추출
            String safeFileName = Paths.get(originalFileName).getFileName().toString();
            Path target = dir.resolve(safeFileName);

            // 스트림으로 파일 저장
            try (OutputStream os = Files.newOutputStream(target)) {
                os.write(cleanedBytes);
            }
        }

        return null;
    }
}

