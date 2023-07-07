package com.study.servlet.board;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.study.dto.BoardDto;
import com.study.dto.BoardSearchCondition;
import com.study.dto.FileDto;
import com.study.exception.FileUploadPathNotExistException;
import com.study.service.BoardService;
import com.study.service.FileService;
import com.study.servlet.MyServlet;
import com.study.util.FileUtil;
import com.study.util.StringUtil;
import com.study.validation.BoardValidation;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;

public class BoardUpdateServlet implements MyServlet {

    private final BoardService boardService = BoardService.getBoardService();
    private final FileService fileService = FileService.getFileService();

    @Override
    public String handle(Map<String, String> paramMap, Map<String, Object> model) throws IOException {
        // 파일업로드 경로 확인
        if (!FileUtil.checkUploadPath()) {
            throw new FileUploadPathNotExistException();
        }

        MultipartRequest multi = null;
        boolean hasError;
        HttpServletRequest request = (HttpServletRequest) model.get("request");

        try {
            multi = new MultipartRequest(
                    request, FileUtil.FILE_PATH, FileUtil.BOARD_FILE_MAX_SIZE, FileUtil.ENC_TYPE,
                    new DefaultFileRenamePolicy());
        } catch (IOException e) {
            e.printStackTrace();
            //TODO 파일등록 실패
            return "redirect:/board";
        }
        BoardSearchCondition condition = new BoardSearchCondition();
        condition.setConditionByReq(multi);
        model.put("condition", condition);

        String boardIdParam = multi.getParameter("boardId");
        Long boardId = null;
        if (StringUtil.isNumeric(boardIdParam)) {
            boardId = Long.valueOf(boardIdParam);
        } else {
            return "redirect:/board?" + condition.getQueryString();
        }

        String writer = multi.getParameter("writer");
        String title = multi.getParameter("title");
        String content = multi.getParameter("content");

        BoardDto findBoard = boardService.findById(boardId);
        findBoard.setWriter(writer);
        findBoard.setTitle(title);
        findBoard.setContent(content);
        String password = multi.getParameter("password");

        // 게시글 유효성 검증
        if (!findBoard.getPassword().equals(password)
                || !BoardValidation.validBoard(findBoard) ) {
            model.put("board", findBoard);
            return "board/update";
        }

        // TODO 게시글 업데이트 -> 트랜잭션 처리
        BoardDto updateBoard = new BoardDto();
        updateBoard.setBoardId(boardId);
        updateBoard.setWriter(writer);
        updateBoard.setTitle(title);
        updateBoard.setContent(content);
        boardService.update(updateBoard);

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
                fileDto.setFileExtension(FileUtil.getFileExtension(fileName));
                fileService.save(fileDto);
            }
        }

        // 파일 삭제
        String[] fileIds = multi.getParameterValues("fileId");
        if (fileIds != null) {
            for (String fileIdStr : fileIds) {
                Long fileId = Long.parseLong(fileIdStr);
                FileDto fileDto = fileService.findById(fileId);

                // 실제 파일 삭제
                File uploadedFile = FileUtil.getUploadedFile(fileDto.getPhysicalName());
                if (uploadedFile.exists()) {
                    if (uploadedFile.delete()) {
                        // 파일 db 삭제
                        fileService.delete(fileId);
                    }
                }
            }
        }

        return "redirect:/board/board?" + condition.getQueryString() + "&boardId=" + boardId;
    }
}
