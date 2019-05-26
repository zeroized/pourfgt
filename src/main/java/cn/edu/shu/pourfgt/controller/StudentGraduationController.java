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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/student/graduation")
public class StudentGraduationController {
    private final GraduationTimetableRepository graduationTimetableRepository;
    private final GraduationPostRepository graduationPostRepository;
    private final GraduationStudentMessageRepository graduationStudentMessageRepository;
    private final GraduationWeightRepository graduationWeightRepository;
    private final GraduationStudentSubmissionRepository graduationStudentSubmissionRepository;
    private final GraduationWorkRepository graduationWorkRepository;
    @Value("${course.current.year}")
    private int currentYear;
    @Value("${course.current.semester}")
    private int currentSemester;
    @Value("${course.announcement.file-path}")
    private String postFileDir;

    public StudentGraduationController(GraduationTimetableRepository graduationTimetableRepository,
                                       GraduationPostRepository graduationPostRepository,
                                       GraduationStudentMessageRepository graduationStudentMessageRepository,
                                       GraduationWeightRepository graduationWeightRepository,
                                       GraduationStudentSubmissionRepository graduationStudentSubmissionRepository, GraduationWorkRepository graduationWorkRepository) {
        this.graduationTimetableRepository = graduationTimetableRepository;
        this.graduationPostRepository = graduationPostRepository;
        this.graduationStudentMessageRepository = graduationStudentMessageRepository;
        this.graduationWeightRepository = graduationWeightRepository;
        this.graduationStudentSubmissionRepository = graduationStudentSubmissionRepository;
        this.graduationWorkRepository = graduationWorkRepository;
    }

    @InitBinder
    public void initDate(WebDataBinder webDataBinder) {
        webDataBinder.addCustomFormatter(new DateFormatter("yyyy-MM-dd"));
    }

    @ModelAttribute
    public void initModel(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        long studentId = Long.parseLong((String) session.getAttribute("userId"));
        GraduationWork graduationWork = graduationWorkRepository
                .findFirstByStudentIdAndYearAndSemester(studentId, currentYear, currentSemester);
        String teacherId = graduationWork.getTeacherId();
        model.addAttribute("studentId", studentId);
        model.addAttribute("teacherId", teacherId);
        model.addAttribute("leftNavId", 1);
        model.addAttribute("year", currentYear);
        model.addAttribute("semester", currentSemester);
        model.addAttribute("availableNavs", new Integer[]{0, 1});
    }

    @RequestMapping("/timetable")
    public ModelAndView timetable(Model model) {
        String teacherId = (String) model.asMap().get("teacherId");
        ModelAndView mav = new ModelAndView("student/graduation/timetable");
        List<GraduationTimetable> events = graduationTimetableRepository
                .findByTeacherIdAndYearAndSemester(teacherId, currentYear, currentSemester);
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
    public @ResponseBody
    GraduationPost getPost(long id) {
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
                             @RequestParam(required = false) Date notifyDate, Model model) throws IOException {
        long studentId = (Long) model.asMap().get("studentId");
        String teacherId = (String) model.asMap().get("teacherId");
        GraduationStudentMessage newMessage = new GraduationStudentMessage();
        long currentTime = new Date().getTime();
        newMessage.setTeacherId(teacherId);
        newMessage.setCreateTime(currentTime);
        newMessage.setTitle(title);
        newMessage.setContent(content);
        newMessage.setYear(currentYear);
        newMessage.setSemester(currentSemester);
        newMessage.setStudentId(studentId);
        if (!file.isEmpty()) {
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
    public ModelAndView submission(Model model) {
        ModelAndView mav = new ModelAndView("student/graduation/submit");
        long studentId = (Long) model.asMap().get("studentId");
        String teacherId = (String) model.asMap().get("teacherId");
        List<GraduationWeight> weights = graduationWeightRepository
                .findByTeacherIdAndYearAndSemester(teacherId, currentYear, currentSemester);
        List<GraduationStudentSubmission> submissions = graduationStudentSubmissionRepository
                .findByYearAndSemesterAndStudentId(currentYear, currentSemester, studentId);
        Map<String, GraduationStudentSubmission> eventMap = new HashMap<>(weights.size());
        for (GraduationStudentSubmission submission : submissions) {
            eventMap.put(submission.getEvent(), submission);
        }
        List<String> events = weights.stream().map(GraduationWeight::getEvent).collect(Collectors.toList());
        mav.addObject("submitted", eventMap);
        events.removeAll(eventMap.keySet());
        mav.addObject("notSubmitted", events);
        mav.addObject("navId", 3);
        return mav;
    }

    @PostMapping("/submitEvent")
    public String submit(String event, MultipartFile file, Model model) throws IOException {
        GraduationStudentSubmission newSubmission = new GraduationStudentSubmission();
        long studentId = (Long) model.asMap().get("studentId");

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
