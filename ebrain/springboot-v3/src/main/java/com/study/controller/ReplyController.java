package com.study.controller;

import com.study.dto.ReplyDto;
import com.study.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 댓글 관련 컨트롤러 입니다.
 */
@RestController()
@RequestMapping("/reply")
@RequiredArgsConstructor
@Slf4j
public class ReplyController {

    private final ReplyService replyService;

    /**
     * 댓글을 등록한 후 등록된 댓글을 반환합니다.
     * @param replyDto 댓글 등록 Dto
     * @return ReplyDto 등록된 댓글 Dto
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody ReplyDto replyDto) {
        try {
            ReplyDto registeredReply = replyService.register(replyDto);
            return new ResponseEntity<>(registeredReply, HttpStatus.OK);
        } catch (Exception e) {
            String errMsg = "댓글 등록에 실패하였습니다";
            log.error(errMsg  + "...... {} replyDto = {}", e.getMessage(), replyDto);
            return new ResponseEntity<>(errMsg, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
