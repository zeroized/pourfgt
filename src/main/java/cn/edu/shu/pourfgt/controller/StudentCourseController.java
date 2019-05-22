package cn.edu.shu.pourfgt.controller;

import cn.edu.shu.pourfgt.dataSource.dao.CourseInfoRepository;
import cn.edu.shu.pourfgt.dataSource.dao.CoursePostRepository;
import cn.edu.shu.pourfgt.dataSource.dao.CourseStudentMessageRepository;
import cn.edu.shu.pourfgt.dataSource.dao.CourseStudentRepository;
import cn.edu.shu.pourfgt.dataSource.entity.CourseInfo;
import cn.edu.shu.pourfgt.dataSource.entity.CoursePost;
import cn.edu.shu.pourfgt.dataSource.entity.CourseStudent;
import cn.edu.shu.pourfgt.dataSource.entity.CourseStudentMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/student/course")
public class StudentCourseController {
    private final String announceFileDir;

    private final CourseStudentRepository courseStudentRepository;
    private final CourseInfoRepository courseInfoRepository;
    private final CoursePostRepository coursePostRepository;
    private final CourseStudentMessageRepository courseStudentMessageRepository;


    public StudentCourseController(@Value("${course.announcement.file-path}") String announceFileDir,
                                   CourseStudentRepository courseStudentRepository,
                                   CourseInfoRepository courseInfoRepository,
                                   CoursePostRepository coursePostRepository,
                                   CourseStudentMessageRepository courseStudentMessageRepository) {
        this.announceFileDir = announceFileDir;
        this.courseStudentRepository = courseStudentRepository;
        this.courseInfoRepository = courseInfoRepository;
        this.coursePostRepository = coursePostRepository;
        this.courseStudentMessageRepository = courseStudentMessageRepository;
    }

    @ModelAttribute
    public void addCourseInfo(@PathVariable(required = false) Long courseDBId, Model model) {
        if (courseDBId != null) {
            long id = courseDBId;
            CourseInfo courseInfo = courseInfoRepository.findById(id);
            model.addAttribute("courseId", courseInfo.getId());
            model.addAttribute("courseName", courseInfo.getCourseName());
        }
    }

    @RequestMapping("/list")
    public ModelAndView courseList() {
        ModelAndView mav = new ModelAndView("student/course/list");
        List<CourseStudent> courses = courseStudentRepository.findByStudentId(18120001);
        List<Long> courseIds = courses.stream().map(CourseStudent::getAttachedId).collect(Collectors.toList());
        mav.addObject("currYear", 2018);
        mav.addObject("currSemester", 0);
        mav.addObject("courses", courses);
        return mav;
    }

    @RequestMapping("/{courseDBId}")
    public String home(@PathVariable long courseDBId) {
        return "redirect:/student/course/" + courseDBId + "/homework";
    }

    @RequestMapping("/{courseDBId}/homework")
    public ModelAndView homework(@PathVariable long courseDBId) {
        ModelAndView mav = new ModelAndView("student/course/sub/homework");
        List<CoursePost> homeworkList = coursePostRepository.findByAttachedIdAndType(courseDBId, 2);
        mav.addObject("homeworkList", homeworkList);
        mav.addObject("navId", 0);
        return mav;
    }

    @GetMapping("/getPost")
    public @ResponseBody
    CoursePost getHomework(long id) {
        return coursePostRepository.findById(id);
    }

    @PostMapping("/doHomework")
    public String doHomework(long courseDBId, String title, String content, int week,
                             @RequestParam(required = false) MultipartFile file) throws IOException {
        CourseStudentMessage newMessage = buildCourseStudentMessage(0, courseDBId, title, content, file);
        newMessage.setHomeworkWeek(week);
        newMessage.setOverdue(false);
        courseStudentMessageRepository.save(newMessage);
        return "redirect:/student/course/" + courseDBId + "/homework";
    }

    @RequestMapping("/{courseDBId}/message")
    public ModelAndView message(@PathVariable long courseDBId) {
        ModelAndView mav = new ModelAndView("student/course/sub/message");
        List<CoursePost> messages = coursePostRepository.findByAttachedIdAndTypeOrType(courseDBId, 0, 1);
        mav.addObject("messages", messages);
        mav.addObject("navId", 1);
        return mav;
    }

    @RequestMapping("/{courseDBId}/question")
    public ModelAndView question(@PathVariable long courseDBId) {
        ModelAndView mav = new ModelAndView("student/course/sub/question");
        List<CourseStudentMessage> questions = courseStudentMessageRepository.findByAttachedIdAndType(courseDBId, 1);
        mav.addObject("questions", questions);
        mav.addObject("navId", 1);
        return mav;
    }

    @PostMapping("/askQuestion")
    public String askQuestion(long courseDBId, String title, String content,
                              @RequestParam(required = false) MultipartFile file) throws IOException {
        CourseStudentMessage newMessage = buildCourseStudentMessage(1, courseDBId, title, content, file);
        courseStudentMessageRepository.save(newMessage);
        return "redirect:/student/course/" + courseDBId + "/question";
    }

    @GetMapping("/getQuestion")
    public @ResponseBody
    CourseStudentMessage getQuestion(long id) {
        return courseStudentMessageRepository.findById(id);
    }

    private CourseStudentMessage buildCourseStudentMessage(int type, long courseDBId, String title, String content,
                                                           MultipartFile file) throws IOException {
        CourseStudentMessage newQuestion = new CourseStudentMessage();
        newQuestion.setStudentId(18120001);
        newQuestion.setType(type);
        newQuestion.setAttachedId(courseDBId);
        newQuestion.setTitle(title);
        newQuestion.setContent(content);
        long currentTime = new Date().getTime();
        newQuestion.setCreateTime(currentTime);
        if (file != null) {
            String localFilePath = announceFileDir + courseDBId + "_" + currentTime + "_" + file.getOriginalFilename();
            File localFile = new File(localFilePath);
            file.transferTo(localFile);
            newQuestion.setHasFile(true);
            newQuestion.setFilePath(localFilePath);
        }
        return newQuestion;
    }
}
