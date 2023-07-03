package com.study.util;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.Enumeration;

public interface RequestUtil {

    static <T> T setParamByReq(HttpServletRequest request, T dto) throws NoSuchFieldException {
        Class<?> clazz = dto.getClass();
        Field[] fields = dto.getClass().getDeclaredFields();

        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String param = params.nextElement();
            if (clazz.getDeclaredField(param) != null) {

            }
        }

        return dto;
    }
}
