package cn.edu.shu.pourfgt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class StudentPageController {
    @RequestMapping("/student")
    public String home() {
        return "redirect:/student/course/list";
    }
}
