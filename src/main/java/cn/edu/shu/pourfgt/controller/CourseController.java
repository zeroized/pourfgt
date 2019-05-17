package cn.edu.shu.pourfgt.controller;

import cn.edu.shu.pourfgt.dataSource.dao.CourseGradeRepository;
import cn.edu.shu.pourfgt.dataSource.dao.CourseInfoRepository;
import cn.edu.shu.pourfgt.dataSource.dao.CourseStudentRepository;
import cn.edu.shu.pourfgt.dataSource.entity.CourseGrade;
import cn.edu.shu.pourfgt.dataSource.entity.CourseInfo;
import cn.edu.shu.pourfgt.dataSource.entity.CourseStudent;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/course")
public class CourseController {
    private final CourseInfoRepository courseInfoRepository;
    private final CourseGradeRepository courseGradeRepository;
    private final CourseStudentRepository courseStudentRepository;

    public CourseController(CourseInfoRepository courseInfoRepository, CourseGradeRepository courseGradeRepository, CourseStudentRepository courseStudentRepository) {
        this.courseInfoRepository = courseInfoRepository;
        this.courseGradeRepository = courseGradeRepository;
        this.courseStudentRepository = courseStudentRepository;
    }

    @RequestMapping("/list")
    public String courseList() {
        return "course/list";
    }

    @RequestMapping("/create")
    public String courseCreate() {
        return "course/create";
    }

    @RequestMapping("/{courseId}")
    public String courseNav(@PathVariable String courseId) {
        return "redirect:/course/{courseId}/studentList";
    }

    @RequestMapping("/{courseId}/studentList")
    public String studentList(@PathVariable String courseId) {
        return "course/sub/studentList";
    }

    @PostMapping("/createCourse")
    public String createCourse(String courseId, String courseName, Integer[] courseType,
                               Integer discussionType, MultipartFile studentList, Integer scoreType,
                               Double normalRatio, String[] name, Double[] ratio) {
        CourseInfo newCourse = new CourseInfo();
        newCourse.setCourseId(courseId);
        newCourse.setCourseName(courseName);
        if (courseType.length == 0)
            newCourse.setCourseType("0");
        else {
            StringBuilder courseTypeString = new StringBuilder("0");
            for (Integer integer : courseType) {
                courseTypeString.append(integer);
            }
            newCourse.setCourseType(courseTypeString.toString());
        }
        newCourse.setDiscussionType(discussionType);
        newCourse.setScoreType(scoreType);
        newCourse.setNormalRatio(normalRatio);

        //TODO revise year and semester
        newCourse.setYear(2018);
        newCourse.setSemester(0);

        //TODO revise teacher id
        newCourse.setTeacherId("00000000");

        long id = courseInfoRepository.save(newCourse).getId();

        //TODO revise student list as from file
        List<String[]> testCourseStudent = new ArrayList<>(3);
        testCourseStudent.add(new String[]{"18120001", "张三"});
        testCourseStudent.add(new String[]{"18120002", "李四"});
        testCourseStudent.add(new String[]{"18120003", "王五"});
        for (String[] strings : testCourseStudent) {
            CourseStudent courseStudent = new CourseStudent();
            courseStudent.setStudentId(Long.valueOf(strings[0]));
            courseStudent.setAttachedId(id);
            courseStudent.setScore(-1);
            courseStudentRepository.save(courseStudent);
        }

        for (int i = 0; i < name.length; i++) {
            CourseGrade newGrade = new CourseGrade();
            newGrade.setAttachedId(id);
            newGrade.setName(name[i]);
            newGrade.setRatio(ratio[i]);
            newGrade.setCourseId(courseId);
            courseGradeRepository.save(newGrade);
        }
        return "redirect:/course/" + id + "/studentList";
    }

    @RequestMapping("/{courseId}/record")
    public String record(@PathVariable String courseId) {
        return "course/sub/record";
    }

    @RequestMapping("/{courseId}/announcement")
    public String announcement(@PathVariable String courseId) {
        return "";
    }

    @RequestMapping("/{courseId}/homework")
    public String homework(@PathVariable String courseId) {
        return "";
    }

    @RequestMapping("/{courseId}/problem")
    public String problem(@PathVariable String courseId) {
        return "";
    }

    @RequestMapping("/{courseId}/discussion")
    public String discussion(@PathVariable String courseId) {
        return "";
    }

    @RequestMapping("/{courseId}/weight")
    public String weight(@PathVariable String courseId) {
        return "";
    }
}
