package model.dao;

import model.Student;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

	public boolean createTable() {
		String sql = "CREATE TABLE IF NOT EXISTS student (" +
				"studentID INT PRIMARY KEY AUTO_INCREMENT, " +
				"first_name VARCHAR(50) NOT NULL, " +
				"last_name VARCHAR(50) NOT NULL, " +
				"username VARCHAR(50) UNIQUE NOT NULL, " +
				"password VARCHAR(255) NOT NULL, " +
				"sex ENUM('M', 'F'), " +
				"birthdate DATE, " +
				"createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";

		try (Connection conn = DatabaseConnection.getConnection();
				Statement stmt = conn.createStatement()) {
			stmt.executeUpdate(sql);
			System.out.println("✅ Student table created successfully!");
			return true;
		} catch (SQLException e) {
			System.err.println("❌ Error creating student table: " + e.getMessage());
			return false;
		}
	}

	public Student getStudentById(int studentID) {
		String sql = "SELECT * FROM student WHERE studentID = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, studentID);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return extractStudentFromResultSet(rs);
			}
		} catch (SQLException e) {
			System.err.println("Error getting student by ID: " + e.getMessage());
		}
		return null;
	}

	public Student getStudentByUsername(String username) {
		String sql = "SELECT * FROM student WHERE username = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, username);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return extractStudentFromResultSet(rs);
			}
		} catch (SQLException e) {
			System.err.println("Error getting student by username: " + e.getMessage());
		}
		return null;
	}

	public Student authenticateStudent(String username, String password) {
		String sql = "SELECT * FROM student WHERE username = ? AND password = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, username);
			pstmt.setString(2, password);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return extractStudentFromResultSet(rs);
			}
		} catch (SQLException e) {
			System.err.println("Error authenticating student: " + e.getMessage());
		}
		return null;
	}

	public List<Student> getAllStudents() {
		List<Student> students = new ArrayList<>();
		String sql = "SELECT * FROM student ORDER BY first_name, last_name";

		try (Connection conn = DatabaseConnection.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {
				students.add(extractStudentFromResultSet(rs));
			}
		} catch (SQLException e) {
			System.err.println("Error getting all students: " + e.getMessage());
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
			System.err.println("Error adding student: " + e.getMessage());
			return false;
		}
	}

	public boolean updateStudent(Student student) {
		String sql = "UPDATE student SET first_name = ?, last_name = ?, username = ?, password = ?, sex = ?, birthdate = ? WHERE studentID = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, student.getFirstName());
			pstmt.setString(2, student.getLastName());
			pstmt.setString(3, student.getUsername());
			pstmt.setString(4, student.getPassword());
			pstmt.setString(5, student.getSex());
			pstmt.setDate(6, new java.sql.Date(student.getBirthdate().getTime()));
			pstmt.setInt(7, student.getStudentID());

			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0;

		} catch (SQLException e) {
			System.err.println("Error updating student: " + e.getMessage());
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
			System.err.println("Error deleting student: " + e.getMessage());
			return false;
		}
	}

	public int getTotalStudents() {
		String sql = "SELECT COUNT(*) FROM student";

		try (Connection conn = DatabaseConnection.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			System.err.println("Error getting total students: " + e.getMessage());
		}
		return 0;
	}

	private Student extractStudentFromResultSet(ResultSet rs) throws SQLException {
		Student student = new Student();
		student.setStudentID(rs.getInt("studentID"));
		student.setFirstName(rs.getString("first_name"));
		student.setLastName(rs.getString("last_name"));
		student.setUsername(rs.getString("username"));
		student.setPassword(rs.getString("password"));
		student.setSex(rs.getString("sex"));
		student.setBirthdate(rs.getDate("birthdate"));
		student.setCreatedAt(rs.getDate("createdAt"));
		return student;
	}
}