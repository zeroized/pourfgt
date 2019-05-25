package cn.edu.shu.pourfgt.dataSource.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class PostgraduateStudentSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private int type;//0 论文;1 文件;2 文档;3 源代码

    private long submitDate;
    private long studentId;
    private String studentName;

    private String filePath;
    private boolean hasResponse = false;
    private String response;
    private boolean hasResponseFile = false;
    private String responseFilePath = "";

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean isHasResponse() {
        return hasResponse;
    }

    public void setHasResponse(boolean hasResponse) {
        this.hasResponse = hasResponse;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public boolean isHasResponseFile() {
        return hasResponseFile;
    }

    public void setHasResponseFile(boolean hasResponseFile) {
        this.hasResponseFile = hasResponseFile;
    }

    public String getResponseFilePath() {
        return responseFilePath;
    }

    public void setResponseFilePath(String responseFilePath) {
        this.responseFilePath = responseFilePath;
    }
}
