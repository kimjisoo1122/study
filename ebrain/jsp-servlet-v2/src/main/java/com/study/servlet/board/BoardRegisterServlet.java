package com.study.servlet.board;

import com.oreilly.servlet.MultipartRequest;
import com.study.dto.BoardDto;
import com.study.dto.BoardSearchCondition;
import com.study.dto.FileDto;
import com.study.exception.FileUploadPathNotExistException;
import com.study.service.BoardService;
import com.study.servlet.multipart.MyFileRenamePolicy;
import com.study.util.FileUtil;
import com.study.util.StringUtil;
import com.study.validation.BoardValidation;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 게시글을 등록하는 서블릿 입니다.
 */
public class BoardRegisterServlet implements MyServlet {

    private final BoardService boardService = BoardService.getBoardService();

    @Override
    public String handle(Map<String, String> paramMap, Map<String, Object> model) throws IOException {
        // 파일을 업로드할 경로를 확인합니다.
        if (!FileUtil.checkUploadPath()) {
            throw new FileUploadPathNotExistException();
        }


        HttpServletRequest request = (HttpServletRequest) model.get("request");
        MultipartRequest multi = null;

        try {
            multi = new MultipartRequest(
                    request, FileUtil.FILE_PATH, FileUtil.BOARD_FILE_MAX_SIZE, FileUtil.ENC_TYPE,
                    new MyFileRenamePolicy());
        } catch (IOException e) {
            e.printStackTrace();
            request.getSession().setAttribute("fileError", "잘못된 파일입니다.");
            return "board/register";
        }

        // 검색조건을 설정합니다.
        BoardSearchCondition condition = new BoardSearchCondition();
        condition.setConditionByMulti(multi);

        // 게시글 DTO 생성
        BoardDto registerBoard = new BoardDto();
        Long categoryId = StringUtil.toLong(multi.getParameter("categoryId"));
        if (categoryId != null) {
            registerBoard.setCategoryId(categoryId);
        }
        registerBoard.setWriter(multi.getParameter("writer"));
        registerBoard.setPassword(multi.getParameter("password"));
        registerBoard.setTitle(multi.getParameter("title"));
        registerBoard.setContent(multi.getParameter("content"));

        // 게시글 유효성 검증
        if (!BoardValidation.isBoardValid(registerBoard)) {
            // 게시글이 유효하지 않은경우 저장된 파일을 삭제처리 합니다.
            FileUtil.deleteFromMulti(multi);

            model.put("board", registerBoard);
            model.put("condition", condition);

            return "board/register";
        } else {
            // BoardDto의 files 리스트에 fileDto를 저장하여 트랜잭션내에서 동시에 등록합니다.
            List<FileDto> files = FileUtil.getFilesFromMulti(multi);
            registerBoard.setFiles(files);
            // 게시글을 등록합니다.
            boardService.register(registerBoard);
        }
        return "redirect:/board?" + condition.getQueryString();
    }


}
