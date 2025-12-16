package com.study.controller;

import com.study.service.FileMetaDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/metadata")
@RequiredArgsConstructor
public class FileMetaDataController {

    private final FileMetaDataService fileMetaDataService;

    @PostMapping("/clean-and-download")
    public ResponseEntity<Resource> cleanAndDownload(@RequestParam("files") List<MultipartFile> files) throws IOException {
        return fileMetaDataService.cleanAndDownload(files);
    }
}
