package cn.edu.shu.pourfgt.controller;

import cn.edu.shu.pourfgt.dataSource.dao.*;
import cn.edu.shu.pourfgt.dataSource.entity.*;
import cn.edu.shu.pourfgt.util.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
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
    private final CourseDiscussionRepository courseDiscussionRepository;

    @Value("${course.current.year}")
    private int currentYear;
    @Value("${course.current.semester}")
    private int currentSemester;


    public StudentCourseController(@Value("${course.announcement.file-path}") String announceFileDir,
                                   CourseStudentRepository courseStudentRepository,
                                   CourseInfoRepository courseInfoRepository,
                                   CoursePostRepository coursePostRepository,
                                   CourseStudentMessageRepository courseStudentMessageRepository, CourseDiscussionRepository courseDiscussionRepository) {
        this.announceFileDir = announceFileDir;
        this.courseStudentRepository = courseStudentRepository;
        this.courseInfoRepository = courseInfoRepository;
        this.coursePostRepository = coursePostRepository;
        this.courseStudentMessageRepository = courseStudentMessageRepository;
        this.courseDiscussionRepository = courseDiscussionRepository;
    }

    @ModelAttribute
    public void initModel(@PathVariable(required = false) Long courseDBId, HttpServletRequest request, Model model) {
        if (courseDBId != null) {
            long id = courseDBId;
            CourseInfo courseInfo = courseInfoRepository.findById(id);
            model.addAttribute("courseId", courseInfo.getId());
            model.addAttribute("courseName", courseInfo.getCourseName());
        }
        HttpSession session = request.getSession();
        Long studentId = Long.valueOf((String) session.getAttribute("userId"));
        model.addAttribute("studentId", studentId);
        model.addAttribute("leftNavId", 0);
        model.addAttribute("availableNavs", new Integer[]{0, 1});
    }

    @RequestMapping("/list")
    public ModelAndView courseList(Model model) {
        ModelAndView mav = new ModelAndView("student/course/list");
        long studentId = (Long) model.asMap().get("studentId");
        List<CourseStudent> courses = courseStudentRepository.findByStudentId(studentId);
        List<Long> courseIds = courses.stream()
                .map(CourseStudent::getAttachedId)
                .collect(Collectors.toList());
        List<String> courseNames = courseIds.stream()
                .map(x -> courseInfoRepository.findById(x.longValue()).getCourseName())
                .collect(Collectors.toList());
        mav.addObject("currYear", currentYear);
        mav.addObject("currSemester", currentSemester);
        mav.addObject("courses", courses);
        mav.addObject("courseNames", courseNames);
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

    @RequestMapping("/getPostFile")
    public ResponseEntity<InputStreamResource> getAnnouncementFile(long id) throws FileNotFoundException {
        String localFilePath = coursePostRepository.findById(id).getFilePath();
        String fileName = Paths.get(localFilePath).getFileName().toString();
        return FileUtil.download(localFilePath, fileName);
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
        mav.addObject("navId", 2);
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

    @RequestMapping("/{courseDBId}/discussion")
    public ModelAndView discussion(@PathVariable long courseDBId) {
        ModelAndView mav = new ModelAndView("student/course/sub/discussion");
        List<CoursePost> discussions = coursePostRepository.findByAttachedIdAndType(courseDBId, 3);
        List<CourseDiscussion> discussionSelections = courseDiscussionRepository.findByAttachedId(courseDBId);
        List<Long> selected = discussionSelections.stream()
                .map(CourseDiscussion::getDiscussionId)
                .distinct()
                .collect(Collectors.toList());
        List<CoursePost> available = discussions.stream()
                .filter(x -> !selected.contains(x.getId()))
                .collect(Collectors.toList());
        mav.addObject("discussions", available);
        mav.addObject("navId", 3);
        return mav;
    }

    @GetMapping("/getDiscussion")
    public @ResponseBody
    CoursePost getDiscussion(long id) {
        return coursePostRepository.findById(id);
    }

    @PostMapping("/selectDiscussion")
    public String selectDiscussion(long courseDBId, long discussionId, long[] studentId, long[] ratio) {
        for (int i = 0; i < studentId.length; i++) {
            CourseDiscussion newDiscussions = new CourseDiscussion();
            newDiscussions.setAttachedId(courseDBId);
            newDiscussions.setDiscussionId(discussionId);
            newDiscussions.setStudentId(studentId[i]);
            newDiscussions.setRatio(ratio[i]);
            courseDiscussionRepository.save(newDiscussions);
        }
        return "redirect:/student/course/" + courseDBId + "/discussion";
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
        if (!file.isEmpty()) {
            String localFilePath = announceFileDir + courseDBId + "_" + currentTime + "_" + file.getOriginalFilename();
            File localFile = new File(localFilePath);
            file.transferTo(localFile);
            newQuestion.setHasFile(true);
            newQuestion.setFilePath(localFilePath);
        }
        return newQuestion;
    }


}
