package com.study.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ServletHandler {

    void getHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
    void postHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

}
