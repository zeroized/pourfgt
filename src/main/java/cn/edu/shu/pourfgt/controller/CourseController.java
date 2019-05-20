package cn.edu.shu.pourfgt.controller;

import cn.edu.shu.pourfgt.dataSource.dao.*;
import cn.edu.shu.pourfgt.dataSource.entity.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
@RequestMapping("/course")
public class CourseController {
    private final String announceFileDir;

    private final CourseInfoRepository courseInfoRepository;
    private final CourseRegularGradeEventRepository courseRegularGradeEventRepository;
    private final CourseStudentRepository courseStudentRepository;
    private final CourseRegularGradeRecordRepository courseRegularGradeRecordRepository;
    private final CourseAnnouncementRepository courseAnnouncementRepository;

    public CourseController(@Value("${course.announcement.file-path}") String announceFileDir,
                            CourseInfoRepository courseInfoRepository,
                            CourseRegularGradeEventRepository courseRegularGradeEventRepository,
                            CourseStudentRepository courseStudentRepository,
                            CourseRegularGradeRecordRepository courseRegularGradeRecordRepository, CourseAnnouncementRepository courseAnnouncementRepository) {
        this.announceFileDir = announceFileDir;
        this.courseInfoRepository = courseInfoRepository;
        this.courseRegularGradeEventRepository = courseRegularGradeEventRepository;
        this.courseStudentRepository = courseStudentRepository;
        this.courseRegularGradeRecordRepository = courseRegularGradeRecordRepository;
        this.courseAnnouncementRepository = courseAnnouncementRepository;
    }

    @InitBinder
    public void initDate(WebDataBinder webDataBinder) {
        webDataBinder.addCustomFormatter(new DateFormatter("yyyy-MM-dd"));
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
        //TODO add checking the group discussion score
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
        return courseRegularGradeRecordRepository
                .findByAttachedIdAndStudentId(courseDBId, studentId);
    }

    @RequestMapping("/{courseDBId}/announcement")
    public ModelAndView announcement(@PathVariable long courseDBId) {
        ModelAndView mav = new ModelAndView("course/sub/announcement");
        List<CourseAnnouncement> announcements = courseAnnouncementRepository.findByAttachedId(courseDBId);
        mav.addObject("announcements", announcements);
        mav.addObject("courseId", courseDBId);
        return mav;
    }

    @PostMapping("/announce/{courseDBId}")
    public String publishAnnouncement(@PathVariable long courseDBId, String title,
                                      String content, int type,
                                      @RequestParam(required = false) MultipartFile file,
                                      @RequestParam(required = false) Integer deadline)
            throws IOException {
        CourseAnnouncement newAnnouncement = new CourseAnnouncement();
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
        } else {
            newAnnouncement.setHasDeadline(false);
            newAnnouncement.setDeadline(-1);
        }
        courseAnnouncementRepository.save(newAnnouncement);
        return "redirect:/course/" + courseDBId + "/announcement";
    }

    @RequestMapping("/getAnnouncement/{announcementId}")
    public @ResponseBody
    CourseAnnouncement getAnnouncement(@PathVariable long announcementId) {
        return courseAnnouncementRepository.findById(announcementId);
    }

    @RequestMapping("/getAnnouncementFile/{announcementId}")
    public ResponseEntity<InputStreamResource> getAnnouncementFile(@PathVariable long announcementId) throws FileNotFoundException {
        String localFilePath = courseAnnouncementRepository.findById(announcementId).getFilePath();
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
