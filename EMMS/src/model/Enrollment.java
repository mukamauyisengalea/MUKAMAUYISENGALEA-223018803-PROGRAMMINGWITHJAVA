package model;

import java.util.Date;

public class Enrollment {
	private int enrollmentID;
	private int studentID;
	private int courseID;
	private Date enrollmentDate;
	private String status;

	// Additional fields for display purposes (from JOIN queries)
	private String studentName;
	private String courseName;
	private String courseCode;

	// Default constructor
	public Enrollment() {}

	// Constructor with basic fields
	public Enrollment(int enrollmentID, int studentID, int courseID, Date enrollmentDate, String status) {
		this.enrollmentID = enrollmentID;
		this.studentID = studentID;
		this.courseID = courseID;
		this.enrollmentDate = enrollmentDate;
		this.status = status;
	}

	// Constructor for creating new enrollment (without ID)
	public Enrollment(int studentID, int courseID, Date enrollmentDate, String status) {
		this.studentID = studentID;
		this.courseID = courseID;
		this.enrollmentDate = enrollmentDate;
		this.status = status;
	}

	// Full constructor with display fields
	public Enrollment(int enrollmentID, int studentID, int courseID, Date enrollmentDate, 
			String status, String studentName, String courseName, String courseCode) {
		this.enrollmentID = enrollmentID;
		this.studentID = studentID;
		this.courseID = courseID;
		this.enrollmentDate = enrollmentDate;
		this.status = status;
		this.studentName = studentName;
		this.courseName = courseName;
		this.courseCode = courseCode;
	}

	// Getters and Setters
	public int getEnrollmentID() { 
		return enrollmentID; 
	}
	public void setEnrollmentID(int enrollmentID) { 
		this.enrollmentID = enrollmentID; 
	}

	public int getStudentID() { 
		return studentID; 
	}
	public void setStudentID(int studentID) { 
		this.studentID = studentID; 
	}

	public int getCourseID() { 
		return courseID; 
	}
	public void setCourseID(int courseID) { 
		this.courseID = courseID; 
	}

	public Date getEnrollmentDate() { 
		return enrollmentDate; 
	}
	public void setEnrollmentDate(Date enrollmentDate) { 
		this.enrollmentDate = enrollmentDate; 
	}

	public String getStatus() { 
		return status; 
	}
	public void setStatus(String status) { 
		this.status = status; 
	}

	public String getStudentName() { 
		return studentName; 
	}
	public void setStudentName(String studentName) { 
		this.studentName = studentName; 
	}

	public String getCourseName() { 
		return courseName; 
	}
	public void setCourseName(String courseName) { 
		this.courseName = courseName; 
	}

	public String getCourseCode() { 
		return courseCode; 
	}
	public void setCourseCode(String courseCode) { 
		this.courseCode = courseCode; 
	}

	// Utility methods
	public boolean isActive() {
		return "ACTIVE".equalsIgnoreCase(status);
	}

	public boolean isCompleted() {
		return "COMPLETED".equalsIgnoreCase(status);
	}

	public boolean isWithdrawn() {
		return "WITHDRAWN".equalsIgnoreCase(status);
	}

	public String getFullCourseInfo() {
		if (courseName != null && courseCode != null) {
			return courseName + " (" + courseCode + ")";
		} else if (courseName != null) {
			return courseName;
		} else {
			return "Course " + courseID;
		}
	}

	public String getFullStudentInfo() {
		if (studentName != null) {
			return studentName + " (ID: " + studentID + ")";
		} else {
			return "Student " + studentID;
		}
	}

	// Validation methods
	public boolean isValid() {
		return studentID > 0 && courseID > 0 && enrollmentDate != null && status != null;
	}

	public static boolean isValidStatus(String status) {
		return status != null && 
				(status.equals("ACTIVE") || status.equals("INACTIVE") || 
						status.equals("COMPLETED") || status.equals("WITHDRAWN"));
	}

