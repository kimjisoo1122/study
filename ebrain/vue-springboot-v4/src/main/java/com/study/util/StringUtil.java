package com.study.util;

import org.springframework.util.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 문자열 Util을 제공합니다.
 */
public interface StringUtil {

    /**
     * 값이 null이면 ""반환 외 기존값 반환
     * @param value
     * @return "" or value
     */
    static String nvl(String value) {
        if (value == null || value.trim().length() == 0) {
            return "";
        } else {
            return value;
        }
    }

    /**
     * 값이 null이면 ifNull반환 외 기존값 반환
     * @param value
     * @param ifNull
     * @return ifNull or value
     */
    static String nvl(String value, String ifNull) {
        if (value == null || value.trim().length() == 0) {
            return ifNull;
        } else {
            return value;
        }
    }

    /**
     * 문자열을 암호화 합니다
     * @param word 원본
     * @return 암호화된 문자열
     */
    static String encrypt(String word) {
        if (!StringUtils.hasText(word)) {
            return "";
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
            throw new RuntimeException("암호화에 실패하였습니다.", e);
        }

        return encryptedWord;
    }
}
