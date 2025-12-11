package util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

	public static void initializeDatabase() {
		System.out.println("Starting comprehensive database initialization...");

		try (Connection conn = DatabaseConnection.getConnection();
				Statement stmt = conn.createStatement()) {

			// Create tables in proper order to handle foreign key dependencies
			createAdminTable(stmt);
			createStudentTable(stmt);
			createCourseTable(stmt);
			createInstructorTable(stmt);
			createAssignmentTable(stmt);
			createEnrollmentTable(stmt);
			createGradeTable(stmt);
			createAssignmentEnrollmentTable(stmt);

			// Create indexes for better performance
			createIndexes(conn);

			// Insert default data
			insertDefaultData(stmt);

			System.out.println(" Database initialization completed successfully!");

		} catch (Exception e) {
			System.err.println(" Database initialization failed: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private static void createAdminTable(Statement stmt) throws SQLException {
		String sql = "CREATE TABLE IF NOT EXISTS admin (" +
				"id INT PRIMARY KEY AUTO_INCREMENT, " +
				"name VARCHAR(100) NOT NULL, " +
				"username VARCHAR(50) UNIQUE NOT NULL, " +
				"password VARCHAR(255) NOT NULL, " +
				"created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
				"updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)";
		stmt.executeUpdate(sql);
		System.out.println(" Admin table created/verified");
	}

	private static void createStudentTable(Statement stmt) throws SQLException {
		String sql = "CREATE TABLE IF NOT EXISTS student (" +
				"studentID INT PRIMARY KEY AUTO_INCREMENT, " +
				"first_name VARCHAR(50) NOT NULL, " +
				"last_name VARCHAR(50) NOT NULL, " +
				"username VARCHAR(50) UNIQUE NOT NULL, " +
				"password VARCHAR(255) NOT NULL, " +
				"sex ENUM('M', 'F'), " +
				"birthdate DATE, " +
				"email VARCHAR(100), " +
				"phone VARCHAR(20), " +
				"address TEXT, " +
				"emergency_contact VARCHAR(100), " +
				"createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
				"updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)";
		stmt.executeUpdate(sql);
		System.out.println(" Student table created/verified");
	}

	private static void createCourseTable(Statement stmt) throws SQLException {
		String sql = "CREATE TABLE IF NOT EXISTS course (" +
				"courseID INT PRIMARY KEY AUTO_INCREMENT, " +
				"courseName VARCHAR(100) NOT NULL, " +
				"courseCode VARCHAR(20) UNIQUE NOT NULL, " +
				"description TEXT, " +
				"credits INT DEFAULT 3, " +
				"duration_weeks INT DEFAULT 16, " +
				"max_students INT DEFAULT 30, " +
				"is_active BOOLEAN DEFAULT TRUE, " +
				"instructorID INT, " + 
				"createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
				"updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, " +
				"FOREIGN KEY (instructorID) REFERENCES instructor(instructorID) ON DELETE SET NULL)"; // ADD THIS LINE
		stmt.executeUpdate(sql);
		System.out.println(" Course table created/verified");
	}

	private static void createInstructorTable(Statement stmt) throws SQLException {
		String sql = "CREATE TABLE IF NOT EXISTS instructor (" +
				"instructorID INT PRIMARY KEY AUTO_INCREMENT, " +
				"name VARCHAR(100) NOT NULL, " +
				"identifier VARCHAR(50) UNIQUE, " +
				"status ENUM('ACTIVE', 'ON_LEAVE', 'INACTIVE') DEFAULT 'ACTIVE', " +
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
		stmt.executeUpdate(sql);
		System.out.println("Instructor table created/verified");
	}

	private static void createAssignmentTable(Statement stmt) throws SQLException {
		String sql = "CREATE TABLE IF NOT EXISTS assignment (" +
				"assignmentID INT PRIMARY KEY AUTO_INCREMENT, " +
				"courseID INT NOT NULL, " +
				"title VARCHAR(200) NOT NULL, " +
				"description TEXT, " +
				"instructions TEXT, " +
				"due_date DATE, " +
				"max_score DECIMAL(5,2) DEFAULT 100, " +
				"assignment_type ENUM('HOMEWORK', 'QUIZ', 'PROJECT', 'EXAM', 'ESSAY') DEFAULT 'HOMEWORK', " +
				"weightage DECIMAL(4,2) DEFAULT 10.00, " +
				"is_published BOOLEAN DEFAULT FALSE, " +
				"created_by INT, " +
				"created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
				"updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, " +
				"FOREIGN KEY (courseID) REFERENCES course(courseID) ON DELETE CASCADE)";
		stmt.executeUpdate(sql);
		System.out.println(" Assignment table created/verified");
	}

	private static void createEnrollmentTable(Statement stmt) throws SQLException {
		String sql = "CREATE TABLE IF NOT EXISTS enrollment (" +
				"enrollmentID INT PRIMARY KEY AUTO_INCREMENT, " +
				"studentID INT NOT NULL, " +
				"courseID INT NOT NULL, " +
				"enrollment_date DATE NOT NULL, " +
				"status ENUM('ACTIVE', 'COMPLETED', 'WITHDRAWN', 'INACTIVE') DEFAULT 'ACTIVE', " +
				"final_grade VARCHAR(5), " +
				"completion_date DATE, " +
				"created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
				"updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, " +
				"FOREIGN KEY (studentID) REFERENCES student(studentID) ON DELETE CASCADE, " +
				"FOREIGN KEY (courseID) REFERENCES course(courseID) ON DELETE CASCADE, " +
				"UNIQUE KEY unique_enrollment (studentID, courseID))";
		stmt.executeUpdate(sql);
		System.out.println(" Enrollment table created/verified");
	}

	private static void createGradeTable(Statement stmt) throws SQLException {
		String sql = "CREATE TABLE IF NOT EXISTS grade (" +
				"gradeID INT PRIMARY KEY AUTO_INCREMENT, " +
				"assignmentID INT NOT NULL, " +
				"studentID INT NOT NULL, " +
				"courseID INT NOT NULL, " +
				"score DECIMAL(5,2) NOT NULL, " +
				"letter_grade VARCHAR(5), " +
				"remarks TEXT, " +
				"graded_by INT, " +
				"graded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
				"created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
				"updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, " +
				"FOREIGN KEY (assignmentID) REFERENCES assignment(assignmentID) ON DELETE CASCADE, " +
				"FOREIGN KEY (studentID) REFERENCES student(studentID) ON DELETE CASCADE, " +
				"FOREIGN KEY (courseID) REFERENCES course(courseID) ON DELETE CASCADE, " +
				"FOREIGN KEY (graded_by) REFERENCES instructor(instructorID) ON DELETE SET NULL, " +
				"UNIQUE KEY unique_grade_entry (studentID, assignmentID))";
		// Removed CHECK constraint for compatibility
		stmt.executeUpdate(sql);
		System.out.println(" Grade table created/verified");
	}

	private static void createAssignmentEnrollmentTable(Statement stmt) throws SQLException {
		String sql = "CREATE TABLE IF NOT EXISTS assignment_enrollment (" +
				"id INT PRIMARY KEY AUTO_INCREMENT, " +
				"assignmentID INT NOT NULL, " +
				"studentID INT NOT NULL, " +
				"submission_date TIMESTAMP NULL, " +
				"submitted_file VARCHAR(255), " +
				"submission_text TEXT, " +
				"status ENUM('NOT_SUBMITTED', 'SUBMITTED', 'LATE', 'GRADED') DEFAULT 'NOT_SUBMITTED', " +
				"submission_notes TEXT, " +
				"created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
				"updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, " +
				"FOREIGN KEY (assignmentID) REFERENCES assignment(assignmentID) ON DELETE CASCADE, " +
				"FOREIGN KEY (studentID) REFERENCES student(studentID) ON DELETE CASCADE, " +
				"UNIQUE KEY unique_assignment_student (assignmentID, studentID))";
		stmt.executeUpdate(sql);
		System.out.println("Assignment Enrollment table created/verified");
	}

	private static void createIndexes(Connection conn) throws SQLException {
		try (Statement stmt = conn.createStatement()) {
			// Student indexes
			createIndexIfNotExists(stmt, "idx_student_username", "student", "username");
			createIndexIfNotExists(stmt, "idx_student_email", "student", "email");

			// Course indexes
			createIndexIfNotExists(stmt, "idx_course_code", "course", "courseCode");
			createIndexIfNotExists(stmt, "idx_course_active", "course", "is_active");

			// Instructor indexes
			createIndexIfNotExists(stmt, "idx_instructor_username", "instructor", "username");
			createIndexIfNotExists(stmt, "idx_instructor_status", "instructor", "status");

			// Assignment indexes
			createIndexIfNotExists(stmt, "idx_assignment_course", "assignment", "courseID");
			createIndexIfNotExists(stmt, "idx_assignment_due_date", "assignment", "due_date");
			createIndexIfNotExists(stmt, "idx_assignment_type", "assignment", "assignment_type");

			// Enrollment indexes
			createIndexIfNotExists(stmt, "idx_enrollment_student", "enrollment", "studentID");
			createIndexIfNotExists(stmt, "idx_enrollment_course", "enrollment", "courseID");
			createIndexIfNotExists(stmt, "idx_enrollment_status", "enrollment", "status");
			createIndexIfNotExists(stmt, "idx_enrollment_date", "enrollment", "enrollment_date");

			// Grade indexes
			createIndexIfNotExists(stmt, "idx_grade_student", "grade", "studentID");
			createIndexIfNotExists(stmt, "idx_grade_assignment", "grade", "assignmentID");
			createIndexIfNotExists(stmt, "idx_grade_course", "grade", "courseID");
			createIndexIfNotExists(stmt, "idx_grade_letter", "grade", "letter_grade");

			// Assignment Enrollment indexes
			createIndexIfNotExists(stmt, "idx_ae_assignment", "assignment_enrollment", "assignmentID");
			createIndexIfNotExists(stmt, "idx_ae_student", "assignment_enrollment", "studentID");
			createIndexIfNotExists(stmt, "idx_ae_status", "assignment_enrollment", "status");

			System.out.println(" All indexes created/verified");
		}
	}

	// Helper method to check if index exists before creating it
	private static void createIndexIfNotExists(Statement stmt, String indexName, String tableName, String columnName) throws SQLException {
		try {
			// First check if the index exists by trying to use it in a query
			String checkSql = "SELECT 1 FROM " + tableName + " USE INDEX (" + indexName + ") LIMIT 1";
			stmt.executeQuery(checkSql);
			// If we get here, the index exists
			System.out.println(" Index " + indexName + " already exists");
		} catch (SQLException e) {
			// Index doesn't exist, so create it
			String createSql = "CREATE INDEX " + indexName + " ON " + tableName + "(" + columnName + ")";
			stmt.executeUpdate(createSql);
			System.out.println(" Created index: " + indexName);
		}
	}

	private static void insertDefaultData(Statement stmt) throws SQLException {
		// Insert default admin accounts
		// Insert default admin accounts
		try {
			stmt.executeUpdate(
					"INSERT IGNORE INTO admin (name, username, password) VALUES " + 
							"('MUKAMA Administrator', 'MUKAMA', 'UCYEYE')"
					);
			System.out.println(" Default admin accounts created");
		} catch (SQLException e) {
			System.err.println(" Warning: Could not insert admin accounts - " + e.getMessage());
		}

		// Insert sample students
		try {
			stmt.executeUpdate(
					"INSERT IGNORE INTO student (first_name, last_name, username, password, sex, birthdate, email) VALUES " +
							"('MUKAMA', 'REGINE', 'Regine', 'MUKAMA', 'F', '2005-04-01','regine@gmail.com'), " +
							"('SEZERANO', 'DELICE', 'Delice', 'sezerano123', 'F', '2001-08-22', 'delice@gmail.com'), " +
							"('TWIZEYIMANA', 'Onesphore', 'Onesphore', 'twizeyimana123', 'M', '1999-12-10', 'onesphore@gmail.com'), " +
							"('MASENGESHO', 'Pacifique', 'Pacifique', 'masengesho123', 'M', '2000-03-30', 'pacifique@gmail.com'), " +
							"('GUHIRWA', 'Divine', 'Divine', 'gihurwa123', 'F', '2002-07-14', 'gihurwa@gmail.com')"
					);
			System.out.println(" Sample students created");
		} catch (SQLException e) {
			System.err.println(" Warning: Could not insert sample students - " + e.getMessage());
		}

		// Insert sample courses
		try {
			stmt.executeUpdate(
					"INSERT IGNORE INTO course (courseName, courseCode, description, credits) VALUES " +
							"('Introduction to Programming', 'CS101', 'Fundamentals of programming and algorithms', 15), " +
							"('Data Structures', 'CS201', 'Advanced data structures and algorithms', 10), " +
							"('Calculus I', 'MATH101', 'Differential and integral calculus', 15), " +
							"('English Composition', 'ENG101', 'Academic writing and communication skills', 10), " +
							"('Database Systems', 'CS301', 'Relational database design and SQL', 20), " +
							"('Web Development', 'CS401', 'Modern web development technologies', 15)"
					);
			System.out.println(" Sample courses created");
		} catch (SQLException e) {
			System.err.println(" Warning: Could not insert sample courses - " + e.getMessage());
		}

		// Insert sample instructors with proper passwords for login
		try {
			stmt.executeUpdate(
					"INSERT IGNORE INTO instructor (name, identifier, status, department, email, username, password, qualifications, assignedSince) VALUES " +
							"('Dr. BUGINGO Emmanuel', 'INST001', 'ACTIVE', 'programming with java', 'bugingo@gmail.com', 'Emmanuel', 'bugingo123', 'PhD in Computer Science', '2024-01-15'), " +
							"('Dr. KAYIJUKA Iddrissa', 'INST002', 'ACTIVE', 'Mathematics', 'kayijuka@gmail.com', 'Iddrissa', 'kayijuka456', 'PhD in Mathematics', '2024-01-15'), " +
							"('Dr. BATAMURIZA Jenitha', 'INS003', 'ACTIVE', 'Computer Science', 'batamuriza@gmail.com', 'Jenitha', 'batamuriza123', 'PhD in Computer Science', '2024-01-10'), " +
							"('Dr. IRADUKUNDA Roger', 'INS004', 'ACTIVE', 'Mathematics', 'iradukunda@gmail.com', 'Roger', 'iradukunda123', 'PhD in Mathematics', '2024-01-10')"
					);
			System.out.println("  Sample instructors created with login credentials");
			System.out.println("   Instructor 1: Emmanuel /bugingo123 ");
			System.out.println("   Instructor 2:  Iddrissa/kayijuka456 ");
			System.out.println("   Instructor 3: Jenitha/ batamuriza123");
			System.out.println("   Instructor 4:Roger /iradukunda123 ");
		} catch (SQLException e) {
			System.err.println(" Warning: Could not insert sample instructors - " + e.getMessage());
		}

		// Insert sample enrollments
		try {
			stmt.executeUpdate(
					"INSERT IGNORE INTO enrollment (studentID, courseID, enrollment_date, status) VALUES " +
							"(1, 1, '2024-01-15', 'ACTIVE'), " +
							"(1, 3, '2024-01-15', 'ACTIVE'), " +
							"(2, 1, '2024-01-16', 'ACTIVE'), " +
							"(2, 2, '2024-01-16', 'ACTIVE'), " +
							"(3, 2, '2024-01-17', 'ACTIVE'), " +
							"(3, 4, '2024-01-17', 'COMPLETED'), " +
							"(4, 5, '2024-01-18', 'ACTIVE'), " +
							"(5, 6, '2024-01-18', 'ACTIVE'), " +
							"(1, 5, '2024-01-19', 'ACTIVE')"
					);
			System.out.println("Sample enrollments created");
		} catch (SQLException e) {
			System.err.println(" Warning: Could not insert sample enrollments - " + e.getMessage());
		}

		// Insert sample assignments
		try {
			stmt.executeUpdate(
					"INSERT IGNORE INTO assignment (courseID, title, description, due_date, max_score, assignment_type, is_published) VALUES " +
							"(1, 'Programming Basics', 'Write your first Java program', '2024-02-15', 100, 'PROJECT', TRUE), " +
							"(1, 'Variables Quiz', 'Test your knowledge of variables and data types', '2024-02-01', 50, 'QUIZ', TRUE), " +
							"(2, 'Linked List Implementation', 'Implement a linked list data structure', '2024-02-20', 100, 'PROJECT', TRUE), " +
							"(3, 'Derivatives Assignment', 'Calculate derivatives of various functions', '2024-02-10', 100, 'HOMEWORK', TRUE), " +
							"(4, 'Argumentative Essay', 'Write an essay on a contemporary issue', '2024-02-25', 100, 'ESSAY', TRUE)"
					);
			System.out.println("Sample assignments created");
		} catch (SQLException e) {
			System.err.println(" Warning: Could not insert sample assignments - " + e.getMessage());
		}

		// Insert sample grades
		try {
			stmt.executeUpdate(
					"INSERT IGNORE INTO grade (assignmentID, studentID, courseID, score, letter_grade, remarks, graded_by) VALUES " +
							"(1, 1, 1, 85.5, 'B+', 'Good work, but needs improvement in documentation', 1), " +
							"(1, 2, 1, 92.0, 'A-', 'Excellent implementation', 1), " +
							"(2, 1, 1, 45.0, 'A', 'Perfect score', 1), " +
							"(2, 2, 1, 42.5, 'B+', 'Minor mistakes in variable naming', 1), " +
							"(3, 3, 2, 78.0, 'C+', 'Basic implementation working', 2), " +
							"(4, 1, 3, 88.0, 'B+', 'Good understanding of derivatives', 2)"
					);
			System.out.println(" Sample grades created");
		} catch (SQLException e) {
			System.err.println(" Warning: Could not insert sample grades - " + e.getMessage());
		}

		System.out.println(" All sample data inserted successfully!");
	}

	// Method to verify database structure
	public static void verifyDatabaseStructure() {
		System.out.println("\n Verifying database structure...");

		try (Connection conn = DatabaseConnection.getConnection();
				Statement stmt = conn.createStatement()) {

			String[] tables = {
					"admin", "student", "course", "instructor", 
					"assignment", "enrollment", "grade", "assignment_enrollment"
			};

			for (String table : tables) {
				try {
					ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM " + table);
					if (rs.next()) {
						System.out.println(" " + table + " table: " + rs.getInt(1) + " records");
					}
				} catch (SQLException e) {
					System.err.println(" " + table + " table: Missing or empty");
				}
			}

		} catch (SQLException e) {
			System.err.println("Error verifying database structure: " + e.getMessage());
		}
	}

	// Method to reset database 
	public static void resetDatabase() {
		System.out.println("  RESETTING DATABASE - THIS WILL DELETE ALL DATA!");

		try (Connection conn = DatabaseConnection.getConnection();
				Statement stmt = conn.createStatement()) {

			// Drop tables in reverse order to handle foreign keys
			String[] tables = {
					"assignment_enrollment", "grade", "enrollment", 
					"assignment", "instructor", "course", "student", "admin"
			};

			for (String table : tables) {
				try {
					stmt.executeUpdate("DROP TABLE IF EXISTS " + table);
					System.out.println("  Dropped table: " + table);
				} catch (SQLException e) {
					System.err.println(" Error dropping table " + table + ": " + e.getMessage());
				}
			}

			System.out.println(" Database reset completed. Run initializeDatabase() to recreate tables.");

		} catch (SQLException e) {
			System.err.println(" Error resetting database: " + e.getMessage());
		}
	}

	public static void main(String[] args) {
		if (args.length > 0 && args[0].equals("reset")) {
			resetDatabase();
		} else if (args.length > 0 && args[0].equals("verify")) {
			verifyDatabaseStructure();
		} else {
			initializeDatabase();
			verifyDatabaseStructure();
		}
	}
}