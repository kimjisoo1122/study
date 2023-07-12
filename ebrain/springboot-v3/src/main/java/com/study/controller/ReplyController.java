package com.study.controller;

import com.study.dto.ReplyDto;
import com.study.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 댓글 관련 컨트롤러 입니다.
 */
@Slf4j
@RestController()
@RequestMapping("/reply")
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    /**
     * 댓글을 등록합니다
     * @param replyDto 댓글 등록 Dto
     * @return ReplyDto 등록된 댓글 Dto
     */
    @PostMapping("/register")
    public ReplyDto register(
            @RequestBody ReplyDto replyDto) {
        ReplyDto registeredReply = replyService.register(replyDto);
        registeredReply.setFormattedCreateDate(registeredReply.getFormattedCreateDate());
        return registeredReply;
    }
}
