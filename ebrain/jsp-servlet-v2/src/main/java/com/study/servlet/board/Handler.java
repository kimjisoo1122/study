package com.study.servlet.board;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface Handler {


    void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
