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
				"identifier VARCHAR(50) UNIQUE, " +
				"status ENUM('ACTIVE','ON_LEAVE','INACTIVE') DEFAULT 'ACTIVE', " +
				"department VARCHAR(100), " +
				"location VARCHAR(100), " +
				"contact VARCHAR(100), " +
				"email VARCHAR(100), " +
				"phone VARCHAR(20), " +
				"username VARCHAR(50) UNIQUE, " +
				"password VARCHAR(255), " +
				"qualifications TEXT, " +
				"assignedSince DATE, " +
				"courseID INT, " +
				"created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
				"updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)";

		try (Connection conn = DatabaseConnection.getConnection();
				Statement stmt = conn.createStatement()) {
			stmt.executeUpdate(sql);
			System.out.println(" Instructor table created successfully!");
			return true;
		} catch (SQLException e) {
			System.err.println(" Error creating instructor table: " + e.getMessage());
			return false;
		}
	}

	// Get instructor by ID
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
			System.err.println(" Error getting instructor by ID: " + e.getMessage());
		}
		return null;
	}

	// Get instructor by username
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
			System.err.println(" Error getting instructor by username: " + e.getMessage());
		}
		return null;
	}

	// Authenticate instructor login
	public Instructor authenticateInstructor(String username, String password) {
		String sql = "SELECT * FROM instructor WHERE username = ? AND password = ? AND status = 'ACTIVE'";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, username);
			pstmt.setString(2, password);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return extractInstructorFromResultSet(rs);
			}
		} catch (SQLException e) {
			System.err.println(" Error authenticating instructor: " + e.getMessage());
		}
		return null;
	}

	// Get all instructors
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
			System.err.println(" Error getting all instructors: " + e.getMessage());
		}
		return instructors;
	}

	// Add instructor (admin function - without password initially)
	public boolean addInstructor(Instructor instructor) {
		String sql = "INSERT INTO instructor (name, identifier, status, department, location, contact, email, phone, assignedSince, courseID) " +
				"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, instructor.getName());
			pstmt.setString(2, instructor.getIdentifier());
			pstmt.setString(3, instructor.getStatus());
			pstmt.setString(4, instructor.getDepartment());
			pstmt.setString(5, instructor.getLocation());
			pstmt.setString(6, instructor.getContact());
			pstmt.setString(7, instructor.getEmail());
			pstmt.setString(8, instructor.getPhone());
			pstmt.setDate(9, new java.sql.Date(instructor.getAssignedSince().getTime()));
			pstmt.setInt(10, instructor.getCourseID());

			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0;

		} catch (SQLException e) {
			System.err.println(" Error adding instructor: " + e.getMessage());
			return false;
		}
	}

	// Setup instructor credentials (first-time login)
	public boolean setupInstructorCredentials(int instructorID, String username, String password) {
		String sql = "UPDATE instructor SET username = ?, password = ? WHERE instructorID = ? AND (username IS NULL OR password IS NULL)";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, username);
			pstmt.setString(2, password);
			pstmt.setInt(3, instructorID);

			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0;

		} catch (SQLException e) {
			System.err.println(" Error setting up instructor credentials: " + e.getMessage());
			return false;
		}
	}

	// Update instructor
	public boolean updateInstructor(Instructor instructor) {
		String sql = "UPDATE instructor SET name = ?, identifier = ?, status = ?, department = ?, " +
				"location = ?, contact = ?, email = ?, phone = ?, username = ?, password = ?, " +
				"assignedSince = ?, courseID = ? WHERE instructorID = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, instructor.getName());
			pstmt.setString(2, instructor.getIdentifier());
			pstmt.setString(3, instructor.getStatus());
			pstmt.setString(4, instructor.getDepartment());
			pstmt.setString(5, instructor.getLocation());
			pstmt.setString(6, instructor.getContact());
			pstmt.setString(7, instructor.getEmail());
			pstmt.setString(8, instructor.getPhone());
			pstmt.setString(9, instructor.getUsername());
			pstmt.setString(10, instructor.getPassword());
			pstmt.setDate(11, new java.sql.Date(instructor.getAssignedSince().getTime()));
			pstmt.setInt(12, instructor.getCourseID());
			pstmt.setInt(13, instructor.getInstructorID());

			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0;

		} catch (SQLException e) {
			System.err.println(" Error updating instructor: " + e.getMessage());
			return false;
		}
	}

	// Delete instructor
	public boolean deleteInstructor(int instructorID) {
		String sql = "DELETE FROM instructor WHERE instructorID = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, instructorID);
			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0;

		} catch (SQLException e) {
			System.err.println(" Error deleting instructor: " + e.getMessage());
			return false;
		}
	}

	// Update instructor status
	public boolean updateInstructorStatus(int instructorID, String status) {
		String sql = "UPDATE instructor SET status = ? WHERE instructorID = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, status);
			pstmt.setInt(2, instructorID);

			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0;

		} catch (SQLException e) {
			System.err.println(" Error updating instructor status: " + e.getMessage());
			return false;
		}
	}

	// Check if instructor needs credential setup
	public boolean needsCredentialSetup(int instructorID) {
		String sql = "SELECT username, password FROM instructor WHERE instructorID = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, instructorID);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				String username = rs.getString("username");
				String password = rs.getString("password");
				return username == null || password == null || 
						username.isEmpty() || password.isEmpty();
			}
		} catch (SQLException e) {
			System.err.println(" Error checking credential setup: " + e.getMessage());
		}
		return false;
	}

	// Check if username exists
	public boolean usernameExists(String username) {
		String sql = "SELECT COUNT(*) FROM instructor WHERE username = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, username);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return rs.getInt(1) > 0;
			}
		} catch (SQLException e) {
			System.err.println(" Error checking username: " + e.getMessage());
		}
		return false;
	}

	// Get total number of instructors
	public int getTotalInstructors() {
		String sql = "SELECT COUNT(*) FROM instructor";

		try (Connection conn = DatabaseConnection.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			System.err.println(" Error getting total instructors: " + e.getMessage());
		}
		return 0;
	}

	// Helper method to extract Instructor from ResultSet
	private Instructor extractInstructorFromResultSet(ResultSet rs) throws SQLException {
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

	// Test method
	public static void main(String[] args) {
		InstructorDAO instructorDAO = new InstructorDAO();

		// Test table creation
		boolean tableCreated = instructorDAO.createTable();
		System.out.println("Instructor table created: " + tableCreated);

		// Test getting all instructors
		List<Instructor> instructors = instructorDAO.getAllInstructors();
		System.out.println("Total instructors in database: " + instructors.size());
	}
}