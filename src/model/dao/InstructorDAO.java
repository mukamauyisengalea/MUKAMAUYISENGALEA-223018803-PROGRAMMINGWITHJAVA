package model.dao;

import model.Instructor;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InstructorDAO {

	public boolean createTable() {
		String sql = "CREATE TABLE IF NOT EXISTS instructor (" +
				"instructorID INT PRIMARY KEY AUTO_INCREMENT, " +
				"name VARCHAR(100) NOT NULL, " +
				"identifier VARCHAR(50) UNIQUE NOT NULL, " +
				"status VARCHAR(20) DEFAULT 'Active', " +
				"location VARCHAR(100), " +
				"contact VARCHAR(100), " +
				"username VARCHAR(50) UNIQUE NOT NULL, " +
				"password VARCHAR(255) NOT NULL, " +
				"assignedSince DATE, " +
				"courseID INT)";

		try (Connection conn = DatabaseConnection.getConnection();
				Statement stmt = conn.createStatement()) {
			stmt.executeUpdate(sql);
			System.out.println("✅ Instructor table created successfully!");
			return true;
		} catch (SQLException e) {
			System.err.println("❌ Error creating instructor table: " + e.getMessage());
			return false;
		}
	}

	public Instructor getInstructorById(int instructorID) {
		String sql = "SELECT * FROM instructor WHERE instructorID = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, instructorID);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return extractInstructorFromResultSet(rs);
			}
		} catch (SQLException e) {
			System.err.println("Error getting instructor by ID: " + e.getMessage());
		}
		return null;
	}

	public Instructor getInstructorByUsername(String username) {
		String sql = "SELECT * FROM instructor WHERE username = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, username);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return extractInstructorFromResultSet(rs);
			}
		} catch (SQLException e) {
			System.err.println("Error getting instructor by username: " + e.getMessage());
		}
		return null;
	}

	public Instructor authenticateInstructor(String username, String password) {
		String sql = "SELECT * FROM instructor WHERE username = ? AND password = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, username);
			pstmt.setString(2, password);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return extractInstructorFromResultSet(rs);
			}
		} catch (SQLException e) {
			System.err.println("Error authenticating instructor: " + e.getMessage());
		}
		return null;
	}

	public List<Instructor> getAllInstructors() {
		List<Instructor> instructors = new ArrayList<>();
		String sql = "SELECT * FROM instructor ORDER BY name";

		try (Connection conn = DatabaseConnection.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {
				instructors.add(extractInstructorFromResultSet(rs));
			}
		} catch (SQLException e) {
			System.err.println("Error getting all instructors: " + e.getMessage());
		}
		return instructors;
	}

	public boolean addInstructor(Instructor instructor) {
		String sql = "INSERT INTO instructor (name, identifier, status, location, contact, username, password, assignedSince, courseID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, instructor.getName());
			pstmt.setString(2, instructor.getIdentifier());
			pstmt.setString(3, instructor.getStatus());
			pstmt.setString(4, instructor.getLocation());
			pstmt.setString(5, instructor.getContact());
			pstmt.setString(6, instructor.getUsername());
			pstmt.setString(7, instructor.getPassword());
			pstmt.setDate(8, new java.sql.Date(instructor.getAssignedSince().getTime()));
			pstmt.setInt(9, instructor.getCourseID());

			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0;

		} catch (SQLException e) {
			System.err.println("Error adding instructor: " + e.getMessage());
			return false;
		}
	}

	public boolean updateInstructor(Instructor instructor) {
		String sql = "UPDATE instructor SET name = ?, identifier = ?, status = ?, location = ?, contact = ?, username = ?, password = ?, assignedSince = ?, courseID = ? WHERE instructorID = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, instructor.getName());
			pstmt.setString(2, instructor.getIdentifier());
			pstmt.setString(3, instructor.getStatus());
			pstmt.setString(4, instructor.getLocation());
			pstmt.setString(5, instructor.getContact());
			pstmt.setString(6, instructor.getUsername());
			pstmt.setString(7, instructor.getPassword());
			pstmt.setDate(8, new java.sql.Date(instructor.getAssignedSince().getTime()));
			pstmt.setInt(9, instructor.getCourseID());
			pstmt.setInt(10, instructor.getInstructorID());

			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0;

		} catch (SQLException e) {
			System.err.println("Error updating instructor: " + e.getMessage());
			return false;
		}
	}

	public boolean deleteInstructor(int instructorID) {
		String sql = "DELETE FROM instructor WHERE instructorID = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, instructorID);
			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0;

		} catch (SQLException e) {
			System.err.println("Error deleting instructor: " + e.getMessage());
			return false;
		}
	}

	public int getTotalInstructors() {
		String sql = "SELECT COUNT(*) FROM instructor";

		try (Connection conn = DatabaseConnection.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			System.err.println("Error getting total instructors: " + e.getMessage());
		}
		return 0;
	}

	private Instructor extractInstructorFromResultSet(ResultSet rs) throws SQLException {
		Instructor instructor = new Instructor();
		instructor.setInstructorID(rs.getInt("instructorID"));
		instructor.setName(rs.getString("name"));
		instructor.setIdentifier(rs.getString("identifier"));
		instructor.setStatus(rs.getString("status"));
		instructor.setLocation(rs.getString("location"));
		instructor.setContact(rs.getString("contact"));
		instructor.setUsername(rs.getString("username"));
		instructor.setPassword(rs.getString("password"));
		instructor.setAssignedSince(rs.getDate("assignedSince"));
		instructor.setCourseID(rs.getInt("courseID"));
		return instructor;
	}
}