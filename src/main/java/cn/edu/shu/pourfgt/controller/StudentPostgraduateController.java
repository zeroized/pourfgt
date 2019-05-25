package cn.edu.shu.pourfgt.controller;

import cn.edu.shu.pourfgt.dataSource.dao.PostgraduatePostRepository;
import cn.edu.shu.pourfgt.dataSource.dao.PostgraduateStudentMessageRepository;
import cn.edu.shu.pourfgt.dataSource.dao.PostgraduateStudentSubmissionRepository;
import cn.edu.shu.pourfgt.dataSource.dao.PostgraduateTimetableRepository;
import cn.edu.shu.pourfgt.dataSource.entity.PostgraduatePost;
import cn.edu.shu.pourfgt.dataSource.entity.PostgraduateStudentMessage;
import cn.edu.shu.pourfgt.dataSource.entity.PostgraduateStudentSubmission;
import cn.edu.shu.pourfgt.dataSource.entity.PostgraduateTimetable;
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
import java.util.stream.Collectors;

@Controller
@RequestMapping("/student/postgraduate")
public class StudentPostgraduateController {
    private final PostgraduateTimetableRepository postgraduateTimetableRepository;
    private final PostgraduatePostRepository postgraduatePostRepository;
    private final PostgraduateStudentMessageRepository postgraduateStudentMessageRepository;
    private final PostgraduateStudentSubmissionRepository postgraduateStudentSubmissionRepository;
    @Value("${course.announcement.file-path}")
    private String postFileDir;

    public StudentPostgraduateController(PostgraduateTimetableRepository postgraduateTimetableRepository, PostgraduatePostRepository postgraduatePostRepository, PostgraduateStudentMessageRepository postgraduateStudentMessageRepository, PostgraduateStudentSubmissionRepository postgraduateStudentSubmissionRepository) {
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
    public void addLeftNav(Model model) {
        model.addAttribute("leftNavId", 2);
    }

    @RequestMapping("/timetable")
    public ModelAndView timetable() {
        ModelAndView mav = new ModelAndView("student/postgraduate/timetable");
        int year = 2019;
        int type = 0;
        List<PostgraduateTimetable> events = postgraduateTimetableRepository
                .findByForYearAndForType(year, type);
        mav.addObject("events", events);
        mav.addObject("navId", 0);
        return mav;
    }

    @RequestMapping("/post")
    public ModelAndView post() {
        ModelAndView mav = new ModelAndView("student/postgraduate/post");
        int year = 2019;
        int type = 0;
        List<PostgraduatePost> posts = postgraduatePostRepository
                .findAll();
        List<PostgraduatePost> displayPosts = posts.stream()
                .filter(x ->
                        ((x.getYear() == year) || (x.getYear() == -1))
                                &&
                                ((x.getType() == type) || (x.getType() == -1))
                ).collect(Collectors.toList());
        mav.addObject("posts", displayPosts);
        mav.addObject("navId", 1);
        return mav;
    }

    @GetMapping("/getPost")
    public PostgraduatePost getPost(long id) {
        return postgraduatePostRepository.findById(id);
    }

    @RequestMapping("/message")
    public ModelAndView message() {
        ModelAndView mav = new ModelAndView("student/postgraduate/message");
        List<PostgraduateStudentMessage> messages = postgraduateStudentMessageRepository.findAll();
        mav.addObject("messages", messages);
        mav.addObject("navId", 2);
        return mav;
    }

    @PostMapping("/addMessage")
    public String addMessage(String title, String content,
                             @RequestParam(required = false) MultipartFile file,
                             @RequestParam(required = false) Date notifyDate) throws IOException {
        long studentId = 18720001;
        int year = 2019;
        int type = 0;
        PostgraduateStudentMessage newMessage = new PostgraduateStudentMessage();
        long currentTime = new Date().getTime();
        newMessage.setCreateTime(currentTime);
        newMessage.setTitle(title);
        newMessage.setContent(content);
        newMessage.setYear(year);
        newMessage.setType(type);
        newMessage.setStudentId(studentId);
        if (file != null) {
            String localFilePath = postFileDir + type + "_" + studentId + "_" + currentTime + "_" + file.getOriginalFilename();
            File localFile = new File(localFilePath);
            file.transferTo(localFile);
            newMessage.setHasFile(true);
            newMessage.setFilePath(localFilePath);
        }
        if (notifyDate != null) {
            newMessage.setHasNotification(true);
            newMessage.setNotifyDate(notifyDate.getTime());
        }
        postgraduateStudentMessageRepository.save(newMessage);
        return "redirect:/student/postgraduate/message";
    }

    @GetMapping("/getMessage")
    public @ResponseBody
    PostgraduateStudentMessage getMessage(long id) {
        return postgraduateStudentMessageRepository.findById(id);
    }

    @RequestMapping("/submit")
    public ModelAndView submission() {
        ModelAndView mav = new ModelAndView("student/postgraduate/submit");
        long studentId = 18720001;
        List<PostgraduateStudentSubmission> submissions = postgraduateStudentSubmissionRepository
                .findByStudentId(studentId);
        mav.addObject("submissions", submissions);
        mav.addObject("navId", 3);
        return mav;
    }

    @PostMapping("/submitEvent")
    public String submit(int type, MultipartFile file) throws IOException {
        PostgraduateStudentSubmission newSubmission = new PostgraduateStudentSubmission();
        long studentId = 18120001;
        String studentName = "张三";
        newSubmission.setType(type);
        long currentTime = new Date().getTime();
        newSubmission.setSubmitDate(currentTime);
        newSubmission.setStudentId(studentId);

        String localFilePath = postFileDir + type + "_" + studentId + "_" + currentTime + "_" + file.getOriginalFilename();
        File localFile = new File(localFilePath);
        file.transferTo(localFile);

        newSubmission.setFilePath(localFilePath);
        newSubmission.setStudentName(studentName);
        postgraduateStudentSubmissionRepository.save(newSubmission);
        return "redirect:/student/postgraduate/submit";
    }
}
