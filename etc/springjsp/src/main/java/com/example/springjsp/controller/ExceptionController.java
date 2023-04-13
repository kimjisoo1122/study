package com.example.springjsp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class ExceptionController {

    @RequestMapping("/ex")
    public String ex() throws Exception {
        throw new Exception("EX");
    }
}
