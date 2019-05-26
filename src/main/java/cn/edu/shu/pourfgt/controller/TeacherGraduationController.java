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
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/teacher/graduation")
public class TeacherGraduationController {
    private final GraduationTimetableRepository graduationTimetableRepository;
    private final GraduationPostRepository graduationPostRepository;
    private final GraduationWorkRepository graduationWorkRepository;
    private final GraduationWeightRepository graduationWeightRepository;

    @Value("${course.announcement.file-path}")
    private String postFileDir;
    @Value("${course.current.year}")
    private int currentYear;
    @Value("${course.current.semester}")
    private int currentSemester;

    private final GraduationStudentMessageRepository graduationStudentMessageRepository;

    public TeacherGraduationController(GraduationWorkRepository graduationWorkRepository,
                                       GraduationTimetableRepository graduationTimetableRepository,
                                       GraduationPostRepository graduationPostRepository,
                                       GraduationWeightRepository graduationWeightRepository, GraduationStudentMessageRepository graduationStudentMessageRepository) {
        this.graduationWorkRepository = graduationWorkRepository;
        this.graduationTimetableRepository = graduationTimetableRepository;
        this.graduationPostRepository = graduationPostRepository;
        this.graduationWeightRepository = graduationWeightRepository;
        this.graduationStudentMessageRepository = graduationStudentMessageRepository;
    }

    @InitBinder
    public void initDate(WebDataBinder webDataBinder) {
        webDataBinder.addCustomFormatter(new DateFormatter("yyyy-MM-dd"));
    }

    @ModelAttribute
    public void initModel(Model model, HttpServletRequest request) {
        model.addAttribute("leftNavId", 1);
        model.addAttribute("year", currentYear);
        model.addAttribute("semester", currentSemester);
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");
        model.addAttribute("teacherId", userId);
    }

    @RequestMapping("/list")
    public ModelAndView list(Model model) {
        ModelAndView mav = new ModelAndView("teacher/graduation/list");
        String teacherId = (String) model.asMap().get("teacherId");
        List<GraduationWork> works = graduationWorkRepository
                .findByTeacherIdAndYearAndSemester(teacherId, currentYear, currentSemester);
        mav.addObject("works", works);
        mav.addObject("navId", 0);
        return mav;
    }

