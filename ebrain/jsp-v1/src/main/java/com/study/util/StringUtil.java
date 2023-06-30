package com.study.util;

public class StringUtil {

    public static String nvl(String value) {
        if (value == null || value.trim().length() == 0) {
            return "";
        } else {
            return value;
        }
    }

    public static String nvl(String value, String ifNull) {
        if (value == null || value.trim().length() == 0) {
            return ifNull;
        } else {
            return value;
        }
    }
}
