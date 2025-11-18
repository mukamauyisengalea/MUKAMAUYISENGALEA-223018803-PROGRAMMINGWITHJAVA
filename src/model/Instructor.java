package model;

import java.util.Date;

public class Instructor {
	private int instructorID;
	private String name;
	private String identifier;
	private String status;
	private String location;
	private String contact;
	private String username;
	private String password;
	private Date assignedSince;
	private int courseID;

	public Instructor() {}

	public Instructor(int instructorID, String name, String identifier, String status,
			String location, String contact, String username, String password,
			Date assignedSince, int courseID) {
		this.instructorID = instructorID;
		this.name = name;
		this.identifier = identifier;
		this.status = status;
		this.location = location;
		this.contact = contact;
		this.username = username;
		this.password = password;
		this.assignedSince = assignedSince;
		this.courseID = courseID;
	}

	// Getters and Setters
	public int getInstructorID() { return instructorID; }
	public void setInstructorID(int instructorID) { this.instructorID = instructorID; }

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public String getIdentifier() { return identifier; }
	public void setIdentifier(String identifier) { this.identifier = identifier; }

	public String getStatus() { return status; }
	public void setStatus(String status) { this.status = status; }

	public String getLocation() { return location; }
	public void setLocation(String location) { this.location = location; }

	public String getContact() { return contact; }
	public void setContact(String contact) { this.contact = contact; }

	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }

	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }

	public Date getAssignedSince() { return assignedSince; }
	public void setAssignedSince(Date assignedSince) { this.assignedSince = assignedSince; }

	public int getCourseID() { return courseID; }
	public void setCourseID(int courseID) { this.courseID = courseID; }
}