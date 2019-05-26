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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/student/postgraduate")
public class StudentPostgraduateController {
    private final PostgraduateStudentRepository postgraduateStudentRepository;
    private final PostgraduateTimetableRepository postgraduateTimetableRepository;
    private final PostgraduatePostRepository postgraduatePostRepository;
    private final PostgraduateStudentMessageRepository postgraduateStudentMessageRepository;
    private final PostgraduateStudentSubmissionRepository postgraduateStudentSubmissionRepository;
    @Value("${course.announcement.file-path}")
    private String postFileDir;
    @Value("${course.current.year}")
    private int currentYear;

    public StudentPostgraduateController(PostgraduateStudentRepository postgraduateStudentRepository,
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
    public void initModel(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Long studentId = Long.valueOf((String) session.getAttribute("userId"));
        PostgraduateStudent student = postgraduateStudentRepository.findFirstByStudentId(studentId);
        model.addAttribute("studentId", studentId);
        model.addAttribute("type", student.getType());
        model.addAttribute("year", student.getYear());
        model.addAttribute("studentName", student.getStudentName());
        model.addAttribute("leftNavId", 2);
        model.addAttribute("availableNavs", new Integer[]{2});
    }

    @RequestMapping("/timetable")
    public ModelAndView timetable(Model model) {
        ModelAndView mav = new ModelAndView("student/postgraduate/timetable");
        int year = (Integer) model.asMap().get("year");
        int type = (Integer) model.asMap().get("type");
        List<PostgraduateTimetable> events = postgraduateTimetableRepository
                .findByForYearAndForType(year, type);
        mav.addObject("events", events);
        mav.addObject("navId", 0);
        return mav;
    }

    @RequestMapping("/post")
    public ModelAndView post(Model model) {
        ModelAndView mav = new ModelAndView("student/postgraduate/post");
        int year = (Integer) model.asMap().get("year");
        int type = (Integer) model.asMap().get("type");
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
    public @ResponseBody
    PostgraduatePost getPost(long id) {
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
        if (!file.isEmpty()) {
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
    public ModelAndView submission(Model model) {
        ModelAndView mav = new ModelAndView("student/postgraduate/submit");
        long studentId = (Long) model.asMap().get("studentId");
        List<PostgraduateStudentSubmission> submissions = postgraduateStudentSubmissionRepository
                .findByStudentId(studentId);
        mav.addObject("submissions", submissions);
        mav.addObject("navId", 3);
        return mav;
    }

    @PostMapping("/submitEvent")
    public String submit(int type, MultipartFile file, Model model) throws IOException {
        PostgraduateStudentSubmission newSubmission = new PostgraduateStudentSubmission();
        long studentId = (Long) model.asMap().get("studentId");
        String studentName = (String) model.asMap().get("studentName");
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
