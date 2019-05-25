package cn.edu.shu.pourfgt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class TeacherPageController {
    @RequestMapping("/teacher")
    public String home() {
        return "redirect:/teacher/course/list";
    }

    @RequestMapping("/teacher/postgraduate")
    public String postgraduateHome() {
        return "redirect:/teacher/postgraduate/list";
    }

    @RequestMapping("/teacher/graduation")
    public String graduationHome() {
        return "redirect:/teacher/graduation/list";
    }
}
