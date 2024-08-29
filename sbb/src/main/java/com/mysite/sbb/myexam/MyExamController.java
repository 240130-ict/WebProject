package com.mysite.sbb.myexam;

import com.mysite.sbb.template.TemplateForm;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequiredArgsConstructor
@RequestMapping("/myexam")
@Controller
public class MyExamController {
    private final MyExamService myExamService;
    private final UserService userService;


    @GetMapping("/list")
    public String list(Model model, @RequestParam(value="page", defaultValue="0") int page,
                       @RequestParam(value = "kw", defaultValue = "") String kw ,
                       @RequestParam(value="category", defaultValue="username") String category,
                       Principal principal) {
        if (kw.isEmpty()) {
            kw = principal.getName();  // 기본적으로 현재 사용자의 이름을 검색어로 사용
        }
        Page<MyExam> paging = this.myExamService.getList(page, kw,category);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
//        List<Template> questionsList = this.templateService.getList();
//        model.addAttribute("questionsList", questionsList);
        return "my/myexam_list";
    }

    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id) {
        MyExam myexam = this.myExamService.getMyExam(id);
        model.addAttribute("myexam", myexam);
        return "my/myexam_detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String tmeplateCreate(TemplateForm templateForm){
        return "template_form";
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String create(@RequestParam(value = "subject") String subject,
                         @RequestParam(value = "title") String title,
                         @RequestParam(value = "documentid") String documentid,
                         @RequestParam(value = "templateid") String temlplateid,
                         @RequestParam(value = "status") String status,
                         Principal principal,@RequestParam(value = "tempowner") String tempowner ){
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.myExamService.create(subject,title, documentid,temlplateid,siteUser,tempowner,status);
        return "redirect:/template/list";
    }
}
