package model.dao;

import model.Course;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

    public boolean createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS course (" +
                "courseID INT PRIMARY KEY AUTO_INCREMENT, " +
                "courseName VARCHAR(100) NOT NULL, " +
                "courseCode VARCHAR(20) NOT NULL UNIQUE, " +
                "description TEXT, " +
                "credits INT DEFAULT 3, " +
                "instructorID INT, " +
                "createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (instructorID) REFERENCES instructor(instructorID) ON DELETE SET NULL, " +
                "CHECK (credits > 0 AND credits <= 10))";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println(" Course table created successfully!");
            return true;
        } catch (SQLException e) {
            System.err.println(" Error creating course table: " + e.getMessage());
            return false;
        }
    }

    // CREATE - Add new course
    public boolean addCourse(Course course) {
        String sql = "INSERT INTO course (courseName, courseCode, description, credits, instructorID) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, course.getCourseName());
            pstmt.setString(2, course.getCourseCode());
            pstmt.setString(3, course.getDescription());
            pstmt.setInt(4, course.getCredits());
            pstmt.setInt(5, course.getInstructorID());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println(" Error adding course: " + e.getMessage());
            return false;
        }
    }

    // READ - Get course by ID
    public Course getCourseById(int courseID) {
        String sql = "SELECT c.*, i.name as instructor_name " +
                    "FROM course c " +
                    "LEFT JOIN instructor i ON c.instructorID = i.instructorID " +
                    "WHERE c.courseID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, courseID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractCourseFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println(" Error getting course by ID: " + e.getMessage());
        }
        return null;
    }

    // READ - Get all courses
    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT c.*, i.name as instructor_name " +
                    "FROM course c " +
                    "LEFT JOIN instructor i ON c.instructorID = i.instructorID " +
                    "ORDER BY c.courseName";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                courses.add(extractCourseFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println(" Error getting all courses: " + e.getMessage());
        }
        return courses;
    }

    // READ - Get courses by instructor
    public List<Course> getCoursesByInstructor(int instructorID) {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT c.*, i.name as instructor_name " +
                    "FROM course c " +
                    "JOIN instructor i ON c.instructorID = i.instructorID " +
                    "WHERE c.instructorID = ? ORDER BY c.courseName";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, instructorID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                courses.add(extractCourseFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println(" Error getting courses by instructor: " + e.getMessage());
        }
        return courses;
    }

    // UPDATE - Update course
    public boolean updateCourse(Course course) {
        String sql = "UPDATE course SET courseName = ?, courseCode = ?, description = ?, credits = ?, instructorID = ? WHERE courseID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, course.getCourseName());
            pstmt.setString(2, course.getCourseCode());
            pstmt.setString(3, course.getDescription());
            pstmt.setInt(4, course.getCredits());
            pstmt.setInt(5, course.getInstructorID());
            pstmt.setInt(6, course.getCourseID());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println(" Error updating course: " + e.getMessage());
            return false;
        }
    }

    // DELETE - Delete course
    public boolean deleteCourse(int courseID) {
        String sql = "DELETE FROM course WHERE courseID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, courseID);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println(" Error deleting course: " + e.getMessage());
            return false;
        }
    }

    // Check if course code exists
    public boolean courseCodeExists(String courseCode) {
        String sql = "SELECT COUNT(*) FROM course WHERE courseCode = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, courseCode);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println(" Error checking course code: " + e.getMessage());
        }
        return false;
    }

    // Get total number of courses
    public int getTotalCourses() {
        String sql = "SELECT COUNT(*) FROM course";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println(" Error getting total courses: " + e.getMessage());
        }
        return 0;
    }

    // Helper method to extract Course object from ResultSet
    private Course extractCourseFromResultSet(ResultSet rs) throws SQLException {
        Course course = new Course();
        course.setCourseID(rs.getInt("courseID"));
        course.setCourseName(rs.getString("courseName"));
        course.setCourseCode(rs.getString("courseCode"));
        course.setDescription(rs.getString("description"));
        course.setCredits(rs.getInt("credits"));
        course.setInstructorID(rs.getInt("instructorID"));
        course.setInstructorName(rs.getString("instructor_name"));
        course.setCreatedAt(rs.getTimestamp("createdAt"));
        course.setUpdatedAt(rs.getTimestamp("updatedAt"));
        return course;
    }

    // Test method
    public static void main(String[] args) {
        CourseDAO courseDAO = new CourseDAO();
        
        // Test table creation
        boolean tableCreated = courseDAO.createTable();
        System.out.println("Course table created: " + tableCreated);
        
        // Test getting all courses
        List<Course> courses = courseDAO.getAllCourses();
        System.out.println("Total courses in database: " + courses.size());
    }
}