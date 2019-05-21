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

    @RequestMapping("/postgraduate")
    public String postgraduateHome() {
        return "postgraduate/home";
    }

    @RequestMapping("/graduationDesign")
    public String graduationDesignHome() {
        return "graduationDesign/home";
    }
}
