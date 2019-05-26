package cn.edu.shu.pourfgt.controller;

import cn.edu.shu.pourfgt.dataSource.dao.*;
import cn.edu.shu.pourfgt.dataSource.entity.*;
import cn.edu.shu.pourfgt.util.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    @Value("${course.current.year}")
    private int currentYear;

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
    public void initModel(Model model, HttpServletRequest request) {
        model.addAttribute("leftNavId", 2);
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");
        model.addAttribute("teacherId", userId);
    }

    @RequestMapping("/list")
    public ModelAndView list(Model model) {
        ModelAndView mav = new ModelAndView("teacher/postgraduate/list");
        String teacherId = (String) model.asMap().get("teacherId");
        List<PostgraduateStudent> students = postgraduateStudentRepository
                .findByTeacherIdAndActive(teacherId, true);
        List<PostgraduateStudent> graduated = postgraduateStudentRepository
                .findByTeacherIdAndActive(teacherId, false);
        mav.addObject("students", students);
        mav.addObject("graduated", graduated);
        mav.addObject("navId", 0);
        return mav;
    }

    @PostMapping("/addStudent")
    public String addStudent(long studentId, String studentName, int year, int type, Model model) {
        PostgraduateStudent newStudent = new PostgraduateStudent();
        String teacherId = (String) model.asMap().get("teacherId");
        newStudent.setTeacherId(teacherId);
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
    public ModelAndView timetable(@RequestParam(required = false) Integer year,
                                  @RequestParam(defaultValue = "0") Integer type, Model model) {
        ModelAndView mav = new ModelAndView("teacher/postgraduate/timetable");
        String teacherId = (String) model.asMap().get("teacherId");
        List<PostgraduateStudent> activeYear = postgraduateStudentRepository.findByActive(true);
        List<Integer> availableYear = activeYear.stream()
                .map(PostgraduateStudent::getYear)
                .distinct()
                .collect(Collectors.toList());
        if (year == null) {
            year = availableYear.get(0);
            if (year == null) {
                year = currentYear;
            }
        }
        List<PostgraduateTimetable> events = postgraduateTimetableRepository
                .findByTeacherIdAndForYearAndForType(teacherId, year, type);
        mav.addObject("years", availableYear);
        mav.addObject("events", events);
        mav.addObject("navId", 1);
        return mav;
    }

    @PostMapping("/createTimetable")
    public String createTimetable(String event, Date keyDate, int year, int type, Model model) {
        PostgraduateTimetable newTimetable = new PostgraduateTimetable();
        String teacherId = (String) model.asMap().get("teacherId");
        newTimetable.setTeacherId(teacherId);
        newTimetable.setForYear(year);
        newTimetable.setForType(type);
        newTimetable.setEvent(event);
        newTimetable.setKeyDate(keyDate.getTime());
        postgraduateTimetableRepository.save(newTimetable);
        return "redirect:/teacher/postgraduate/timetable";
    }

    @RequestMapping("/post")
    public ModelAndView post(Model model) {
        ModelAndView mav = new ModelAndView("teacher/postgraduate/post");
        String teacherId = (String) model.asMap().get("teacherId");
        List<PostgraduatePost> posts = postgraduatePostRepository.findByTeacherId(teacherId);
        mav.addObject("posts", posts);
        mav.addObject("navId", 2);
        return mav;
    }

    @PostMapping("/addPost")
    public String addPost(String title, String content, int year, int type,
                          @RequestParam(required = false) MultipartFile file,
                          @RequestParam(required = false) Date notifyDate, Model model) throws IOException {
        PostgraduatePost newPost = new PostgraduatePost();
        String teacherId = (String) model.asMap().get("teacherId");
        newPost.setTeacherId(teacherId);
        long currentTime = new Date().getTime();
        newPost.setTitle(title);
        newPost.setCreateTime(currentTime);
        newPost.setYear(year);
        newPost.setType(type);
        newPost.setContent(content);
        if (!file.isEmpty()) {
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

    @RequestMapping("/getPostFile")
    public ResponseEntity<InputStreamResource> getPostFile(long id) throws FileNotFoundException {
        String localFilePath = postgraduatePostRepository.findById(id).getFilePath();
        String fileName = Paths.get(localFilePath).getFileName().toString();
        return FileUtil.download(localFilePath, fileName);
    }

    @RequestMapping("/message")
    public ModelAndView message(Model model) {
        ModelAndView mav = new ModelAndView("teacher/postgraduate/message");
        String teacherId = (String) model.asMap().get("teacherId");

        List<PostgraduateStudentMessage> allMessage = postgraduateStudentMessageRepository
                .findAll();

        List<PostgraduateStudent> students = postgraduateStudentRepository.findByTeacherIdAndActive(teacherId, true);
        List<Long> studentIds = students.stream()
                .map(PostgraduateStudent::getStudentId)
                .collect(Collectors.toList());
        List<PostgraduateStudentMessage> messages = allMessage.stream()
                .filter(x -> studentIds.contains(x.getStudentId()))
                .collect(Collectors.toList());

        mav.addObject("messages", messages);
        mav.addObject("navId", 3);
        return mav;
    }

    @GetMapping("/getMessage")
    public @ResponseBody
    PostgraduateStudentMessage getMessage(long id) {
        return postgraduateStudentMessageRepository.findById(id);
    }

    @PostMapping("/respond")
    public String respondMessage(long messageId, String response) {
        PostgraduateStudentMessage message = postgraduateStudentMessageRepository.findById(messageId);
        postgraduateStudentMessageRepository.delete(message);
        message.setHasResponse(true);
        message.setResponse(response);
        postgraduateStudentMessageRepository.save(message);
        return "redirect:/teacher/postgraduate/message";
    }

    @RequestMapping("/submission")
    public ModelAndView submission(@RequestParam(required = false) Long studentId, Model model) {
        ModelAndView mav = new ModelAndView("teacher/postgraduate/submission");
        String teacherId = (String) model.asMap().get("teacherId");
        List<PostgraduateStudent> students = postgraduateStudentRepository.findByTeacherIdAndActive(teacherId, true);
        mav.addObject("students", students);
        List<PostgraduateStudentSubmission> submissions;
        if (studentId != null) {
            submissions = postgraduateStudentSubmissionRepository
                    .findByStudentId(studentId);
        } else {
            submissions = postgraduateStudentSubmissionRepository
                    .findByStudentId(students.get(0).getStudentId());
        }
        mav.addObject("submissions", submissions);
        mav.addObject("navId", 4);
        return mav;
    }

    @GetMapping("/getSubmission")
    public PostgraduateStudentSubmission getSubmission(long id) {
        return postgraduateStudentSubmissionRepository.findById(id);
    }

    @PostMapping("/respondSubmission")
    public String respondSubmission(long submissionId, String response) {
        PostgraduateStudentSubmission submission = postgraduateStudentSubmissionRepository.findById(submissionId);
        postgraduateStudentSubmissionRepository.delete(submission);
        submission.setHasResponse(true);
        submission.setResponse(response);
        postgraduateStudentSubmissionRepository.save(submission);
        return "redirect:/teacher/postgraduate/submission";
    }

}
