package cn.edu.shu.pourfgt.dataSource.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {
    @Id
    private String userId;
    private String password;
    private int role;//0 老师;1 学生
    private int grade;//-1 老师;0 本科生;1 研究生

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}
