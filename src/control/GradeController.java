package control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import util.DatabaseConnection;

import model.Grade;

public class GradeController {

	public List<Grade> getGradesByStudent(int studentID) {
        List<Grade> grades = new ArrayList<>();
        String query = "SELECT * FROM grade WHERE studentID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, studentID);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Grade grade = new Grade(
                );
                grades.add(grade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return grades;
    }
    
    public boolean addGrade(Grade grade) {
        String query = "INSERT INTO grade (AssignmentId, studentID, score, remarks, createdAt, courseID) VALUES (?, ?, ?, ?, CURDATE(), ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, grade.getAssignmentID());
            stmt.setInt(2, grade.getStudentID());
            stmt.setDouble(3, grade.getScore());
            stmt.setString(4, grade.getRemarks());
            stmt.setInt(5, grade.getCourseID());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
