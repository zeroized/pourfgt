package cn.edu.shu.pourfgt.controller;

import cn.edu.shu.pourfgt.dataSource.dao.CourseInfoRepository;
import cn.edu.shu.pourfgt.dataSource.dao.CourseStudentRepository;
import cn.edu.shu.pourfgt.dataSource.entity.CourseInfo;
import cn.edu.shu.pourfgt.dataSource.entity.CourseStudent;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/student/course")
public class StudentCourseController {
    private final CourseStudentRepository courseStudentRepository;
    private final CourseInfoRepository courseInfoRepository;


    public StudentCourseController(CourseStudentRepository courseStudentRepository, CourseInfoRepository courseInfoRepository) {
        this.courseStudentRepository = courseStudentRepository;
        this.courseInfoRepository = courseInfoRepository;
    }

    @ModelAttribute
    public void addCourseInfo(@PathVariable(required = false) Long courseDBId, Model model) {
        if (courseDBId != null) {
            Optional<CourseInfo> courseInfoOption = courseInfoRepository.findById(courseDBId);
            if (courseInfoOption.isPresent()) {
                CourseInfo courseInfo = courseInfoOption.get();
                model.addAttribute("courseId", courseInfo.getId());
                model.addAttribute("courseName", courseInfo.getCourseName());
            }
        }
    }

    @RequestMapping("/list")
    public ModelAndView courseList() {
        ModelAndView mav = new ModelAndView("student/course/list");
        List<CourseStudent> courses = courseStudentRepository.findByStudentId(18120001);
        List<CourseInfo> courseInfos = courseInfoRepository.findByTeacherId("00000000");
        mav.addObject("currYear", 2018);
        mav.addObject("currSemester", 0);
        mav.addObject("courses", courseInfos);
        return mav;
    }
}
