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

public class BoardRegisterServlet implements MyServlet {

    private final BoardService boardService = BoardService.getBoardService();
    private final FileService fileService = FileService.getFileService();

    @Override
    public String handle(Map<String, String> paramMap, Map<String, Object> model) throws IOException {
        // 파일업로드 경로 확인
        if (!FileUtil.checkUploadPath()) {
            throw new FileUploadPathNotExistException();
        }

        HttpServletRequest request = (HttpServletRequest) model.get("request");
        boolean hasError = false;
        MultipartRequest multi = null;
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

        // 게시글 DTO 생성
        BoardDto board = new BoardDto();
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
            model.put("board", board);
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

        if (hasError) {
            return "board/register";
        } else {
            return "redirect:/board?" + condition.getQueryString();
        }
    }
}
