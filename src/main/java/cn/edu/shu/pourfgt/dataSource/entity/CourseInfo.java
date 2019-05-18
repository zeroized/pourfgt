package cn.edu.shu.pourfgt.dataSource.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CourseInfo {
    @Id
    private long id;
    private String courseId;
    private String courseName;
    private int year;
    private int semester; //0 秋季;1 冬季;2 春季;3 夏季

    private String teacherId;

    private String courseType;
    private int discussionType;//0 单人;1 小组

    private int scoreType;//0 优;1 A;2 分
    private double normalRatio;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public int getDiscussionType() {
        return discussionType;
    }

    public void setDiscussionType(int discussionType) {
        this.discussionType = discussionType;
    }

    public int getScoreType() {
        return scoreType;
    }

    public void setScoreType(int scoreType) {
        this.scoreType = scoreType;
    }

    public double getNormalRatio() {
        return normalRatio;
    }

    public void setNormalRatio(double normalRatio) {
        this.normalRatio = normalRatio;
    }
}
