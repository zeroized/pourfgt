package cn.edu.shu.pourfgt.controller;

import cn.edu.shu.pourfgt.dataSource.dao.GraduationWorkRepository;
import cn.edu.shu.pourfgt.dataSource.entity.GraduationWork;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/teacher/graduation")
public class TeacherGraduationController {
    private final GraduationWorkRepository graduationWorkRepository;

    public TeacherGraduationController(GraduationWorkRepository graduationWorkRepository) {
        this.graduationWorkRepository = graduationWorkRepository;
    }

    @ModelAttribute
    public void addLeftNav(Model model) {
        model.addAttribute("leftNavId", 1);
    }

    @RequestMapping("/list")
    public ModelAndView list() {
        ModelAndView mav = new ModelAndView("teacher/graduation/list");
        int currentYear = 2018;
        int currentSemester = 0;
        List<GraduationWork> works = graduationWorkRepository.findByYearAndSemester(currentYear, currentSemester);
        mav.addObject("works", works);
        mav.addObject("year", currentYear);
        mav.addObject("semester", currentSemester);
        mav.addObject("navId", 0);
        return mav;
    }
}
