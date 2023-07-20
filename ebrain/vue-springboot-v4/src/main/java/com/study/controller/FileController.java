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
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriUtils;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;

/**
 * 파일을 처리하는 API 컨트롤러 입니다.
 */
@Controller
@RequestMapping("/file")
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
    @GetMapping("/{fileId}")
    public ResponseEntity<Resource> fileDown(
            @PathVariable("fileId") Long fileId) throws MalformedURLException, FileNotFoundException {

        FileDto findFile = fileService.findById(fileId);
        UrlResource resource = new UrlResource("file:" + findFile.getFullPath());

        if (!resource.exists() || !resource.isReadable()) {
            throw new FileNotFoundException("download file not found ......" + findFile.getFullPath());
        }

        String encodedOriginalFileName = UriUtils.encode(findFile.getOriginalName(), StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedOriginalFileName + "\"";

        log.info("file download ...... fileName = {}", findFile.getOriginalName());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }

    //TODO 예외처리
    @ExceptionHandler(FileNotFoundException.class)
    public ModelAndView fileNotFoundHandler(FileNotFoundException e) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("errorPage");
        return mv;
    }
}
