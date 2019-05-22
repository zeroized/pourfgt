package cn.edu.shu.pourfgt.controller;

import cn.edu.shu.pourfgt.dataSource.dao.*;
import cn.edu.shu.pourfgt.dataSource.entity.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/teacher/course")
public class TeacherCourseController {
    private final String announceFileDir;
    private final CourseStudentMessageRepository courseStudentMessageRepository;

    private final CourseInfoRepository courseInfoRepository;
    private final CourseRegularGradeEventRepository courseRegularGradeEventRepository;
    private final CourseStudentRepository courseStudentRepository;
    private final CourseRegularGradeRecordRepository courseRegularGradeRecordRepository;
    private final CoursePostRepository coursePostRepository;
    @Value("${course.semester.start-day}")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDay;

    public TeacherCourseController(@Value("${course.announcement.file-path}") String announceFileDir,
                                   CourseInfoRepository courseInfoRepository,
                                   CourseRegularGradeEventRepository courseRegularGradeEventRepository,
                                   CourseStudentRepository courseStudentRepository,
                                   CourseRegularGradeRecordRepository courseRegularGradeRecordRepository, CoursePostRepository coursePostRepository, CourseStudentMessageRepository courseStudentMessageRepository) {
        this.announceFileDir = announceFileDir;
        this.courseInfoRepository = courseInfoRepository;
        this.courseRegularGradeEventRepository = courseRegularGradeEventRepository;
        this.courseStudentRepository = courseStudentRepository;
        this.courseRegularGradeRecordRepository = courseRegularGradeRecordRepository;
        this.coursePostRepository = coursePostRepository;
        this.courseStudentMessageRepository = courseStudentMessageRepository;
    }

    @InitBinder
    public void initDate(WebDataBinder webDataBinder) {
        webDataBinder.addCustomFormatter(new DateFormatter("yyyy-MM-dd"));
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
        ModelAndView mav = new ModelAndView("teacher/course/list");
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
        return "redirect:/teacher/course/" + id + "/studentList";
    }

    @RequestMapping("/{courseDBId}")
    public String courseNav(@PathVariable long courseDBId) {
        return "redirect:/teacher/course/" + courseDBId + "/studentList";
    }

    @RequestMapping("/{courseDBId}/studentList")
    public ModelAndView studentList(@PathVariable long courseDBId) {
        ModelAndView mav = new ModelAndView("teacher/course/sub/studentList");
        List<CourseStudent> students = courseStudentRepository.findByAttachedId(courseDBId);
        mav.addObject("students", students);
        mav.addObject("navId", 0);
        return mav;
    }

    @PostMapping("/record/{courseDBId}")
    public String recordRegularGrade(@PathVariable long courseDBId, long studentId,
                                     String eventName, long score) {
        //TODO add checking the group discussion score
        CourseRegularGradeRecord newRecord = new CourseRegularGradeRecord();
        newRecord.setAttachedId(courseDBId);
        newRecord.setStudentId(studentId);
        newRecord.setEventName(eventName);
        newRecord.setScore(score);
        courseRegularGradeRecordRepository.save(newRecord);
        return "redirect:/teacher/course/" + courseDBId + "/studentList";
    }

    @RequestMapping("/{courseDBId}/getScore/{studentId}")
    public @ResponseBody
    List<CourseRegularGradeRecord> getScore(@PathVariable long courseDBId,
                                            @PathVariable long studentId) {
        return courseRegularGradeRecordRepository
                .findByAttachedIdAndStudentId(courseDBId, studentId);
    }

    @RequestMapping("/{courseDBId}/announcement")
    public ModelAndView announcement(@PathVariable long courseDBId) {
        ModelAndView mav = new ModelAndView("teacher/course/sub/announcement");
        List<CoursePost> announcements = coursePostRepository.findByAttachedId(courseDBId);
        mav.addObject("announcements", announcements);
        mav.addObject("navId", 1);
        return mav;
    }

