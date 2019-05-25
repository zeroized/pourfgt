package cn.edu.shu.pourfgt.controller;

import cn.edu.shu.pourfgt.dataSource.dao.*;
import cn.edu.shu.pourfgt.dataSource.entity.*;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/teacher/postgraduate")
public class TeacherPostgraduateController {
    private final PostgraduateStudentRepository postgraduateStudentRepository;
    private final PostgraduateTimetableRepository postgraduateTimetableRepository;
    private final PostgraduatePostRepository postgraduatePostRepository;
    private final PostgraduateStudentMessageRepository postgraduateStudentMessageRepository;
    private final PostgraduateStudentSubmissionRepository postgraduateStudentSubmissionRepository;
    @Value("${course.announcement.file-path}")
    private String postFileDir;

    public TeacherPostgraduateController(PostgraduateStudentRepository postgraduateStudentRepository,
                                         PostgraduateTimetableRepository postgraduateTimetableRepository,
                                         PostgraduatePostRepository postgraduatePostRepository,
                                         PostgraduateStudentMessageRepository postgraduateStudentMessageRepository,
                                         PostgraduateStudentSubmissionRepository postgraduateStudentSubmissionRepository) {
        this.postgraduateStudentRepository = postgraduateStudentRepository;
        this.postgraduateTimetableRepository = postgraduateTimetableRepository;
        this.postgraduatePostRepository = postgraduatePostRepository;
        this.postgraduateStudentMessageRepository = postgraduateStudentMessageRepository;
        this.postgraduateStudentSubmissionRepository = postgraduateStudentSubmissionRepository;
    }

    @InitBinder
    public void initDate(WebDataBinder webDataBinder) {
        webDataBinder.addCustomFormatter(new DateFormatter("yyyy-MM-dd"));
    }

    @ModelAttribute
    public void addLeftNavId(Model model) {
        model.addAttribute("leftNavId", 2);
    }

    @RequestMapping("/list")
    public ModelAndView list() {
        ModelAndView mav = new ModelAndView("teacher/postgraduate/list");
        List<PostgraduateStudent> students = postgraduateStudentRepository.findByActive(true);
        List<PostgraduateStudent> graduated = postgraduateStudentRepository.findByActive(false);
        mav.addObject("students", students);
        mav.addObject("graduated", graduated);
        mav.addObject("navId", 0);
        return mav;
    }

    @PostMapping("/addStudent")
    public String addStudent(long studentId, String studentName, int year, int type) {
        PostgraduateStudent newStudent = new PostgraduateStudent();
        newStudent.setStudentId(studentId);
        newStudent.setStudentName(studentName);
        newStudent.setYear(year);
        newStudent.setType(type);
        postgraduateStudentRepository.save(newStudent);
        return "redirect:/teacher/postgraduate/list";
    }

    @RequestMapping("/graduate")
    public String graduate(long id) {
        PostgraduateStudent student = postgraduateStudentRepository.findById(id);
        postgraduateStudentRepository.delete(student);
        student.setActive(false);
        postgraduateStudentRepository.save(student);
        return "redirect:/teacher/postgraduate/list";
    }

    @RequestMapping("/timetable")
    public ModelAndView timetable(@RequestParam(defaultValue = "2019") Integer year,
                                  @RequestParam(defaultValue = "0") Integer type) {
        ModelAndView mav = new ModelAndView("teacher/postgraduate/timetable");
        List<PostgraduateTimetable> events = postgraduateTimetableRepository
                .findByForYearAndForType(year, type);
        mav.addObject("events", events);
        mav.addObject("navId", 1);
        return mav;
    }

    @PostMapping("/createTimetable")
    public String createTimetable(String event, Date keyDate, int year, int type) {
        PostgraduateTimetable newTimetable = new PostgraduateTimetable();
        newTimetable.setForYear(year);
        newTimetable.setForType(type);
        newTimetable.setEvent(event);
        newTimetable.setKeyDate(keyDate.getTime());
        postgraduateTimetableRepository.save(newTimetable);
        return "redirect:/teacher/postgraduate/timetable";
    }

    @RequestMapping("/post")
    public ModelAndView post() {
        ModelAndView mav = new ModelAndView("teacher/postgraduate/post");
        List<PostgraduatePost> posts = postgraduatePostRepository.findAll();
        mav.addObject("posts", posts);
        mav.addObject("navId", 2);
        return mav;
    }

    @PostMapping("/addPost")
    public String addPost(String title, String content, int year, int type,
                          @RequestParam(required = false) MultipartFile file,
                          @RequestParam(required = false) Date notifyDate) throws IOException {
        PostgraduatePost newPost = new PostgraduatePost();
        long currentTime = new Date().getTime();
        newPost.setTitle(title);
        newPost.setCreateTime(currentTime);
        newPost.setYear(year);
        newPost.setType(type);
        newPost.setContent(content);
        if (file != null) {
            String localFilePath = postFileDir + currentTime + "_" + file.getOriginalFilename();
            File localFile = new File(localFilePath);
            file.transferTo(localFile);
            newPost.setHasFile(true);
            newPost.setFilePath(localFilePath);
        }
        if (notifyDate != null) {
            newPost.setHasNotification(true);
            newPost.setNotifyDate(notifyDate.getTime());
        }
        postgraduatePostRepository.save(newPost);
        return "redirect:/teacher/postgraduate/post";
    }

    @GetMapping("/getPost")
    public @ResponseBody
    PostgraduatePost getPost(long id) {
        return postgraduatePostRepository.findById(id);
    }

    @RequestMapping("/message")
    public ModelAndView message() {
        ModelAndView mav = new ModelAndView("teacher/postgraduate/message");
        List<PostgraduateStudentMessage> messages = postgraduateStudentMessageRepository.findAll();

        mav.addObject("messages", messages);
        mav.addObject("navId", 3);
        return mav;
    }

    @GetMapping("/getMessage")
    public @ResponseBody
    PostgraduateStudentMessage getMessage(long id) {
        return postgraduateStudentMessageRepository.findById(id);
    }

    @RequestMapping("/submission")
    public ModelAndView submission(@RequestParam(required = false) Long studentId) {
        ModelAndView mav = new ModelAndView("teacher/postgraduate/submission");
        List<PostgraduateStudentSubmission> submissions;
        if (studentId != null) {
            submissions = postgraduateStudentSubmissionRepository
                    .findByStudentId(studentId);
        } else {
            submissions = new ArrayList<>();
        }
        mav.addObject("submissions", submissions);
        mav.addObject("navId", 4);
        return mav;
    }

    @GetMapping("/getSubmission")
    public PostgraduateStudentSubmission getSubmission(long id) {
        return postgraduateStudentSubmissionRepository.findById(id);
    }

}