    @PostMapping("/addWork")
    public String addWork(long studentId, String studentName, String title,
                          String description, Model model) {
        GraduationWork newWork = new GraduationWork();
        String teacherId = (String) model.asMap().get("teacherId");
        newWork.setTeacherId(teacherId);
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
    public ModelAndView timetable(Model model) {
        ModelAndView mav = new ModelAndView("teacher/graduation/timetable");
        String teacherId = (String) model.asMap().get("teacherId");
        List<GraduationTimetable> events = graduationTimetableRepository
                .findByTeacherIdAndYearAndSemester(teacherId, currentYear, currentSemester);
        mav.addObject("events", events);
        mav.addObject("navId", 1);
        return mav;
    }

    @PostMapping("/createTimetable")
    public String createTimetableEvent(String event, Date keyDate, Model model) {
        GraduationTimetable newEvent = new GraduationTimetable();
        String teacherId = (String) model.asMap().get("teacherId");
        newEvent.setTeacherId(teacherId);
        newEvent.setEvent(event);
        newEvent.setKeyDate(keyDate.getTime());
        newEvent.setYear(currentYear);
        newEvent.setSemester(currentSemester);
        graduationTimetableRepository.save(newEvent);
        return "redirect:/teacher/graduation/timetable";
    }

    @RequestMapping("/post")
    public ModelAndView post(Model model) {
        ModelAndView mav = new ModelAndView("teacher/graduation/post");
        String teacherId = (String) model.asMap().get("teacherId");
        List<GraduationPost> posts = graduationPostRepository
                .findByTeacherIdAndYearAndSemester(teacherId, currentYear, currentSemester);
        mav.addObject("posts", posts);
        mav.addObject("navId", 2);
        return mav;
    }

    @PostMapping("/addPost")
    public String addPost(String title, String content,
                          @RequestParam(required = false) MultipartFile file,
                          @RequestParam(required = false) Date notifyDate, Model model) throws IOException {
        GraduationPost newPost = new GraduationPost();
        long currentTime = new Date().getTime();
        String teacherId = (String) model.asMap().get("teacherId");
        newPost.setTeacherId(teacherId);
        newPost.setCreateTime(currentTime);
        newPost.setYear(currentYear);
        newPost.setSemester(currentSemester);
        newPost.setTitle(title);
        newPost.setContent(content);
        if (!file.isEmpty()) {
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

    @RequestMapping("/getPostFile")
    public ResponseEntity<InputStreamResource> getPostFile(long id) throws FileNotFoundException {
        String localFilePath = graduationPostRepository.findById(id).getFilePath();
        String fileName = Paths.get(localFilePath).getFileName().toString();
        return FileUtil.download(localFilePath, fileName);
    }

    @RequestMapping("/message")
    public ModelAndView message(Model model) {
        ModelAndView mav = new ModelAndView("teacher/graduation/message");
        String teacherId = (String) model.asMap().get("teacherId");
        List<GraduationStudentMessage> messages = graduationStudentMessageRepository
                .findByTeacherIdAndYearAndSemester(teacherId, currentYear, currentSemester);
        mav.addObject("messages", messages);
        mav.addObject("navId", 3);
        return mav;
    }

    @GetMapping("/getMessage")
    public @ResponseBody
    GraduationStudentMessage getMessage(long id) {
        return graduationStudentMessageRepository.findById(id);
    }

    @PostMapping("/respond")
    public String respondMessage(long messageId, String response) {
        GraduationStudentMessage message = graduationStudentMessageRepository.findById(messageId);
        graduationStudentMessageRepository.delete(message);
        message.setHasResponse(true);
        message.setResponse(response);
        graduationStudentMessageRepository.save(message);
        return "redirect:/teacher/graduation/message";
    }

    @RequestMapping("/weight")
    public ModelAndView weight(Model model) {
        ModelAndView mav = new ModelAndView("teacher/graduation/weight");
        String teacherId = (String) model.asMap().get("teacherId");
        List<GraduationWeight> weights = graduationWeightRepository
                .findByTeacherIdAndYearAndSemester(teacherId, currentYear, currentSemester);
        if (weights.size() == 0) {
            GraduationWeight essay = new GraduationWeight();
            essay.setTeacherId(teacherId);
            essay.setYear(currentYear);
            essay.setSemester(currentSemester);
            essay.setEvent("毕业论文");
            essay.setRatio(1);
            graduationWeightRepository.save(essay);
            weights.add(essay);
            GraduationWeight design = new GraduationWeight();
            design.setTeacherId(teacherId);
            design.setYear(currentYear);
            design.setSemester(currentSemester);
            design.setEvent("毕业设计验收");
            design.setRatio(0);
            graduationWeightRepository.save(design);
            weights.add(design);
            GraduationWeight presentation = new GraduationWeight();
            presentation.setTeacherId(teacherId);
            presentation.setYear(currentYear);
            presentation.setSemester(currentSemester);
            presentation.setEvent("毕业答辩");
            presentation.setRatio(0);
            graduationWeightRepository.save(presentation);
            weights.add(presentation);
        }
        mav.addObject("events", weights);
        mav.addObject("navId", 4);
        return mav;
    }

    @PostMapping("/updateWeight")
    public String updateWeight(long[] id, double[] ratio) {
        double sum = Arrays.stream(ratio).sum();
        for (int i = 0; i < ratio.length; i++) {
            ratio[i] = ratio[i] / sum;
        }
        for (int i = 0; i < id.length; i++) {
            GraduationWeight event = graduationWeightRepository.findById(id[i]);
            graduationWeightRepository.delete(event);
            event.setRatio(ratio[i]);
            graduationWeightRepository.save(event);
        }
        return "redirect:/teacher/graduation/weight";
    }

}
