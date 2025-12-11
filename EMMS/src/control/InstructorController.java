package control;

import model.Instructor;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InstructorController {

    // Get all instructors from the database
    public List<Instructor> getAllInstructors() {
        List<Instructor> instructors = new ArrayList<>();
        String sql = "SELECT * FROM instructor ORDER BY assignedSince DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Instructor instructor = extractInstructorFromResultSet(rs);
                instructors.add(instructor);
            }
        } catch (SQLException e) {
            System.err.println(" Error getting all instructors: " + e.getMessage());
            e.printStackTrace();
        }
        return instructors;
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
        return null;
    }

    // Add instructor (admin function - without password initially)
    public boolean addInstructor(Instructor instructor) {
        
        boolean hasCredentials = instructor.hasCredentials();
        
        String sql;
        if (hasCredentials) {
            sql = "INSERT INTO instructor (name, identifier, status, location, contact, username, password, assignedSince, courseID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        } else {
            sql = "INSERT INTO instructor (name, identifier, status, location, contact, assignedSince, courseID) VALUES (?, ?, ?, ?, ?, ?, ?)";
        }

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, instructor.getName());
            pstmt.setString(2, instructor.getIdentifier());
            pstmt.setString(3, instructor.getStatus());
            pstmt.setString(4, instructor.getLocation());
            pstmt.setString(5, instructor.getContact());
            
            if (hasCredentials) {
                pstmt.setString(6, instructor.getUsername());
                pstmt.setString(7, instructor.getPassword());
                pstmt.setDate(8, new java.sql.Date(instructor.getAssignedSince().getTime()));
                pstmt.setInt(9, instructor.getCourseID());
            } else {
                pstmt.setDate(6, new java.sql.Date(instructor.getAssignedSince().getTime()));
                pstmt.setInt(7, instructor.getCourseID());
            }

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println(" Error adding instructor: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Add instructor without credentials (admin function)
 
    public boolean addInstructorBasic(String name, String identifier, String status,
            String location, String contact, Date assignedSince, int courseID) {
// Use the constructor that accepts minimal parameters
Instructor instructor = new Instructor(name, identifier, status, location, contact, assignedSince, courseID);
return addInstructor(instructor);
}

    @SuppressWarnings("unused")
	private String generateEmailFromName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "instructor@edu.com";
        }
        String cleanName = name.toLowerCase().replaceAll("\\s+", ".");
        return cleanName + "@edu.com";
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
            e.printStackTrace();
            return false;
        }
    }

    // Update instructor
    public boolean updateInstructor(Instructor instructor) {
        String sql = "UPDATE instructor SET name = ?, identifier = ?, status = ?, location = ?, " +
                    "contact = ?, username = ?, password = ?, assignedSince = ?, courseID = ? " +
                    "WHERE instructorID = ?";

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
            System.err.println(" Error updating instructor: " + e.getMessage());
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
        instructor.setLocation(rs.getString("location"));
        instructor.setContact(rs.getString("contact"));
        instructor.setUsername(rs.getString("username"));
        instructor.setPassword(rs.getString("password"));
        instructor.setAssignedSince(rs.getDate("assignedSince"));
        instructor.setCourseID(rs.getInt("courseID"));
        return instructor;
    }

    // For UI compatibility
    public void setVisible(boolean b) {
        // Empty implementation for UI compatibility
    }

    // Test method
    public static void main(String[] args) {
        InstructorController controller = new InstructorController();
        
        // Test getting all instructors
        List<Instructor> instructors = controller.getAllInstructors();
        System.out.println("Total instructors: " + instructors.size());
        
        for (Instructor instructor : instructors) {
            System.out.println(instructor.getName() + " - " + 
                             (instructor.hasCredentials() ? "Has credentials" : "Needs setup"));
        }
    }
}