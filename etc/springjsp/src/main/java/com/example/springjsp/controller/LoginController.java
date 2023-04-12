package com.example.springjsp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/login")
public class LoginController {

    @GetMapping("/login")
    public String loginForm() {
        return "loginForm";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam(value = "id") String id,
            @RequestParam(value = "pwd") String pwd,
            @RequestParam(name = "rememberId") boolean rememberId,
            @RequestParam(name = "toUrl") String toUrl,
            HttpServletResponse response,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {

        if (!loginCheck(id, pwd)) {
            redirectAttributes.addAttribute("msg", "id 또는 pwd가 일치하지 않습니다.");
            return "redirect:/login/login";
        }

        HttpSession session = request.getSession(true);
        session.setAttribute("id", id);

        if (rememberId) {
            Cookie cookie = new Cookie("id", id);
            cookie.setMaxAge(1800);
            response.addCookie(cookie);
        } else {
            Cookie cookie = new Cookie("id", id);
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }

        toUrl = toUrl == null || toUrl.isEmpty() ? "/" : toUrl;
        Object id1 = session.getAttribute("id");
        System.out.println("id1 = " + id1);
        return "redirect:" + toUrl;
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
    private boolean loginCheck(String id, String pwd) {
        return "asdf".equals(id) && "1234".equals(pwd);
    }
}
