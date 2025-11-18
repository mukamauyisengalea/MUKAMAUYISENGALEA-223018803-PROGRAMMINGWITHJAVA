package control;

import model.Instructor;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InstructorController {

	public List<Instructor> getAllInstructors() {
		List<Instructor> instructors = new ArrayList<>();
		String sql = "SELECT * FROM instructor ORDER BY assignedSince DESC";

		try (Connection conn = DatabaseConnection.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {
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
				instructors.add(instructor);
			}
		} catch (SQLException e) {
			e.printStackTrace();
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
			e.printStackTrace();
			return false;
		}
	}

	public void setVisible(boolean b) {
		// TODO Auto-generated method stub
		
	}
}

