package model.dao;

import util.DatabaseConnection;
import java.sql.*;

public class AssignmentEnrollmentDAO {

	public boolean createTable() {
		String sql = "CREATE TABLE IF NOT EXISTS assignment_enrollment (" +
				"id INT PRIMARY KEY AUTO_INCREMENT, " +
				"assignmentID INT NOT NULL, " +
				"studentID INT NOT NULL, " +
				"submission_date TIMESTAMP NULL, " +
				"submitted_file VARCHAR(255), " +
				"submission_text TEXT, " +
				"status VARCHAR(20) DEFAULT 'NOT_SUBMITTED', " +
				"FOREIGN KEY (assignmentID) REFERENCES assignment(assignmentID) ON DELETE CASCADE, " +
				"FOREIGN KEY (studentID) REFERENCES student(studentID) ON DELETE CASCADE, " +
				"UNIQUE KEY unique_assignment_student (assignmentID, studentID))";

		try (Connection conn = DatabaseConnection.getConnection();
				Statement stmt = conn.createStatement()) {
			stmt.executeUpdate(sql);
			System.out.println("✅ Assignment Enrollment table created successfully!");
			return true;
		} catch (SQLException e) {
			System.err.println("❌ Error creating assignment enrollment table: " + e.getMessage());
			return false;
		}
	}
}