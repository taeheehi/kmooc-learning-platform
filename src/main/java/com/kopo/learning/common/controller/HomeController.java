package com.kopo.learning.common.controller;

import com.kopo.learning.common.vo.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model, @SessionAttribute(name="user", required=false) User user) {
        model.addAttribute("user", user);
        return "common/home";
    }
}
