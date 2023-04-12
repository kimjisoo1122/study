package com.example.springjsp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/board")
public class BoardController {

    @RequestMapping("/list")
    public String boardList(
            HttpServletRequest request,
            HttpSession session) {

        if (session.getAttribute("id") == null) {
            return "redirect:/login/login?toUrl=" + request.getRequestURL();
        }
        return "boardList";
    }
}
