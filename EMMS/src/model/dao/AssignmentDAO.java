package model.dao;

import model.Assignment;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AssignmentDAO {

	public boolean createTable() {
		String sql = "CREATE TABLE IF NOT EXISTS assignment (" +
				"assignmentID INT PRIMARY KEY AUTO_INCREMENT, " +
				"courseID INT NOT NULL, " +
				"title VARCHAR(200) NOT NULL, " +
				"description TEXT, " +
				"due_date DATE, " +
				"max_score DECIMAL(5,2) DEFAULT 100, " +
				"assignment_type VARCHAR(50), " +
				"created_by INT, " +
				"status VARCHAR(50) DEFAULT 'Active', " +
				"remarks TEXT, " +
				"created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
				"FOREIGN KEY (courseID) REFERENCES course(courseID) ON DELETE CASCADE, " +
				"FOREIGN KEY (created_by) REFERENCES instructor(instructorID) ON DELETE SET NULL)";

		try (Connection conn = DatabaseConnection.getConnection();
				Statement stmt = conn.createStatement()) {
			stmt.executeUpdate(sql);
			System.out.println("Assignment table created successfully!");
			return true;
		} catch (SQLException e) {
			System.err.println(" Error creating assignment table: " + e.getMessage());
			return false;
		}
	}

	public Assignment getAssignmentById(int assignmentID) {
		String sql = "SELECT * FROM assignment WHERE assignmentID = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, assignmentID);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return extractAssignmentFromResultSet(rs);
			}
		} catch (SQLException e) {
			System.err.println("Error getting assignment by ID: " + e.getMessage());
		}
		return null;
	}

	public List<Assignment> getAssignmentsByCourse(int courseID) {
		List<Assignment> assignments = new ArrayList<>();
		String sql = "SELECT * FROM assignment WHERE courseID = ? ORDER BY due_date";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, courseID);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				assignments.add(extractAssignmentFromResultSet(rs));
			}
		} catch (SQLException e) {
			System.err.println("Error getting assignments by course: " + e.getMessage());
		}
		return assignments;
	}

	public List<Assignment> getAssignmentsByStatus(String status) {
		List<Assignment> assignments = new ArrayList<>();
		String sql = "SELECT * FROM assignment WHERE status = ? ORDER BY due_date";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, status);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				assignments.add(extractAssignmentFromResultSet(rs));
			}
		} catch (SQLException e) {
			System.err.println("Error getting assignments by status: " + e.getMessage());
		}
		return assignments;
	}

	public List<Assignment> getAllAssignments() {
		List<Assignment> assignments = new ArrayList<>();
		String sql = "SELECT * FROM assignment ORDER BY due_date DESC";

		try (Connection conn = DatabaseConnection.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {
				assignments.add(extractAssignmentFromResultSet(rs));
			}
		} catch (SQLException e) {
			System.err.println("Error getting all assignments: " + e.getMessage());
		}
		return assignments;
	}

	public boolean addAssignment(Assignment assignment) {
		String sql = "INSERT INTO assignment (courseID, title, description, due_date, max_score, assignment_type, created_by, status, remarks) " +
				"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, assignment.getCourseID());
			pstmt.setString(2, assignment.getTitle());
			pstmt.setString(3, assignment.getDescription());
			pstmt.setDate(4, assignment.getDueDate());
			pstmt.setDouble(5, assignment.getMaxScore());
			pstmt.setString(6, assignment.getAssignmentType());
			pstmt.setInt(7, assignment.getCreatedBy());
			pstmt.setString(8, assignment.getStatus());
			pstmt.setString(9, assignment.getRemarks());

			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0;

		} catch (SQLException e) {
			System.err.println("Error adding assignment: " + e.getMessage());
			return false;
		}
	}

	public boolean updateAssignment(Assignment assignment) {
		String sql = "UPDATE assignment SET courseID = ?, title = ?, description = ?, due_date = ?, " +
				"max_score = ?, assignment_type = ?, status = ?, remarks = ? WHERE assignmentID = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, assignment.getCourseID());
			pstmt.setString(2, assignment.getTitle());
			pstmt.setString(3, assignment.getDescription());
			pstmt.setDate(4, assignment.getDueDate());
			pstmt.setDouble(5, assignment.getMaxScore());
			pstmt.setString(6, assignment.getAssignmentType());
			pstmt.setString(7, assignment.getStatus());
			pstmt.setString(8, assignment.getRemarks());
			pstmt.setInt(9, assignment.getAssignmentID());

			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0;

		} catch (SQLException e) {
			System.err.println("Error updating assignment: " + e.getMessage());
			return false;
		}
	}

	public boolean deleteAssignment(int assignmentID) {
		String sql = "DELETE FROM assignment WHERE assignmentID = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, assignmentID);
			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0;

		} catch (SQLException e) {
			System.err.println("Error deleting assignment: " + e.getMessage());
			return false;
		}
	}

	public boolean updateAssignmentStatus(int assignmentID, String status, String remarks) {
		String sql = "UPDATE assignment SET status = ?, remarks = ? WHERE assignmentID = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, status);
			pstmt.setString(2, remarks);
			pstmt.setInt(3, assignmentID);

			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0;

		} catch (SQLException e) {
			System.err.println("Error updating assignment status: " + e.getMessage());
			return false;
		}
	}

	private Assignment extractAssignmentFromResultSet(ResultSet rs) throws SQLException {
		Assignment assignment = new Assignment();
		assignment.setAssignmentID(rs.getInt("assignmentID"));
		assignment.setCourseID(rs.getInt("courseID"));
		assignment.setTitle(rs.getString("title"));
		assignment.setDescription(rs.getString("description"));
		assignment.setDueDate(rs.getDate("due_date"));
		assignment.setMaxScore(rs.getDouble("max_score"));
		assignment.setAssignmentType(rs.getString("assignment_type"));
		assignment.setCreatedBy(rs.getInt("created_by"));
		assignment.setStatus(rs.getString("status"));
		assignment.setRemarks(rs.getString("remarks"));
		assignment.setCreatedAt(rs.getDate("created_at"));
		return assignment;
	}
}