package com.study.controller;

import com.study.dto.FileDto;
import com.study.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriUtils;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;

/**
 * 파일을 처리하는 API 컨트롤러 입니다.
 */
@Controller
@RequestMapping("/api/file")
@RequiredArgsConstructor
@Slf4j
public class FileController {

    private final FileService fileService;

    /**
     * 파일다운로드를 처리합니다.
     * @param fileId 파일번호
     * @return
     * @throws MalformedURLException 파일경로가 올바르지 않은경우
     * @throws FileNotFoundException 파일을 찾지 못했을 경우
     */
    @GetMapping(value = "/{fileId}", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<Resource> downloadFile(
            @PathVariable("fileId") Long fileId) throws MalformedURLException, FileNotFoundException {

        FileDto findFile = fileService.findByFileId(fileId);
        UrlResource resource = new UrlResource("file:" + findFile.getFullPath());

        if (!resource.exists() || !resource.isReadable()) {
            log.error("파일을 찾을수 없습니다. 경로 : {}", findFile.getFullPath());

            throw new FileNotFoundException("파일을 찾을 수 없습니다.");
        }

        String encodedOriginalFileName = UriUtils.encode(findFile.getOriginalName(), StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedOriginalFileName + "\"";

        log.info("다운로드파일 : {}", findFile.getOriginalName());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }
}
