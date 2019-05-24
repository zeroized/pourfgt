package cn.edu.shu.pourfgt.dataSource.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class GraduationStudentSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private int year;
    private int semester;

    private long submitDate;
    private long studentId;

    private String event;
    private String filePath;
    private double score = -1;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public long getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(long submitDate) {
        this.submitDate = submitDate;
    }

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
