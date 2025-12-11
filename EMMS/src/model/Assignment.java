package model;

import java.sql.Date;

public class Assignment {
	private int assignmentID;
	private int courseID;
	private String title;
	private String description;
	private Date dueDate;
	private double maxScore;
	private String assignmentType;
	private int createdBy;
	private Date createdAt;
	private String status;
	private String remarks;

	// Default Constructor
	public Assignment() {}

	// Constructor with all fields
	public Assignment(int assignmentID, int courseID, String title, String description,
			Date dueDate, double maxScore, String assignmentType, int createdBy,
			Date createdAt, String status, String remarks) {
		this.assignmentID = assignmentID;
		this.courseID = courseID;
		this.title = title;
		this.description = description;
		this.dueDate = dueDate;
		this.maxScore = maxScore;
		this.assignmentType = assignmentType;
		this.createdBy = createdBy;
		this.createdAt = createdAt;
		this.status = status;
		this.remarks = remarks;
	}

	// Constructor for new assignments (without ID and createdAt)
	public Assignment(int courseID, String title, String description, Date dueDate,
			double maxScore, String assignmentType, int createdBy, String status, String remarks) {
		this.courseID = courseID;
		this.title = title;
		this.description = description;
		this.dueDate = dueDate;
		this.maxScore = maxScore;
		this.assignmentType = assignmentType;
		this.createdBy = createdBy;
		this.status = status;
		this.remarks = remarks;
	}

	// Getters and Setters
	public int getAssignmentID() { return assignmentID; }
	public void setAssignmentID(int assignmentID) { this.assignmentID = assignmentID; }

	public int getCourseID() { return courseID; }
	public void setCourseID(int courseID) { this.courseID = courseID; }

	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }

	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }

	public Date getDueDate() { return dueDate; }
	public void setDueDate(Date dueDate) { this.dueDate = dueDate; }

	public double getMaxScore() { return maxScore; }
	public void setMaxScore(double maxScore) { this.maxScore = maxScore; }

	public String getAssignmentType() { return assignmentType; }
	public void setAssignmentType(String assignmentType) { this.assignmentType = assignmentType; }

	public int getCreatedBy() { return createdBy; }
	public void setCreatedBy(int createdBy) { this.createdBy = createdBy; }

	public Date getCreatedAt() { return createdAt; }
	public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

	public String getStatus() { return status; }
	public void setStatus(String status) { this.status = status; }

	public String getRemarks() { return remarks; }
	public void setRemarks(String remarks) { this.remarks = remarks; }

	@Override
	public String toString() {
		return "Assignment{" +
				"assignmentID=" + assignmentID +
				", courseID=" + courseID +
				", title='" + title + '\'' +
				", description='" + description + '\'' +
				", dueDate=" + dueDate +
				", maxScore=" + maxScore +
				", assignmentType='" + assignmentType + '\'' +
				", createdBy=" + createdBy +
				", createdAt=" + createdAt +
				", status='" + status + '\'' +
				", remarks='" + remarks + '\'' +
				'}';
	}
}