package cn.edu.shu.pourfgt.dataSource.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class CourseStudent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long attachedId;
    private long studentId;
    private String studentName;

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

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}
