package control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.DatabaseConnection;

import model.Admin;
import model.Instructor;
import model.Student;

public class AuthenticationController {

	public Admin authenticateAdmin(String username, String password) {
		String query = "SELECT * FROM admin WHERE username = ? AND password = ?";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(query)) {

			stmt.setString(1, username);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				return new Admin(
						rs.getInt("id"),
						rs.getString("name"),
						rs.getString("username"),
						rs.getString("password")
						);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Instructor authenticateInstructor(String username, String password) {
		String query = "SELECT * FROM instructor WHERE username = ? AND password = ?";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(query)) {

			stmt.setString(1, username);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				return new Instructor(
						rs.getInt("instructorID"),
						rs.getString("name"),
						rs.getString("identifier"),
						rs.getString("status"),
						rs.getString("location"),
						rs.getString("contact"),
						rs.getString("username"),
						rs.getString("password"),
						rs.getDate("assignedSince"),
						rs.getInt("courseID")
						);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Student authenticateStudent(String username, String password) {
		String query = "SELECT * FROM student WHERE username = ? AND password = ?";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(query)) {

			stmt.setString(1, username);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				return new Student(
						rs.getInt("studentID"),
						rs.getString("First_name"),
						rs.getString("last_name"),
						rs.getString("username"),
						rs.getString("password"),
						rs.getString("sex"),
						rs.getDate("birthdate"),
						rs.getDate("createdAt")
						);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
