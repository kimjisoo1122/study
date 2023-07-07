package com.study.servlet.board;

import com.study.dto.FileDto;
import com.study.service.FileService;
import com.study.util.FileUtil;
import com.study.util.StringUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 첨부파일을 다운로드 하는 서블릿 입니다.
 */
public class FileDownServlet implements MyServlet {

    private final FileService fileService = FileService.getFileService();

    @Override
    public String handle(Map<String, String> paramMap, Map<String, Object> model) throws IOException {
        HttpServletResponse response = (HttpServletResponse) model.get("response");

        // 파일dto 조회
        Long fileId = StringUtil.toLong(paramMap.get("fileId"));
        if (fileId == null) {
            return null;
        }

        FileDto findFile = fileService.findById(fileId);

        // 업로드된 파일을 생성합니다.
        File file = FileUtil.createFile(findFile.getPhysicalName());

        // 한글 오리지널파일명 인코딩 utf-8로 읽어서 8859_1로 인코딩합니다.
        String originalFileName = new String(findFile.getOriginalName()
                .getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);

        try (FileInputStream is = new FileInputStream(file)) {
            // 지정되지 않은 파일 형식
            response.setContentType("application/octet-stream");
            // 다운로드 화면 출력
            response.setHeader("Content-Disposition", "attachment; filename=" + originalFileName);

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
        return null;
    }
}
