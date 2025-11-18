package util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

	// Test connection
	public static void main(String[] args) {
		try {
			Connection conn = getConnection();
			System.out.println("✅ Database connection successful!");

			// Test if tables exist
			if (!checkTableExists(conn, "courses")) {
				System.out.println("⚠️ Courses table doesn't exist. Creating database structure...");
				createDatabaseStructure();
			}

			conn.close();
		} catch (SQLException e) {
			System.err.println("❌ Database connection failed: " + e.getMessage());
		}
	}

	// Check if table exists
	private static boolean checkTableExists(Connection conn, String tableName) {
		try {
			DatabaseMetaData dbm = conn.getMetaData();
			ResultSet tables = dbm.getTables(null, null, tableName, null);
			return tables.next();
		} catch (SQLException e) {
			return false;
		}
	}

	// Create database structure
	private static void createDatabaseStructure() {
		try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {

			// Create courses table
			stmt.executeUpdate(
					"CREATE TABLE courses (" +
							"course_id VARCHAR(10) PRIMARY KEY, " +
							"course_name VARCHAR(100) NOT NULL, " +
							"instructor_id VARCHAR(20), " +
							"schedule VARCHAR(100), " +
							"enrolled_students INT DEFAULT 0)"
					);

			// Create students table
			stmt.executeUpdate(
					"CREATE TABLE students (" +
							"student_id VARCHAR(10) PRIMARY KEY, " +
							"name VARCHAR(100) NOT NULL, " +
							"email VARCHAR(100), " +
							"major VARCHAR(50), " +
							"enrollment_date DATE)"
					);

			// Create course enrollment table
			stmt.executeUpdate(
					"CREATE TABLE course_enrollment (" +
							"enrollment_id INT AUTO_INCREMENT PRIMARY KEY, " +
							"student_id VARCHAR(10), " +
							"course_id VARCHAR(10), " +
							"FOREIGN KEY (student_id) REFERENCES students(student_id), " +
							"FOREIGN KEY (course_id) REFERENCES courses(course_id))"
					);

			// Create assignments table
			stmt.executeUpdate(
					"CREATE TABLE assignments (" +
							"assignment_id INT AUTO_INCREMENT PRIMARY KEY, " +
							"assignment_name VARCHAR(100) NOT NULL, " +
							"course_id VARCHAR(10), " +
							"due_date DATE, " +
							"total_points INT, " +
							"description TEXT, " +
							"status ENUM('Draft', 'Active', 'Completed', 'Grading'), " +
							"FOREIGN KEY (course_id) REFERENCES courses(course_id))"
					);

			// Create grades table
			stmt.executeUpdate(
					"CREATE TABLE grades (" +
							"grade_id INT AUTO_INCREMENT PRIMARY KEY, " +
							"student_id VARCHAR(10), " +
							"course_id VARCHAR(10), " +
							"assignment_id INT, " +
							"grade VARCHAR(5), " +
							"comments TEXT, " +
							"FOREIGN KEY (student_id) REFERENCES students(student_id), " +
							"FOREIGN KEY (course_id) REFERENCES courses(course_id), " +
							"FOREIGN KEY (assignment_id) REFERENCES assignments(assignment_id))"
					);

			// Create course materials table
			stmt.executeUpdate(
					"CREATE TABLE course_materials (" +
							"material_id INT AUTO_INCREMENT PRIMARY KEY, " +
							"course_id VARCHAR(10), " +
							"material_type VARCHAR(50), " +
							"title VARCHAR(200), " +
							"description TEXT, " +
							"file_path VARCHAR(500), " +
							"upload_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
							"FOREIGN KEY (course_id) REFERENCES courses(course_id))"
					);

			// Insert sample data
			insertSampleData(conn);

			System.out.println("✅ Database structure created successfully!");

		} catch (SQLException e) {
			System.err.println("❌ Error creating database structure: " + e.getMessage());
		}
	}

	// Add these methods to your existing DatabaseConnection class

	// Instructor management methods
	public static List<Object[]> getAllInstructors() {
	    List<Object[]> instructors = new ArrayList<>();
	    String sql = "SELECT instructor_id, name, email, department, phone, office, status FROM instructors";
	    
	    try (Connection conn = getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql);
	         ResultSet rs = stmt.executeQuery()) {
	        
	        while (rs.next()) {
	            Object[] instructor = {
	                rs.getString("instructor_id"),
	                rs.getString("name"),
	                rs.getString("email"),
	                rs.getString("department"),
	                getInstructorCourses(rs.getString("instructor_id")),
	                rs.getString("status")
	            };
	            instructors.add(instructor);
	        }
	    } catch (SQLException e) {
	        System.err.println("Error getting all instructors: " + e.getMessage());
	        e.printStackTrace();
	    }
	    return instructors;
	}


	public static boolean addInstructor(String instructorId, String name, String email, 
	                                  String department, String phone, String office) {
	    String sql = "INSERT INTO instructors (instructor_id, name, email, department, phone, office) VALUES (?, ?, ?, ?, ?, ?)";
	    
	    try (Connection conn = getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        
	        stmt.setString(1, instructorId);
	        stmt.setString(2, name);
	        stmt.setString(3, email);
	        stmt.setString(4, department);
	        stmt.setString(5, phone);
	        stmt.setString(6, office);
	        
	        int rowsAffected = stmt.executeUpdate();
	        return rowsAffected > 0;
	    } catch (SQLException e) {
	        System.err.println("Error adding instructor: " + e.getMessage());
	        e.printStackTrace();
	        return false;
	    }
	}

	public static boolean deleteInstructor(String instructorId) {
	    // ========== TEMPORARY DEBUG CODE - ADD THIS AT THE BEGINNING ==========
	    System.out.println("=== DELETE INSTRUCTOR DEBUG ===");
	    System.out.println("Input ID: " + instructorId);
	    System.out.println("Tables check:");
	    try (Connection debugConn = getConnection()) {
	        DatabaseMetaData dbm = debugConn.getMetaData();
	        ResultSet tables = dbm.getTables(null, null, "instructors", null);
	        System.out.println("instructors table exists: " + tables.next());
	        
	        tables = dbm.getTables(null, null, "instructor", null);
	        System.out.println("instructor table exists: " + tables.next());
	        
	        // Also check what data exists in both tables
	        System.out.println("Data in instructors table:");
	        try (PreparedStatement stmt = debugConn.prepareStatement("SELECT instructor_id, name FROM instructors");
	             ResultSet rs = stmt.executeQuery()) {
	            while (rs.next()) {
	                System.out.println("  - " + rs.getString("instructor_id") + ": " + rs.getString("name"));
	            }
	        } catch (SQLException e) {
	            System.out.println("  No data in instructors table or table doesn't exist");
	        }
	        
	        System.out.println("Data in instructor table:");
	        try (PreparedStatement stmt = debugConn.prepareStatement("SELECT instructorID, identifier, name FROM instructor");
	             ResultSet rs = stmt.executeQuery()) {
	            while (rs.next()) {
	                System.out.println("  - " + rs.getInt("instructorID") + " (" + rs.getString("identifier") + "): " + rs.getString("name"));
	            }
	        } catch (SQLException e) {
	            System.out.println("  No data in instructor table or table doesn't exist");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    System.out.println("=== END DEBUG ===");
	    // ========== END DEBUG CODE ==========
	    
	    Connection conn = null;
	    try {
	        conn = getConnection();
	        conn.setAutoCommit(false);
	        
	        System.out.println("DEBUG: Attempting to delete instructor with ID: " + instructorId);
	        
	        // First update courses to remove instructor assignment
	        String updateCoursesSQL = "UPDATE courses SET instructor_id = NULL WHERE instructor_id = ?";
	        try (PreparedStatement stmt = conn.prepareStatement(updateCoursesSQL)) {
	            stmt.setString(1, instructorId);
	            int updated = stmt.executeUpdate();
	            System.out.println("DEBUG: Updated " + updated + " courses");
	        }
	        
	        // Then delete the instructor
	        String deleteInstructorSQL = "DELETE FROM instructors WHERE instructor_id = ?";
	        try (PreparedStatement stmt = conn.prepareStatement(deleteInstructorSQL)) {
	            stmt.setString(1, instructorId);
	            int rowsAffected = stmt.executeUpdate();
	            
	            System.out.println("DEBUG: Deleted " + rowsAffected + " instructors");
	            
	            if (rowsAffected > 0) {
	                conn.commit();
	                return true;
	            } else {
	                conn.rollback();
	                return false;
	            }
	        }
	    } catch (SQLException e) {
	        System.err.println("ERROR in deleteInstructor: " + e.getMessage());
	        e.printStackTrace();
	        if (conn != null) {
	            try {
	                conn.rollback();
	            } catch (SQLException ex) {
	                System.err.println("Error rolling back transaction: " + ex.getMessage());
	            }
	        }
	        return false;
	    } finally {
	        if (conn != null) {
	            try {
	                conn.setAutoCommit(true);
	                conn.close();
	            } catch (SQLException e) {
	                System.err.println("Error closing connection: " + e.getMessage());
	            }
	        }
	    }
	}

	public static boolean updateInstructorStatus(String instructorId, String status) {
	    String sql = "UPDATE instructors SET status = ? WHERE instructor_id = ?";
	    
	    try (Connection conn = getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        
	        stmt.setString(1, status);
	        stmt.setString(2, instructorId);
	        
	        int rowsAffected = stmt.executeUpdate();
	        return rowsAffected > 0;
	    } catch (SQLException e) {
	        System.err.println("Error updating instructor status: " + e.getMessage());
	        e.printStackTrace();
	        return false;
	    }
	}

	public static boolean assignCourseToInstructor(String instructorId, String courseCode) {
	    String sql = "UPDATE courses SET instructor_id = ? WHERE course_id = ?";
	    
	    try (Connection conn = getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        
	        stmt.setString(1, instructorId);
	        stmt.setString(2, courseCode);
	        
	        int rowsAffected = stmt.executeUpdate();
	        return rowsAffected > 0;
	    } catch (SQLException e) {
	        System.err.println("Error assigning course to instructor: " + e.getMessage());
	        e.printStackTrace();
	        return false;
	    }
	}

	public static void debugDatabaseState(String instructorId) {
	    System.out.println("=== DATABASE DEBUG INFORMATION ===");
	    
	    try (Connection conn = getConnection()) {
	        // Check tables
	        DatabaseMetaData dbm = conn.getMetaData();
	        String[] tableTypes = {"TABLE"};
	        ResultSet tables = dbm.getTables(null, null, "%", tableTypes);
	        
	        System.out.println("Existing tables:");
	        while (tables.next()) {
	            System.out.println("  - " + tables.getString("TABLE_NAME"));
	        }
	        
	        // Check instructors data
	        System.out.println("\nInstructors in database:");
	        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM instructors");
	             ResultSet rs = stmt.executeQuery()) {
	            while (rs.next()) {
	                System.out.println("  - ID: " + rs.getString("instructor_id") + 
	                                 ", Name: " + rs.getString("name"));
	            }
	        } catch (SQLException e) {
	            System.out.println("  No instructors table or empty: " + e.getMessage());
	        }
	        
	        // Check courses for this instructor
	        System.out.println("\nCourses for instructor " + instructorId + ":");
	        try (PreparedStatement stmt = conn.prepareStatement(
	                "SELECT * FROM courses WHERE instructor_id = ?")) {
	            stmt.setString(1, instructorId);
	            ResultSet rs = stmt.executeQuery();
	            int count = 0;
	            while (rs.next()) {
	                count++;
	                System.out.println("  - " + rs.getString("course_id") + ": " + rs.getString("course_name"));
	            }
	            System.out.println("  Total: " + count + " courses");
	        }
	        
	    } catch (SQLException e) {
	        System.err.println("Debug error: " + e.getMessage());
	        e.printStackTrace();
	    }
	    System.out.println("=== END DEBUG ===");
	}
	
	public static List<Object[]> getInstructorCourses(String instructorId) {
	    System.out.println("DEBUG: Getting courses for instructor: " + instructorId);
	    
	    List<Object[]> courses = new ArrayList<>();
	    
	    // First, check if instructor exists in any table
	    if (!checkInstructorExists(instructorId)) {
	        System.out.println("DEBUG: Instructor " + instructorId + " not found in database");
	        return courses;
	    }
	    
	    String sql = "SELECT course_id, course_name, schedule, enrolled_students FROM courses WHERE instructor_id = ?";

	    try (Connection conn = getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {

	        stmt.setString(1, instructorId);
	        ResultSet rs = stmt.executeQuery();

	        int count = 0;
	        while (rs.next()) {
	            count++;
	            Object[] course = {
	                rs.getString("course_id"),
	                rs.getString("course_name"),
	                rs.getString("schedule"),
	                rs.getInt("enrolled_students"),
	                "View/Edit"
	            };
	            courses.add(course);
	        }
	        System.out.println("DEBUG: Found " + count + " courses for instructor " + instructorId);
	        
	    } catch (SQLException e) {
	        System.err.println("ERROR getting instructor courses: " + e.getMessage());
	        e.printStackTrace();
	    }
	    
	    return courses;
	}

	private static boolean checkInstructorExists(String instructorId) {
	    String sql = "SELECT instructor_id FROM instructors WHERE instructor_id = ?";
	    try (Connection conn = getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        
	        stmt.setString(1, instructorId);
	        ResultSet rs = stmt.executeQuery();
	        return rs.next();
	        
	    } catch (SQLException e) {
	        System.err.println("Error checking instructor existence: " + e.getMessage());
	        return false;
	    }
	}
	// Create instructors table if it doesn't exist
	public static void createInstructorsTable() {
	    String sql = "CREATE TABLE IF NOT EXISTS instructors (" +
	                 "instructor_id VARCHAR(20) PRIMARY KEY, " +
	                 "name VARCHAR(100) NOT NULL, " +
	                 "email VARCHAR(100) UNIQUE NOT NULL, " +
	                 "department VARCHAR(50), " +
	                 "phone VARCHAR(20), " +
	                 "office VARCHAR(50), " +
	                 "status ENUM('Active', 'On Leave', 'Inactive') DEFAULT 'Active', " +
	                 "created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
	    
	    try (Connection conn = getConnection();
	         Statement stmt = conn.createStatement()) {
	        stmt.executeUpdate(sql);
	        
	        // Insert sample instructors if table is empty
	        String checkSql = "SELECT COUNT(*) FROM instructors";
	        ResultSet rs = stmt.executeQuery(checkSql);
	        if (rs.next() && rs.getInt(1) == 0) {
	            insertSampleInstructors(conn);
	        }
	    } catch (SQLException e) {
	        System.err.println("Error creating instructors table: " + e.getMessage());
	        e.printStackTrace();
	    }
	}

	private static void insertSampleInstructors(Connection conn) {
	    try (Statement stmt = conn.createStatement()) {
	        stmt.executeUpdate(
	            "INSERT INTO instructors (instructor_id, name, email, department, phone, office, status) VALUES " +
	            "('INST001', 'Dr. Sarah Johnson', 'sarah.johnson@edu.com', 'Computer Science', '+1-555-0101', 'CS Building Room 101', 'Active'), " +
	            "('INST002', 'Prof. Michael Chen', 'michael.chen@edu.com', 'Mathematics', '+1-555-0102', 'Math Building Room 205', 'Active'), " +
	            "('INST003', 'Dr. Emily Davis', 'emily.davis@edu.com', 'Physics', '+1-555-0103', 'Physics Building Room 150', 'On Leave'), " +
	            "('INST004', 'Prof. Robert Wilson', 'robert.wilson@edu.com', 'Computer Science', '+1-555-0104', 'CS Building Room 203', 'Active')"
	        );
	        System.out.println("✅ Sample instructors inserted successfully!");
	    } catch (SQLException e) {
	        System.err.println("❌ Error inserting sample instructors: " + e.getMessage());
	    }
	}
	
	// Insert sample data
	private static void insertSampleData(Connection conn) {
		try (Statement stmt = conn.createStatement()) {

			// Insert courses
			stmt.executeUpdate(
					"INSERT INTO courses VALUES " +
							"('CS101', 'Introduction to Programming', 'INST001', 'Mon/Wed 9:00-10:30', 45), " +
							"('CS201', 'Data Structures', 'INST001', 'Tue/Thu 11:00-12:30', 38), " +
							"('CS301', 'Algorithms', 'INST001', 'Mon/Wed/Fri 14:00-15:00', 32)"
					);

			// Insert students
			stmt.executeUpdate(
					"INSERT INTO students VALUES " +
							"('S1001', 'John Doe', 'john.doe@edu.com', 'Computer Science', '2024-09-01'), " +
							"('S1002', 'Jane Smith', 'jane.smith@edu.com', 'Computer Science', '2024-09-01'), " +
							"('S1003', 'Mike Johnson', 'mike.johnson@edu.com', 'Software Engineering', '2024-09-01'), " +
							"('S1004', 'Sarah Wilson', 'sarah.wilson@edu.com', 'Computer Science', '2024-09-01')"
					);

			// Insert course enrollment
			stmt.executeUpdate(
					"INSERT INTO course_enrollment (student_id, course_id) VALUES " +
							"('S1001', 'CS101'), ('S1002', 'CS101'), ('S1003', 'CS201'), ('S1004', 'CS301')"
					);

			// Insert assignments
			stmt.executeUpdate(
					"INSERT INTO assignments (assignment_name, course_id, due_date, total_points, description, status) VALUES " +
							"('Programming Project 1', 'CS101', '2024-12-15', 100, 'Basic programming concepts', 'Active'), " +
							"('Data Structures Quiz', 'CS201', '2024-12-10', 50, 'Linked lists and trees', 'Grading'), " +
							"('Algorithm Analysis', 'CS301', '2024-12-20', 100, 'Complexity analysis', 'Draft')"
					);

			// Insert grades
			stmt.executeUpdate(
					"INSERT INTO grades (student_id, course_id, assignment_id, grade, comments) VALUES " +
							"('S1001', 'CS101', 1, 'A', 'Excellent work'), " +
							"('S1002', 'CS101', 1, 'B+', 'Good understanding'), " +
							"('S1003', 'CS201', 2, 'A-', 'Well prepared'), " +
							"('S1004', 'CS301', 3, 'A', 'Outstanding')"
					);

			System.out.println("✅ Sample data inserted successfully!");

		} catch (SQLException e) {
			System.err.println("❌ Error inserting sample data: " + e.getMessage());
		}
	}
	
	public static void testInstructorData(String instructorId) {
	    System.out.println("=== TESTING INSTRUCTOR DATA ===");
	    
	    // Test courses
	    List<Object[]> courses = getInstructorCourses(instructorId);
	    System.out.println("Courses found: " + courses.size());
	    
	    // Test students
	    List<Object[]> students = getInstructorStudents(instructorId);
	    System.out.println("Students found: " + students.size());
	    
	    // Test assignments
	    List<Object[]> assignments = getInstructorAssignments(instructorId);
	    System.out.println("Assignments found: " + assignments.size());
	    
	    System.out.println("=== TEST COMPLETE ===");
	}

	// Student methods
	public static List<Object[]> getInstructorStudents(String instructorId) {
	    System.out.println("DEBUG: Getting students for instructor: " + instructorId);
	    
	    List<Object[]> students = new ArrayList<>();
	    String sql = "SELECT s.student_id, s.name, c.course_id, s.email, 'Good' as performance " +
	            "FROM students s " +
	            "JOIN course_enrollment ce ON s.student_id = ce.student_id " +
	            "JOIN courses c ON ce.course_id = c.course_id " +
	            "WHERE c.instructor_id = ?";

	    try (Connection conn = getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {

	        stmt.setString(1, instructorId);
	        ResultSet rs = stmt.executeQuery();

	        int count = 0;
	        while (rs.next()) {
	            count++;
	            Object[] student = {
	                rs.getString("student_id"),
	                rs.getString("name"),
	                rs.getString("course_id"),
	                rs.getString("email"),
	                rs.getString("performance")
	            };
	            students.add(student);
	        }
	        System.out.println("DEBUG: Found " + count + " students for instructor " + instructorId);
	        
	    } catch (SQLException e) {
	        System.err.println("ERROR getting instructor students: " + e.getMessage());
	        e.printStackTrace();
	    }
	    return students;
	}

	// Assignment methods - THIS WAS MISSING!
	public static List<Object[]> getInstructorAssignments(String instructorId) {
		List<Object[]> assignments = new ArrayList<>();
		String sql = "SELECT a.assignment_name, c.course_id, a.due_date, '0/0' as submissions, a.status " +
				"FROM assignments a " +
				"JOIN courses c ON a.course_id = c.course_id " +
				"WHERE c.instructor_id = ?";

		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, instructorId);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Object[] assignment = {
						rs.getString("assignment_name"),
						rs.getString("course_id"),
						rs.getDate("due_date"),
						rs.getString("submissions"),
						rs.getString("status")
				};
				assignments.add(assignment);
			}
		} catch (SQLException e) {
			System.err.println("Error getting instructor assignments: " + e.getMessage());
			e.printStackTrace();
		}
		return assignments;
	}

	// Grade methods
	public static List<Object[]> getInstructorGrades(String instructorId) {
		List<Object[]> grades = new ArrayList<>();
		String sql = "SELECT s.name, c.course_id, a.assignment_name, g.grade, g.comments " +
				"FROM grades g " +
				"JOIN students s ON g.student_id = s.student_id " +
				"JOIN courses c ON g.course_id = c.course_id " +
				"JOIN assignments a ON g.assignment_id = a.assignment_id " +
				"WHERE c.instructor_id = ?";

		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, instructorId);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Object[] grade = {
						rs.getString("name"),
						rs.getString("course_id"),
						rs.getString("assignment_name"),
						rs.getString("grade"),
						rs.getString("comments")
				};
				grades.add(grade);
			}
		} catch (SQLException e) {
			System.err.println("Error getting instructor grades: " + e.getMessage());
			e.printStackTrace();
		}
		return grades;
	}

	// Add course material
	public static boolean addCourseMaterial(String courseId, String materialType, String title, String description) {
		String sql = "INSERT INTO course_materials (course_id, material_type, title, description) VALUES (?, ?, ?, ?)";

		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, courseId);
			stmt.setString(2, materialType);
			stmt.setString(3, title);
			stmt.setString(4, description);

			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			System.err.println("Error adding course material: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// Create assignment
	public static boolean createAssignment(String assignmentName, String courseId, String dueDate, int totalPoints, String description) {
	    System.out.println("DEBUG: Creating assignment - Name: " + assignmentName + ", Course: " + courseId);
	    
	    String sql = "INSERT INTO assignments (assignment_name, course_id, due_date, total_points, description, status) VALUES (?, ?, ?, ?, ?, 'Draft')";

	    try (Connection conn = getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {

	        stmt.setString(1, assignmentName);
	        stmt.setString(2, courseId);
	        stmt.setString(3, dueDate);
	        stmt.setInt(4, totalPoints);
	        stmt.setString(5, description);

	        int rowsAffected = stmt.executeUpdate();
	        System.out.println("DEBUG: Assignment created, rows affected: " + rowsAffected);
	        return rowsAffected > 0;
	        
	    } catch (SQLException e) {
	        System.err.println("ERROR creating assignment: " + e.getMessage());
	        e.printStackTrace();
	        return false;
	    }
	}

	// Add/Update grade
	public static boolean updateGrade(String studentId, String courseId, int assignmentId, String grade, String comments) {
		String sql = "INSERT INTO grades (student_id, course_id, assignment_id, grade, comments) VALUES (?, ?, ?, ?, ?) " +
				"ON DUPLICATE KEY UPDATE grade = ?, comments = ?";

		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, studentId);
			stmt.setString(2, courseId);
			stmt.setInt(3, assignmentId);
			stmt.setString(4, grade);
			stmt.setString(5, comments);
			stmt.setString(6, grade);
			stmt.setString(7, comments);

			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			System.err.println("Error updating grade: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// Get all courses for combobox
	public static List<String> getAllCourses(String instructorId) {
		List<String> courses = new ArrayList<>();
		String sql = "SELECT course_id FROM courses WHERE instructor_id = ?";

		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, instructorId);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				courses.add(rs.getString("course_id"));
			}
		} catch (SQLException e) {
			System.err.println("Error getting all courses: " + e.getMessage());
			e.printStackTrace();
		}
		return courses;
	}

	// Get all students for combobox
	public static List<String> getAllStudents(String instructorId) {
		List<String> students = new ArrayList<>();
		String sql = "SELECT DISTINCT s.student_id, s.name " +
				"FROM students s " +
				"JOIN course_enrollment ce ON s.student_id = ce.student_id " +
				"JOIN courses c ON ce.course_id = c.course_id " +
				"WHERE c.instructor_id = ?";

		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, instructorId);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				students.add(rs.getString("student_id") + " - " + rs.getString("name"));
			}
		} catch (SQLException e) {
			System.err.println("Error getting all students: " + e.getMessage());
			e.printStackTrace();
		}
		return students;
	}
	
	public static boolean checkInstructorsTableExists() {
	    String sql = "SELECT COUNT(*) FROM instructors";
	    try (Connection conn = getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql);
	         ResultSet rs = stmt.executeQuery()) {
	        return true;
	    } catch (SQLException e) {
	        System.err.println("instructors table doesn't exist or is empty: " + e.getMessage());
	        return false;
	    }
	}

	public static boolean checkInstructorTableExists() {
	    String sql = "SELECT COUNT(*) FROM instructor";
	    try (Connection conn = getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql);
	         ResultSet rs = stmt.executeQuery()) {
	        return true;
	    } catch (SQLException e) {
	        System.err.println("instructor table doesn't exist or is empty: " + e.getMessage());
	        return false;
	    }
	}
	// Add these methods to DatabaseConnection class

	public static Object[] getInstructorDetails(String instructorId) {
	    String sql = "SELECT instructor_id, name, email, department, phone, office, status FROM instructors WHERE instructor_id = ?";
	    
	    try (Connection conn = getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        
	        stmt.setString(1, instructorId);
	        ResultSet rs = stmt.executeQuery();
	        
	        if (rs.next()) {
	            return new Object[]{
	                rs.getString("instructor_id"),
	                rs.getString("name"),
	                rs.getString("email"),
	                rs.getString("department"),
	                rs.getString("phone"),
	                rs.getString("office"),
	                rs.getString("status")
	            };
	        }
	    } catch (SQLException e) {
	        System.err.println("Error getting instructor details: " + e.getMessage());
	        e.printStackTrace();
	    }
	    return null;
	}

	public static int getInstructorStudentCount(String instructorId) {
	    String sql = "SELECT COUNT(DISTINCT ce.student_id) as student_count " +
	                 "FROM course_enrollment ce " +
	                 "JOIN courses c ON ce.course_id = c.course_id " +
	                 "WHERE c.instructor_id = ?";
	    
	    try (Connection conn = getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        
	        stmt.setString(1, instructorId);
	        ResultSet rs = stmt.executeQuery();
	        
	        if (rs.next()) {
	            return rs.getInt("student_count");
	        }
	    } catch (SQLException e) {
	        System.err.println("Error getting student count: " + e.getMessage());
	        e.printStackTrace();
	    }
	    return 0;
	}

	public static int getInstructorActiveAssignments(String instructorId) {
	    String sql = "SELECT COUNT(*) as assignment_count " +
	                 "FROM assignments a " +
	                 "JOIN courses c ON a.course_id = c.course_id " +
	                 "WHERE c.instructor_id = ? AND a.status IN ('Active', 'Grading')";
	    
	    try (Connection conn = getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        
	        stmt.setString(1, instructorId);
	        ResultSet rs = stmt.executeQuery();
	        
	        if (rs.next()) {
	            return rs.getInt("assignment_count");
	        }
	    } catch (SQLException e) {
	        System.err.println("Error getting active assignments: " + e.getMessage());
	        e.printStackTrace();
	    }
	    return 0;
	}
	
	
	// Get all assignments for combobox
	public static List<String> getAllAssignments(String instructorId) {
		List<String> assignments = new ArrayList<>();
		String sql = "SELECT a.assignment_id, a.assignment_name " +
				"FROM assignments a " +
				"JOIN courses c ON a.course_id = c.course_id " +
				"WHERE c.instructor_id = ?";

		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, instructorId);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				assignments.add(rs.getInt("assignment_id") + " - " + rs.getString("assignment_name"));
			}
		} catch (SQLException e) {
			System.err.println("Error getting all assignments: " + e.getMessage());
			e.printStackTrace();
		}
		return assignments;
	}
}