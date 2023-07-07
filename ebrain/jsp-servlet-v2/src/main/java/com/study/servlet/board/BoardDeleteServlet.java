package com.study.servlet.board;

import com.study.dto.BoardSearchCondition;
import com.study.dto.FileDto;
import com.study.dto.ReplyDto;
import com.study.service.BoardService;
import com.study.service.FileService;
import com.study.service.ReplyService;
import com.study.util.StringUtil;
import com.study.validation.BoardValidation;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 게시글을 삭제하는 서블릿 입니다.
 */
public class BoardDeleteServlet implements MyServlet {

    private final BoardService boardService = BoardService.getBoardService();
    private final FileService fileService = FileService.getFileService();
    private final ReplyService replyService = ReplyService.getReplyService();

    @Override
    public String handle(Map<String, String> paramMap, Map<String, Object> model) throws IOException {
        HttpServletRequest request = (HttpServletRequest) model.get("request");

        BoardSearchCondition condition = new BoardSearchCondition();
        condition.setConditionByParam(paramMap);

        String removePassword = StringUtil.nvl(paramMap.get("removePassword"));
        Long boardId = StringUtil.toLong(paramMap.get("boardId"));
        // 게시글번호가 잘못된 경우 게시글목록으로 리다이렉트 합니다.
        if (boardId == null) {
            request.getSession().setAttribute("removeMsg", "게시글 삭제에 실패하였습니다.");
            return "redirect:/board?" + condition.getQueryString();
        }

        // 비밀번호 검증을 진행합니다.
        String findPassword = boardService.findById(boardId).getPassword();
        if (findPassword.equals(BoardValidation.encrypt(removePassword))) {
            // 댓글, 첨부파일, 게시글을 트랜잭션 내에서 삭제
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
