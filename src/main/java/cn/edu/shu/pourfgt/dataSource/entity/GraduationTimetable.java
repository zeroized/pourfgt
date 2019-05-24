package cn.edu.shu.pourfgt.dataSource.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class GraduationTimetable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private int year;
    private int semester;
    private long keyDate;
    private String event;

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
