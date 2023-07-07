package com.study.servlet.board;

import com.oreilly.servlet.MultipartRequest;
import com.study.dto.BoardDto;
import com.study.dto.BoardSearchCondition;
import com.study.dto.FileDto;
import com.study.exception.FileUploadPathNotExistException;
import com.study.service.BoardService;
import com.study.service.FileService;
import com.study.servlet.multipart.MyFileRenamePolicy;
import com.study.util.FileUtil;
import com.study.util.StringUtil;
import com.study.validation.BoardValidation;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 게시글을 수정하는 서블릿 입니다.
 */
public class BoardUpdateServlet implements MyServlet {

    private final BoardService boardService = BoardService.getBoardService();
    private final FileService fileService = FileService.getFileService();

    @Override
    public String handle(Map<String, String> paramMap, Map<String, Object> model) throws IOException {
        // 파일을 업로드할 경로를 확인합니다.
        if (!FileUtil.checkUploadPath()) {
            throw new FileUploadPathNotExistException();
        }

        HttpServletRequest request = (HttpServletRequest) model.get("request");
        Long boardId = StringUtil.toLong(request.getParameter("boardId"));
        // 게시글이 유효하지 않으면 게시글목록으로 리다이렉트 합니다.
        if (boardId == null) {
            return "redirect:/board";
        }
        MultipartRequest multi = null;

        try {
            multi = new MultipartRequest(
                    request, FileUtil.FILE_PATH, FileUtil.BOARD_FILE_MAX_SIZE, FileUtil.ENC_TYPE,
                    new MyFileRenamePolicy());
        } catch (IOException e) {
            e.printStackTrace();
            request.getSession().setAttribute("fileError", "잘못된 파일입니다.");

            // 게시글, 파일목록을 조회하여 다시 업데이트 폼으로 포워딩 합니다.
            BoardDto findBoard = boardService.findById(boardId);
            List<FileDto> files = fileService.findByBoardId(boardId);

            model.put("board", findBoard);
            model.put("files", files);

            return "board/update";
        }

        // 검색조건을 설정합니다.
        BoardSearchCondition condition = new BoardSearchCondition();
        condition.setConditionByMulti(multi);

        // 업데이트 값을 설정 합니다.
        BoardDto findBoard = boardService.findById(boardId);
        String findPassword = findBoard.getPassword();

        BoardDto updateBoard = findBoard;
        updateBoard.setBoardId(boardId);
        updateBoard.setWriter(multi.getParameter("writer"));
        updateBoard.setTitle(multi.getParameter("title"));
        updateBoard.setContent(multi.getParameter("content"));
        updateBoard.setPassword(multi.getParameter("password"));

        // 게시글의 유효성을 검증합니다.
        String encryptPassword = BoardValidation.encrypt(multi.getParameter("password"));
        if (!findPassword.equals(encryptPassword)
                || !BoardValidation.isBoardValid(updateBoard) ) {
            // 게시글이 유효하지 않은경우 저장된 파일을 삭제처리 합니다.
            FileUtil.deleteFromMulti(multi);

            model.put("condition", condition);
            model.put("board", updateBoard);
            model.put("files", fileService.findByBoardId(boardId));

            return "board/update";
        } else {
            // 첨부파일을 등록 합니다.
            updateBoard.setFiles(FileUtil.getFilesFromMulti(multi));
            // 삭제할 파일을 등록 합니다.
            updateBoard.setFileIds(multi.getParameterValues("fileId"));

            boardService.update(updateBoard);
        }

        return "redirect:/board/board?" + condition.getQueryString() + "&boardId=" + boardId;
    }
}
