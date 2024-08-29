package com.mysite.sbb.Community.communityQuestion;

import com.mysite.sbb.Community.communityAnswer.CommunityAnswerForm;
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

@RequestMapping("/communityquestion")
@RequiredArgsConstructor
@Controller
public class CommunityQuestionController {

    private final CommunityQuestionService communityQuestionService;
    private final UserService userService;

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "kw", defaultValue = "") String kw){
        Page<CommunityQuestion> paging = this.communityQuestionService.getList(page,kw);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        return "community/communityquestion_list";
    }

    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, CommunityAnswerForm communityAnswerForm){
        CommunityQuestion communityQuestion = this.communityQuestionService.getCommunityQuestion(id);
        model.addAttribute("communityQuestion", communityQuestion);
        return  "community/communityquestion_detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String communityQuestionCreate(CommunityQuestionForm communityQuestionForm){
        return "community/communityquestion_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String communityQuestionCreate(@Valid CommunityQuestionForm communityQuestionForm, BindingResult bindingResult
    , Principal principal){
        if(bindingResult.hasErrors()){
            return "community/communityquestion_form";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.communityQuestionService.create(communityQuestionForm.getSubject(), communityQuestionForm.getContent(),siteUser);
        return "redirect:/communityquestion/list" ;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String communityQuestionModify(CommunityQuestionForm communityQuestionForm, @PathVariable("id") Integer id, Principal principal) {
        CommunityQuestion CommunityQuestion = this.communityQuestionService.getCommunityQuestion(id);
        if(!CommunityQuestion.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        communityQuestionForm.setSubject(CommunityQuestion.getSubject());
        communityQuestionForm.setContent(CommunityQuestion.getContent());
        return "community/communityquestion_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String communityQuestionModify(@Valid CommunityQuestionForm communityQuestionForm, BindingResult bindingResult,
                                 Principal principal, @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "community/communityquestion_form";
        }
        CommunityQuestion communityQuestion = this.communityQuestionService.getCommunityQuestion(id);
        if (!communityQuestion.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.communityQuestionService.modify(communityQuestion, communityQuestionForm.getSubject(), communityQuestionForm.getContent());
        return String.format("redirect:/communityquestion/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String communityQuestionDelete(Principal principal, @PathVariable("id") Integer id) {
        CommunityQuestion communityQuestion = this.communityQuestionService.getCommunityQuestion(id);
        if (!communityQuestion.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.communityQuestionService.delete(communityQuestion);
        return "redirect:/communityquestion/list";
    }
}
