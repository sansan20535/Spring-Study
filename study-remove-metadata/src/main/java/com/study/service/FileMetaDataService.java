package com.study.service;

import com.study.util.MetaDataRemover;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
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
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service
@RequiredArgsConstructor
public class FileMetaDataService {

    @Value("${file.storage.path}")
    private String storagePath;

    private final MetaDataRemover metadataRemover;

    @Qualifier("fileProcessExecutor")
    private final Executor fileProcessExecutor;

    public ResponseEntity<Resource> cleanAndDownload(List<MultipartFile> files) throws IOException {

        if (files == null || files.isEmpty()) {
            throw new RuntimeException("업로드된 파일이 없습니다.");
        }

        Path dir = Paths.get(storagePath);
        Files.createDirectories(dir); // 디렉터리 없으면 생성

        List<CompletableFuture<FileProcessResult>> futures = files.stream()
                .map(file -> CompletableFuture.supplyAsync(() -> processOne(file, dir), fileProcessExecutor))
                .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        return null;
    }

    private FileProcessResult processOne(MultipartFile file, Path dir) {
        try {
            if (file == null || file.isEmpty()) {
                return FileProcessResult.fail(null, "파일이 존재하지 않습니다.");
            }

            String originalFileName = file.getOriginalFilename();
            if (originalFileName == null || originalFileName.isBlank()) {
                return FileProcessResult.fail(null, "잘못된 파일 이름이 포함되어 있습니다.");
            }

            String contentType = file.getContentType();
            if (contentType == null || contentType.isBlank()) {
                // 너무 엄격하면 프론트/브라우저마다 튈 수 있어 기본값 처리 추천
                contentType = "application/octet-stream";
            }

            // 메타데이터 제거 (⚠️ 대용량이면 getBytes가 메모리 부담)
            byte[] cleanedBytes = metadataRemover.remove(
                    file.getBytes(),
                    contentType,
                    originalFileName
            );

            // 경로 traversal 방지
            String safeFileName = Paths.get(originalFileName).getFileName().toString();

            // 파일명 충돌 방지(동명이 여러 개면 덮어쓰기 되므로 UUID 권장)
            Path target = dir.resolve(withUuid(safeFileName)).normalize();
            if (!target.startsWith(dir)) {
                return FileProcessResult.fail(originalFileName, "잘못된 파일명(경로 탈출 시도)");
            }

            // 저장
            try (OutputStream os = Files.newOutputStream(target,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING)) {
                os.write(cleanedBytes);
            }

            return FileProcessResult.ok(originalFileName, target.toString());

        } catch (Exception e) {
            return FileProcessResult.fail(file != null ? file.getOriginalFilename() : null, e.getMessage());
        }
    }

    private String withUuid(String safeFileName) {
        int dot = safeFileName.lastIndexOf('.');
        String base = dot > 0 ? safeFileName.substring(0, dot) : safeFileName;
        String ext = dot > 0 ? safeFileName.substring(dot) : "";
        String id = UUID.randomUUID().toString().replace("-", "");
        return base + "_" + id + ext;
    }

    public record FileProcessResult(boolean success, String originalName, String savedPath, String error) {
        public static FileProcessResult ok(String originalName, String savedPath) {
            return new FileProcessResult(true, originalName, savedPath, null);
        }
        public static FileProcessResult fail(String originalName, String error) {
            return new FileProcessResult(false, originalName, null, error);
        }
    }
}

