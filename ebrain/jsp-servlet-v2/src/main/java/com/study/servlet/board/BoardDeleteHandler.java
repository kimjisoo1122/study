package com.study.servlet.board;

import com.study.dto.BoardDto;
import com.study.dto.BoardSearchCondition;
import com.study.dto.FileDto;
import com.study.dto.ReplyDto;
import com.study.service.BoardService;
import com.study.service.FileService;
import com.study.service.ReplyService;
import com.study.servlet.ServletHandler;
import com.study.util.FileUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class BoardDeleteHandler implements ServletHandler {

    private final BoardService boardService = BoardService.getBoardService();
    private final FileService fileService = FileService.getFileService();
    private final ReplyService replyService = ReplyService.getReplyService();

    @Override
    public void getHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BoardSearchCondition condition = new BoardSearchCondition();
        condition.setConditionByReq(request);
        request.setAttribute("condition", condition);

        Long boardId = Optional.of(request.getParameter("boardId"))
                .map(Long::valueOf)
                .orElse(null);

        String removePassword = request.getParameter("removePassword");

        if (boardId == null) {
            // TODO 게시글번호 유효성
        }

        // 게시글 조회
        BoardDto board = boardService.findById(boardId);

        if (removePassword.equals(board.getPassword())) {
            // 파일 삭제
            List<FileDto> fileList = fileService.findByBoardId(boardId);
            for (FileDto fileDto : fileList) {
                File regisetdFile = FileUtil.getUploadedFile(fileDto.getPhysicalName());
                if (regisetdFile.exists()) {
                    if (regisetdFile.delete()) {
                        fileService.delete(fileDto.getFileId());
                    }
                }
            }

            // 댓글, 파일db, 게시글을 트랜잭션 내에서 삭제
            int deletedRowCnt = boardService.delete(boardId);
            if (deletedRowCnt > 0) {
                request.getSession().setAttribute("removeMsg", "게시글을 삭제하였습니다.");
                response.sendRedirect("/board/?" + condition.getQueryString());
                return;
            }
        }

        List<FileDto> files = fileService.findByBoardId(boardId);
        request.setAttribute("files", files);
        List<ReplyDto> replies = replyService.findByBoardId(boardId);
        request.setAttribute("replies", replies);

        request.getSession().setAttribute("removeErrMsg", "게시글 삭제에 실패하였습니다.");
        response.sendRedirect("/board/board?" + condition.getQueryString() + "&boardId=" + boardId);
    }
}
