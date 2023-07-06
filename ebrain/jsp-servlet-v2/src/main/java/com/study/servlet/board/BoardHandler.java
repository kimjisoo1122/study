package com.study.servlet.board;

import com.study.dto.BoardDto;
import com.study.dto.BoardSearchCondition;
import com.study.dto.FileDto;
import com.study.dto.ReplyDto;
import com.study.service.BoardService;
import com.study.service.FileService;
import com.study.service.ReplyService;
import com.study.servlet.ServletHandler;
import com.study.util.JspViewResolver;
import com.study.util.StringUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class BoardHandler implements ServletHandler {

    private final BoardService boardService = BoardService.getBoardService();
    private final FileService fileService = FileService.getFileService();
    private final ReplyService replyService = ReplyService.getReplyService();

    @Override
    public void getHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BoardSearchCondition condition = new BoardSearchCondition();
        condition.setConditionByReq(request);
        request.setAttribute("condition", condition);

        String boardIdStr = StringUtil.nvl(request.getParameter("boardId"));
        Long boardId = null;
        if (StringUtil.isNumeric(boardIdStr)) {
            boardId = Long.valueOf(request.getParameter("boardId"));
        } else {
            response.sendRedirect("/board?" + condition.getQueryString());
        }

        // 조회수 증가
        boardService.increaseViewCnt();

        // 게시글 조회
        BoardDto board = boardService.findById(boardId);

        // 첨부파일 조회

        List<FileDto> files = fileService.findByBoardId(boardId);

        // 댓글 조회
        List<ReplyDto> replies = replyService.findByBoardId(boardId);

        request.setAttribute("board", board);
        request.setAttribute("files", files);
        request.setAttribute("replies", replies);
        String viewPath = JspViewResolver.getViewPath("/board/board");
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(viewPath);
        requestDispatcher.forward(request, response);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
