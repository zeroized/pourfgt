package cn.edu.shu.pourfgt.controller;

import cn.edu.shu.pourfgt.dataSource.dao.*;
import cn.edu.shu.pourfgt.dataSource.entity.*;
import cn.edu.shu.pourfgt.util.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
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
import java.util.*;

@Controller
@RequestMapping("/teacher/course")
public class TeacherCourseController {
    private final CourseStudentMessageRepository courseStudentMessageRepository;
    private final CourseInfoRepository courseInfoRepository;
    private final CourseRegularGradeEventRepository courseRegularGradeEventRepository;
    private final CourseStudentRepository courseStudentRepository;
    private final CourseRegularGradeRecordRepository courseRegularGradeRecordRepository;
    private final CoursePostRepository coursePostRepository;
    private final CourseDiscussionRepository courseDiscussionRepository;
    @Value("${course.semester.start-day}")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDay;
    @Value("${course.announcement.file-path}")
    private String announceFileDir;
    @Value("${course.current.year}")
    private int currentYear;
    @Value("${course.current.semester}")
    private int currentSemester;

    public TeacherCourseController(CourseInfoRepository courseInfoRepository,
                                   CourseRegularGradeEventRepository courseRegularGradeEventRepository,
                                   CourseStudentRepository courseStudentRepository,
                                   CourseRegularGradeRecordRepository courseRegularGradeRecordRepository, CoursePostRepository coursePostRepository, CourseStudentMessageRepository courseStudentMessageRepository, CourseDiscussionRepository courseDiscussionRepository) {
        this.courseInfoRepository = courseInfoRepository;
        this.courseRegularGradeEventRepository = courseRegularGradeEventRepository;
        this.courseStudentRepository = courseStudentRepository;
        this.courseRegularGradeRecordRepository = courseRegularGradeRecordRepository;
        this.coursePostRepository = coursePostRepository;
        this.courseStudentMessageRepository = courseStudentMessageRepository;
        this.courseDiscussionRepository = courseDiscussionRepository;
    }

    @InitBinder
    public void initDate(WebDataBinder webDataBinder) {
        webDataBinder.addCustomFormatter(new DateFormatter("yyyy-MM-dd"));
    }

    @ModelAttribute
    public void initModel(@PathVariable(required = false) Long courseDBId, Model model,
                          HttpServletRequest request) {
        if (courseDBId != null) {
            long id = courseDBId;
            CourseInfo courseInfo = courseInfoRepository.findById(id);
            model.addAttribute("courseId", courseInfo.getId());
            model.addAttribute("courseName", courseInfo.getCourseName());
        }
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");
        model.addAttribute("teacherId", userId);
        model.addAttribute("leftNavId", 0);
    }


    @RequestMapping("/list")
    public ModelAndView courseList(Model model) {
        ModelAndView mav = new ModelAndView("teacher/course/list");
        List<CourseInfo> courseInfos = courseInfoRepository.findByTeacherId((String) model.asMap().get("teacherId"));
        mav.addObject("currYear", currentYear);
        mav.addObject("currSemester", currentSemester);
        mav.addObject("courses", courseInfos);
        return mav;
    }

    @RequestMapping("/create")
    public String courseCreate() {
        return "teacher/course/create";
    }


    @PostMapping("/createCourse")
    public String createCourse(String courseId, String courseName, Integer[] courseType,
                               Integer discussionType, MultipartFile studentList, Integer scoreType,
                               Double normalRatio, String[] name, Double[] ratio, Model model) {
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

        newCourse.setYear(this.currentYear);
        newCourse.setSemester(this.currentSemester);

        String teacherId = (String) model.asMap().get("teacherId");
        newCourse.setTeacherId(teacherId);

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
            newGrade.setCourseId(courseId);
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
                              @RequestParam(required = false) Integer week,
                              @RequestParam(required = false) MultipartFile file,
                              @RequestParam(required = false) Integer deadline,
                              @RequestParam(required = false) String forward)
            throws IOException {
        CoursePost newAnnouncement = new CoursePost();
        newAnnouncement.setAttachedId(courseDBId);
        long currentTime = new Date().getTime();
        newAnnouncement.setCreateTime(currentTime);
        newAnnouncement.setTitle(title);
        newAnnouncement.setContent(content);
        newAnnouncement.setType(type);
        if (!file.isEmpty()) {
            String localFilePath = announceFileDir + courseDBId + "_" + currentTime + "_" + file.getOriginalFilename();
            File localFile = new File(localFilePath);
            file.transferTo(localFile);
            newAnnouncement.setHasFile(true);
            newAnnouncement.setFilePath(localFilePath);
        } else {
            newAnnouncement.setHasFile(false);
            newAnnouncement.setFilePath("");
        }
        if (week != null) {
            newAnnouncement.setWeek(week);
        } else {
            newAnnouncement.setWeek(-1);
        }
        if (deadline != null) {
            newAnnouncement.setHasDeadline(true);
            newAnnouncement.setDeadline(deadline);
        } else {
            newAnnouncement.setHasDeadline(false);
            newAnnouncement.setDeadline(-1);
        }
        coursePostRepository.save(newAnnouncement);
        if (forward != null) {
            return "redirect:/teacher/course/" + courseDBId + "/" + forward;
        }
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
        return FileUtil.download(localFilePath, fileName);
    }

