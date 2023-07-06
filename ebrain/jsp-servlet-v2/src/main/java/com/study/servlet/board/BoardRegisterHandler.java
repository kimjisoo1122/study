package com.study.servlet.board;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.study.dto.BoardDto;
import com.study.dto.BoardSearchCondition;
import com.study.dto.FileDto;
import com.study.service.BoardService;
import com.study.service.FileService;
import com.study.servlet.ServletHandler;
import com.study.util.FileUtil;
import com.study.util.JspViewResolver;
import com.study.util.StringUtil;
import com.study.validation.BoardValidation;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;

public class BoardRegisterHandler implements ServletHandler {

    private final BoardService boardService = BoardService.getBoardService();
    private final FileService fileService = FileService.getFileService();

    @Override
    public void getHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 검색 조건
        BoardSearchCondition condition = new BoardSearchCondition();
        condition.setConditionByReq(request);
        request.setAttribute("condition", condition);

        String path = JspViewResolver.getViewPath("/board/register");
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(path);
        requestDispatcher.forward(request, response);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 하... 외부라이브러리 사용하면 빌드를 gradle말고 인텔리제이로 해야함...
        // 검색 조건
        BoardSearchCondition condition = new BoardSearchCondition();
        condition.setConditionByReq(request);
        request.setAttribute("condition", condition);

        // 에러 체크 (에러발생 또는 유효성 검증 실패시 다시 form으로 포워딩)
        boolean hasError = false;
        BoardDto board = new BoardDto();

        // 업로드 폴더 확인
        if (FileUtil.checkUploadPath()) {
            MultipartRequest multi = null;
            try {
                // TODO 파일명 난수로 저장 (한글명 X)
                // TODO 파일중복일시 커스텀정책 만들기
                multi = new MultipartRequest(
                        request, FileUtil.FILE_PATH, FileUtil.BOARD_FILE_MAX_SIZE, FileUtil.ENC_TYPE,
                        new DefaultFileRenamePolicy());
            } catch (IOException e) {
                e.printStackTrace();
                request.setAttribute("fileError", "파일사이즈는 10MB를 넘을 수 없습니다.");
                hasError = true;
            }

            // 파라미터 파싱
            String categoryId = multi.getParameter("categoryId");
            if (StringUtil.isNumeric(categoryId)) {
                board.setCategoryId(Long.valueOf(categoryId));
            }
            board.setWriter(StringUtil.nvl(multi.getParameter("writer")));
            board.setPassword(StringUtil.nvl(multi.getParameter("password")));
            board.setTitle(StringUtil.nvl(multi.getParameter("title")));
            board.setContent(StringUtil.nvl(multi.getParameter("content")));

            if (!BoardValidation.validBoard(board)) {
                hasError = true;
                request.setAttribute("board", board);
            } else {
                Long boardId = boardService.register(board);
                //TODO 게시글 생성 안될때 파일삭제처리

                // 파일 db 저장
                Enumeration fileInputs = multi.getFileNames();
                while (fileInputs.hasMoreElements()) {
                    String fileInput = (String) fileInputs.nextElement();
                    String fileName = multi.getFilesystemName(fileInput);
                    String originalFileName = multi.getOriginalFileName(fileInput);
                    if (fileName != null) {
                        File file = FileUtil.getUploadedFile(fileName);
                        FileDto fileDto = new FileDto();
                        fileDto.setBoardId(boardId);
                        fileDto.setPhysicalName(fileName);
                        fileDto.setPath(FileUtil.FILE_PATH);
                        fileDto.setOriginalName(originalFileName);
                        fileDto.setFileSize(file.length());
                        String fileExtension =
                                fileName.substring(fileName.lastIndexOf(".") + 1);
                        fileDto.setFileExtension(fileExtension);
                        fileService.save(fileDto);
                    }
                }
            }
        } else {
            hasError = true;
        }

        // 에러가 있는 경우 포워딩 게시글 등록시 게시글목록으로 이동
        String viewPath = JspViewResolver.getViewPath("/board/register");
        if (hasError) {
            RequestDispatcher requestDispatcher = request.getRequestDispatcher(viewPath);
            requestDispatcher.forward(request, response);
        } else {
            response.sendRedirect("/board?" + condition.getQueryString());
        }
    }
}
