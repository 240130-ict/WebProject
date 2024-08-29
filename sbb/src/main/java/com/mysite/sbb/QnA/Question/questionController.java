package com.mysite.sbb.QnA.Question;

import com.mysite.sbb.QnA.Answer.answerForm;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RequestMapping("/question")
@RequiredArgsConstructor
@Controller
public class questionController {

    private final questionService questionService;
    private final UserService userService;

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "kw", defaultValue = "") String kw){
        Page<question> paging = this.questionService.getList(page,kw);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        return "cs/question_list";
    }

    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, answerForm AnswerForm){
        question question = this.questionService.getQuestion(id);
        model.addAttribute("question", question);
        return  "cs/question_detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String QuestionCreate(questionForm questionForm){
        return "cs/question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String QuestionCreate(@Valid questionForm questionForm, BindingResult bindingResult
    , Principal principal){
        if(bindingResult.hasErrors()){
            return "cs/question_form";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.questionService.create(questionForm.getSubject(), questionForm.getContent(),siteUser);
        return "redirect:/question/list" ;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String QuestionModify(questionForm questionForm, @PathVariable("id") Integer id, Principal principal) {
        question question = this.questionService.getQuestion(id);
        if(!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());
        return "cs/question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String QuestionModify(@Valid questionForm QuestionForm, BindingResult bindingResult,
                                 Principal principal, @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "cs/question_form";
        }
        question Question = this.questionService.getQuestion(id);
        if (!Question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.questionService.modify(Question, QuestionForm.getSubject(), QuestionForm.getContent());
        return String.format("redirect:/question/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String QuestionDelete(Principal principal, @PathVariable("id") Integer id) {
        question Question = this.questionService.getQuestion(id);
        if (!Question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.questionService.delete(Question);
        return "redirect:/question/list";
    }
}
