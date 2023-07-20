package com.study.controller;

import com.study.api.ResponseDto;
import com.study.api.ResponseValidFormDto;
import com.study.api.ResponseStatus;
import com.study.dto.BoardRegisterForm;
import com.study.dto.BoardSearchCondition;
import com.study.dto.BoardUpdateForm;
import com.study.service.BoardService;
import com.study.service.FileService;
import com.study.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

/**
 * 게시글을 처리하는 API컨트롤러 입니다.
 */
@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final FileService fileService;
    private final ReplyService replyService;

    /**
     * 게시글목록을 조회합니다.
     * @param page 현재페이지
     * @param pageSize 페이지크기
     * @param condition 검색조건
     * @return boardList 게시글목록, boardCnt 개시글갯수
     */
    @GetMapping()
    public ResponseEntity<ResponseDto> getBoardList(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @ModelAttribute(value = "condition") BoardSearchCondition condition) {

        condition.setPagination(page, pageSize);

        ResponseDto response = new ResponseDto();
        response.setStatus(ResponseStatus.SUCCESS);
        response.setData(
                Map.of("boardList", boardService.findAllByCondition(condition),
                        "boardCnt", boardService.getBoardCnt(condition)));

        return ResponseEntity.ok(response);
    }

    /**
     * 조회수를 1 증가 시키고 게시글을 조회합니다.
     * @param boardId 게시글번호
     * @return board 게시글
     */
    @GetMapping("/{boardId}")
    public ResponseEntity<ResponseDto> getBoard(
             @PathVariable("boardId") Long boardId) {

        boardService.increaseViewCnt(boardId);

        ResponseDto response = new ResponseDto();
        response.setStatus(ResponseStatus.SUCCESS);
        response.setData(
                Map.of("board", boardService.findBoardById(boardId),
                        "files", fileService.findByBoardId(boardId),
                        "replies", replyService.findByBoardId(boardId)));

        return ResponseEntity.ok(response);
    }

    /**
     * 게시글을 등록합니다.
     * @param form 게시글등록정보
     * @param bindingResult 유효성검증객체
     * @return boardId 등록된 게시글번호
     */
    @PostMapping
    public ResponseEntity<ResponseValidFormDto> registerBoard(
            @Validated BoardRegisterForm form,
            BindingResult bindingResult) throws IOException {

        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .badRequest()
                    .body(createValidFormResponse(bindingResult));
        }

        ResponseValidFormDto response = new ResponseValidFormDto();
        response.setStatus(ResponseStatus.SUCCESS);
        response.setData(Map.of("boardId", boardService.register(form)));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    /**
     * 게시글을 업데이트합니다.
     */
    @PutMapping("/{boardId}")
    public ResponseEntity<ResponseValidFormDto> updateBoard(
            @PathVariable("boardId") Long boardId,
            @Validated BoardUpdateForm form,
            BindingResult bindingResult) throws IOException {

        if (boardService.isPasswordNotMatch(boardId, form.getPassword())) {
            bindingResult.rejectValue("password", null,
                    "비밀번호가 맞지 않습니다");
        }

        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .badRequest()
                    .body(createValidFormResponse(bindingResult));
            }

        boardService.update(form);

        ResponseValidFormDto response = new ResponseValidFormDto();
        response.setStatus(ResponseStatus.SUCCESS);

        return ResponseEntity.ok(response);
    }

    /**
     * 게시글을 삭제합니다.
     * @param boardId 게시글번호
     * @param deleteMap 게시글비밀번호를 담고있는 Map
     * @return ResponseDto
     */
    @DeleteMapping("/{boardId}")
    public ResponseEntity<ResponseDto> deleteBoard(
            @PathVariable("boardId") Long boardId,
            @RequestBody Map<String, String> deleteMap) {

        if (boardService.isPasswordNotMatch(boardId, deleteMap.get("deletePassword"))) {
            throw new IllegalArgumentException("비밀번호가 맞지 않습니다");
        }

        boardService.delete(boardId);

        ResponseDto response = new ResponseDto();
        response.setStatus(ResponseStatus.SUCCESS);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(response);
    }

    /**
     * 유효성검증에 실패한 Form데이터의 에러값을 저장해서 반환합니다.
     * @param bindingResult 유효성검증객체
     * @return ResponseValidFormDto
     */
    private ResponseValidFormDto createValidFormResponse(BindingResult bindingResult) {
        ResponseValidFormDto response = new ResponseValidFormDto();
        response.setStatus(ResponseStatus.FAIL);
        response.setErrorMessage("잘못된 데이터입니다.");

        // 에러필드이름과 에러메시지를 응답값에 담습니다.
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            response.getErrorFields().put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return response;
    }
}
