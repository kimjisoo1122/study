package com.study.validation;

import com.study.dto.BoardDto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 게시글 관련 유효성을 검증합니다.
 */
public interface BoardValidation {

    /**
     * 게시글을 검증합니다.
     * 1. 작성자는 3자 이상 5자 미만이여야 합니다.
     * 2. 제목은 4자 이상 16자 미만이여야 합니다.
     * 3. 내용은 4자 이상 2000자 미만이여야 합니다.
     * 4. 비밀번호는 대소문자 영문, 숫자, 특수문자가 각 하나 이상 포함되어야 합니다.
     * @param boardDto
     * @return 유효하면 TRUE 유효하지 않으면 FALSE 반환
     */
    static boolean isBoardValid(BoardDto boardDto) {
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

    /**
     * SHA256 단방향 해시알고리즘을 통해 암호화 합니다.
     * @param word
     * @return  64글자의 암호화된 문자열, 입력값이 null이면 ""반환
     */
    static String encrypt(String word) {
        if (word == null) {
            return null;
        }

        String encryptedWord = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(word.getBytes());

            StringBuilder sb = new StringBuilder();

            for (byte byteDatum : md.digest()) {
                sb.append(Integer.toString((byteDatum & 0xff) + 0x100, 16).substring(1));
            }

            encryptedWord = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encryptedWord;
    }
}
