package com.study.controller;

import com.study.dto.*;
import com.study.page.PageHandler;
import com.study.service.BoardService;
import com.study.service.CategoryService;
import com.study.service.FileService;
import com.study.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * 게시글 관련 컨트롤러 입니다.
 */
@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final CategoryService categoryService;
    private final BoardService boardService;
    private final FileService fileService;
    private final ReplyService replyService;

    /**
     * 게시글을 조회합니다.
     * @param condition 검색조건
     * @param page 현재페이지 (default : 1)
     * @param pageSize 페이지크기 (default : 10)
     * @param model
     * @return board/boardList
     */
    @GetMapping()
    public String boardList(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @ModelAttribute(value = "condition") BoardSearchCondition condition,
            Model model) {
        setPagination(page, pageSize, condition);

        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("boards", boardService.findByCondition(condition));
        model.addAttribute("pageHandler",
                new PageHandler(condition.getPage(), boardService.getTotalSize(condition)));

        return "board/boardList";
    }

    /**
     * 게시글번호로 게시글을 조회합니다.
     * @param boardId 게시글번호
     * @param condition 검색조건
     * @param model
     * @return board/board
     */
    @GetMapping("/{boardId}")
    public String board(
            @PathVariable("boardId") Long boardId,
            @ModelAttribute("condition") BoardSearchCondition condition,
            Model model) {

        boardService.increaseViewCnt(boardId);

        model.addAttribute("files", fileService.findByBoardId(boardId));
        model.addAttribute("board", boardService.findById(boardId));
        model.addAttribute("replies", replyService.findByBoardId(boardId));

        return "board/board";
    }

    /**
     * 게시글 등록폼으로 이동합니다
     * @param board 게시글 DTO
     * @param condition 검색조건
     * @param model
     * @return board/register
     */
    @GetMapping("/register")
    private String registerForm(
            @ModelAttribute("board") BoardDto board,
            @ModelAttribute("condition") BoardSearchCondition condition,
            Model model) {

        model.addAttribute("categories", categoryService.findAll());

        return "board/register";
    }

    /**
     * 게시글을 등록합니다
     * @param board 게시글 DTO
     * @param bindingResult 게시글 Validation
     * @param condition 검색조건
     * @param model
     * @return redirect:/board
     */
    @PostMapping("/register")
    private String register(
            @Validated @ModelAttribute("board") BoardDto board,
            BindingResult bindingResult,
            @ModelAttribute("condition") BoardSearchCondition condition,
            RedirectAttributes redirectAttributes,
            Model model) {

        model.addAttribute("categories", categoryService.findAll());

        if (bindingResult.hasErrors()) {
            return "board/register";
        }



        setRedirectFromCondition(condition, redirectAttributes);
        return "redirect:/board";
    }

    private void setRedirectFromCondition(BoardSearchCondition condition, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("fromDate", condition.getFromDate());
        redirectAttributes.addAttribute("toDate", condition.getToDate());
        redirectAttributes.addAttribute("search", condition.getSearch());
        redirectAttributes.addAttribute("searchCategory", condition.getSearchCategory());
    }


    /**
     * 검색조건 페이징처리를 위한 condition객체의 값을 설정합니다.
     * @param page
     * @param pageSize
     * @param condition
     */
    private void setPagination(int page, int pageSize, BoardSearchCondition condition) {
        condition.setPage(page);
        condition.setPageSize(pageSize);
        condition.setOffset((page - 1) * pageSize);
        condition.setLimit(pageSize);
    }
}
