package com.mysite.sbb.myexam;

import com.mysite.sbb.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequiredArgsConstructor
@RequestMapping("/studentmanage")
@Controller
public class StudentManageController {

    private final MyExamService myExamService;
    private final UserService userService;


    @GetMapping("/list")
    public String list(Model model, @RequestParam(value="page", defaultValue="0") int page,
                       @RequestParam(value = "kw", defaultValue = "") String kw ,
                       @RequestParam(value="category", defaultValue="tempowner") String category,
                       Principal principal) {
        if (kw.isEmpty()) {
            kw = principal.getName();  // 기본적으로 현재 사용자의 이름을 검색어로 사용
        }
        Page<MyExam> paging = this.myExamService.getList(page, kw,category);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
//        List<Template> questionsList = this.templateService.getList();
//        model.addAttribute("questionsList", questionsList);
        return "my/studentmanage_list";
    }

    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id) {
        MyExam myexam = this.myExamService.getMyExam(id);
        model.addAttribute("myexam", myexam);
        return "my/studentmanage_detail";
    }

    @PostMapping("/detail/{id}")
    public String questionModify(@RequestParam(value = "status") String status,
                                 @RequestParam(value = "point") String point,
                                 Principal principal, @PathVariable("id") Integer id) {
        MyExam myexam = this.myExamService.getMyExam(id);
        this.myExamService.modify(myexam, status,point);
        return "redirect:/studentmanage/list";
    }

}
