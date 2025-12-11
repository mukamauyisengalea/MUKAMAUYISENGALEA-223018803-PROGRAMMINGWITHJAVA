package util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.Instructor;
import model.Student;

public class DatabaseConnection {
	private static final String URL = "jdbc:mysql://localhost:3306/education_monitoring_management_system";
	private static final String USER = "root";
	private static final String PASSWORD = "";

	static {
		try {
			// Try both driver classes for compatibility
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
			} catch (ClassNotFoundException e1) {
				// Try older driver class
				Class.forName("com.mysql.jdbc.Driver");
			}
		} catch (ClassNotFoundException e) {
			System.err.println("MySQL JDBC Driver not found!");
			e.printStackTrace();
		}
	}

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}

	// INSTRUCTOR MANAGEMENT METHODS

	public static List<Object[]> getAllInstructors() {
		List<Object[]> instructors = new ArrayList<>();
		String sql = "SELECT instructorID, name, email, department, status, username FROM instructor ORDER BY name";

		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				Object[] instructor = {
						rs.getInt("instructorID"),
						rs.getString("name"),
						rs.getString("email"),
						rs.getString("department"),
						getInstructorCourses(rs.getInt("instructorID")),
						rs.getString("status"),
						rs.getString("username") != null ? " Setup" : " Pending"
				};
				instructors.add(instructor);
			}
		} catch (SQLException e) {
			System.err.println(" Error getting all instructors: " + e.getMessage());
			e.printStackTrace();
		}
		return instructors;
	}

	public static boolean addInstructor(String name, String identifier, String email, 
			String department, String phone, String location) {
		String sql = "INSERT INTO instructor (name, identifier, email, department, phone, location, status, assignedSince) VALUES (?, ?, ?, ?, ?, ?, 'ACTIVE', CURDATE())";

		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, name);
			stmt.setString(2, identifier);
			stmt.setString(3, email);
			stmt.setString(4, department);
			stmt.setString(5, phone);
			stmt.setString(6, location);

			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			System.err.println(" Error adding instructor: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public static boolean deleteInstructor(int instructorID) {
		Connection conn = null;
		try {
			conn = getConnection();
			conn.setAutoCommit(false);

			// First update courses to remove instructor assignment
			String updateCoursesSQL = "UPDATE course SET instructorID = NULL WHERE instructorID = ?";
			try (PreparedStatement stmt = conn.prepareStatement(updateCoursesSQL)) {
				stmt.setInt(1, instructorID);
				stmt.executeUpdate();
			}

			// Then delete the instructor
			String deleteInstructorSQL = "DELETE FROM instructor WHERE instructorID = ?";
			try (PreparedStatement stmt = conn.prepareStatement(deleteInstructorSQL)) {
				stmt.setInt(1, instructorID);
				int rowsAffected = stmt.executeUpdate();

				if (rowsAffected > 0) {
					conn.commit();
					return true;
				} else {
					conn.rollback();
					return false;
				}
			}
		} catch (SQLException e) {
			System.err.println(" ERROR in deleteInstructor: " + e.getMessage());
			e.printStackTrace();
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException ex) {
					System.err.println(" Error rolling back transaction: " + ex.getMessage());
				}
			}
			return false;
		} finally {
			if (conn != null) {
				try {
					conn.setAutoCommit(true);
					conn.close();
				} catch (SQLException e) {
					System.err.println(" Error closing connection: " + e.getMessage());
				}
			}
		}
	}

	public static boolean updateInstructorStatus(int instructorID, String status) {
		String sql = "UPDATE instructor SET status = ? WHERE instructorID = ?";

		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, status);
			stmt.setInt(2, instructorID);

			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			System.err.println(" Error updating instructor status: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public static boolean assignCourseToInstructor(int instructorID, int courseID) {
		String sql = "UPDATE course SET instructorID = ? WHERE courseID = ?";

		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, instructorID);
			stmt.setInt(2, courseID);

			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			System.err.println(" Error assigning course to instructor: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}



	// Get instructor details
	public static Object[] getInstructorDetails(int instructorID) {
		String sql = "SELECT instructorID, name, email, department, phone, contact, status FROM instructor WHERE instructorID = ?";

		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, instructorID);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				return new Object[]{
						rs.getInt("instructorID"),
						rs.getString("name"),
						rs.getString("email"),
						rs.getString("department"),
						rs.getString("phone"),
						rs.getString("contact"),
						rs.getString("status")
				};
			}
		} catch (SQLException e) {
			System.err.println(" Error getting instructor details: " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	// Get student count for instructor
	public static int getInstructorStudentCount(int instructorID) {
		String sql = "SELECT COUNT(DISTINCT e.studentID) as student_count " +
				"FROM enrollment e " +
				"JOIN course c ON e.courseID = c.courseID " +
				"WHERE c.instructorID = ?";

		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, instructorID);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				return rs.getInt("student_count");
			}
		} catch (SQLException e) {
			System.err.println(" Error getting student count: " + e.getMessage());
			e.printStackTrace();
		}
		return 0;
	}

	// Get active assignments count
	public static int getInstructorActiveAssignments(int instructorID) {
		String sql = "SELECT COUNT(*) as assignment_count " +
				"FROM assignment a " +
				"JOIN course c ON a.courseID = c.courseID " +
				"WHERE c.instructorID = ? AND a.is_published = TRUE";

		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, instructorID);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				return rs.getInt("assignment_count");
			}
		} catch (SQLException e) {
			System.err.println(" Error getting active assignments: " + e.getMessage());
			e.printStackTrace();
		}
		return 0;
	}

	// Add these methods to your DatabaseConnection.java

	// Get assignments for instructor's courses
	public static List<Object[]> getInstructorAssignmentsForGrading(int instructorID) {
		List<Object[]> assignments = new ArrayList<>();
		String sql = "SELECT a.assignmentID, a.title, c.courseCode, c.courseName " +
				"FROM assignment a " +
				"JOIN course c ON a.courseID = c.courseID " +
				"WHERE c.instructorID = ? AND a.is_published = TRUE";

		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, instructorID);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				assignments.add(new Object[]{
						rs.getInt("assignmentID"),
						rs.getString("title"),
						rs.getString("courseCode"),
						rs.getString("courseName")
				});
			}
		} catch (SQLException e) {
			System.err.println(" Error getting instructor assignments: " + e.getMessage());
			e.printStackTrace();
		}
		return assignments;
	}

	// Get students enrolled in instructor's courses
	public static List<Object[]> getInstructorStudentsForGrading(int instructorID) {
		List<Object[]> students = new ArrayList<>();
		String sql = "SELECT DISTINCT s.studentID, s.first_name, s.last_name, c.courseID, c.courseCode " +
				"FROM student s " +
				"JOIN enrollment e ON s.studentID = e.studentID " +
				"JOIN course c ON e.courseID = c.courseID " +
				"WHERE c.instructorID = ? AND e.status = 'ACTIVE'";

		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, instructorID);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				students.add(new Object[]{
						rs.getInt("studentID"),
						rs.getString("first_name") + " " + rs.getString("last_name"),
						rs.getInt("courseID"),
						rs.getString("courseCode")
				});
			}
		} catch (SQLException e) {
			System.err.println(" Error getting instructor students: " + e.getMessage());
			e.printStackTrace();
		}
		return students;
	}

	// Add new grade
	public static boolean addGrade(int assignmentID, int studentID, int courseID, 
			double score, String letterGrade, String remarks, int gradedBy) {
		String sql = "INSERT INTO grade (assignmentID, studentID, courseID, score, letter_grade, remarks, graded_by) " +
				"VALUES (?, ?, ?, ?, ?, ?, ?) " +
				"ON DUPLICATE KEY UPDATE score = ?, letter_grade = ?, remarks = ?, graded_by = ?";

		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, assignmentID);
			stmt.setInt(2, studentID);
			stmt.setInt(3, courseID);
			stmt.setDouble(4, score);
			stmt.setString(5, letterGrade);
			stmt.setString(6, remarks);
			stmt.setInt(7, gradedBy);
			stmt.setDouble(8, score);
			stmt.setString(9, letterGrade);
			stmt.setString(10, remarks);
			stmt.setInt(11, gradedBy);

			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			System.err.println(" Error adding grade: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// Get existing grades for display
	public static List<Object[]> getInstructorGrades(int instructorID) {
		List<Object[]> grades = new ArrayList<>();
		String sql = "SELECT s.first_name, s.last_name, c.courseCode, a.title, g.score, g.letter_grade, g.remarks " +
				"FROM grade g " +
				"JOIN student s ON g.studentID = s.studentID " +
				"JOIN assignment a ON g.assignmentID = a.assignmentID " +
				"JOIN course c ON g.courseID = c.courseID " +
				"WHERE c.instructorID = ? " +
				"ORDER BY c.courseCode, s.last_name, s.first_name";

		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, instructorID);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				grades.add(new Object[]{
						rs.getString("first_name") + " " + rs.getString("last_name"),
						rs.getString("courseCode"),
						rs.getString("title"),
						rs.getDouble("score"),
						rs.getString("letter_grade"),
						rs.getString("remarks")
				});
			}
		} catch (SQLException e) {
			System.err.println(" Error getting instructor grades: " + e.getMessage());
			e.printStackTrace();
		}
		return grades;
	}
	//Add these methods to your DatabaseConnection.java

	//Get instructor's courses for assignment creation
	public static List<Object[]> getInstructorCourses(int instructorID) {
		List<Object[]> courses = new ArrayList<>();
		String sql = "SELECT courseID, courseCode, courseName FROM course WHERE instructorID = ? AND is_active = TRUE";

		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, instructorID);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				courses.add(new Object[]{
						rs.getInt("courseID"),
						rs.getString("courseCode"),
						rs.getString("courseName")
				});
			}
		} catch (SQLException e) {
			System.err.println(" Error getting instructor courses: " + e.getMessage());
			e.printStackTrace();
		}
		return courses;
	}

	//Create new assignment
	//In DatabaseConnection.java - Update the createAssignment method signature
	public static boolean createAssignment(int courseID, String title, String description, 
			String instructions, java.sql.Date dueDate, double maxScore,
			String assignmentType, double weightage, int createdBy) {
		String sql = "INSERT INTO assignment (courseID, title, description, instructions, due_date, " +
				"max_score, assignment_type, weightage, is_published, created_by) " +
				"VALUES (?, ?, ?, ?, ?, ?, ?, ?, TRUE, ?)";

		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, courseID);
			stmt.setString(2, title);
			stmt.setString(3, description);
			stmt.setString(4, instructions);
			stmt.setDate(5, dueDate); // Now using java.sql.Date
			stmt.setDouble(6, maxScore);
			stmt.setString(7, assignmentType);
			stmt.setDouble(8, weightage);
			stmt.setInt(9, createdBy);

			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			System.err.println(" Error creating assignment: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	//Get assignments for display in assignments tab
	public static List<Object[]> getInstructorAssignments(int instructorID) {
		List<Object[]> assignments = new ArrayList<>();
		String sql = "SELECT a.title, c.courseCode, a.due_date, a.assignment_type, a.max_score, " +
				"a.is_published, COUNT(ae.studentID) as submissions " +
				"FROM assignment a " +
				"JOIN course c ON a.courseID = c.courseID " +
				"LEFT JOIN assignment_enrollment ae ON a.assignmentID = ae.assignmentID AND ae.status != 'NOT_SUBMITTED' " +
				"WHERE c.instructorID = ? " +
				"GROUP BY a.assignmentID " +
				"ORDER BY a.due_date DESC";

		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, instructorID);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				String status = rs.getBoolean("is_published") ? "Published" : "Draft";
				int submissions = rs.getInt("submissions");
				String submissionText = submissions + " submitted";

				assignments.add(new Object[]{
						rs.getString("title"),
						rs.getString("courseCode"),
						rs.getDate("due_date"),
						submissionText,
						status,
						"View/Edit"
				});
			}
		} catch (SQLException e) {
			System.err.println(" Error getting instructor assignments: " + e.getMessage());
			e.printStackTrace();
		}
		return assignments;
	}
	// Add these methods to your DatabaseConnection.java class

	public static boolean checkUserExists(String email, String userType) {
	    Connection conn = null;
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
	    
	    try {
	        conn = getConnection();
	        String table = "";
	        
	        if (userType.equalsIgnoreCase("student")) {
	            table = "students";
	        } else if (userType.equalsIgnoreCase("instructor")) {
	            table = "instructors";
	        } else if (userType.equalsIgnoreCase("admin")) {
	            table = "admins";
	        } else {
	            return false;
	        }
	        
	        String sql = "SELECT COUNT(*) FROM " + table + " WHERE email = ?";
	        stmt = conn.prepareStatement(sql);
	        stmt.setString(1, email);
	        rs = stmt.executeQuery();
	        
	        if (rs.next()) {
	            return rs.getInt(1) > 0;
	        }
	    } catch (SQLException e) {
	        System.err.println("Error checking user existence: " + e.getMessage());
	        e.printStackTrace();
	    } finally {
	        closeResources(conn, stmt, rs);
	    }
	    return false;
	}

	public static boolean registerStudent(Student student) {
	    Connection conn = null;
	    PreparedStatement stmt = null;
	    
	    try {
	        conn = getConnection();
	        
	        // First, ensure the students table exists with the correct schema
	        createStudentsTable();
	        
	        String sql = "INSERT INTO students (studentID, firstName, lastName, username, password, email, phone, sex, birthdate, createdAt) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	        stmt = conn.prepareStatement(sql);
	        
	        // Generate a student ID if not set
	        if (student.getStudentID() == 0) {
	            // Generate a new ID
	            int newId = generateNewStudentId();
	            student.setStudentID(newId);
	        }
	        
	        stmt.setInt(1, student.getStudentID());
	        stmt.setString(2, student.getFirstName());
	        stmt.setString(3, student.getLastName());
	        stmt.setString(4, student.getUsername());
	        stmt.setString(5, student.getPassword()); // Note: Hash passwords in production!
	        stmt.setString(6, student.getEmail());
	        stmt.setString(7, student.getPhone());
	        stmt.setString(8, student.getSex() != null ? student.getSex() : "Not specified");
	        
	        // Handle birthdate
	        if (student.getBirthdate() != null) {
	            stmt.setDate(9, new java.sql.Date(student.getBirthdate().getTime()));
	        } else {
	            stmt.setNull(9, java.sql.Types.DATE);
	        }
	        
	        // Handle createdAt
	        if (student.getCreatedAt() != null) {
	            stmt.setTimestamp(10, new java.sql.Timestamp(student.getCreatedAt().getTime()));
	        } else {
	            stmt.setTimestamp(10, new java.sql.Timestamp(System.currentTimeMillis()));
	        }
	        
	        int rowsAffected = stmt.executeUpdate();
	        return rowsAffected > 0;
	    } catch (SQLException e) {
	        System.err.println("Error registering student: " + e.getMessage());
	        e.printStackTrace();
	        return false;
	    } finally {
	        closeResources(conn, stmt, null);
	    }
	}

	public static boolean registerInstructor(Instructor instructor) {
	    Connection conn = null;
	    PreparedStatement stmt = null;
	    
	    try {
	        conn = getConnection();
	        
	        // First, ensure the instructors table exists with the correct schema
	        createInstructorsTable();
	        
	        String sql = "INSERT INTO instructors (instructorID, name, identifier, status, department, email, phone, username, password, createdAt) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	        stmt = conn.prepareStatement(sql);
	        
	        // Generate an instructor ID if not set
	        if (instructor.getInstructorID() == 0) {
	            // Generate a new ID
	            int newId = generateNewInstructorId();
	            instructor.setInstructorID(newId);
	        }
	        
	        stmt.setInt(1, instructor.getInstructorID());
	        stmt.setString(2, instructor.getName());
	        stmt.setString(3, instructor.getIdentifier());
	        stmt.setString(4, instructor.getStatus() != null ? instructor.getStatus() : "PENDING");
	        stmt.setString(5, instructor.getDepartment());
	        stmt.setString(6, instructor.getEmail());
	        stmt.setString(7, instructor.getPhone());
	        stmt.setString(8, instructor.getUsername());
	        stmt.setString(9, instructor.getPassword()); // Note: Hash passwords in production!
	        
	        // Set createdAt timestamp
	        stmt.setTimestamp(10, new java.sql.Timestamp(System.currentTimeMillis()));
	        
	        int rowsAffected = stmt.executeUpdate();
	        return rowsAffected > 0;
	    } catch (SQLException e) {
	        System.err.println("Error registering instructor: " + e.getMessage());
	        e.printStackTrace();
	        return false;
	    } finally {
	        closeResources(conn, stmt, null);
	    }
	}

	// Helper method to generate new student ID
	private static int generateNewStudentId() {
	    Connection conn = null;
	    Statement stmt = null;
	    ResultSet rs = null;
	    
	    try {
	        conn = getConnection();
	        stmt = conn.createStatement();
	        rs = stmt.executeQuery("SELECT MAX(studentID) FROM students");
	        
	        if (rs.next()) {
	            int maxId = rs.getInt(1);
	            return maxId + 1;
	        }
	    } catch (SQLException e) {
	        System.err.println("Error generating new student ID: " + e.getMessage());
	    } finally {
	        closeResources(conn, stmt, rs);
	    }
	    return 1000; // Default starting ID
	}

	// Helper method to generate new instructor ID
	private static int generateNewInstructorId() {
	    Connection conn = null;
	    Statement stmt = null;
	    ResultSet rs = null;
	    
	    try {
	        conn = getConnection();
	        stmt = conn.createStatement();
	        rs = stmt.executeQuery("SELECT MAX(instructorID) FROM instructors");
	        
	        if (rs.next()) {
	            int maxId = rs.getInt(1);
	            return maxId + 1;
	        }
	    } catch (SQLException e) {
	        System.err.println("Error generating new instructor ID: " + e.getMessage());
	    } finally {
	        closeResources(conn, stmt, rs);
	    }
	    return 100; // Default starting ID
	}

	// Helper method to create students table if it doesn't exist
	public static void createStudentsTable() {
	    Connection conn = null;
	    Statement stmt = null;
	    
	    try {
	        conn = getConnection();
	        stmt = conn.createStatement();
	        
	        String sql = "CREATE TABLE IF NOT EXISTS students (" +
	                     "studentID INT PRIMARY KEY, " +
	                     "firstName VARCHAR(50) NOT NULL, " +
	                     "lastName VARCHAR(50) NOT NULL, " +
	                     "username VARCHAR(50) UNIQUE NOT NULL, " +
	                     "password VARCHAR(100) NOT NULL, " +
	                     "sex VARCHAR(10), " +
	                     "birthdate DATE, " +
	                     "createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
	                     "email VARCHAR(100) UNIQUE NOT NULL, " +
	                     "phone VARCHAR(20), " +
	                     "address VARCHAR(200), " +
	                     "emergencyContact VARCHAR(100), " +
	                     "department VARCHAR(50)" +
	                     ")";
	        stmt.executeUpdate(sql);
	        System.out.println("Students table created/verified successfully.");
	        
	    } catch (SQLException e) {
	        System.err.println("Error creating students table: " + e.getMessage());
	        e.printStackTrace();
	    } finally {
	        closeResources(conn, stmt, null);
	    }
	}


	// Helper method to close database resources
	private static void closeResources(Connection conn, Statement stmt, ResultSet rs) {
	    try {
	        if (rs != null) rs.close();
	        if (stmt != null) stmt.close();
	        if (conn != null) conn.close();
	    } catch (SQLException e) {
	        System.err.println("Error closing resources: " + e.getMessage());
	    }
	}
	// For backward compatibility
	public static void createInstructorsTable() {
		// This method now uses the DAO class to maintain consistency
		model.dao.InstructorDAO instructorDAO = new model.dao.InstructorDAO();
		instructorDAO.createTable();
	}

}