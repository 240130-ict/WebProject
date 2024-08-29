package com.mysite.sbb.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping("/signup")
    public String signup(UserCreateForm userCreateForm) {
        return "signup_form";
    }

    @PostMapping("/signup")
    public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "signup_form";
        }

        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            bindingResult.rejectValue("Password2", "패스워드가 일치하지 않습니다.");
            return "signup_form";
        }
        try {
            userService.create(userCreateForm.getUsername(), userCreateForm.getEmail(), userCreateForm.getPassword1(),userCreateForm.getRole());
        }catch(DataIntegrityViolationException e){
            e.printStackTrace();
            bindingResult.reject("singupFailed","이미 등록된 사용자입니다.");
            return "signup_form";
        }catch(Exception e){
            e.printStackTrace();
            bindingResult.reject("sigupFailed",e.getMessage());
            return "signup_form";
        }
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(){
        return "login_form";
    }

    @GetMapping("/mypage")
    public String mypage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();

        Optional<SiteUser> siteUserOptional = this.userRepository.findByUsername(username);

        if (siteUserOptional.isPresent()) {
            SiteUser siteUser = siteUserOptional.get();
            model.addAttribute("siteUser", siteUser);
        } else {
            // 사용자가 없을 경우 예외 처리
            model.addAttribute("errorMessage", "사용자 정보를 찾을 수 없습니다.");
        }
        return "my/mypage";
    }
}