    @RequestMapping("/getHomeWorkFile")
    public ResponseEntity<InputStreamResource> getHomeworkFile(long id) throws FileNotFoundException {
        String localFilePath = courseStudentMessageRepository.findById(id).getFilePath();
        String fileName = Paths.get(localFilePath).getFileName().toString();
        return FileUtil.download(localFilePath, fileName);
    }

    @RequestMapping("/{courseDBId}/homework")
    public ModelAndView homework(@PathVariable long courseDBId,
                                 @RequestParam(defaultValue = "1") int week) {
        ModelAndView mav = new ModelAndView("teacher/course/sub/homework");
        List<CourseStudentMessage> messages = courseStudentMessageRepository
                .findByAttachedIdAndTypeAndHomeworkWeek(courseDBId, 0, week);
        mav.addObject("week", week);
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

    @PostMapping("/answer")
    public String answerQuestion(long questionId, String response) {
        CourseStudentMessage message = courseStudentMessageRepository.findById(questionId);
        long courseDBId = message.getAttachedId();
        courseStudentMessageRepository.delete(message);
        message.setHasResponse(true);
        message.setResponse(response);
        courseStudentMessageRepository.save(message);
        return "redirect:/teacher/course/" + courseDBId + "/question";
    }

    @RequestMapping("/{courseDBId}/discussion")
    public ModelAndView discussion(@PathVariable long courseDBId) {
        ModelAndView mav = new ModelAndView("teacher/course/sub/discussion");
        List<CoursePost> discussions = coursePostRepository.findByAttachedIdAndType(courseDBId, 3);
        mav.addObject("discussions", discussions);
        mav.addObject("navId", 4);
        return mav;
    }

    @GetMapping("/getDiscussion")
    public @ResponseBody
    Map<String, Object> getDiscussion(long id, long courseId) {
        CoursePost discussion = coursePostRepository.findById(id);
        List<CourseDiscussion> students = courseDiscussionRepository
                .findByAttachedIdAndDiscussionId(courseId, id);
        Map<String, Object> objectMap = new HashMap<>(3);
        objectMap.put("discussion", discussion);
        objectMap.put("students", students);
        if (students.size() == 0) {
            objectMap.put("selected", false);
        } else {
            objectMap.put("selected", true);
        }
        return objectMap;
    }

    //TODO finish the function
    @PostMapping("/scoreDiscussion")
    public String scoreDiscussionGroup(long courseDBId, long discussionId, int score) {
        List<CourseDiscussion> students = courseDiscussionRepository.findByAttachedIdAndDiscussionId(courseDBId, discussionId);
        for (CourseDiscussion student : students) {
            CourseRegularGradeRecord newRecord = new CourseRegularGradeRecord();
            newRecord.setAttachedId(courseDBId);
            newRecord.setStudentId(student.getStudentId());
            newRecord.setEventName("研讨");
            newRecord.setScore(student.getRatio() * score);
            courseRegularGradeRecordRepository.save(newRecord);
        }
        return "redirect:/teacher/course/" + courseDBId + "/discussion";
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
    public String updateWeight(@PathVariable long courseDBId,
                               long[] id, double[] ratio) {
        double sum = Arrays.stream(ratio).sum();
        for (int i = 0; i < ratio.length; i++) {
            ratio[i] = ratio[i] / sum;
        }
        for (int i = 0; i < id.length; i++) {
            CourseRegularGradeEvent event = courseRegularGradeEventRepository.findById(id[i]);
            courseRegularGradeEventRepository.delete(event);
            event.setRatio(ratio[i]);
            courseRegularGradeEventRepository.save(event);
        }
        return "redirect:/teacher/course/" + courseDBId + "/weight";
    }
}
