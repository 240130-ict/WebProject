package com.mysite.sbb.notices;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/notices")
public class NoticeController {

    private final NoticeService noticeService;

    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("notices", noticeService.findAll());
        return "notice/notice_list";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable("id") Long id, Model model) {
        model.addAttribute("notice", noticeService.findById(id));
        return "notice/notice_view";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("notice", new Notice());
        return "notice/notice_form";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Notice notice) {
        noticeService.save(notice);
        return "redirect:/notices";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("notice", noticeService.findById(id));
        return "notice/notice_form";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, @ModelAttribute Notice notice) {
        notice.setId(id);
        noticeService.save(notice);
        return "redirect:/notices";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        noticeService.deleteById(id);
        return "redirect:/notices";
    }
}

