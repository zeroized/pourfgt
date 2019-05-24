package cn.edu.shu.pourfgt.controller;

import cn.edu.shu.pourfgt.dataSource.dao.GraduationTimetableRepository;
import cn.edu.shu.pourfgt.dataSource.dao.GraduationWorkRepository;
import cn.edu.shu.pourfgt.dataSource.entity.GraduationTimetable;
import cn.edu.shu.pourfgt.dataSource.entity.GraduationWork;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/teacher/graduation")
public class TeacherGraduationController {
    private final GraduationTimetableRepository graduationTimetableRepository;
    private int currentYear = 2018;

    private final GraduationWorkRepository graduationWorkRepository;
    private int currentSemester = 0;

    public TeacherGraduationController(GraduationWorkRepository graduationWorkRepository,
                                       GraduationTimetableRepository graduationTimetableRepository) {
        this.graduationWorkRepository = graduationWorkRepository;
        this.graduationTimetableRepository = graduationTimetableRepository;
    }

    @InitBinder
    public void initDate(WebDataBinder webDataBinder) {
        webDataBinder.addCustomFormatter(new DateFormatter("yyyy-MM-dd"));
    }

    @ModelAttribute
    public void addLeftNav(Model model) {
        model.addAttribute("leftNavId", 1);
        model.addAttribute("year", currentYear);
        model.addAttribute("semester", currentSemester);
    }

    @RequestMapping("/list")
    public ModelAndView list() {
        ModelAndView mav = new ModelAndView("teacher/graduation/list");
        List<GraduationWork> works = graduationWorkRepository
                .findByYearAndSemester(currentYear, currentSemester);
        mav.addObject("works", works);
        mav.addObject("navId", 0);
        return mav;
    }

    @PostMapping("/addWork")
    public String addWork(long studentId, String studentName, String title,
                          String description) {
        GraduationWork newWork = new GraduationWork();
        newWork.setStudentId(studentId);
        newWork.setStudentName(studentName);
        newWork.setTitle(title);
        newWork.setDescription(description);
        newWork.setYear(currentYear);
        newWork.setSemester(currentSemester);
        return "redirect:/teacher/graduation/list";
    }

    @RequestMapping("/timetable")
    public ModelAndView timetable() {
        ModelAndView mav = new ModelAndView("teacher/graduation/timetable");
        List<GraduationTimetable> events = graduationTimetableRepository
                .findByYearAndSemester(currentYear, currentSemester);
        mav.addObject("events", events);
        mav.addObject("navId", 1);
        return mav;
    }

    @PostMapping("/createTimetable")
    public String createTimetableEvent(String event, Date keyDate) {
        GraduationTimetable newEvent = new GraduationTimetable();
        newEvent.setEvent(event);
        newEvent.setKeyDate(keyDate.getTime());
        newEvent.setYear(currentYear);
        newEvent.setSemester(currentSemester);
        graduationTimetableRepository.save(newEvent);
        return "redirect:/teacher/graduation/timetable";
    }
}
