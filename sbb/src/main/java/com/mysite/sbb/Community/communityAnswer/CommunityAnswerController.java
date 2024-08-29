package com.mysite.sbb.Community.communityAnswer;

import com.mysite.sbb.Community.communityQuestion.CommunityQuestion;
import com.mysite.sbb.Community.communityQuestion.CommunityQuestionService;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RequestMapping("/communityanswer")
@RequiredArgsConstructor
@Controller
public class CommunityAnswerController {

    private final CommunityQuestionService communityQuestionService;
    private final CommunityAnswerService communityAnswerService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String createAnswer(Model model, @PathVariable("id") Integer id, @Valid CommunityAnswerForm communityAnswerForm,
                               BindingResult bindingResult, Principal principal) {
        CommunityQuestion communityQuestion = this.communityQuestionService.getCommunityQuestion(id);
        SiteUser siteUser = userService.getUser(principal.getName());
        if (bindingResult.hasErrors()) {
            model.addAttribute("communityQuestion", communityQuestion);
        }
        this.communityAnswerService.create(communityQuestion, communityAnswerForm.getContent(), siteUser);
        return String.format("redirect:/communityquestion/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String communityAnswerModify(CommunityAnswerForm communityAnswerForm, @PathVariable("id") Integer id, Principal principal) {
        CommunityAnswer communityAnswer = this.communityAnswerService.getCommunityAnswer(id);
        if (!communityAnswer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        communityAnswerForm.setContent(communityAnswer.getContent());
        return "communityanswer_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String answerModify(@Valid CommunityAnswerForm communityAnswerForm, BindingResult bindingResult,
                               @PathVariable("id") Integer id, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "communityanswer_form";
        }
        CommunityAnswer communityAnswer = this.communityAnswerService.getCommunityAnswer(id);
        if (!communityAnswer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.communityAnswerService.modify(communityAnswer, communityAnswerForm.getContent());
        return String.format("redirect:/communityquestion/detail/%s", communityAnswer.getCommunityQuestion().getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String answerDelete(Principal principal, @PathVariable("id") Integer id) {
        CommunityAnswer communityAnswer = this.communityAnswerService.getCommunityAnswer(id);
        if (!communityAnswer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.communityAnswerService.delete(communityAnswer);
        return String.format("redirect:/communityquestion/detail/%s", communityAnswer.getCommunityQuestion().getId());
    }
}
