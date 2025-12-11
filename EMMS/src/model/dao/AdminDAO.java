package model.dao;

import model.Admin;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {

	public boolean createTable() {
		String sql = "CREATE TABLE IF NOT EXISTS admin (" +
				"id INT PRIMARY KEY AUTO_INCREMENT, " +
				"name VARCHAR(100) NOT NULL, " +
				"username VARCHAR(50) UNIQUE NOT NULL, " +
				"password VARCHAR(255) NOT NULL)";

		try (Connection conn = DatabaseConnection.getConnection();
				Statement stmt = conn.createStatement()) {
			stmt.executeUpdate(sql);
			System.out.println(" Admin table created successfully!");
			return true;
		} catch (SQLException e) {
			System.err.println("Error creating admin table: " + e.getMessage());
			return false;
		}
	}

	public Admin getAdminById(int id) {
		String sql = "SELECT * FROM admin WHERE id = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return extractAdminFromResultSet(rs);
			}
		} catch (SQLException e) {
			System.err.println("Error getting admin by ID: " + e.getMessage());
		}
		return null;
	}

	public Admin getAdminByUsername(String username) {
		String sql = "SELECT * FROM admin WHERE username = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, username);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return extractAdminFromResultSet(rs);
			}
		} catch (SQLException e) {
			System.err.println("Error getting admin by username: " + e.getMessage());
		}
		return null;
	}

	public List<Admin> getAllAdmins() {
		List<Admin> admins = new ArrayList<>();
		String sql = "SELECT * FROM admin ORDER BY name";

		try (Connection conn = DatabaseConnection.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {
				admins.add(extractAdminFromResultSet(rs));
			}
		} catch (SQLException e) {
			System.err.println("Error getting all admins: " + e.getMessage());
		}
		return admins;
	}

	public boolean addAdmin(Admin admin) {
		String sql = "INSERT INTO admin (name, username, password) VALUES (?, ?, ?)";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, admin.getName());
			pstmt.setString(2, admin.getUsername());
			pstmt.setString(3, admin.getPassword());

			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0;

		} catch (SQLException e) {
			System.err.println("Error adding admin: " + e.getMessage());
			return false;
		}
	}

	public boolean updateAdmin(Admin admin) {
		String sql = "UPDATE admin SET name = ?, username = ?, password = ? WHERE id = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, admin.getName());
			pstmt.setString(2, admin.getUsername());
			pstmt.setString(3, admin.getPassword());
			pstmt.setInt(4, admin.getId());

			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0;

		} catch (SQLException e) {
			System.err.println("Error updating admin: " + e.getMessage());
			return false;
		}
	}

	public boolean deleteAdmin(int id) {
		String sql = "DELETE FROM admin WHERE id = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, id);
			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0;

		} catch (SQLException e) {
			System.err.println("Error deleting admin: " + e.getMessage());
			return false;
		}
	}

	private Admin extractAdminFromResultSet(ResultSet rs) throws SQLException {
		Admin admin = new Admin();
		admin.setId(rs.getInt("id"));
		admin.setName(rs.getString("name"));
		admin.setUsername(rs.getString("username"));
		admin.setPassword(rs.getString("password"));
		return admin;
	}
}