package model;

import java.util.Date;

public class Course {
    private int courseID;
    private String courseName;
    private String courseCode;
    private Date createdAt;

    public Course() {}

    public Course(int courseID, String courseName, String courseCode, Date createdAt) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getCourseID() { return courseID; }
    public void setCourseID(int courseID) { this.courseID = courseID; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}