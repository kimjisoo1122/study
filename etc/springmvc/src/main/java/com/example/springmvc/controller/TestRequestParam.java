package com.example.springmvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller()
public class TestRequestParam {
    @ResponseBody
    @RequestMapping("/hello")
    public String hello(@RequestParam(name = "num", required = false) int num) {
        System.out.println("num = " + num);
        return "ok";
    }
}
