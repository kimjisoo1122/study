package com.study.servlet.board;

import com.study.dao.BoardDao;
import com.study.dao.FileDao;
import com.study.dao.ReplyDao;
import com.study.dto.BoardDto;
import com.study.dto.BoardSearchCondition;
import com.study.dto.FileDto;
import com.study.dto.ReplyDto;
import com.study.servlet.ServletHandler;
import com.study.util.JspViewResolver;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class BoardDeleteHandler implements ServletHandler {
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
        BoardDao boardDao = new BoardDao();
        BoardDto board = boardDao.findById(boardId);

        if (removePassword.equals(board.getPassword())) {
            // 파일 삭제
            FileDao fileDao = new FileDao();
            List<FileDto> fileList = fileDao.findByBoardId(boardId);
            for (FileDto fileDto : fileList) {
                File regisetdFile = new File(fileDto.getPath() + File.separator + fileDto.getName());
                if (regisetdFile.exists()) {
                    if (regisetdFile.delete()) {
                        fileDao.delete(fileDto.getFileId());
                    }
                }
            }

            // 댓글, 파일db, 게시글을 트랜잭션 내에서 삭제
            int deletedRowCnt = boardDao.deleteBoardAll(boardId);
            if (deletedRowCnt > 0) {
                request.getSession().setAttribute("removeMsg", "게시글을 삭제하였습니다.");
                response.sendRedirect("/board/?" + condition.getQueryString());
                return;
            }
        }

        FileDao fileDao = new FileDao();
        List<FileDto> files = fileDao.findByBoardId(boardId);
        request.setAttribute("files", files);
        ReplyDao replyDao = new ReplyDao();
        List<ReplyDto> replies = replyDao.findByBoardId(boardId);
        request.setAttribute("replies", replies);

        request.getSession().setAttribute("removeErrMsg", "게시글 삭제에 실패하였습니다.");
        response.sendRedirect("/board/board?" + condition.getQueryString() + "&boardId=" + boardId);

    }
}
