package control;

import model.Student;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentController {
    
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM student ORDER BY createdAt DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Student student = new Student();
                student.setStudentID(rs.getInt("studentID"));
                student.setFirstName(rs.getString("first_name"));
                student.setLastName(rs.getString("last_name"));
                student.setUsername(rs.getString("username"));
                student.setPassword(rs.getString("password"));
                student.setSex(rs.getString("sex"));
                student.setBirthdate(rs.getDate("birthdate"));
                student.setCreatedAt(rs.getDate("createdAt"));
                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }
    
    public boolean addStudent(Student student) {
        String sql = "INSERT INTO student (first_name, last_name, username, password, sex, birthdate) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, student.getFirstName());
            pstmt.setString(2, student.getLastName());
            pstmt.setString(3, student.getUsername());
            pstmt.setString(4, student.getPassword());
            pstmt.setString(5, student.getSex());
            pstmt.setDate(6, new java.sql.Date(student.getBirthdate().getTime()));
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteStudent(int studentID) {
        String sql = "DELETE FROM student WHERE studentID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, studentID);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateStudent(Student student) {
        String sql = "UPDATE student SET first_name=?, last_name=?, username=?, sex=?, birthdate=? WHERE studentID=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, student.getFirstName());
            pstmt.setString(2, student.getLastName());
            pstmt.setString(3, student.getUsername());
            pstmt.setString(4, student.getSex());
            pstmt.setDate(5, new java.sql.Date(student.getBirthdate().getTime()));
            pstmt.setInt(6, student.getStudentID());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}