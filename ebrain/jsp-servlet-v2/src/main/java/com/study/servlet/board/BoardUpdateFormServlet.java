package com.study.servlet.board;

import com.study.dto.BoardDto;
import com.study.dto.BoardSearchCondition;
import com.study.dto.FileDto;
import com.study.service.BoardService;
import com.study.service.FileService;
import com.study.util.StringUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 게시글 업데이트 폼을 조회하는 서블릿 입니다.
 */
public class BoardUpdateFormServlet implements MyServlet {

    private final BoardService boardService = BoardService.getBoardService();
    private final FileService fileService = FileService.getFileService();

    @Override
    public String handle(Map<String, String> paramMap, Map<String, Object> model) throws IOException {
        BoardSearchCondition condition = new BoardSearchCondition();
        condition.setConditionByParam(paramMap);

        Long boardId = StringUtil.toLong(paramMap.get("boardId"));
        if (boardId == null) {
            // 게시글번호가 유효하지 않은 경우 게시글목록으로 리다이렉트
            return "redirect:/board?" + condition.getQueryString();
        }

        // 게시글을 조회합니다.
        BoardDto board = boardService.findById(boardId);
        // 첨부파일을 조회합니다.
        List<FileDto> files = fileService.findByBoardId(board.getBoardId());

        model.put("condition", condition);
        model.put("board", board);
        model.put("files", files);

        return "board/update";
    }
}