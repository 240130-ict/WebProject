package com.mysite.sbb;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @GetMapping("/index")
    public String index() {
        return "/index";
    }

    @GetMapping("/introduce")
    public String introduce(){
        return "/introduce";
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/index";
    }

    @GetMapping("/qna")
    public String qna() {
        return "/qna_list";
    }

    @GetMapping("/faq")
    public String faq() {
        return "/cs/faq";
    }
    @RequestMapping("/default")
    public String defaultAfterLogin(HttpServletRequest request, Authentication authentication) {
        if (request.isUserInRole("ROLE_TEACHER")) {
            return "redirect:/teacher/home";
        } else if (request.isUserInRole("ROLE_STUDENT")) {
            return "redirect:/student/home";
        }
        return "redirect:/";
    }


}
