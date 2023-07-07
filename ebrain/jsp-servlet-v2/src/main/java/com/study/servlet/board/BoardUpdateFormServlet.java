package com.study.servlet.board;

import com.study.dto.BoardDto;
import com.study.dto.BoardSearchCondition;
import com.study.dto.FileDto;
import com.study.service.BoardService;
import com.study.service.FileService;
import com.study.servlet.MyServlet;
import com.study.util.StringUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class BoardUpdateFormServlet implements MyServlet {

    private final BoardService boardService = BoardService.getBoardService();
    private final FileService fileService = FileService.getFileService();

    @Override
    public String handle(Map<String, String> paramMap, Map<String, Object> model) throws IOException {
        BoardSearchCondition condition = new BoardSearchCondition();
        condition.setConditionByParam(paramMap);

        String boardIdParam = paramMap.get("boardId");
        Long boardId = null;
        if (StringUtil.isNumeric(boardIdParam)) {
            boardId = Long.valueOf(boardIdParam);
        } else {
            // 게시글번호 잘못들어온 경우 게시글목록으로 리다이렉트
            return "redirect:/board?" + condition.getQueryString();
        }

        // 게시글 조회
        BoardDto board = boardService.findById(boardId);
        // 첨부파일 조회
        List<FileDto> files = fileService.findByBoardId(board.getBoardId());

        model.put("condition", condition);
        model.put("board", board);
        model.put("files", files);

        return "board/update";
    }
}