	// Status constants
	public static final String STATUS_ACTIVE = "ACTIVE";
	public static final String STATUS_INACTIVE = "INACTIVE";
	public static final String STATUS_COMPLETED = "COMPLETED";
	public static final String STATUS_WITHDRAWN = "WITHDRAWN";

	// toString method for debugging
	@Override
	public String toString() {
		return String.format(
				"Enrollment{enrollmentID=%d, studentID=%d, courseID=%d, enrollmentDate=%s, status='%s', studentName='%s', courseName='%s'}",
				enrollmentID, studentID, courseID, enrollmentDate, status, studentName, courseName
				);
	}

	// equals and hashCode methods
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Enrollment that = (Enrollment) obj;
		return enrollmentID == that.enrollmentID;
	}

	@Override
	public int hashCode() {
		return Integer.hashCode(enrollmentID);
	}

	// Builder pattern for easy object creation
	public static class Builder {
		private int enrollmentID;
		private int studentID;
		private int courseID;
		private Date enrollmentDate;
		private String status;
		private String studentName;
		private String courseName;
		private String courseCode;

		public Builder setEnrollmentID(int enrollmentID) {
			this.enrollmentID = enrollmentID;
			return this;
		}

		public Builder setStudentID(int studentID) {
			this.studentID = studentID;
			return this;
		}

		public Builder setCourseID(int courseID) {
			this.courseID = courseID;
			return this;
		}

		public Builder setEnrollmentDate(Date enrollmentDate) {
			this.enrollmentDate = enrollmentDate;
			return this;
		}

		public Builder setStatus(String status) {
			this.status = status;
			return this;
		}

		public Builder setStudentName(String studentName) {
			this.studentName = studentName;
			return this;
		}

		public Builder setCourseName(String courseName) {
			this.courseName = courseName;
			return this;
		}

		public Builder setCourseCode(String courseCode) {
			this.courseCode = courseCode;
			return this;
		}

		public Enrollment build() {
			Enrollment enrollment = new Enrollment();
			enrollment.enrollmentID = this.enrollmentID;
			enrollment.studentID = this.studentID;
			enrollment.courseID = this.courseID;
			enrollment.enrollmentDate = this.enrollmentDate;
			enrollment.status = this.status;
			enrollment.studentName = this.studentName;
			enrollment.courseName = this.courseName;
			enrollment.courseCode = this.courseCode;
			return enrollment;
		}
	}

	// Static factory methods
	public static Enrollment createActiveEnrollment(int studentID, int courseID) {
		Enrollment enrollment = new Enrollment();
		enrollment.studentID = studentID;
		enrollment.courseID = courseID;
		enrollment.enrollmentDate = new Date();
		enrollment.status = STATUS_ACTIVE;
		return enrollment;
	}

	public static Enrollment createEnrollment(int studentID, int courseID, String status) {
		Enrollment enrollment = new Enrollment();
		enrollment.studentID = studentID;
		enrollment.courseID = courseID;
		enrollment.enrollmentDate = new Date();
		enrollment.status = status;
		return enrollment;
	}

	// Test method
	public static void main(String[] args) {
		// Test the Enrollment class
		Enrollment enrollment1 = new Enrollment(1, 1001, 201, new Date(), "ACTIVE");
		System.out.println("Enrollment 1: " + enrollment1);

		// Using builder pattern
		Enrollment enrollment2 = new Enrollment.Builder()
		.setStudentID(1002)
		.setCourseID(202)
		.setEnrollmentDate(new Date())
		.setStatus("ACTIVE")
		.setStudentName("John Doe")
		.setCourseName("Mathematics")
		.setCourseCode("MATH101")
		.build();
		System.out.println("Enrollment 2: " + enrollment2);

		// Using static factory method
		Enrollment enrollment3 = Enrollment.createActiveEnrollment(1003, 203);
		System.out.println("Enrollment 3: " + enrollment3);

		// Test utility methods
		System.out.println("Is enrollment1 active? " + enrollment1.isActive());
		System.out.println("Is enrollment1 valid? " + enrollment1.isValid());
		System.out.println("Full course info: " + enrollment2.getFullCourseInfo());
	}
}