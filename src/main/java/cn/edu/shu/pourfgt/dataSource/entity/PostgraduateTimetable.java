package cn.edu.shu.pourfgt.dataSource.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class PostgraduateTimetable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String teacherId;
    private int forYear;
    private int forType;
    private long keyDate;
    private String event;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public int getForYear() {
        return forYear;
    }

    public void setForYear(int forYear) {
        this.forYear = forYear;
    }

    public int getForType() {
        return forType;
    }

    public void setForType(int forType) {
        this.forType = forType;
    }

    public long getKeyDate() {
        return keyDate;
    }

    public void setKeyDate(long keyDate) {
        this.keyDate = keyDate;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
