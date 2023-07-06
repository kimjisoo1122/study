package com.study.servlet.board;

import com.study.dto.FileDto;
import com.study.service.FileService;
import com.study.servlet.ServletHandler;
import com.study.util.FileUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class BoardFileDownHandler implements ServletHandler {
    @Override
    public void getHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 파일dto 조회
        Long fileId = Long.parseLong(request.getParameter("fileId"));
        FileService fileService = FileService.getFileService();
        FileDto fileDto = fileService.findById(fileId);

        // 파일 생성
        File file = FileUtil.getUploadedFile(fileDto.getPhysicalName());

        // 한글 오리지널파일명 인코딩 utf-8로 읽어서 8859_1로 인코딩
        String fileName = new String(fileDto.getOriginalName().getBytes("utf-8"), "8859_1");

        try (FileInputStream is = new FileInputStream(file)) {
            // 지정되지 않은 파일 형식
            response.setContentType("application/octet-stream");
            // 다운로드 화면 출력
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

            try (OutputStream os = response.getOutputStream()) {
                int length;
                byte[] buffer = new byte[1024];
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
