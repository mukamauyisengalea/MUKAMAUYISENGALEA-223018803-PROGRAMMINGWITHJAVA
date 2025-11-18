package model;

import java.util.Date;

public class Grade {
    private int gradeID;
    private int assignmentID;
    private int studentID;
    private int courseID;
    private double score;
    private String letterGrade;
    private String remarks;
    private int gradedBy;
    private Date createdAt;
    private Date updatedAt;
    
    // Default constructor
    public Grade() {}
    
    // Full constructor
    public Grade(int gradeID, int assignmentID, int studentID, int courseID, 
                 double score, String letterGrade, String remarks, int gradedBy, 
                 Date createdAt, Date updatedAt) {
        this.gradeID = gradeID;
        this.assignmentID = assignmentID;
        this.studentID = studentID;
        this.courseID = courseID;
        this.score = score;
        this.letterGrade = letterGrade;
        this.remarks = remarks;
        this.gradedBy = gradedBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Constructor without auto-generated fields (for insertion)
    public Grade(int assignmentID, int studentID, int courseID, double score, 
                 String letterGrade, String remarks, int gradedBy) {
        this.assignmentID = assignmentID;
        this.studentID = studentID;
        this.courseID = courseID;
        this.score = score;
        this.letterGrade = letterGrade;
        this.remarks = remarks;
        this.gradedBy = gradedBy;
    }
    
    // Getters and Setters
    public int getGradeID() { return gradeID; }
    public void setGradeID(int gradeID) { this.gradeID = gradeID; }
    
    public int getAssignmentID() { return assignmentID; }
    public void setAssignmentID(int assignmentID) { this.assignmentID = assignmentID; }
    
    public int getStudentID() { return studentID; }
    public void setStudentID(int studentID) { this.studentID = studentID; }
    
    public int getCourseID() { return courseID; }
    public void setCourseID(int courseID) { this.courseID = courseID; }
    
    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }
    
    public String getLetterGrade() { return letterGrade; }
    public void setLetterGrade(String letterGrade) { this.letterGrade = letterGrade; }
    
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    
    public int getGradedBy() { return gradedBy; }
    public void setGradedBy(int gradedBy) { this.gradedBy = gradedBy; }
    
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    
    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
    
    // Utility method to calculate letter grade from score
    public static String calculateLetterGrade(double score) {
        if (score >= 93) return "A";
        else if (score >= 90) return "A-";
        else if (score >= 87) return "B+";
        else if (score >= 83) return "B";
        else if (score >= 80) return "B-";
        else if (score >= 77) return "C+";
        else if (score >= 73) return "C";
        else if (score >= 70) return "C-";
        else if (score >= 67) return "D+";
        else if (score >= 63) return "D";
        else if (score >= 60) return "D-";
        else return "F";
    }
    
    // Utility method to calculate GPA points
    public static double calculateGPAPoints(String letterGrade) {
        switch (letterGrade) {
            case "A": return 4.0;
            case "A-": return 3.7;
            case "B+": return 3.3;
            case "B": return 3.0;
            case "B-": return 2.7;
            case "C+": return 2.3;
            case "C": return 2.0;
            case "C-": return 1.7;
            case "D+": return 1.3;
            case "D": return 1.0;
            case "D-": return 0.7;
            case "F": return 0.0;
            default: return 0.0;
        }
    }
    
    @Override
    public String toString() {
        return String.format("Grade{gradeID=%d, studentID=%d, assignmentID=%d, score=%.2f, letterGrade='%s'}",
                gradeID, studentID, assignmentID, score, letterGrade);
    }
}