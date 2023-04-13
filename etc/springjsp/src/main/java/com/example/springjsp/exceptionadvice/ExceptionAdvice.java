package com.example.springjsp.exceptionadvice;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice("com.example.springjsp.controller")
public class ExceptionAdvice {

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String catcher(Exception ex) {
        return "ex";
    }
}
