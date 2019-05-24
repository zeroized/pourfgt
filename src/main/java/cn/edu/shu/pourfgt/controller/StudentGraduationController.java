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
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/student/graduation")
public class StudentGraduationController {
    private final GraduationTimetableRepository graduationTimetableRepository;
    private final GraduationPostRepository graduationPostRepository;
    private final GraduationStudentMessageRepository graduationStudentMessageRepository;
    private final GraduationWeightRepository graduationWeightRepository;
    private final GraduationStudentSubmissionRepository graduationStudentSubmissionRepository;
    private int currentSemester = 0;
    private int currentYear = 2018;
    @Value("${course.announcement.file-path}")
    private String postFileDir;

    public StudentGraduationController(GraduationTimetableRepository graduationTimetableRepository,
                                       GraduationPostRepository graduationPostRepository,
                                       GraduationStudentMessageRepository graduationStudentMessageRepository,
                                       GraduationWeightRepository graduationWeightRepository,
                                       GraduationStudentSubmissionRepository graduationStudentSubmissionRepository) {
        this.graduationTimetableRepository = graduationTimetableRepository;
        this.graduationPostRepository = graduationPostRepository;
        this.graduationStudentMessageRepository = graduationStudentMessageRepository;
        this.graduationWeightRepository = graduationWeightRepository;
        this.graduationStudentSubmissionRepository = graduationStudentSubmissionRepository;
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

    @RequestMapping("/timetable")
    public ModelAndView timetable() {
        ModelAndView mav = new ModelAndView("student/graduation/timetable");
        List<GraduationTimetable> events = graduationTimetableRepository
                .findByYearAndSemester(currentYear, currentSemester);
        mav.addObject("events", events);
        mav.addObject("navId", 0);
        return mav;
    }

    @RequestMapping("/post")
    public ModelAndView post() {
        ModelAndView mav = new ModelAndView("student/graduation/post");
        List<GraduationPost> posts = graduationPostRepository
                .findByYearAndSemester(currentYear, currentSemester);
        mav.addObject("posts", posts);
        mav.addObject("navId", 1);
        return mav;
    }

    @GetMapping("/getPost")
    public GraduationPost getPost(long id) {
        return graduationPostRepository.findById(id);
    }

    @RequestMapping("/message")
    public ModelAndView message() {
        ModelAndView mav = new ModelAndView("student/graduation/message");
        List<GraduationStudentMessage> messages = graduationStudentMessageRepository
                .findByYearAndSemester(currentYear, currentSemester);
        mav.addObject("messages", messages);
        mav.addObject("navId", 2);
        return mav;
    }

    @PostMapping("/addMessage")
    public String addMessage(String title, String content,
                             @RequestParam(required = false) MultipartFile file,
                             @RequestParam(required = false) Date notifyDate) throws IOException {
        long studentId = 18120001;
        GraduationStudentMessage newMessage = new GraduationStudentMessage();
        long currentTime = new Date().getTime();
        newMessage.setCreateTime(currentTime);
        newMessage.setTitle(title);
        newMessage.setContent(content);
        newMessage.setYear(currentYear);
        newMessage.setSemester(currentSemester);
        newMessage.setStudentId(studentId);
        if (file != null) {
            String localFilePath = postFileDir + currentSemester + "-" + currentYear + "_" + studentId + "_" + currentTime + "_" + file.getOriginalFilename();
            File localFile = new File(localFilePath);
            file.transferTo(localFile);
            newMessage.setHasFile(true);
            newMessage.setFilePath(localFilePath);
        }
        if (notifyDate != null) {
            newMessage.setHasNotification(true);
            newMessage.setNotifyDate(notifyDate.getTime());
        }
        graduationStudentMessageRepository.save(newMessage);
        return "redirect:/student/graduation/message";
    }

    @GetMapping("/getMessage")
    public @ResponseBody
    GraduationStudentMessage getMessage(long id) {
        return graduationStudentMessageRepository.findById(id);
    }

    @RequestMapping("/submit")
    public ModelAndView submission() {
        ModelAndView mav = new ModelAndView("student/graduation/submit");
        long studentId = 18120001;
        List<GraduationWeight> weights = graduationWeightRepository
                .findByYearAndSemester(currentYear, currentSemester);
        if (weights.size() == 0) {
            GraduationStudentSubmission submission = graduationStudentSubmissionRepository
                    .findFirstByYearAndSemesterAndStudentId(currentYear, currentSemester, studentId);
            Map<String, GraduationStudentSubmission> eventMap = new HashMap<>(1);
            if (submission != null) {
                eventMap.put("毕业论文", submission);
                mav.addObject("submitted", eventMap);
                mav.addObject("notSubmitted", new ArrayList<String>());
            } else {
                mav.addObject("submitted", eventMap);
                mav.addObject("notSubmitted", new String[]{"毕业论文"});
            }

        } else {
            List<GraduationStudentSubmission> submissions = graduationStudentSubmissionRepository
                    .findByYearAndSemesterAndStudentId(currentYear, currentSemester, studentId);
            Map<String, GraduationStudentSubmission> eventMap = new HashMap<>(weights.size());
            for (GraduationStudentSubmission submission : submissions) {
                eventMap.put(submission.getEvent(), submission);
            }
            List<String> events = weights.stream().map(GraduationWeight::getEvent).collect(Collectors.toList());
            mav.addObject("submitted", eventMap);
            mav.addObject("notSubmitted", events.removeAll(eventMap.keySet()));
        }
        mav.addObject("navId", 3);
        return mav;
    }

    @PostMapping("/submitEvent")
    public String submit(String event, MultipartFile file) throws IOException {
        GraduationStudentSubmission newSubmission = new GraduationStudentSubmission();
        long studentId = 18120001;
        newSubmission.setYear(currentYear);
        newSubmission.setSemester(currentSemester);
        long currentTime = new Date().getTime();
        newSubmission.setSubmitDate(currentTime);
        newSubmission.setStudentId(studentId);
        newSubmission.setEvent(event);

        String localFilePath = postFileDir + currentSemester + "-" + currentYear + "_" + studentId + "_" + event + "_" + file.getOriginalFilename();
        File localFile = new File(localFilePath);
        file.transferTo(localFile);

        newSubmission.setFilePath(localFilePath);
        graduationStudentSubmissionRepository.save(newSubmission);

        return "redirect:/student/graduation/submit";
    }
}
