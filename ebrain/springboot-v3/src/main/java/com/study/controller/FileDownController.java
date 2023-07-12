package com.study.controller;

import com.study.dto.FileDto;
import com.study.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriUtils;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;

/**
 * 파일다운로드를 처리하는 컨트롤러 입니다.
 */
@Controller
@RequestMapping("/fileDown")
@RequiredArgsConstructor
@Slf4j
public class FileDownController {

    private final FileService fileService;

    @GetMapping("/{fileId}")
    public ResponseEntity<Resource> fileDown(
            @PathVariable("fileId") Long fileId) throws MalformedURLException, FileNotFoundException {

        FileDto findFile = fileService.findById(fileId);
        String fullPath = findFile.getPath() + findFile.getPhysicalName();
        UrlResource resource = new UrlResource("file:" + fullPath);

        if (!resource.exists() || !resource.isReadable()) {
            throw new FileNotFoundException("download file not found ......" + fullPath);
        }

        String encodedOriginalFileName = UriUtils.encode(findFile.getOriginalName(), StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedOriginalFileName + "\"";

        log.info("file download ...... fileName = {}", findFile.getOriginalName());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }
}
