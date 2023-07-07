package com.study.servlet.board;

import com.study.dto.BoardDto;
import com.study.dto.BoardSearchCondition;
import com.study.dto.FileDto;
import com.study.dto.ReplyDto;
import com.study.service.BoardService;
import com.study.service.FileService;
import com.study.service.ReplyService;
import com.study.servlet.MyServlet;
import com.study.util.FileUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BoardDeleteServlet implements MyServlet {

    private final BoardService boardService = BoardService.getBoardService();
    private final FileService fileService = FileService.getFileService();
    private final ReplyService replyService = ReplyService.getReplyService();

    @Override
    public String handle(Map<String, String> paramMap, Map<String, Object> model) throws IOException {
        HttpServletRequest request = (HttpServletRequest) model.get("request");

        BoardSearchCondition condition = new BoardSearchCondition();
        condition.setConditionByParam(paramMap);

        Long boardId = Optional.of(request.getParameter("boardId"))
                .map(Long::valueOf)
                .orElse(null);

        String removePassword = paramMap.get("removePassword");

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
                return "redirect:/board?" + condition.getQueryString();
            }
        }

        List<FileDto> files = fileService.findByBoardId(boardId);
        List<ReplyDto> replies = replyService.findByBoardId(boardId);

        model.put("condition", condition);
        model.put("files", files);
        model.put("replies", replies);

        request.getSession().setAttribute("removeErrMsg", "게시글 삭제에 실패하였습니다.");
        return "redirect:/board/board?" + condition.getQueryString() + "&boardId=" + boardId;
    }
}
