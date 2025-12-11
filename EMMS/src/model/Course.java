package model;

import java.util.Date;

public class Course {
    private int courseID;
    private String courseName;
    private String courseCode;
    private String description;
    private int credits;
    private int instructorID;
    private String instructorName;
    private Date createdAt;
    private Date updatedAt;

    // Constructors
    public Course() {}

    public Course(int courseID, String courseName, String courseCode, String description, 
                 int credits, int instructorID, Date createdAt, Date updatedAt) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.description = description;
        this.credits = credits;
        this.instructorID = instructorID;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Course(String courseName, String courseCode, String description, 
                 int credits, int instructorID) {
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.description = description;
        this.credits = credits;
        this.instructorID = instructorID;
    }

    // Getters and Setters
    public int getCourseID() { return courseID; }
    public void setCourseID(int courseID) { this.courseID = courseID; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }

    public int getInstructorID() { return instructorID; }
    public void setInstructorID(int instructorID) { this.instructorID = instructorID; }

    public String getInstructorName() { return instructorName; }
    public void setInstructorName(String instructorName) { this.instructorName = instructorName; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }

    // Utility methods
    @Override
    public String toString() {
        return "Course{" +
                "courseID=" + courseID +
                ", courseName='" + courseName + '\'' +
                ", courseCode='" + courseCode + '\'' +
                ", credits=" + credits +
                ", instructorID=" + instructorID +
                '}';
    }

    // Validation methods
    public boolean isValid() {
        return courseName != null && !courseName.trim().isEmpty() &&
               courseCode != null && !courseCode.trim().isEmpty() &&
               credits > 0 && credits <= 10;
    }

    public String getCourseInfo() {
        return courseCode + " - " + courseName + " (" + credits + " credits)";
    }
}