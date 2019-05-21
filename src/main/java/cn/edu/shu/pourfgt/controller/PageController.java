package cn.edu.shu.pourfgt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class PageController {
    @RequestMapping("/")
    public String home() {
        return "redirect:/teacher/course";
    }

    @RequestMapping("teacher/course")
    public String courseHome() {
        return "redirect:/teacher/course/list";
    }

    @RequestMapping("teacher/postgraduate")
    public String postgraduateHome() {
        return "postgraduate/home";
    }

    @RequestMapping("teacher/graduationDesign")
    public String graduationDesignHome() {
        return "graduationDesign/home";
    }
}
