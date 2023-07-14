package com.study.controller;

import com.study.dto.BoardRegisterForm;
import com.study.dto.BoardSearchCondition;
import com.study.dto.BoardUpdateForm;
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
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 게시글 관련 컨트롤러 입니다.
 */
@Controller()
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final CategoryService categoryService;
    private final BoardService boardService;
    private final FileService fileService;
    private final ReplyService replyService;

    /**
     * 게시글목록을 조회합니다.
     * @param condition 검색조건
     * @param page 현재페이지 (default : 1)
     * @param pageSize 페이지크기 (default : 10)
     * @param model categories (카테고리), boards (게시글목록), pageHandler (페이징처리)
     * @return View : board/boardList
     */
    @GetMapping()
    public String boardList(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @ModelAttribute(value = "condition") BoardSearchCondition condition,
            Model model) {

        // 게시글검색조건의 페이징설정
        condition.setPagination(page, pageSize);

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
     * @param model files (파일정보), board (게시글), replies (댓글)
     * @return View : board/board
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
     * @param form 게시글 등록 Form
     * @param condition 검색조건
     * @param model categories (카테고리)
     * @return View : board/register
     */
    @GetMapping("/register")
    private String registerForm(
            @ModelAttribute("form") BoardRegisterForm form,
            @ModelAttribute("condition") BoardSearchCondition condition,
            Model model) {

        model.addAttribute("categories", categoryService.findAll());

        return "board/register";
    }

    /**
     * 게시글을 등록 합니다.
     * @param form 게시글 등록 Form
     * @param bindingResult Form 유효성검증 객체
     * @param condition 검색조건
     * @return 성공 : redirect:/board (게시글 목록), 실패 :  board/register (등록폼)
     */
    @PostMapping("/register")
    private String register(
            @Validated @ModelAttribute("form") BoardRegisterForm form,
            BindingResult bindingResult,
            @ModelAttribute("condition") BoardSearchCondition condition,
            Model model) throws IOException {

        if (!form.validConfirmPassword()) {
            bindingResult.rejectValue("confirmPassword", null,
                    "비밀번호가 서로 맞지 않습니다.");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            return "board/register";
        }

        boardService.register(form);

        return "redirect:/board" + getEncodedQueryParam(condition);
    }

    /**
     * 게시글의 업데이트 폼을 조회합니다.
     * @param boardId 게시글 번호
     * @param condition 검색조건
     * @param model form (업데이트 폼), files (파일), categories (카테고리)
     * @return View : board/update
     */
    @GetMapping("/update/{boardId}")
    public String updateForm(
            @PathVariable("boardId") Long boardId,
            @ModelAttribute("condition") BoardSearchCondition condition,
            Model model) {

        model.addAttribute("form", boardService.findById(boardId));
        model.addAttribute("files", fileService.findByBoardId(boardId));
        model.addAttribute("categories", categoryService.findAll());

        return "board/update";
    }

    /**
     * 게시글을 업데이트 합니다.
     * @param boardId 게시글 번호
     * @param condition 검색 조건
     * @param form 업데이트 폼
     * @param bindingResult 업데이트 유효성 검증
     * @param model files (파일), categories (카테고리)
     * @return 성공 : redirect:/board/{boardId} (게시글 정보), 실패 : board/update (업데이트폼)
     * @throws IOException 게시글 첨부파일 추가하거나 삭제 할 경우 예외발생
     */
    @PostMapping("/update/{boardId}")
    public String update(
            @PathVariable("boardId") Long boardId,
            @ModelAttribute("condition") BoardSearchCondition condition,
            @Validated @ModelAttribute("form") BoardUpdateForm form,
            BindingResult bindingResult,
            Model model) throws IOException {

        if (!boardService.isPasswordMatch(boardId, form.getPassword())) {
            bindingResult.rejectValue("password", null,
                    "비밀번호가 맞지 않습니다");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("files", fileService.findByBoardId(boardId));
            model.addAttribute("categories", categoryService.findAll());
            return "board/update";
        }

        boardService.update(form);

        return "redirect:/board/" + boardId + getEncodedQueryParam(condition);
    }

    /**
     * 게시글을 삭제 합니다.
     * @param boardId 게시글 번호
     * @param removePassword 게시글 비밀번호
     * @param condition 검색조건
     * @param redirectAttributes 삭제 메시지 전달
     * @return 성공 : redirect:/board (게시글 목록) 실패 : redirect:/board/{boardId} (게시글 정보)
     */
    @PostMapping("/delete/{boardId}")
    public String delete(
            @PathVariable("boardId") Long boardId,
            @RequestParam("removePassword") String removePassword,
            @ModelAttribute("condition") BoardSearchCondition condition,
            RedirectAttributes redirectAttributes) {

        try {
            if (!boardService.isPasswordMatch(boardId, removePassword)) {
                throw new IllegalArgumentException();
            }

            boardService.delete(boardId);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("deleteMsg",
                    "게시글 삭제에 실패하였습니다.");

            return "redirect:/board/" + boardId + getEncodedQueryParam(condition);
        }

        redirectAttributes.addFlashAttribute("deleteMsg",
                "게시글 삭제에 성공하였습니다.");

        return "redirect:/board" + getEncodedQueryParam(condition);
    }


    /**
     * 서버 내부에서 리다이렉트 하는 경우
     * 쿼리스트링에 포함된 한글을 인코딩후 쿼리스트링을 반환합니다.
     * @param condition 검색조건
     * @return 인코딩된 검색조건
     */
    private String getEncodedQueryParam(BoardSearchCondition condition) {
        condition.setSearch(UriUtils.encode(condition.getSearch(), StandardCharsets.UTF_8));
        return condition.getQueryParamString();
    }
}