package cn.edu.shu.pourfgt.dataSource.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CourseRegularGrade {
    @Id
    private long id;
    private long attachedId;
    private String eventName;
    private double score;
}
