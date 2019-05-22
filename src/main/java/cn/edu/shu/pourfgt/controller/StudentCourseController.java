package cn.edu.shu.pourfgt.controller;

import cn.edu.shu.pourfgt.dataSource.dao.CourseInfoRepository;
import cn.edu.shu.pourfgt.dataSource.dao.CoursePostRepository;
import cn.edu.shu.pourfgt.dataSource.dao.CourseStudentRepository;
import cn.edu.shu.pourfgt.dataSource.entity.CourseInfo;
import cn.edu.shu.pourfgt.dataSource.entity.CoursePost;
import cn.edu.shu.pourfgt.dataSource.entity.CourseStudent;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/student/course")
public class StudentCourseController {
    private final CourseStudentRepository courseStudentRepository;
    private final CourseInfoRepository courseInfoRepository;
    private final CoursePostRepository coursePostRepository;


    public StudentCourseController(CourseStudentRepository courseStudentRepository, CourseInfoRepository courseInfoRepository, CoursePostRepository coursePostRepository) {
        this.courseStudentRepository = courseStudentRepository;
        this.courseInfoRepository = courseInfoRepository;
        this.coursePostRepository = coursePostRepository;
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

    @RequestMapping("/{courseDBId}/homework")
    public ModelAndView homework(@PathVariable long courseDBId) {
        ModelAndView mav = new ModelAndView("student/course/sub/homework");
        List<CoursePost> homeworkList = coursePostRepository.findByAttachedIdAndType(courseDBId, 2);
        mav.addObject("homeworkList", homeworkList);
        return mav;
    }

    @GetMapping("/getHomework")
    public @ResponseBody
    CoursePost getHomework(long id) {
        return coursePostRepository.findById(id);
    }
}