    @PostMapping("/post/{courseDBId}")
    public String releasePost(@PathVariable long courseDBId, String title,
                                      String content, int type,
                                      @RequestParam(required = false) int week,
                                      @RequestParam(required = false) MultipartFile file,
                                      @RequestParam(required = false) Integer deadline)
            throws IOException {
        CoursePost newAnnouncement = new CoursePost();
        newAnnouncement.setAttachedId(courseDBId);
        long currentTime = new Date().getTime();
        newAnnouncement.setCreateTime(currentTime);
        newAnnouncement.setTitle(title);
        newAnnouncement.setContent(content);
        newAnnouncement.setType(type);
        if (file != null) {
            String localFilePath = announceFileDir + courseDBId + "_" + currentTime + "_" + file.getOriginalFilename();
            File localFile = new File(localFilePath);
            file.transferTo(localFile);
            newAnnouncement.setHasFile(true);
            newAnnouncement.setFilePath(localFilePath);
        } else {
            newAnnouncement.setHasFile(false);
            newAnnouncement.setFilePath("");
        }
        if (deadline != null) {
            newAnnouncement.setHasDeadline(true);
            newAnnouncement.setDeadline(deadline);
            newAnnouncement.setWeek(week);
        } else {
            newAnnouncement.setHasDeadline(false);
            newAnnouncement.setDeadline(-1);
            newAnnouncement.setWeek(-1);
        }
        coursePostRepository.save(newAnnouncement);
        return "redirect:/teacher/course/" + courseDBId + "/announcement";
    }

    @RequestMapping("/getPost")
    public @ResponseBody
    CoursePost getAnnouncement(long id) {
        return coursePostRepository.findById(id);
    }

    @RequestMapping("/getAnnouncementFile")
    public ResponseEntity<InputStreamResource> getAnnouncementFile(long id) throws FileNotFoundException {
        String localFilePath = coursePostRepository.findById(id).getFilePath();
        String fileName = Paths.get(localFilePath).getFileName().toString();
        File file = new File(localFilePath);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .contentLength(file.length())
                .body(resource);
    }

    @RequestMapping("/{courseDBId}/homework")
    public ModelAndView homework(@PathVariable long courseDBId,
                                 @RequestParam(defaultValue = "1") int week) {
        ModelAndView mav = new ModelAndView("teacher/course/sub/homework");
        List<CourseStudentMessage> messages = courseStudentMessageRepository
                .findByAttachedIdAndTypeAndHomeworkWeek(courseDBId, 0, week);
        mav.addObject("homeworkList", messages);
        mav.addObject("navId", 2);
        return mav;
    }

    @GetMapping("/getHomework")
    public @ResponseBody
    CourseStudentMessage getHomework(long id) {
        return courseStudentMessageRepository.findById(id);
    }

    @RequestMapping("/{courseDBId}/question")
    public ModelAndView question(@PathVariable long courseDBId) {
        ModelAndView mav = new ModelAndView("teacher/course/sub/question");
        List<CourseStudentMessage> questions = courseStudentMessageRepository.findByAttachedIdAndType(courseDBId, 1);
        mav.addObject("questions", questions);
        mav.addObject("navId", 3);

        return mav;
    }

    @GetMapping("/getQuestion")
    public @ResponseBody
    CourseStudentMessage getQuestion(long id) {
        return courseStudentMessageRepository.findById(id);
    }

    @RequestMapping("/{courseDBId}/discussion")
    public ModelAndView discussion(@PathVariable long courseDBId) {
        ModelAndView mav = new ModelAndView("teacher/course/sub/discussion");
        List<CoursePost> discussions = coursePostRepository.findByAttachedIdAndType(courseDBId, 3);
        mav.addObject("discussions", discussions);
        mav.addObject("navId", 4);
        return mav;
    }

    @RequestMapping("/{courseDBId}/weight")
    public ModelAndView weight(@PathVariable long courseDBId) {
        ModelAndView mav = new ModelAndView("teacher/course/sub/weight");
        List<CourseRegularGradeEvent> events = courseRegularGradeEventRepository.findByAttachedId(courseDBId);
        mav.addObject("events", events);
        mav.addObject("navId", 5);
        return mav;
    }

    @PostMapping("/updateWeight/{courseDBId}")
    public String updateWeight(@PathVariable String courseDBId,
                               long[] id, double[] ratio) {
        return "redirect:/teacher/course/" + courseDBId + "/weight";
    }
}
