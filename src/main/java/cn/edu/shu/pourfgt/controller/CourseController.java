package cn.edu.shu.pourfgt.controller;

import cn.edu.shu.pourfgt.dataSource.dao.CourseInfoRepository;
import cn.edu.shu.pourfgt.dataSource.dao.CourseRegularGradeEventRepository;
import cn.edu.shu.pourfgt.dataSource.dao.CourseRegularGradeRecordRepository;
import cn.edu.shu.pourfgt.dataSource.dao.CourseStudentRepository;
import cn.edu.shu.pourfgt.dataSource.entity.CourseInfo;
import cn.edu.shu.pourfgt.dataSource.entity.CourseRegularGradeEvent;
import cn.edu.shu.pourfgt.dataSource.entity.CourseRegularGradeRecord;
import cn.edu.shu.pourfgt.dataSource.entity.CourseStudent;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/course")
public class CourseController {
    private final CourseInfoRepository courseInfoRepository;
    private final CourseRegularGradeEventRepository courseRegularGradeEventRepository;
    private final CourseStudentRepository courseStudentRepository;
    private final CourseRegularGradeRecordRepository courseRegularGradeRecordRepository;

    public CourseController(CourseInfoRepository courseInfoRepository, CourseRegularGradeEventRepository courseRegularGradeEventRepository, CourseStudentRepository courseStudentRepository, CourseRegularGradeRecordRepository courseRegularGradeRecordRepository) {
        this.courseInfoRepository = courseInfoRepository;
        this.courseRegularGradeEventRepository = courseRegularGradeEventRepository;
        this.courseStudentRepository = courseStudentRepository;
        this.courseRegularGradeRecordRepository = courseRegularGradeRecordRepository;
    }

    @RequestMapping("/list")
    public ModelAndView courseList() {
        ModelAndView mav = new ModelAndView("course/list");
        List<CourseInfo> courseInfos = courseInfoRepository.findByTeacherId("00000000");
        mav.addObject("currYear", 2018);
        mav.addObject("currSemester", 0);
        mav.addObject("courses", courseInfos);
        return mav;
    }

    @RequestMapping("/create")
    public String courseCreate() {
        return "course/create";
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
            CourseStudent newStudent = new CourseStudent();
            newStudent.setStudentId(Long.valueOf(strings[0]));
            newStudent.setAttachedId(id);
            newStudent.setStudentName(strings[1]);
            courseStudentRepository.save(newStudent);
        }

        for (int i = 0; i < name.length; i++) {
            CourseRegularGradeEvent newGrade = new CourseRegularGradeEvent();
            newGrade.setAttachedId(id);
            newGrade.setName(name[i]);
            newGrade.setRatio(ratio[i]);
            newGrade.setCourseId(courseId);
            courseRegularGradeEventRepository.save(newGrade);
        }
        return "redirect:/course/" + id + "/studentList";
    }

    @RequestMapping("/{courseDBId}")
    public String courseNav(@PathVariable long courseDBId) {
        return "redirect:/course/" + courseDBId + "/studentList";
    }

    @RequestMapping("/{courseDBId}/studentList")
    public ModelAndView studentList(@PathVariable long courseDBId) {
        ModelAndView mav = new ModelAndView("course/sub/studentList");
        List<CourseStudent> students = courseStudentRepository.findByAttachedId(courseDBId);
        mav.addObject("students", students);
        mav.addObject("courseId", courseDBId);
        return mav;
    }

    @PostMapping("/record/{courseDBId}")
    public String recordRegularGrade(@PathVariable long courseDBId, long studentId,
                                     String eventName, long score) {
        CourseRegularGradeRecord newRecord = new CourseRegularGradeRecord();
        newRecord.setAttachedId(courseDBId);
        newRecord.setStudentId(studentId);
        newRecord.setEventName(eventName);
        newRecord.setScore(score);
        courseRegularGradeRecordRepository.save(newRecord);
        return "redirect:/course/" + courseDBId + "/studentList";
    }

    @RequestMapping("/{courseDBId}/getScore/{studentId}")
    public @ResponseBody
    List<CourseRegularGradeRecord> getScore(@PathVariable long courseDBId,
                                            @PathVariable long studentId) {
        return courseRegularGradeRecordRepository.findByAttachedIdAndStudentId(courseDBId, studentId);
    }

    @RequestMapping("/{courseDBId}/announcement")
    public String announcement(@PathVariable long courseDBId) {
        return "";
    }

    @RequestMapping("/{courseDBId}/homework")
    public String homework(@PathVariable long courseDBId) {
        return "";
    }

    @RequestMapping("/{courseDBId}/problem")
    public String problem(@PathVariable long courseDBId) {
        return "";
    }

    @RequestMapping("/{courseDBId}/discussion")
    public String discussion(@PathVariable long courseDBId) {
        return "";
    }

    @RequestMapping("/{courseDBId}/weight")
    public String weight(@PathVariable long courseDBId) {
        return "";
    }
}
