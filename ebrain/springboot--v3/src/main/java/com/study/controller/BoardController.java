package com.study.controller;

import com.study.dto.BoardDto;
import com.study.dto.BoardSearchCondition;
import com.study.dto.CategoryDto;
import com.study.page.PageHandler;
import com.study.service.BoardService;
import com.study.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

/**
 * 게시글 관련 컨트롤러 입니다.
 */
@Controller
//@RequestMapping("/board/*")
@RequiredArgsConstructor
public class BoardController {

    private final CategoryService categoryService;
    private final BoardService boardService;

    @GetMapping("/board")
    public String list(
            @ModelAttribute(value = "condition") BoardSearchCondition condition,
            Model model) {

        Map<String, List<CategoryDto>> categories = categoryService.findAll();
        List<BoardDto> boards = boardService.findByCondition(condition);
        int totalCnt = boardService.countByCondition(condition);

        model.addAttribute("categories", categories);
        model.addAttribute("boards", boards);
        model.addAttribute("pageHandler", new PageHandler(condition.getPage(), totalCnt));

        return "board/boardList";
    }
}
