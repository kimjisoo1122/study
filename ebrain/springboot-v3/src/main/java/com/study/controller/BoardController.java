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
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

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

        BoardSearchCondition paginatedCondition = configurePagination(page, pageSize, condition);

        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("boards", boardService.findByCondition(paginatedCondition));
        model.addAttribute("pageHandler",
                new PageHandler(paginatedCondition.getPage(), boardService.getTotalSize(paginatedCondition)));

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
     * @param form 게시글 등록 Form
     * @param condition 검색조건
     * @param model
     * @return board/register
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
     * @return
     * @throws IOException
     */
    @PostMapping("/register")
    private String register(
            @Validated @ModelAttribute("form") BoardRegisterForm form,
            BindingResult bindingResult,
            @ModelAttribute("condition") BoardSearchCondition condition,
            Model model) throws IOException {

        if (!form.checkConfirmPassword()) {
            bindingResult.rejectValue("confirmPassword", null,
                    "비밀번호가 서로 맞지 않습니다.");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            return "board/register";
        }
        // 유효성 검증 종료

        boardService.register(form);

        // 한글 파라미터 인코딩 (서버내에선 한글 인코딩 해야함, URI입력은 톰캣이 해줌)
        return "redirect:/board" + encodeQueryParam(condition).getQueryParamString();
    }

    @GetMapping("/update/{boardId}")
    public String updateForm(
            @PathVariable("boardId") Long boardId,
            @ModelAttribute("condition") BoardSearchCondition condition,
            Model model) {

        BoardDto findBoard = boardService.findById(boardId);
        BoardUpdateForm updateForm = createUpdateForm(findBoard);

        model.addAttribute("form", updateForm);
        model.addAttribute("files", fileService.findByBoardId(boardId));
        model.addAttribute("categories", categoryService.findAll());

        return "board/update";
    }

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
        // 유효성 검증 종료

        boardService.update(form);



        return "redirect:/board/" + boardId + encodeQueryParam(condition).getQueryParamString();
    }

    /**
     * 서버 내부에서 리다이렉트하는 경우 쿼리스트링을 인코딩해준다.
     * @param condition 검색조건
     * @return 인코딩된 검색조건
     */
    private BoardSearchCondition encodeQueryParam(BoardSearchCondition condition) {
        condition.setSearch(UriUtils.encode(condition.getSearch(), StandardCharsets.UTF_8));
        return condition;
    }

    /**
     * 게시글 DTO를 업데이트 전용 DTO로 변환하여 반환합니다.
     * @param boardDto 게시글 DTO
     * @return BoardUpdateForm 업데이트 Form
     */
    private BoardUpdateForm createUpdateForm(BoardDto boardDto) {

        BoardUpdateForm boardUpdateForm = new BoardUpdateForm();
        boardUpdateForm.setBoardId(boardDto.getBoardId());
        boardUpdateForm.setCategoryId(boardDto.getCategoryId());
        boardUpdateForm.setCategoryName(boardDto.getCategoryName());
        boardUpdateForm.setWriter(boardDto.getWriter());
        boardUpdateForm.setTitle(boardDto.getTitle());
        boardUpdateForm.setContent(boardDto.getContent());
        boardUpdateForm.setViewCnt(boardDto.getViewCnt());
        boardUpdateForm.setCreateDate(boardDto.getCreateDate());
        boardUpdateForm.setUpdateDate(boardDto.getUpdateDate());
        return boardUpdateForm;
    }

    /**
     * 검색조건 페이징처리를 위한 condition객체의 값을 설정합니다.
     * @param page
     * @param pageSize
     * @param condition
     * @return BoardSearchCondition 페이지설정된 검색조건 객체
     */
    private BoardSearchCondition configurePagination(int page, int pageSize, BoardSearchCondition condition) {
        condition.setPage(page);
        condition.setPageSize(pageSize);
        condition.setOffset((page - 1) * pageSize);
        condition.setLimit(pageSize);
        return condition;
    }
}
