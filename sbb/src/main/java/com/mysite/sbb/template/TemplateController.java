package com.mysite.sbb.template;

import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequiredArgsConstructor
@RequestMapping("/template")
@Controller
public class TemplateController {

    private final TemplateService templateService;
    private final UserService userService;


    @GetMapping("/list")
    public String list(Model model, @RequestParam(value="page", defaultValue="0") int page,
                       @RequestParam(value = "kw", defaultValue = "") String kw,
                       @RequestParam(value = "category", defaultValue = "category") String category) {
        Page<Template> paging = this.templateService.getList(page, kw, category);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
//        List<Template> questionsList = this.templateService.getList();
//        model.addAttribute("questionsList", questionsList);
        return "template/template_list";
    }

    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id) {
        Template template = this.templateService.getTemplate(id);
        model.addAttribute("template", template);
        return "template/template_detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String tmeplateCreate(TemplateForm templateForm){
        return "template/template_form";
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String create(@Valid TemplateForm templateForm, BindingResult bindingResult, Principal principal){
        SiteUser siteUser = this.userService.getUser(principal.getName());
        if (bindingResult.hasErrors()) {
            return "template/template_form";
        }
        this.templateService.create(templateForm.getTemplateName(),templateForm.getTemplateId(),siteUser,templateForm.getTemplateSubject());
        return "redirect:/template/list";
    }
}
