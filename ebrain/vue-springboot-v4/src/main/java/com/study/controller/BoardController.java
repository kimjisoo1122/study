package com.study.controller;

import com.study.api.ResponseDto;
import com.study.api.ResponseStatus;
import com.study.dto.BoardSearchCondition;
import com.study.service.BoardService;
import com.study.service.CategoryService;
import com.study.service.FileService;
import com.study.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardController {

    private final CategoryService categoryService;
    private final BoardService boardService;
    private final FileService fileService;
    private final ReplyService replyService;


    @GetMapping("")
    public ResponseEntity<ResponseDto> boardList(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @ModelAttribute(value = "condition") BoardSearchCondition condition) {

        condition.setPagination(page, pageSize);

        ResponseDto response = new ResponseDto();
        response.setStatus(ResponseStatus.SUCCESS);
        response.setData(
                Map.of("boardList", boardService.findByCondition(condition),
                        "boardCnt", boardService.getTotalSize(condition)));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/cnt")
    public ResponseEntity<ResponseDto> getBoardCnt(
            @ModelAttribute(value = "condition") BoardSearchCondition condition) {

        ResponseDto response = new ResponseDto();
        response.setStatus(ResponseStatus.SUCCESS);
        response.setData(Map.of("boardCnt", boardService.getTotalSize(condition)));

        return ResponseEntity.ok(response);
    }
}
