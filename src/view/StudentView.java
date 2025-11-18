package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.Student;

public class StudentView extends JFrame {
	private Student student;

	public StudentView(Student student) {
		this.student = student;
		setTitle("Student Portal - " + student.getFirstName() + " " + student.getLastName());
		setSize(700, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		initComponents();
	}

	private void initComponents() {
		JTabbedPane tabbedPane = new JTabbedPane();

		// My Courses Tab
		tabbedPane.addTab("My Courses", createCoursesPanel());

		// Grades Tab
		tabbedPane.addTab("Grades", createGradesPanel());

		// Assignments Tab
		tabbedPane.addTab("Assignments", createAssignmentsPanel());

		// Profile Tab
		tabbedPane.addTab("My Profile", createProfilePanel());

		add(tabbedPane, BorderLayout.CENTER);

		// Top panel with welcome message and logout
		JPanel topPanel = new JPanel(new BorderLayout());
		JLabel welcomeLabel = new JLabel("Welcome, " + student.getFirstName() + " " + student.getLastName() + "!");
		welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
		topPanel.add(welcomeLabel, BorderLayout.WEST);

		JButton logoutButton = new JButton("Logout");
		logoutButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new LoginForm().setVisible(true);
				dispose();
			}
		});
		topPanel.add(logoutButton, BorderLayout.EAST);

		add(topPanel, BorderLayout.NORTH);
	}

	private JPanel createCoursesPanel() {
		JPanel panel = new JPanel(new BorderLayout());

		// Course list header
		JLabel headerLabel = new JLabel("My Enrolled Courses", JLabel.CENTER);
		headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
		panel.add(headerLabel, BorderLayout.NORTH);

		// Sample course data - replace with actual database data
		String[] columns = {"Course Code", "Course Name", "Instructor", "Schedule"};
		Object[][] data = {
				{"CS101", "Introduction to Programming", "Dr. Smith", "Mon/Wed 9:00-10:30"},
				{"MATH201", "Calculus I", "Prof. Johnson", "Tue/Thu 11:00-12:30"},
				{"ENG101", "English Composition", "Dr. Williams", "Mon/Wed/Fri 14:00-15:00"}
		};

		JTable courseTable = new JTable(data, columns);
		JScrollPane scrollPane = new JScrollPane(courseTable);
		panel.add(scrollPane, BorderLayout.CENTER);

		return panel;
	}

	private JPanel createGradesPanel() {
		JPanel panel = new JPanel(new BorderLayout());

		JLabel headerLabel = new JLabel("My Grades and Progress", JLabel.CENTER);
		headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
		panel.add(headerLabel, BorderLayout.NORTH);

		// Sample grades data
		String[] columns = {"Course", "Assignment", "Grade", "Feedback"};
		Object[][] data = {
				{"CS101", "Assignment 1", "A", "Excellent work!"},
				{"CS101", "Midterm Exam", "B+", "Good understanding"},
				{"MATH201", "Quiz 1", "A-", "Well done"},
				{"ENG101", "Essay 1", "B", "Good arguments, needs better structure"}
		};

		JTable gradesTable = new JTable(data, columns);
		JScrollPane scrollPane = new JScrollPane(gradesTable);
		panel.add(scrollPane, BorderLayout.CENTER);

		// Summary panel
		JPanel summaryPanel = new JPanel(new GridLayout(1, 3));
		summaryPanel.add(new JLabel("GPA: 3.5", JLabel.CENTER));
		summaryPanel.add(new JLabel("Courses: 3", JLabel.CENTER));
		summaryPanel.add(new JLabel("Credits: 9", JLabel.CENTER));

		panel.add(summaryPanel, BorderLayout.SOUTH);

		return panel;
	}

	private JPanel createAssignmentsPanel() {
		JPanel panel = new JPanel(new BorderLayout());

		JLabel headerLabel = new JLabel("My Assignments", JLabel.CENTER);
		headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
		panel.add(headerLabel, BorderLayout.NORTH);

		// Sample assignments data
		String[] columns = {"Course", "Assignment", "Due Date", "Status", "Submission"};
		Object[][] data = {
				{"CS101", "Programming Project", "2024-12-15", "Pending", "Not Submitted"},
				{"MATH201", "Problem Set 5", "2024-12-10", "Completed", "Submitted"},
				{"ENG101", "Research Paper", "2024-12-20", "In Progress", "Draft Saved"}
		};

		JTable assignmentsTable = new JTable(data, columns);
		JScrollPane scrollPane = new JScrollPane(assignmentsTable);
		panel.add(scrollPane, BorderLayout.CENTER);

		// Buttons panel
		JPanel buttonPanel = new JPanel();
		JButton submitButton = new JButton("Submit Assignment");
		JButton viewButton = new JButton("View Details");

		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(StudentView.this, 
						"Assignment submission feature coming soon!");
			}
		});

		viewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(StudentView.this, 
						"Assignment details will be shown here!");
			}
		});

		buttonPanel.add(submitButton);
		buttonPanel.add(viewButton);
		panel.add(buttonPanel, BorderLayout.SOUTH);

		return panel;
	}

	private JPanel createProfilePanel() {
		JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		// Student information display
		panel.add(new JLabel("Student ID:"));
		panel.add(new JLabel(String.valueOf(student.getStudentID())));

		panel.add(new JLabel("First Name:"));
		panel.add(new JLabel(student.getFirstName()));

		panel.add(new JLabel("Last Name:"));
		panel.add(new JLabel(student.getLastName()));

		panel.add(new JLabel("Username:"));
		panel.add(new JLabel(student.getUsername()));

		panel.add(new JLabel("Gender:"));
		panel.add(new JLabel(student.getSex()));

		panel.add(new JLabel("Birthdate:"));
		panel.add(new JLabel(student.getBirthdate().toString()));

		panel.add(new JLabel("Enrollment Date:"));
		panel.add(new JLabel(student.getCreatedAt().toString()));

		// Edit profile button
		JButton editProfileButton = new JButton("Edit Profile");
		editProfileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(StudentView.this, 
						"Profile editing feature coming soon!");
			}
		});

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(editProfileButton);

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(panel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		return mainPanel;
	}
}