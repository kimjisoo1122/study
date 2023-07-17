package com.study.controller;

import com.study.dto.BoardSearchCondition;
import com.study.page.PageHandler;
import com.study.service.BoardService;
import com.study.service.CategoryService;
import com.study.service.FileService;
import com.study.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final CategoryService categoryService;
    private final BoardService boardService;
    private final FileService fileService;
    private final ReplyService replyService;


    @GetMapping
    public ResponseEntity<Map<String, Object>> boardList(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @ModelAttribute(value = "condition") BoardSearchCondition condition) {
        HashMap<String, Object> response = new HashMap<>();

        condition.setPagination(page, pageSize);

        response.put("categories", categoryService.findAll());
        response.put("boards", boardService.findByCondition(condition));
        response.put("pageHandler",
                new PageHandler(condition.getPage(), boardService.getTotalSize(condition)));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
