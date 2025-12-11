package model;

import java.sql.Timestamp; 
import java.util.Date;

public class Instructor {
    private int instructorID;
    private String name;
    private String identifier;
    private String status;
    private String department;
    private String location;
    private String contact;
    private String email;
    private String phone;
    private String username;
    private String password;
    private String qualifications;
    private Date assignedSince;
    private int courseID;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // ========== CONSTRUCTORS ==========
    
    // 1. Default constructor (REQUIRED for DAO extraction)
    public Instructor() {
        this.status = "ACTIVE";
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }

    // 2. Constructor for admin adding instructor (without password)
    public Instructor(String name, String identifier, String status,
                     String department, String location, String contact, String email,
                     String phone, Date assignedSince, int courseID) {
        this(); // Call default constructor
        this.name = name;
        this.identifier = identifier;
        this.status = status;
        this.department = department;
        this.location = location;
        this.contact = contact;
        this.email = email;
        this.phone = phone;
        this.assignedSince = assignedSince;
        this.courseID = courseID;
        // username and password will be null initially
    }

    // 3. Constructor with minimal parameters (for backward compatibility)
    public Instructor(String name, String identifier, String status,
                     String location, String contact, Date assignedSince, int courseID) {
        this(); // Call default constructor
        this.name = name;
        this.identifier = identifier;
        this.status = status;
        this.department = "General"; // Default department
        this.location = location;
        this.contact = contact;
        this.email = generateEmailFromName(name); // Auto-generate email
        this.phone = ""; // Empty phone
        this.assignedSince = assignedSince;
        this.courseID = courseID;
    }
 // Add this constructor to Instructor.java
    public Instructor(int instructorID, String name, String identifier, String status,
                     String location, String contact, String username, String password,
                     Date assignedSince, int courseID) {
        this(); // Call default constructor
        this.instructorID = instructorID;
        this.name = name;
        this.identifier = identifier;
        this.status = status;
        this.location = location;
        this.contact = contact;
        this.username = username;
        this.password = password;
        this.assignedSince = assignedSince;
        this.courseID = courseID;
        // Set default values for missing fields
        this.department = "General";
        this.email = generateEmailFromName(name);
        this.phone = "";
        this.qualifications = "";
    }
    // 4. Full constructor with all fields
    public Instructor(int instructorID, String name, String identifier, String status,
                     String department, String location, String contact, String email,
                     String phone, String username, String password, String qualifications,
                     Date assignedSince, int courseID, Timestamp createdAt, Timestamp updatedAt) {
        this.instructorID = instructorID;
        this.name = name;
        this.identifier = identifier;
        this.status = status;
        this.department = department;
        this.location = location;
        this.contact = contact;
        this.email = email;
        this.phone = phone;
        this.username = username;
        this.password = password;
        this.qualifications = qualifications;
        this.assignedSince = assignedSince;
        this.courseID = courseID;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // ========== GETTERS AND SETTERS ==========
    public int getInstructorID() { return instructorID; }
    public void setInstructorID(int instructorID) { this.instructorID = instructorID; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getIdentifier() { return identifier; }
    public void setIdentifier(String identifier) { this.identifier = identifier; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getQualifications() { return qualifications; }
    public void setQualifications(String qualifications) { this.qualifications = qualifications; }

    public Date getAssignedSince() { return assignedSince; }
    public void setAssignedSince(Date assignedSince) { this.assignedSince = assignedSince; }

    public int getCourseID() { return courseID; }
    public void setCourseID(int courseID) { this.courseID = courseID; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    // ========== UTILITY METHODS ==========
    public boolean hasCredentials() {
        return username != null && password != null && 
               !username.isEmpty() && !password.isEmpty();
    }

    public boolean isActive() {
        return "ACTIVE".equals(status);
    }

    public boolean isOnLeave() {
        return "ON_LEAVE".equals(status);
    }

    public boolean isInactive() {
        return "INACTIVE".equals(status);
    }

    public String getFullInfo() {
        return name + " (" + identifier + ") - " + department + " - " + status;
    }

    public boolean isValidForRegistration() {
        return name != null && !name.trim().isEmpty() &&
               identifier != null && !identifier.trim().isEmpty() &&
               email != null && !email.trim().isEmpty() &&
               department != null && !department.trim().isEmpty();
    }

    public boolean canLogin() {
        return hasCredentials() && isActive();
    }

    @Override
    public String toString() {
        return name + " (ID: " + instructorID + ", " + identifier + ")";
    }

    // ========== PRIVATE HELPER METHODS ==========
    private String generateEmailFromName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "instructor@edu.com";
        }
        String cleanName = name.toLowerCase().replaceAll("\\s+", ".");
        return cleanName + "@edu.com";
    }
}