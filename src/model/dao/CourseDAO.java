package model.dao;

import model.Grade;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

	public boolean createTable() {
		String sql = "CREATE TABLE IF NOT EXISTS grade (" +
				"gradeID INT PRIMARY KEY AUTO_INCREMENT, " +
				"assignmentID INT NOT NULL, " +
				"studentID INT NOT NULL, " +
				"courseID INT NOT NULL, " +
				"score DECIMAL(5,2) NOT NULL, " +
				"letter_grade VARCHAR(5), " +
				"remarks TEXT, " +
				"graded_by INT, " +
				"created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
				"updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, " +
				"FOREIGN KEY (assignmentID) REFERENCES assignment(assignmentID) ON DELETE CASCADE, " +
				"FOREIGN KEY (studentID) REFERENCES student(studentID) ON DELETE CASCADE, " +
				"FOREIGN KEY (courseID) REFERENCES course(courseID) ON DELETE CASCADE, " +
				"FOREIGN KEY (graded_by) REFERENCES instructor(instructorID) ON DELETE SET NULL, " +
				"UNIQUE KEY unique_grade_entry (studentID, assignmentID), " +
				"CHECK (score >= 0 AND score <= 100))";

		try (Connection conn = DatabaseConnection.getConnection();
				Statement stmt = conn.createStatement()) {
			stmt.executeUpdate(sql);
			System.out.println("✅ Grade table created successfully!");
			return true;
		} catch (SQLException e) {
			System.err.println("❌ Error creating grade table: " + e.getMessage());
			return false;
		}
	}

	public Grade getGradeById(int gradeID) {
		String sql = "SELECT * FROM grade WHERE gradeID = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, gradeID);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return extractGradeFromResultSet(rs);
			}
		} catch (SQLException e) {
			System.err.println("Error getting grade by ID: " + e.getMessage());
		}
		return null;
	}

	public List<Grade> getGradesByStudent(int studentID) {
		List<Grade> grades = new ArrayList<>();
		String sql = "SELECT * FROM grade WHERE studentID = ? ORDER BY created_at DESC";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, studentID);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				grades.add(extractGradeFromResultSet(rs));
			}
		} catch (SQLException e) {
			System.err.println("Error getting grades by student: " + e.getMessage());
		}
		return grades;
	}

	public List<Grade> getGradesByAssignment(int assignmentID) {
		List<Grade> grades = new ArrayList<>();
		String sql = "SELECT * FROM grade WHERE assignmentID = ? ORDER BY score DESC";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, assignmentID);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				grades.add(extractGradeFromResultSet(rs));
			}
		} catch (SQLException e) {
			System.err.println("Error getting grades by assignment: " + e.getMessage());
		}
		return grades;
	}

	public List<Grade> getGradesByCourse(int courseID) {
		List<Grade> grades = new ArrayList<>();
		String sql = "SELECT * FROM grade WHERE courseID = ? ORDER BY studentID, assignmentID";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, courseID);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				grades.add(extractGradeFromResultSet(rs));
			}
		} catch (SQLException e) {
			System.err.println("Error getting grades by course: " + e.getMessage());
		}
		return grades;
	}

	public boolean addGrade(Grade grade) {
		String sql = "INSERT INTO grade (assignmentID, studentID, courseID, score, letter_grade, remarks, graded_by) VALUES (?, ?, ?, ?, ?, ?, ?)";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, grade.getAssignmentID());
			pstmt.setInt(2, grade.getStudentID());
			pstmt.setInt(3, grade.getCourseID());
			pstmt.setDouble(4, grade.getScore());
			pstmt.setString(5, grade.getLetterGrade());
			pstmt.setString(6, grade.getRemarks());
			pstmt.setInt(7, grade.getGradedBy());

			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0;

		} catch (SQLException e) {
			System.err.println("Error adding grade: " + e.getMessage());
			return false;
		}
	}

	public boolean updateGrade(Grade grade) {
		String sql = "UPDATE grade SET score = ?, letter_grade = ?, remarks = ?, graded_by = ? WHERE gradeID = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setDouble(1, grade.getScore());
			pstmt.setString(2, grade.getLetterGrade());
			pstmt.setString(3, grade.getRemarks());
			pstmt.setInt(4, grade.getGradedBy());
			pstmt.setInt(5, grade.getGradeID());

			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0;

		} catch (SQLException e) {
			System.err.println("Error updating grade: " + e.getMessage());
			return false;
		}
	}

	public boolean deleteGrade(int gradeID) {
		String sql = "DELETE FROM grade WHERE gradeID = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, gradeID);
			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0;

		} catch (SQLException e) {
			System.err.println("Error deleting grade: " + e.getMessage());
			return false;
		}
	}

	public double calculateStudentGPA(int studentID) {
		String sql = "SELECT letter_grade FROM grade WHERE studentID = ?";
		double totalPoints = 0;
		int count = 0;

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, studentID);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				String letterGrade = rs.getString("letter_grade");
				totalPoints += Grade.calculateGPAPoints(letterGrade);
				count++;
			}

			return count > 0 ? totalPoints / count : 0.0;

		} catch (SQLException e) {
			System.err.println("Error calculating GPA: " + e.getMessage());
			return 0.0;
		}
	}

	public double getAverageScoreByAssignment(int assignmentID) {
		String sql = "SELECT AVG(score) as average_score FROM grade WHERE assignmentID = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, assignmentID);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return rs.getDouble("average_score");
			}
		} catch (SQLException e) {
			System.err.println("Error getting average score: " + e.getMessage());
		}
		return 0.0;
	}

	private Grade extractGradeFromResultSet(ResultSet rs) throws SQLException {
		Grade grade = new Grade();
		grade.setGradeID(rs.getInt("gradeID"));
		grade.setAssignmentID(rs.getInt("assignmentID"));
		grade.setStudentID(rs.getInt("studentID"));
		grade.setCourseID(rs.getInt("courseID"));
		grade.setScore(rs.getDouble("score"));
		grade.setLetterGrade(rs.getString("letter_grade"));
		grade.setRemarks(rs.getString("remarks"));
		grade.setGradedBy(rs.getInt("graded_by"));
		grade.setCreatedAt(rs.getTimestamp("created_at"));
		grade.setUpdatedAt(rs.getTimestamp("updated_at"));
		return grade;
	}
}