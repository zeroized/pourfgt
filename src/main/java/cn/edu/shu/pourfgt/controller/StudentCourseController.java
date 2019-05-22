package cn.edu.shu.pourfgt.controller;

import cn.edu.shu.pourfgt.dataSource.dao.CourseInfoRepository;
import cn.edu.shu.pourfgt.dataSource.dao.CoursePostRepository;
import cn.edu.shu.pourfgt.dataSource.dao.CourseStudentMessageRepository;
import cn.edu.shu.pourfgt.dataSource.dao.CourseStudentRepository;
import cn.edu.shu.pourfgt.dataSource.entity.CourseInfo;
import cn.edu.shu.pourfgt.dataSource.entity.CoursePost;
import cn.edu.shu.pourfgt.dataSource.entity.CourseStudent;
import cn.edu.shu.pourfgt.dataSource.entity.CourseStudentMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/student/course")
public class StudentCourseController {
    private final CourseStudentRepository courseStudentRepository;
    private final CourseInfoRepository courseInfoRepository;
    private final CoursePostRepository coursePostRepository;
    private final CourseStudentMessageRepository courseStudentMessageRepository;


    public StudentCourseController(CourseStudentRepository courseStudentRepository, CourseInfoRepository courseInfoRepository, CoursePostRepository coursePostRepository, CourseStudentMessageRepository courseStudentMessageRepository) {
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
    public String doHomework() {
        return "";
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
    public String askQuestion(long courseId, String title, String content,
                              @RequestParam(required = false) MultipartFile file) {
        return "redirect:/student/course/" + courseId + "/question";
    }

    @GetMapping("/getQuestion")
    public @ResponseBody
    CourseStudentMessage getQuestion(long id) {
        return courseStudentMessageRepository.findById(id);
    }

}
