package model.dao;

import model.Enrollment;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDAO {

	public boolean createTable() {
		String sql = "CREATE TABLE IF NOT EXISTS enrollment (" +
				"enrollmentID INT PRIMARY KEY AUTO_INCREMENT, " +
				"studentID INT NOT NULL, " +
				"courseID INT NOT NULL, " +
				"enrollment_date DATE NOT NULL, " +
				"status VARCHAR(20) DEFAULT 'ACTIVE', " +
				"FOREIGN KEY (studentID) REFERENCES student(studentID) ON DELETE CASCADE, " +
				"FOREIGN KEY (courseID) REFERENCES course(courseID) ON DELETE CASCADE, " +
				"UNIQUE KEY unique_enrollment (studentID, courseID))";

		try (Connection conn = DatabaseConnection.getConnection();
				Statement stmt = conn.createStatement()) {
			stmt.executeUpdate(sql);
			System.out.println("Enrollment table created successfully!");
			return true;
		} catch (SQLException e) {
			System.err.println("Error creating enrollment table: " + e.getMessage());
			return false;
		}
	}

	public Enrollment getEnrollmentById(int enrollmentID) {
		String sql = "SELECT * FROM enrollment WHERE enrollmentID = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, enrollmentID);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return extractEnrollmentFromResultSet(rs);
			}
		} catch (SQLException e) {
			System.err.println("Error getting enrollment by ID: " + e.getMessage());
		}
		return null;
	}

	public List<Enrollment> getEnrollmentsByStudent(int studentID) {
		List<Enrollment> enrollments = new ArrayList<>();
		String sql = "SELECT e.*, c.courseName, c.courseCode FROM enrollment e " +
				"JOIN course c ON e.courseID = c.courseID " +
				"WHERE e.studentID = ? ORDER BY e.enrollment_date DESC";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, studentID);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				Enrollment enrollment = extractEnrollmentFromResultSet(rs);
				enrollment.setCourseName(rs.getString("courseName"));
				enrollment.setCourseCode(rs.getString("courseCode"));
				enrollments.add(enrollment);
			}
		} catch (SQLException e) {
			System.err.println("Error getting enrollments by student: " + e.getMessage());
		}
		return enrollments;
	}

	public List<Enrollment> getEnrollmentsByCourse(int courseID) {
		List<Enrollment> enrollments = new ArrayList<>();
		String sql = "SELECT e.*, s.first_name, s.last_name FROM enrollment e " +
				"JOIN student s ON e.studentID = s.studentID " +
				"WHERE e.courseID = ? ORDER BY e.enrollment_date DESC";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, courseID);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				Enrollment enrollment = extractEnrollmentFromResultSet(rs);
				enrollment.setStudentName(rs.getString("first_name") + " " + rs.getString("last_name"));
				enrollments.add(enrollment);
			}
		} catch (SQLException e) {
			System.err.println("Error getting enrollments by course: " + e.getMessage());
		}
		return enrollments;
	}

	public List<Enrollment> getAllEnrollments() {
		List<Enrollment> enrollments = new ArrayList<>();
		String sql = "SELECT e.*, s.first_name, s.last_name, c.courseName " +
				"FROM enrollment e " +
				"JOIN student s ON e.studentID = s.studentID " +
				"JOIN course c ON e.courseID = c.courseID " +
				"ORDER BY e.enrollment_date DESC";

		try (Connection conn = DatabaseConnection.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {
				Enrollment enrollment = extractEnrollmentFromResultSet(rs);
				enrollment.setStudentName(rs.getString("first_name") + " " + rs.getString("last_name"));
				enrollment.setCourseName(rs.getString("courseName"));
				enrollments.add(enrollment);
			}
		} catch (SQLException e) {
			System.err.println("Error getting all enrollments: " + e.getMessage());
		}
		return enrollments;
	}

	public boolean addEnrollment(Enrollment enrollment) {
		String sql = "INSERT INTO enrollment (studentID, courseID, enrollment_date, status) VALUES (?, ?, ?, ?)";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, enrollment.getStudentID());
			pstmt.setInt(2, enrollment.getCourseID());
			pstmt.setDate(3, new java.sql.Date(enrollment.getEnrollmentDate().getTime()));
			pstmt.setString(4, enrollment.getStatus());

			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0;

		} catch (SQLException e) {
			System.err.println("Error adding enrollment: " + e.getMessage());
			return false;
		}
	}

	public boolean updateEnrollmentStatus(int enrollmentID, String status) {
		String sql = "UPDATE enrollment SET status = ? WHERE enrollmentID = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, status);
			pstmt.setInt(2, enrollmentID);

			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0;

		} catch (SQLException e) {
			System.err.println("Error updating enrollment status: " + e.getMessage());
			return false;
		}
	}

	public boolean deleteEnrollment(int enrollmentID) {
		String sql = "DELETE FROM enrollment WHERE enrollmentID = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, enrollmentID);
			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0;

		} catch (SQLException e) {
			System.err.println("Error deleting enrollment: " + e.getMessage());
			return false;
		}
	}

	public boolean isStudentEnrolled(int studentID, int courseID) {
		String sql = "SELECT COUNT(*) FROM enrollment WHERE studentID = ? AND courseID = ? AND status = 'ACTIVE'";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, studentID);
			pstmt.setInt(2, courseID);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return rs.getInt(1) > 0;
			}
		} catch (SQLException e) {
			System.err.println("Error checking enrollment: " + e.getMessage());
		}
		return false;
	}

	public int getEnrollmentCountByCourse(int courseID) {
		String sql = "SELECT COUNT(*) FROM enrollment WHERE courseID = ? AND status = 'ACTIVE'";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, courseID);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			System.err.println("Error getting enrollment count: " + e.getMessage());
		}
		return 0;
	}

	private Enrollment extractEnrollmentFromResultSet(ResultSet rs) throws SQLException {
		Enrollment enrollment = new Enrollment();
		enrollment.setEnrollmentID(rs.getInt("enrollmentID"));
		enrollment.setStudentID(rs.getInt("studentID"));
		enrollment.setCourseID(rs.getInt("courseID"));
		enrollment.setEnrollmentDate(rs.getDate("enrollment_date"));
		enrollment.setStatus(rs.getString("status"));
		return enrollment;
	}
}