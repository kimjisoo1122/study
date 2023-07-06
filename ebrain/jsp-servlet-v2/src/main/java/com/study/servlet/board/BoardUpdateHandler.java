package com.study.servlet.board;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.study.dao.BoardDao;
import com.study.dao.FileDao;
import com.study.dto.BoardDto;
import com.study.dto.BoardSearchCondition;
import com.study.dto.FileDto;
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
import java.util.*;

public class BoardUpdateHandler implements ServletHandler {
    @Override
    public void getHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 검색 조건
        BoardSearchCondition condition = new BoardSearchCondition();
        condition.setConditionByReq(request);
        request.setAttribute("condition", condition);

        // 파라미터 유효성 검증
        String boardId = request.getParameter("boardId");
        if (!StringUtil.isNumeric(boardId)) {
            response.sendRedirect("/board?" + condition.getQueryString());
        }

        // 게시글 조회
        BoardDao boardDao = new BoardDao();
        BoardDto board = boardDao.findById(Long.valueOf(boardId));
        request.setAttribute("board", board);

        // 첨부파일 조회
        FileDao fileDao = new FileDao();
        List<FileDto> files = fileDao.findByBoardId(board.getBoardId());
        request.setAttribute("files", files);

        String viewPath = JspViewResolver.getViewPath("/board/update");
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(viewPath);
        requestDispatcher.forward(request, response);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (FileUtil.checkUploadPath()) {
            MultipartRequest multi = null;
            // 실패시 포워딩
            String viewPath = JspViewResolver.getViewPath("/board/update");
            RequestDispatcher requestDispatcher = request.getRequestDispatcher(viewPath);


            try {
                multi = new MultipartRequest(
                        request, FileUtil.FILE_PATH, FileUtil.BOARD_FILE_MAX_SIZE, FileUtil.ENC_TYPE,
                        new DefaultFileRenamePolicy());
            } catch (IOException e) {
                e.printStackTrace();
                request.setAttribute("fileError", "파일사이즈는 10MB를 넘을 수 없습니다.");
                requestDispatcher.forward(request, response);
            }
            Long boardId = Optional.ofNullable(multi.getParameter("boardId"))
                    .map(Long::valueOf)
                    .orElse(null);

            // 게시글번호 검증
            if (boardId == null) {
                requestDispatcher.forward(request, response);
            }

            FileDao fileDao = new FileDao();
            List<FileDto> files = fileDao.findByBoardId(boardId);
            request.setAttribute("files", files);

            BoardSearchCondition condition = new BoardSearchCondition();
            condition.setConditionByReq(multi);
            request.setAttribute("condition", condition);


            String writer = multi.getParameter("writer");
            String title = multi.getParameter("title");
            String content = multi.getParameter("content");

            BoardDao boardDao = new BoardDao();
            BoardDto findBoard = boardDao.findById(boardId);
            findBoard.setWriter(writer);
            findBoard.setTitle(title);
            findBoard.setContent(content);
            String password = multi.getParameter("password");

            // 게시글 유효성 검증
            if (!findBoard.getPassword().equals(password)
                    || !BoardValidation.validBoard(findBoard) ) {
                request.setAttribute("board", findBoard);
                requestDispatcher.forward(request, response);
            }

            // TODO 게시글 업데이트 -> 트랜잭션 처리
            Map<String, String> updateMap = new HashMap<>();
            updateMap.put("writer", writer);
            updateMap.put("title", title);
            updateMap.put("content", content);
            boardDao.update(updateMap, boardId);

            // 파일 db 저장
            Enumeration fileInputs = multi.getFileNames();
            while (fileInputs.hasMoreElements()) {
                String fileInput = (String) fileInputs.nextElement();
                String fileName = multi.getFilesystemName(fileInput);
                String originalFileName = multi.getOriginalFileName(fileInput);
                if (fileName != null) {
                    FileDto fileDto = new FileDto();
                    fileDto.setBoardId(boardId);
                    fileDto.setPhysicalName(fileName);
                    fileDto.setPath(FileUtil.FILE_PATH);
                    fileDto.setOriginalName(originalFileName);
                    fileDao.save(fileDto);
                }
            }

            // 파일 삭제
            String[] fileIds = multi.getParameterValues("fileId");
            if (fileIds != null) {
                for (String fileIdStr : fileIds) {
                    Long fileId = Long.parseLong(fileIdStr);
                    FileDto fileDto = fileDao.findById(fileId);

                    // 실제 파일 삭제
                    File uploadedFile = FileUtil.getUploadedFile(fileDto.getPhysicalName());
                    if (uploadedFile.exists()) {
                        if (uploadedFile.delete()) {
                            // 파일 db 삭제
                            fileDao.delete(fileId);
                        }
                    }
                }
            }

        response.sendRedirect("/board/board?boardId=" + boardId + condition.getQueryString());
        }
    }
}
