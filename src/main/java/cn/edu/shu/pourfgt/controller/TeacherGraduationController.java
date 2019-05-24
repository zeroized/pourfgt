package cn.edu.shu.pourfgt.controller;

import cn.edu.shu.pourfgt.dataSource.dao.GraduationPostRepository;
import cn.edu.shu.pourfgt.dataSource.dao.GraduationTimetableRepository;
import cn.edu.shu.pourfgt.dataSource.dao.GraduationWorkRepository;
import cn.edu.shu.pourfgt.dataSource.entity.GraduationPost;
import cn.edu.shu.pourfgt.dataSource.entity.GraduationTimetable;
import cn.edu.shu.pourfgt.dataSource.entity.GraduationWork;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/teacher/graduation")
public class TeacherGraduationController {
    private final GraduationTimetableRepository graduationTimetableRepository;
    private int currentYear = 2018;
    private final GraduationPostRepository graduationPostRepository;
    @Value("${course.announcement.file-path}")
    private String postFileDir;
    private final GraduationWorkRepository graduationWorkRepository;
    private int currentSemester = 0;

    public TeacherGraduationController(GraduationWorkRepository graduationWorkRepository,
                                       GraduationTimetableRepository graduationTimetableRepository, GraduationPostRepository graduationPostRepository) {
        this.graduationWorkRepository = graduationWorkRepository;
        this.graduationTimetableRepository = graduationTimetableRepository;
        this.graduationPostRepository = graduationPostRepository;
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
        graduationWorkRepository.save(newWork);
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

    @RequestMapping("/post")
    public ModelAndView post() {
        ModelAndView mav = new ModelAndView("teacher/graduation/post");
        List<GraduationPost> posts = graduationPostRepository
                .findByYearAndSemester(currentYear, currentSemester);
        mav.addObject("posts", posts);
        mav.addObject("navId", 2);
        return mav;
    }

    @PostMapping("/addPost")
    public String addPost(String title, String content,
                          @RequestParam(required = false) MultipartFile file,
                          @RequestParam(required = false) Date notifyDate) throws IOException {
        GraduationPost newPost = new GraduationPost();
        long currentTime = new Date().getTime();
        newPost.setCreateTime(currentTime);
        newPost.setYear(currentYear);
        newPost.setSemester(currentSemester);
        newPost.setTitle(title);
        newPost.setContent(content);
        if (file != null) {
            String localFilePath = postFileDir + currentYear + "-" + currentSemester + "_" + currentTime + "_" + file.getOriginalFilename();
            File localFile = new File(localFilePath);
            file.transferTo(localFile);
            newPost.setHasFile(true);
            newPost.setFilePath(localFilePath);
        }
        if (notifyDate != null) {
            newPost.setHasNotification(true);
            newPost.setNotifyDate(notifyDate.getTime());
        }
        graduationPostRepository.save(newPost);
        return "redirect:/teacher/graduation/post";
    }

    @GetMapping("/getPost")
    public @ResponseBody
    GraduationPost getPost(long id) {
        return graduationPostRepository.findById(id);
    }

}
