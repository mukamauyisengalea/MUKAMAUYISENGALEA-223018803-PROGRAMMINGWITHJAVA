package control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import util.DatabaseConnection;
import model.Admin;
import model.Instructor;
import model.Student;

public class AuthenticationController {

    public Admin authenticateAdmin(String username, String password) {
        String query = "SELECT * FROM admin WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Admin(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("username"),
                        rs.getString("password")
                );
            }
        } catch (SQLException e) {
            System.err.println(" Error authenticating admin: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public Instructor authenticateInstructor(String username, String password) {
        String query = "SELECT * FROM instructor WHERE username = ? AND password = ? AND status = 'ACTIVE'";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Use the DAO-style extraction to handle all fields properly
                Instructor instructor = new Instructor();
                instructor.setInstructorID(rs.getInt("instructorID"));
                instructor.setName(rs.getString("name"));
                instructor.setIdentifier(rs.getString("identifier"));
                instructor.setStatus(rs.getString("status"));
                instructor.setDepartment(rs.getString("department"));
                instructor.setLocation(rs.getString("location"));
                instructor.setContact(rs.getString("contact"));
                instructor.setEmail(rs.getString("email"));
                instructor.setPhone(rs.getString("phone"));
                instructor.setUsername(rs.getString("username"));
                instructor.setPassword(rs.getString("password"));
                instructor.setQualifications(rs.getString("qualifications"));
                instructor.setAssignedSince(rs.getDate("assignedSince"));
                instructor.setCourseID(rs.getInt("courseID"));
                instructor.setCreatedAt(rs.getTimestamp("created_at"));
                instructor.setUpdatedAt(rs.getTimestamp("updated_at"));
                
                return instructor;
            }
        } catch (SQLException e) {
            System.err.println(" Error authenticating instructor: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Alternative method using constructor (if you add the constructor above)
    public Instructor authenticateInstructorWithConstructor(String username, String password) {
        String query = "SELECT * FROM instructor WHERE username = ? AND password = ? AND status = 'ACTIVE'";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Instructor(
                        rs.getInt("instructorID"),
                        rs.getString("name"),
                        rs.getString("identifier"),
                        rs.getString("status"),
                        rs.getString("location"),
                        rs.getString("contact"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getDate("assignedSince"),
                        rs.getInt("courseID")
                );
            }
        } catch (SQLException e) {
            System.err.println(" Error authenticating instructor: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public Student authenticateStudent(String username, String password) {
        String query = "SELECT * FROM student WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Student(
                        rs.getInt("studentID"),
                        rs.getString("first_name"), // Fixed: should be "first_name" not "First_name"
                        rs.getString("last_name"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("sex"),
                        rs.getDate("birthdate"),
                        rs.getDate("createdAt")
                );
            }
        } catch (SQLException e) {
            System.err.println(" Error authenticating student: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Additional authentication methods
    public boolean checkUsernameExists(String username, String userType) {
        String tableName = getTableName(userType);
        if (tableName == null) return false;

        String query = "SELECT COUNT(*) FROM " + tableName + " WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println(" Error checking username existence: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean changePassword(String username, String userType, String newPassword) {
        String tableName = getTableName(userType);
        if (tableName == null) return false;

        String query = "UPDATE " + tableName + " SET password = ? WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, newPassword);
            stmt.setString(2, username);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println(" Error changing password: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private String getTableName(String userType) {
        switch (userType.toLowerCase()) {
            case "admin":
                return "admin";
            case "instructor":
                return "instructor";
            case "student":
                return "student";
            default:
                return null;
        }
    }

    // Method to check if instructor needs to setup credentials
    public boolean instructorNeedsSetup(String username) {
        String query = "SELECT username, password FROM instructor WHERE username = ? AND (username IS NULL OR password IS NULL)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            return rs.next(); // Returns true if instructor needs setup

        } catch (SQLException e) {
            System.err.println(" Error checking instructor setup status: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Test method
    public static void main(String[] args) {
        AuthenticationController authController = new AuthenticationController();
        
        // Test admin authentication
        Admin admin = authController.authenticateAdmin("admin", "password");
        System.out.println("Admin authenticated: " + (admin != null));
        
        // Test instructor authentication
        Instructor instructor = authController.authenticateInstructor("instructor1", "password");
        System.out.println("Instructor authenticated: " + (instructor != null));
        
        // Test student authentication
        Student student = authController.authenticateStudent("student1", "password");
        System.out.println("Student authenticated: " + (student != null));
    }
}