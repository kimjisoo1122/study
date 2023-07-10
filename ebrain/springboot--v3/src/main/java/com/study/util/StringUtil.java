package com.study.util;

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
     * String값의 null과 ""(빈값)체크
     * @param value
     * @return null or empty -> true
     */
    static boolean isEmpty(String value) {
        return value == null || value.trim().length() == 0;
    }

    /**
     * 스트링 문자열이 정수 또는 실수인지 확인한다.
     * @param str
     * @return
     */
    static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    /**
     * 문자열을 Long으로 변환 합니다.
     * @param value 유효한 숫자 문자열
     * @return 유효하지 않으면 널을 반환합니다.
     */
    static Long toLong(String value) {
        return StringUtil.isNumeric(value) ? Long.valueOf(value) : null;
    }
}
