package com.study.controller;

import com.study.api.ResponseDto;
import com.study.api.ResponseApiStatus;
import com.study.dto.ReplyDto;
import com.study.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 댓글을 처리하는 API 컨트롤러 입니다.
 */
@RestController
@RequestMapping("/api/reply")
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    /**
     * 댓글을 등록합니다.
     * @param replyDto boardId 게시글번호, replyContent 댓글내용
     * @return reply 등록된 댓글
     */
    @PostMapping
    public ResponseEntity<ResponseDto> register(
            @RequestBody ReplyDto replyDto) {

        ResponseDto response = new ResponseDto();
        response.setStatus(ResponseApiStatus.SUCCESS);
        response.setData(Map.of("reply", replyService.register(replyDto)));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
