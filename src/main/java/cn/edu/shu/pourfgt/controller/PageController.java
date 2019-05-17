package cn.edu.shu.pourfgt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class PageController {
    @RequestMapping
    public String home() {
        return "redirect:/course";
    }

    @RequestMapping("course")
    public String courseHome() {
        return "redirect:/course/list";
    }

    @RequestMapping("postgraduate")
    public String postgraduateHome() {
        return "postgraduate/home";
    }

    @RequestMapping("graduationDesign")
    public String graduationDesignHome() {
        return "graduationDesign/home";
    }
}
