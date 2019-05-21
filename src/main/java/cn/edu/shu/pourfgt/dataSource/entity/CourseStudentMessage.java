package cn.edu.shu.pourfgt.dataSource.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class CourseStudentMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long attachedId;
    private int type;//0 作业;1 问题

    private int homeworkWeek = -1;
    private boolean overdue = false;
    private long studentId;
    private long createTime;

    private String content;
    private boolean hasFile;
    private String filePath;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAttachedId() {
        return attachedId;
    }

    public void setAttachedId(long attachedId) {
        this.attachedId = attachedId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getHomeworkWeek() {
        return homeworkWeek;
    }

    public void setHomeworkWeek(int homeworkWeek) {
        this.homeworkWeek = homeworkWeek;
    }

    public boolean isOverdue() {
        return overdue;
    }

    public void setOverdue(boolean overdue) {
        this.overdue = overdue;
    }

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isHasFile() {
        return hasFile;
    }

    public void setHasFile(boolean hasFile) {
        this.hasFile = hasFile;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
