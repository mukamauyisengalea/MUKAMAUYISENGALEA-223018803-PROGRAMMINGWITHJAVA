package util;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseInitializer {

	public static void initializeDatabase() {
		System.out.println("🚀 Starting comprehensive database initialization...");

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
			createIndexes(stmt);

			// Insert default data
			insertDefaultData(stmt);

			System.out.println("🎉 Database initialization completed successfully!");

		} catch (Exception e) {
			System.err.println("❌ Database initialization failed: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private static void createAdminTable(Statement stmt) throws SQLException {
		String sql = "CREATE TABLE IF NOT EXISTS admin (" +
				"id INT PRIMARY KEY AUTO_INCREMENT, " +
				"name VARCHAR(100) NOT NULL, " +
				"username VARCHAR(50) UNIQUE NOT NULL, " +
				"password VARCHAR(255) NOT NULL, " +
				"email VARCHAR(100), " +
				"phone VARCHAR(20), " +
				"created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
				"updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)";
		stmt.executeUpdate(sql);
		System.out.println("✅ Admin table created/verified");
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
		System.out.println("✅ Student table created/verified");
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
				"createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
				"updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)";
		stmt.executeUpdate(sql);
		System.out.println("✅ Course table created/verified");
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
				"username VARCHAR(50) UNIQUE NOT NULL, " +
				"password VARCHAR(255) NOT NULL, " +
				"qualifications TEXT, " +
				"assignedSince DATE, " +
				"courseID INT, " +
				"created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
				"updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)";
		stmt.executeUpdate(sql);
		System.out.println("✅ Instructor table created/verified");
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
		System.out.println("✅ Assignment table created/verified");
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
		System.out.println("✅ Enrollment table created/verified");
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
				"UNIQUE KEY unique_grade_entry (studentID, assignmentID), " +
				"CHECK (score >= 0 AND score <= 100))";
		stmt.executeUpdate(sql);
		System.out.println("✅ Grade table created/verified");
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
		System.out.println("✅ Assignment Enrollment table created/verified");
	}

	private static void createIndexes(Statement stmt) throws SQLException {
		// Student indexes
		stmt.executeUpdate("CREATE INDEX IF NOT EXISTS idx_student_username ON student(username)");
		stmt.executeUpdate("CREATE INDEX IF NOT EXISTS idx_student_email ON student(email)");

		// Course indexes
		stmt.executeUpdate("CREATE INDEX IF NOT EXISTS idx_course_code ON course(courseCode)");
		stmt.executeUpdate("CREATE INDEX IF NOT EXISTS idx_course_active ON course(is_active)");

		// Instructor indexes
		stmt.executeUpdate("CREATE INDEX IF NOT EXISTS idx_instructor_username ON instructor(username)");
		stmt.executeUpdate("CREATE INDEX IF NOT EXISTS idx_instructor_status ON instructor(status)");

		// Assignment indexes
		stmt.executeUpdate("CREATE INDEX IF NOT EXISTS idx_assignment_course ON assignment(courseID)");
		stmt.executeUpdate("CREATE INDEX IF NOT EXISTS idx_assignment_due_date ON assignment(due_date)");
		stmt.executeUpdate("CREATE INDEX IF NOT EXISTS idx_assignment_type ON assignment(assignment_type)");

		// Enrollment indexes
		stmt.executeUpdate("CREATE INDEX IF NOT EXISTS idx_enrollment_student ON enrollment(studentID)");
		stmt.executeUpdate("CREATE INDEX IF NOT EXISTS idx_enrollment_course ON enrollment(courseID)");
		stmt.executeUpdate("CREATE INDEX IF NOT EXISTS idx_enrollment_status ON enrollment(status)");
		stmt.executeUpdate("CREATE INDEX IF NOT EXISTS idx_enrollment_date ON enrollment(enrollment_date)");

		// Grade indexes
		stmt.executeUpdate("CREATE INDEX IF NOT EXISTS idx_grade_student ON grade(studentID)");
		stmt.executeUpdate("CREATE INDEX IF NOT EXISTS idx_grade_assignment ON grade(assignmentID)");
		stmt.executeUpdate("CREATE INDEX IF NOT EXISTS idx_grade_course ON grade(courseID)");
		stmt.executeUpdate("CREATE INDEX IF NOT EXISTS idx_grade_letter ON grade(letter_grade)");

		// Assignment Enrollment indexes
		stmt.executeUpdate("CREATE INDEX IF NOT EXISTS idx_ae_assignment ON assignment_enrollment(assignmentID)");
		stmt.executeUpdate("CREATE INDEX IF NOT EXISTS idx_ae_student ON assignment_enrollment(studentID)");
		stmt.executeUpdate("CREATE INDEX IF NOT EXISTS idx_ae_status ON assignment_enrollment(status)");

		System.out.println("✅ All indexes created/verified");
	}

	private static void insertDefaultData(Statement stmt) throws SQLException {
		// Insert default admin accounts
		stmt.executeUpdate(
				"INSERT IGNORE INTO admin (name, username, password, email) VALUES " +
						"('System Administrator', 'admin', 'admin123', 'admin@education.edu'), " +
						"('MUKAMA Administrator', 'MUKAMA', 'UCYEYE', 'mukama@education.edu')"
				);
		System.out.println("✅ Default admin accounts created");

		// Insert sample students
		stmt.executeUpdate(
				"INSERT IGNORE INTO student (first_name, last_name, username, password, sex, birthdate, email) VALUES " +
						"('John', 'Doe', 'john.doe', 'password123', 'M', '2000-05-15', 'john.doe@student.edu'), " +
						"('Jane', 'Smith', 'jane.smith', 'password123', 'F', '2001-08-22', 'jane.smith@student.edu'), " +
						"('Mike', 'Johnson', 'mike.johnson', 'password123', 'M', '1999-12-10', 'mike.johnson@student.edu'), " +
						"('Sarah', 'Wilson', 'sarah.wilson', 'password123', 'F', '2000-03-30', 'sarah.wilson@student.edu'), " +
						"('David', 'Brown', 'david.brown', 'password123', 'M', '2001-07-14', 'david.brown@student.edu')"
				);
		System.out.println("✅ Sample students created");

		// Insert sample courses
		stmt.executeUpdate(
				"INSERT IGNORE INTO course (courseName, courseCode, description, credits) VALUES " +
						"('Introduction to Programming', 'CS101', 'Fundamentals of programming and algorithms', 3), " +
						"('Data Structures', 'CS201', 'Advanced data structures and algorithms', 4), " +
						"('Calculus I', 'MATH101', 'Differential and integral calculus', 4), " +
						"('English Composition', 'ENG101', 'Academic writing and communication skills', 3), " +
						"('Database Systems', 'CS301', 'Relational database design and SQL', 3), " +
						"('Web Development', 'CS401', 'Modern web development technologies', 3)"
				);
		System.out.println("✅ Sample courses created");

		// Insert sample instructors with proper passwords for login
		stmt.executeUpdate(
				"INSERT IGNORE INTO instructor (name, identifier, status, department, email, username, password, qualifications, assignedSince) VALUES " +
						"('Dr. Sarah Johnson', 'INST001', 'ACTIVE', 'Computer Science', 'sarah.johnson@university.edu', 'sjohnson', 'instructor123', 'PhD in Computer Science', '2024-01-15'), " +
						"('Prof. Michael Chen', 'INST002', 'ACTIVE', 'Mathematics', 'michael.chen@university.edu', 'mchen', 'teach456', 'PhD in Mathematics', '2024-01-15'), " +
						"('Dr. Robert Smith', 'INS003', 'ACTIVE', 'Computer Science', 'r.smith@education.edu', 'dr.smith', 'password123', 'PhD in Computer Science', '2024-01-10'), " +
						"('Prof. Maria Garcia', 'INS004', 'ACTIVE', 'Mathematics', 'm.garcia@education.edu', 'prof.garcia', 'password123', 'PhD in Mathematics', '2024-01-10')"
				);
		System.out.println("✅ Sample instructors created with login credentials");
		System.out.println("   Instructor 1: sjohnson / instructor123");
		System.out.println("   Instructor 2: mchen / teach456");
		System.out.println("   Instructor 3: dr.smith / password123");
		System.out.println("   Instructor 4: prof.garcia / password123");

		// Insert sample enrollments
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
		System.out.println("✅ Sample enrollments created");

		// Insert sample assignments
		stmt.executeUpdate(
				"INSERT IGNORE INTO assignment (courseID, title, description, due_date, max_score, assignment_type, is_published) VALUES " +
						"(1, 'Programming Basics', 'Write your first Java program', '2024-02-15', 100, 'PROJECT', TRUE), " +
						"(1, 'Variables Quiz', 'Test your knowledge of variables and data types', '2024-02-01', 50, 'QUIZ', TRUE), " +
						"(2, 'Linked List Implementation', 'Implement a linked list data structure', '2024-02-20', 100, 'PROJECT', TRUE), " +
						"(3, 'Derivatives Assignment', 'Calculate derivatives of various functions', '2024-02-10', 100, 'HOMEWORK', TRUE), " +
						"(4, 'Argumentative Essay', 'Write an essay on a contemporary issue', '2024-02-25', 100, 'ESSAY', TRUE)"
				);
		System.out.println("✅ Sample assignments created");

		// Insert sample grades
		stmt.executeUpdate(
				"INSERT IGNORE INTO grade (assignmentID, studentID, courseID, score, letter_grade, remarks, graded_by) VALUES " +
						"(1, 1, 1, 85.5, 'B+', 'Good work, but needs improvement in documentation', 1), " +
						"(1, 2, 1, 92.0, 'A-', 'Excellent implementation', 1), " +
						"(2, 1, 1, 45.0, 'A', 'Perfect score', 1), " +
						"(2, 2, 1, 42.5, 'B+', 'Minor mistakes in variable naming', 1), " +
						"(3, 3, 2, 78.0, 'C+', 'Basic implementation working', 2), " +
						"(4, 1, 3, 88.0, 'B+', 'Good understanding of derivatives', 2)"
				);
		System.out.println("✅ Sample grades created");

		System.out.println("🎉 All sample data inserted successfully!");
	}

	// Method to verify database structure
	public static void verifyDatabaseStructure() {
		System.out.println("\n🔍 Verifying database structure...");

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
						System.out.println("✅ " + table + " table: " + rs.getInt(1) + " records");
					}
				} catch (SQLException e) {
					System.err.println("❌ " + table + " table: Missing or empty");
				}
			}

		} catch (SQLException e) {
			System.err.println("❌ Error verifying database structure: " + e.getMessage());
		}
	}

	// Method to reset database (use with caution!)
	public static void resetDatabase() {
		System.out.println("⚠️  RESETTING DATABASE - THIS WILL DELETE ALL DATA!");

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
					System.out.println("🗑️  Dropped table: " + table);
				} catch (SQLException e) {
					System.err.println("❌ Error dropping table " + table + ": " + e.getMessage());
				}
			}

			System.out.println("✅ Database reset completed. Run initializeDatabase() to recreate tables.");

		} catch (SQLException e) {
			System.err.println("❌ Error resetting database: " + e.getMessage());
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