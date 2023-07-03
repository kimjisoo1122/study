package com.study.validation;

import com.study.dto.BoardDto;

public interface BoardValidation {

    static boolean validBoard(BoardDto boardDto) {
        if (boardDto == null) {
            return false;
        }

        String writer = boardDto.getWriter();
        String password = boardDto.getPassword();
        String title = boardDto.getTitle();
        String content = boardDto.getContent();

        if (writer == null || (writer.length() < 3 || writer.length() > 5)) {
            return false;
        } else if (password == null
                || (password.length() < 4 || password.length() > 15)
                || !password.matches("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).*$") ){
            return false;
        } else if (title == null || (title.length() < 4 || title.length() > 99)) {
            return false;
        } else if (content == null || (content.length() < 4 || content.length() > 1999)) {
            return false;
        }

        return true;
    }
}
