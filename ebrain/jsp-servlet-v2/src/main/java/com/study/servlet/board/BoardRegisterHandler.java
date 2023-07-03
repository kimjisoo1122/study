package com.study.servlet.board;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.study.dto.BoardDto;
import com.study.dto.BoardSearchCondition;
import com.study.servlet.ServletHandler;
import com.study.util.FileUtil;
import com.study.util.StringUtil;
import com.study.validation.BoardValidation;
import jdk.jfr.SettingControl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BoardRegisterHandler implements ServletHandler {

    @Override
    public void getHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 검색 조건
        BoardSearchCondition condition = new BoardSearchCondition();
        condition.setConditionByReq(request);

        // 파라미터 파싱
        BoardDto boardDto = new BoardDto();
        String writer = StringUtil.nvl(request.getParameter("writer"));
        String password = StringUtil.nvl(request.getParameter("password"));
        String title = StringUtil.nvl(request.getParameter("title"));
        String content = StringUtil.nvl(request.getParameter("content"));
        String categoryId = request.getParameter("categoryId");
        if (StringUtil.isNumeric(categoryId)) {
            boardDto.setCategoryId(Long.valueOf(categoryId));
        }
        boardDto.setWriter(writer);
        boardDto.setPassword(password);
        boardDto.setTitle(title);
        boardDto.setContent(content);





    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        // 업로드 폴더 확인
//        if (FileUtil.checkUploadPath()) {
//            MultipartRequest multi = null;
//            try {
//                // TODO 파일명 난수로 저장 (한글명 X)
//                // TODO 파일중복일시 커스텀정책 만들기
//                multi = new MultipartRequest(
//                        request, FileUtil.FILE_PATH, FileUtil.BOARD_FILE_MAX_SIZE, FileUtil.ENC_TYPE,
//                        new DefaultFileRenamePolicy());
//            } catch (IOException e) {
//                e.printStackTrace();
//                request.setAttribute("fileError", "파일사이즈는 10MB를 넘을 수 없습니다.");
//                request.setAttribute("condition", condition);
//                response.sendRedirect("/board/register.jsp");
//            }
//        } else {
//
//        }
//
//        // 파라미터 파싱
//        String writer = multi.getParameter("writer");
//        String password = multi.getParameter("password");
//        String title = multi.getParameter("title");
//        String content = multi.getParameter("content");
//        String categoryId = multi.getParameter("categoryId");
//
//        BoardValidation.validBoard()
    }
}
