package com.study.servlet.board;

import com.study.dto.BoardDto;
import com.study.dto.BoardSearchCondition;
import com.study.dto.FileDto;
import com.study.dto.ReplyDto;
import com.study.service.BoardService;
import com.study.service.FileService;
import com.study.service.ReplyService;
import com.study.util.StringUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 게시글번호로 게시글을 조회하는 서블릿 입니다.
 */
public class BoardServlet implements MyServlet {

    private final BoardService boardService = BoardService.getBoardService();
    private final FileService fileService = FileService.getFileService();
    private final ReplyService replyService = ReplyService.getReplyService();

    @Override
    public String handle(Map<String, String> paramMap, Map<String, Object> model) throws IOException {
        // 검색조건을 설정합니다.
        BoardSearchCondition condition = new BoardSearchCondition();
        condition.setConditionByParam(paramMap);

        Long boardId = StringUtil.toLong(paramMap.get("boardId"));
        if (boardId == null) {
            return "redirect:/board?" + condition.getQueryString();
        }

        // 조회수 증가
        boardService.increaseViewCnt();
        // 게시글 조회
        BoardDto board = boardService.findById(boardId);
        // 첨부파일 조회
        List<FileDto> files = fileService.findByBoardId(boardId);
        // 댓글 조회
        List<ReplyDto> replies = replyService.findByBoardId(boardId);

        model.put("condition", condition);
        model.put("board", board);
        model.put("files", files);
        model.put("replies", replies);

        return "board/board";
    }
}
