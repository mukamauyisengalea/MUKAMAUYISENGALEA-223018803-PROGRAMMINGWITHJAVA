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
    private String email;
    private String phone;
    private String address;
    private String emergencyContact;

    public Student() {
        this.createdAt = new Date(); // Set default creation date
    }

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
        // Generate default email if not provided
        if (this.email == null) {
            this.email = generateEmailFromName();
        }
    }

    // Full constructor with all fields
    public Student(int studentID, String firstName, String lastName, String username, 
                  String password, String sex, Date birthdate, Date createdAt,
                  String email, String phone, String address, String emergencyContact) {
        this.studentID = studentID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.sex = sex;
        this.birthdate = birthdate;
        this.createdAt = createdAt;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.emergencyContact = emergencyContact;
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

    // Complete the getEmail method with proper logic
    public String getEmail() {
        if (this.email == null || this.email.isEmpty()) {
            // Generate email from name if not set
            this.email = generateEmailFromName();
        }
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Complete the getPhone method with proper logic
    public String getPhone() {
        return this.phone != null ? this.phone : "Not provided";
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Additional getters and setters for new fields
    public String getAddress() {
        return address != null ? address : "Not provided";
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmergencyContact() {
        return emergencyContact != null ? emergencyContact : "Not provided";
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    // Utility Methods
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public int getAge() {
        if (birthdate == null) {
            return 0;
        }
        Date now = new Date();
        long ageInMillis = now.getTime() - birthdate.getTime();
        return (int) (ageInMillis / (1000L * 60 * 60 * 24 * 365));
    }

    public boolean isAdult() {
        return getAge() >= 18;
    }

    public boolean hasValidCredentials() {
        return username != null && !username.isEmpty() && 
               password != null && !password.isEmpty();
    }

    public boolean isProfileComplete() {
        return firstName != null && !firstName.isEmpty() &&
               lastName != null && !lastName.isEmpty() &&
               username != null && !username.isEmpty() &&
               email != null && !email.isEmpty() &&
               sex != null && !sex.isEmpty() &&
               birthdate != null;
    }

    // Validation Methods
    public boolean isValidEmail() {
        if (email == null) return false;
        // Simple email validation
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    public boolean isValidPhone() {
        if (phone == null || phone.isEmpty()) return true; // Phone is optional
        // Basic phone number validation (allows various formats)
        return phone.matches("^[\\d\\s\\-\\(\\)\\+]+$");
    }

    public boolean isValidBirthdate() {
        if (birthdate == null) return false;
        Date now = new Date();
        return birthdate.before(now); // Birthdate should be in the past
    }

    // Business Logic Methods
    public String getStatus() {
        if (!isProfileComplete()) {
            return "Incomplete";
        }
        if (!hasValidCredentials()) {
            return "Pending Setup";
        }
        return "Active";
    }

    public String getFormattedBirthdate() {
        if (birthdate == null) return "Not set";
        return new java.text.SimpleDateFormat("yyyy-MM-dd").format(birthdate);
    }

    public String getFormattedCreatedAt() {
        if (createdAt == null) return "Not set";
        return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createdAt);
    }

    // Helper method to generate email from name
    private String generateEmailFromName() {
        if (firstName == null || lastName == null) {
            return "student@edu.com";
        }
        String cleanFirstName = firstName.toLowerCase().replaceAll("[^a-z]", "");
        String cleanLastName = lastName.toLowerCase().replaceAll("[^a-z]", "");
        if (cleanFirstName.isEmpty() || cleanLastName.isEmpty()) {
            return "student@edu.com";
        }
        return cleanFirstName + "." + cleanLastName + "@student.edu";
    }

    // Display Methods
    public String getDisplayInfo() {
        return getFullName() + " (ID: " + studentID + ") - " + getStatus();
    }

    public String getDetailedInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append(" STUDENT INFORMATION\n");
        sb.append("=====================\n");
        sb.append("ID: ").append(studentID).append("\n");
        sb.append("Name: ").append(getFullName()).append("\n");
        sb.append("Username: ").append(username != null ? username : "Not set").append("\n");
        sb.append("Email: ").append(getEmail()).append("\n");
        sb.append("Phone: ").append(getPhone()).append("\n");
        sb.append("Gender: ").append(sex != null ? sex : "Not set").append("\n");
        sb.append("Birthdate: ").append(getFormattedBirthdate()).append("\n");
        sb.append("Age: ").append(getAge()).append(" years\n");
        sb.append("Status: ").append(getStatus()).append("\n");
        sb.append("Registered: ").append(getFormattedCreatedAt()).append("\n");
        sb.append("Address: ").append(getAddress()).append("\n");
        sb.append("Emergency Contact: ").append(getEmergencyContact()).append("\n");
        return sb.toString();
    }

    // Comparison Methods
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Student student = (Student) obj;
        return studentID == student.studentID;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(studentID);
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentID=" + studentID +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", email='" + getEmail() + '\'' +
                ", status='" + getStatus() + '\'' +
                '}';
    }

    // Import/Export Methods
    public String toCSV() {
        return studentID + "," +
               (firstName != null ? firstName : "") + "," +
               (lastName != null ? lastName : "") + "," +
               (username != null ? username : "") + "," +
               getEmail() + "," +
               (phone != null ? phone : "") + "," +
               (sex != null ? sex : "") + "," +
               getFormattedBirthdate() + "," +
               getStatus();
    }

    // Clone method
    public Student clone() {
        return new Student(
            this.studentID,
            this.firstName,
            this.lastName,
            this.username,
            this.password,
            this.sex,
            this.birthdate != null ? (Date) this.birthdate.clone() : null,
            this.createdAt != null ? (Date) this.createdAt.clone() : null,
            this.email,
            this.phone,
            this.address,
            this.emergencyContact
        );
    }

    // Static utility methods
    public static boolean isValidStudentID(int studentID) {
        return studentID > 0;
    }

    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty() && name.matches("^[a-zA-Z\\s]+$");
    }

    public static boolean isValidUsername(String username) {
        return username != null && username.matches("^[a-zA-Z0-9._-]{3,20}$");
    }

    public static Student createMinimalStudent(String firstName, String lastName, String username) {
        Student student = new Student();
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setUsername(username);
        student.setPassword("temp123"); 
        student.setCreatedAt(new Date());
        return student;
    }
}