package model;

import java.util.Date;

public class Student {
    private int studentID;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String sex;
    private Date birthdate;
    private Date createdAt;

    public Student() {}

    public Student(int studentID, String firstName, String lastName, String username, 
                  String password, String sex, Date birthdate, Date createdAt) {
        this.studentID = studentID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.sex = sex;
        this.birthdate = birthdate;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getStudentID() { return studentID; }
    public void setStudentID(int studentID) { this.studentID = studentID; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getSex() { return sex; }
    public void setSex(String sex) { this.sex = sex; }

    public Date getBirthdate() { return birthdate; }
    public void setBirthdate(Date birthdate) { this.birthdate = birthdate; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}