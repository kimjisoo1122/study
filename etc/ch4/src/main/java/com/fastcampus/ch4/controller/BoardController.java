package com.fastcampus.ch4.controller;

import com.fastcampus.ch4.domain.BoardDto;
import com.fastcampus.ch4.domain.PageHandler;
import com.fastcampus.ch4.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    BoardService boardService;

    @GetMapping("/list")
    public String list(
            HttpServletRequest request,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            Model model) throws Exception {

        if (hasNotLogin(request)) {
            return "redirect:/login/login?toUrl=" + request.getRequestURL();
        }
        PageHandler pageHandler = new PageHandler(boardService.getCount(), page, pageSize);
        System.out.println("pageHandler = " + pageHandler);
        Map map = new HashMap();
        map.put("offset", (page - 1) * pageSize);
        map.put("pageSize", pageSize);
        List<BoardDto> list = boardService.getPage(map);
        model.addAttribute("list", list);
        model.addAttribute("ph", pageHandler);
        return "boardList";
    }

    private boolean hasNotLogin(HttpServletRequest request) {
        return request.getSession().getAttribute("id") == null;
    }
}
