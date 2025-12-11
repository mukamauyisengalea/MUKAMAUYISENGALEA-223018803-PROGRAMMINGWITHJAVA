package model.dao;

import model.Grade;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GradeDAO {

	public boolean createTable() {
		String sql = "CREATE TABLE IF NOT EXISTS grade (" +
				"gradeID INT PRIMARY KEY AUTO_INCREMENT, " +
				"assignmentID INT NOT NULL, " +
				"studentID INT NOT NULL, " +
				"courseID INT NOT NULL, " +
				"score DECIMAL(5,2) NOT NULL, " +
				"letter_grade VARCHAR(5), " +
				"remarks TEXT, " +
				"graded_by INT, " +
				"created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
				"updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, " +
				"FOREIGN KEY (assignmentID) REFERENCES assignment(assignmentID) ON DELETE CASCADE, " +
				"FOREIGN KEY (studentID) REFERENCES student(studentID) ON DELETE CASCADE, " +
				"FOREIGN KEY (courseID) REFERENCES course(courseID) ON DELETE CASCADE, " +
				"FOREIGN KEY (graded_by) REFERENCES instructor(instructorID) ON DELETE SET NULL, " +
				"UNIQUE KEY unique_grade_entry (studentID, assignmentID), " +
				"CHECK (score >= 0 AND score <= 100))";

		try (Connection conn = DatabaseConnection.getConnection();
				Statement stmt = conn.createStatement()) {
			stmt.executeUpdate(sql);
			System.out.println(" Grade table created successfully!");
			return true;
		} catch (SQLException e) {
			System.err.println(" Error creating grade table: " + e.getMessage());
			return false;
		}
	}

	// CREATE - Add new grade
	public boolean addGrade(Grade grade) {
		String sql = "INSERT INTO grade (assignmentID, studentID, courseID, score, letter_grade, remarks, graded_by) VALUES (?, ?, ?, ?, ?, ?, ?)";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, grade.getAssignmentID());
			pstmt.setInt(2, grade.getStudentID());
			pstmt.setInt(3, grade.getCourseID());
			pstmt.setDouble(4, grade.getScore());
			pstmt.setString(5, grade.getLetterGrade());
			pstmt.setString(6, grade.getRemarks());
			pstmt.setInt(7, grade.getGradedBy());

			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0;

		} catch (SQLException e) {
			System.err.println(" Error adding grade: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// READ - Get grade by ID
	public Grade getGradeById(int gradeID) {
		String sql = "SELECT * FROM grade WHERE gradeID = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, gradeID);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return extractGradeFromResultSet(rs);
			}
		} catch (SQLException e) {
			System.err.println(" Error getting grade by ID: " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	// READ - Get all grades
	public List<Grade> getAllGrades() {
		List<Grade> grades = new ArrayList<>();
		String sql = "SELECT * FROM grade ORDER BY created_at DESC";

		try (Connection conn = DatabaseConnection.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {
				grades.add(extractGradeFromResultSet(rs));
			}
		} catch (SQLException e) {
			System.err.println(" Error getting all grades: " + e.getMessage());
			e.printStackTrace();
		}
		return grades;
	}

	// READ - Get grades by student ID
	public List<Grade> getGradesByStudent(int studentID) {
		List<Grade> grades = new ArrayList<>();
		String sql = "SELECT g.*, s.first_name, s.last_name, c.courseName, a.title as assignment_title " +
				"FROM grade g " +
				"JOIN student s ON g.studentID = s.studentID " +
				"JOIN course c ON g.courseID = c.courseID " +
				"JOIN assignment a ON g.assignmentID = a.assignmentID " +
				"WHERE g.studentID = ? ORDER BY g.created_at DESC";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, studentID);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				Grade grade = extractGradeFromResultSet(rs);
				// You can add additional info if needed
				grades.add(grade);
			}
		} catch (SQLException e) {
			System.err.println(" Error getting grades by student: " + e.getMessage());
			e.printStackTrace();
		}
		return grades;
	}

	// READ - Get grades by assignment ID
	public List<Grade> getGradesByAssignment(int assignmentID) {
		List<Grade> grades = new ArrayList<>();
		String sql = "SELECT g.*, s.first_name, s.last_name, c.courseName " +
				"FROM grade g " +
				"JOIN student s ON g.studentID = s.studentID " +
				"JOIN course c ON g.courseID = c.courseID " +
				"WHERE g.assignmentID = ? ORDER BY g.score DESC";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, assignmentID);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				grades.add(extractGradeFromResultSet(rs));
			}
		} catch (SQLException e) {
			System.err.println(" Error getting grades by assignment: " + e.getMessage());
			e.printStackTrace();
		}
		return grades;
	}

	// READ - Get grades by course ID
	public List<Grade> getGradesByCourse(int courseID) {
		List<Grade> grades = new ArrayList<>();
		String sql = "SELECT g.*, s.first_name, s.last_name, a.title as assignment_title " +
				"FROM grade g " +
				"JOIN student s ON g.studentID = s.studentID " +
				"JOIN assignment a ON g.assignmentID = a.assignmentID " +
				"WHERE g.courseID = ? ORDER BY g.studentID, g.assignmentID";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, courseID);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				grades.add(extractGradeFromResultSet(rs));
			}
		} catch (SQLException e) {
			System.err.println(" Error getting grades by course: " + e.getMessage());
			e.printStackTrace();
		}
		return grades;
	}

	// READ - Get grades by student and course
	public List<Grade> getGradesByStudentAndCourse(int studentID, int courseID) {
		List<Grade> grades = new ArrayList<>();
		String sql = "SELECT g.*, a.title as assignment_title, a.assignment_type " +
				"FROM grade g " +
				"JOIN assignment a ON g.assignmentID = a.assignmentID " +
				"WHERE g.studentID = ? AND g.courseID = ? ORDER BY a.due_date";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, studentID);
			pstmt.setInt(2, courseID);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				grades.add(extractGradeFromResultSet(rs));
			}
		} catch (SQLException e) {
			System.err.println(" Error getting grades by student and course: " + e.getMessage());
			e.printStackTrace();
		}
		return grades;
	}

	// UPDATE - Update grade
	public boolean updateGrade(Grade grade) {
		String sql = "UPDATE grade SET score = ?, letter_grade = ?, remarks = ?, graded_by = ? WHERE gradeID = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setDouble(1, grade.getScore());
			pstmt.setString(2, grade.getLetterGrade());
			pstmt.setString(3, grade.getRemarks());
			pstmt.setInt(4, grade.getGradedBy());
			pstmt.setInt(5, grade.getGradeID());

			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0;

		} catch (SQLException e) {
			System.err.println(" Error updating grade: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// UPDATE - Update grade score and auto-calculate letter grade
	public boolean updateGradeScore(int gradeID, double score, String remarks, int gradedBy) {
		String letterGrade = Grade.calculateLetterGrade(score);
		String sql = "UPDATE grade SET score = ?, letter_grade = ?, remarks = ?, graded_by = ? WHERE gradeID = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setDouble(1, score);
			pstmt.setString(2, letterGrade);
			pstmt.setString(3, remarks);
			pstmt.setInt(4, gradedBy);
			pstmt.setInt(5, gradeID);

			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0;

		} catch (SQLException e) {
			System.err.println(" Error updating grade score: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// DELETE - Delete grade
	public boolean deleteGrade(int gradeID) {
		String sql = "DELETE FROM grade WHERE gradeID = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, gradeID);
			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0;

		} catch (SQLException e) {
			System.err.println(" Error deleting grade: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// Check if grade exists for student and assignment
	public boolean gradeExists(int studentID, int assignmentID) {
		String sql = "SELECT COUNT(*) FROM grade WHERE studentID = ? AND assignmentID = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, studentID);
			pstmt.setInt(2, assignmentID);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return rs.getInt(1) > 0;
			}
		} catch (SQLException e) {
			System.err.println(" Error checking if grade exists: " + e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	// Get grade by student and assignment
	public Grade getGradeByStudentAndAssignment(int studentID, int assignmentID) {
		String sql = "SELECT * FROM grade WHERE studentID = ? AND assignmentID = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, studentID);
			pstmt.setInt(2, assignmentID);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return extractGradeFromResultSet(rs);
			}
		} catch (SQLException e) {
			System.err.println(" Error getting grade by student and assignment: " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	// Calculate student GPA
	public double calculateStudentGPA(int studentID) {
		String sql = "SELECT letter_grade FROM grade WHERE studentID = ?";
		double totalPoints = 0;
		int count = 0;

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, studentID);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				String letterGrade = rs.getString("letter_grade");
				if (letterGrade != null) {
					totalPoints += Grade.calculateGPAPoints(letterGrade);
					count++;
				}
			}

			return count > 0 ? totalPoints / count : 0.0;

		} catch (SQLException e) {
			System.err.println(" Error calculating GPA: " + e.getMessage());
			e.printStackTrace();
			return 0.0;
		}
	}

	// Calculate course average
	public double getCourseAverage(int courseID) {
		String sql = "SELECT AVG(score) as average_score FROM grade WHERE courseID = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, courseID);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return rs.getDouble("average_score");
			}
		} catch (SQLException e) {
			System.err.println(" Error getting course average: " + e.getMessage());
			e.printStackTrace();
		}
		return 0.0;
	}

	// Calculate assignment average
	public double getAssignmentAverage(int assignmentID) {
		String sql = "SELECT AVG(score) as average_score FROM grade WHERE assignmentID = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, assignmentID);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return rs.getDouble("average_score");
			}
		} catch (SQLException e) {
			System.err.println(" Error getting assignment average: " + e.getMessage());
			e.printStackTrace();
		}
		return 0.0;
	}

	// Get grade statistics for a course
	public GradeStatistics getCourseGradeStatistics(int courseID) {
		String sql = "SELECT " +
				"COUNT(*) as total_grades, " +
				"AVG(score) as average_score, " +
				"MIN(score) as min_score, " +
				"MAX(score) as max_score, " +
				"COUNT(CASE WHEN letter_grade = 'A' THEN 1 END) as a_count, " +
				"COUNT(CASE WHEN letter_grade = 'B' THEN 1 END) as b_count, " +
				"COUNT(CASE WHEN letter_grade = 'C' THEN 1 END) as c_count, " +
				"COUNT(CASE WHEN letter_grade = 'D' THEN 1 END) as d_count, " +
				"COUNT(CASE WHEN letter_grade = 'F' THEN 1 END) as f_count " +
				"FROM grade WHERE courseID = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, courseID);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return new GradeStatistics(
						rs.getInt("total_grades"),
						rs.getDouble("average_score"),
						rs.getDouble("min_score"),
						rs.getDouble("max_score"),
						rs.getInt("a_count"),
						rs.getInt("b_count"),
						rs.getInt("c_count"),
						rs.getInt("d_count"),
						rs.getInt("f_count")
						);
			}
		} catch (SQLException e) {
			System.err.println(" Error getting grade statistics: " + e.getMessage());
			e.printStackTrace();
		}
		return new GradeStatistics();
	}

	// Get total number of grades
	public int getTotalGrades() {
		String sql = "SELECT COUNT(*) FROM grade";

		try (Connection conn = DatabaseConnection.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			System.err.println(" Error getting total grades: " + e.getMessage());
			e.printStackTrace();
		}
		return 0;
	}

	// Helper method to extract Grade object from ResultSet
	private Grade extractGradeFromResultSet(ResultSet rs) throws SQLException {
		Grade grade = new Grade();
		grade.setGradeID(rs.getInt("gradeID"));
		grade.setAssignmentID(rs.getInt("assignmentID"));
		grade.setStudentID(rs.getInt("studentID"));
		grade.setCourseID(rs.getInt("courseID"));
		grade.setScore(rs.getDouble("score"));
		grade.setLetterGrade(rs.getString("letter_grade"));
		grade.setRemarks(rs.getString("remarks"));
		grade.setGradedBy(rs.getInt("graded_by"));
		grade.setCreatedAt(rs.getTimestamp("created_at"));
		grade.setUpdatedAt(rs.getTimestamp("updated_at"));
		return grade;
	}

	// Inner class for grade statistics
	public static class GradeStatistics {
		private int totalGrades;
		private double averageScore;
		private double minScore;
		private double maxScore;
		private int aCount;
		private int bCount;
		private int cCount;
		private int dCount;
		private int fCount;

		public GradeStatistics() {}

		public GradeStatistics(int totalGrades, double averageScore, double minScore, 
				double maxScore, int aCount, int bCount, int cCount, 
				int dCount, int fCount) {
			this.totalGrades = totalGrades;
			this.averageScore = averageScore;
			this.minScore = minScore;
			this.maxScore = maxScore;
			this.aCount = aCount;
			this.bCount = bCount;
			this.cCount = cCount;
			this.dCount = dCount;
			this.fCount = fCount;
		}

		// Getters
		public int getTotalGrades() { return totalGrades; }
		public double getAverageScore() { return averageScore; }
		public double getMinScore() { return minScore; }
		public double getMaxScore() { return maxScore; }
		public int getACount() { return aCount; }
		public int getBCount() { return bCount; }
		public int getCCount() { return cCount; }
		public int getDCount() { return dCount; }
		public int getFCount() { return fCount; }

		public double getAPercentage() { 
			return totalGrades > 0 ? (aCount * 100.0) / totalGrades : 0; 
		}
		public double getBPercentage() { 
			return totalGrades > 0 ? (bCount * 100.0) / totalGrades : 0; 
		}
		public double getCPercentage() { 
			return totalGrades > 0 ? (cCount * 100.0) / totalGrades : 0; 
		}
		public double getDPercentage() { 
			return totalGrades > 0 ? (dCount * 100.0) / totalGrades : 0; 
		}
		public double getFPercentage() { 
			return totalGrades > 0 ? (fCount * 100.0) / totalGrades : 0; 
		}
	}

	// Test method
	public static void main(String[] args) {
		GradeDAO gradeDAO = new GradeDAO();

		// Test table creation
		boolean tableCreated = gradeDAO.createTable();
		System.out.println("Table created: " + tableCreated);

		// Test getting all grades
		List<Grade> grades = gradeDAO.getAllGrades();
		System.out.println("Total grades in database: " + grades.size());

		// Test statistics
		int totalGrades = gradeDAO.getTotalGrades();
		System.out.println("Total grades count: " + totalGrades);
	}
}