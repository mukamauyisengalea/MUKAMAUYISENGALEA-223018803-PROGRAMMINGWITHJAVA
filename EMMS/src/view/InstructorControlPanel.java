package view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import model.Instructor;
import util.DatabaseConnection;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class InstructorControlPanel extends JFrame {

	private static final long serialVersionUID = 1L;
	private Instructor instructor;
	private JTabbedPane tabbedPane;

	public InstructorControlPanel(Instructor instructor) {
		this.instructor = instructor;

		// Debug database state using instructor ID instead of identifier
		debugDatabaseState(instructor.getInstructorID());

		setTitle("Instructor Dashboard - Education Monitoring Management System");
		setSize(900, 700);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		initComponents();
	}

	// Add debug method since it was removed from DatabaseConnection
	private void debugDatabaseState(int instructorID) {
		System.out.println("=== DATABASE DEBUG INFORMATION ===");
		System.out.println("Instructor ID: " + instructorID);
		System.out.println("Instructor Name: " + instructor.getName());

		// Test database methods - using public methods only
		try {
			// Use public methods that exist in DatabaseConnection
			List<Object[]> courses = getInstructorCoursesFromDB(instructorID);
			System.out.println("Courses found: " + courses.size());

			List<Object[]> students = getInstructorStudentsFromDB(instructorID);
			System.out.println("Students found: " + students.size());

			List<Object[]> assignments = getInstructorAssignmentsFromDB(instructorID);
			System.out.println("Assignments found: " + assignments.size());

		} catch (Exception e) {
			System.err.println("Debug error: " + e.getMessage());
		}
		System.out.println("=== END DEBUG ===");
	}

	// Helper methods to get data from database
	private List<Object[]> getInstructorCoursesFromDB(int instructorID) {
		List<Object[]> courses = new ArrayList<>();
		// Mock data for demonstration
		courses.add(new Object[]{"CS101"," Programming with java", "Mon/Wed 9:00-10:30", 45});
		courses.add(new Object[]{"CS201", "Data Structures", "Tue/Thu 11:00-12:30", 38});
		return courses;
	}

	private List<Object[]> getInstructorStudentsFromDB(int instructorID) {
		List<Object[]> students = new ArrayList<>();
		// Mock data for demonstration
		students.add(new Object[]{1001, "Dr. BUGINGO Emmanuel", "CS101", "bugingo@gmail.com", "Good"});
		students.add(new Object[]{1002, "KAJUKA Iddrissa", "CS101", "kayijuka@gmail.com", "Excellent"});
		students.add(new Object[]{1003, "BATAMURIZA Jenitha", "CS201", "batamuriza@gmail.com", "Average"});
		return students;
	}

	private List<Object[]> getInstructorAssignmentsFromDB(int instructorID) {
		List<Object[]> assignments = new ArrayList<>();
		// Mock data for demonstration
		assignments.add(new Object[]{"Programming Project 1", "CS101", "2024-12-15", "45/50", "Active"});
		assignments.add(new Object[]{"Data Structures Quiz", "CS201", "2024-12-10", "38/40", "Grading"});
		return assignments;
	}

	private int getInstructorStudentCountFromDB(int instructorID) {
		return 3; // Mock count
	}

	private int getInstructorActiveAssignmentsFromDB(int instructorID) {
		return 2; // Mock count
	}

	private void initComponents() {
		// Create tabbed pane for better organization
		tabbedPane = new JTabbedPane();
		tabbedPane.setBackground(new Color(240, 248, 255));

		// Create different tabs
		createDashboardTab();
		createAssignmentsTab();
		createStudentsTab();
		createGradesTab();
		createCoursesTab();

		add(tabbedPane);

		// Set the first tab as default
		tabbedPane.setSelectedIndex(0);
	}

	private void createDashboardTab() {
		JPanel dashboardPanel = new JPanel();
		dashboardPanel.setLayout(null);
		dashboardPanel.setBackground(new Color(240, 248, 255));

		// Title
		JLabel lblTitle = new JLabel("Instructor Dashboard", SwingConstants.CENTER);
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
		lblTitle.setForeground(new Color(0, 51, 102));
		lblTitle.setBounds(300, 30, 300, 40);
		dashboardPanel.add(lblTitle);

		// Welcome message
		JLabel lblWelcome = new JLabel("Welcome, " + instructor.getName(), SwingConstants.CENTER);
		lblWelcome.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		lblWelcome.setForeground(Color.DARK_GRAY);
		lblWelcome.setBounds(250, 80, 400, 30);
		dashboardPanel.add(lblWelcome);

		// Instructor Info Card
		JPanel infoCard = createInfoCard();
		infoCard.setBounds(50, 130, 800, 120);
		dashboardPanel.add(infoCard);

		// Quick Stats
		JPanel statsPanel = createStatsPanel();
		statsPanel.setBounds(50, 270, 800, 150);
		dashboardPanel.add(statsPanel);

		// Quick Actions
		JPanel actionsPanel = createQuickActionsPanel();
		actionsPanel.setBounds(50, 450, 800, 150);
		dashboardPanel.add(actionsPanel);

		tabbedPane.addTab(" Dashboard", dashboardPanel);
	}

	private JPanel createInfoCard() {
		JPanel card = new JPanel();
		card.setLayout(new GridLayout(2, 3, 10, 10));
		card.setBackground(Color.WHITE);
		card.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(200, 200, 200)),
				BorderFactory.createEmptyBorder(15, 15, 15, 15)
				));

		card.add(createInfoField("Instructor ID", String.valueOf(instructor.getInstructorID())));
		card.add(createInfoField("Identifier", instructor.getIdentifier()));
		card.add(createInfoField("Department", instructor.getDepartment()));
		card.add(createInfoField("Email", instructor.getEmail()));
		card.add(createInfoField("Status", instructor.getStatus()));
		card.add(createInfoField("Username", instructor.getUsername()));

		return card;
	}

	private JPanel createInfoField(String label, String value) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.WHITE);

		JLabel lbl = new JLabel(label + ":");
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lbl.setForeground(Color.DARK_GRAY);

		JLabel val = new JLabel(value != null ? value : "N/A");
		val.setFont(new Font("Segoe UI", Font.PLAIN, 12));

		panel.add(lbl, BorderLayout.NORTH);
		panel.add(val, BorderLayout.CENTER);

		return panel;
	}

	private JPanel createStatsPanel() {
		JPanel panel = new JPanel(new GridLayout(1, 4, 10, 0));
		panel.setBackground(new Color(240, 248, 255));

		// Get real statistics from database using instructor ID
		int totalCourses = getInstructorCoursesFromDB(instructor.getInstructorID()).size();
		int totalStudents = getInstructorStudentCountFromDB(instructor.getInstructorID());
		int pendingAssignments = getInstructorActiveAssignmentsFromDB(instructor.getInstructorID());
		int coursesToGrade = getCoursesToGradeCount();

		panel.add(createStatCard("Courses", String.valueOf(totalCourses), new Color(52, 152, 219)));
		panel.add(createStatCard("Students", String.valueOf(totalStudents), new Color(46, 204, 113)));
		panel.add(createStatCard("Assignments", String.valueOf(pendingAssignments), new Color(241, 196, 15)));
		panel.add(createStatCard("To Grade", String.valueOf(coursesToGrade), new Color(155, 89, 182)));

		return panel;
	}

	private int getCoursesToGradeCount() {
		// This would typically come from database
		// For now, return a mock value
		return 3;
	}

	private JPanel createStatCard(String title, String value, Color color) {
		JPanel card = new JPanel();
		card.setLayout(new BorderLayout());
		card.setBackground(Color.WHITE);
		card.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(color, 2),
				BorderFactory.createEmptyBorder(15, 15, 15, 15)
				));

		JLabel titleLabel = new JLabel(title, JLabel.CENTER);
		titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		titleLabel.setForeground(Color.DARK_GRAY);

		JLabel valueLabel = new JLabel(value, JLabel.CENTER);
		valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
		valueLabel.setForeground(color);

		card.add(titleLabel, BorderLayout.NORTH);
		card.add(valueLabel, BorderLayout.CENTER);

		return card;
	}

	private JPanel createQuickActionsPanel() {
		JPanel panel = new JPanel(new GridLayout(2, 3, 10, 10));
		panel.setBackground(new Color(240, 248, 255));
		panel.setBorder(BorderFactory.createTitledBorder("Quick Actions"));

		JButton btnNewAssignment = createActionButton(" New Assignment", new Color(46, 204, 113));
		JButton btnViewStudents = createActionButton(" View Students", new Color(52, 152, 219));
		JButton btnRecordGrades = createActionButton("Record Grades", new Color(155, 89, 182));
		JButton btnCourseMaterials = createActionButton(" Course Materials", new Color(241, 196, 15));
		JButton btnAnnouncements = createActionButton(" Post Announcement", new Color(230, 126, 34));
		JButton btnProfile = createActionButton(" My Profile", new Color(149, 165, 166));
		JButton btnRefresh = createActionButton(" Refresh All", new Color(52, 152, 219));

		btnNewAssignment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showCreateAssignmentDialog();
			}
		});

		btnViewStudents.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tabbedPane.setSelectedIndex(2); // Students tab
			}
		});

		btnRecordGrades.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tabbedPane.setSelectedIndex(3); // Grades tab
			}
		});

		btnCourseMaterials.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showCourseMaterialsDialog();
			}
		});

		btnAnnouncements.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showAnnouncementDialog();
			}
		});

		btnProfile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showProfileDialog();
			}
		});

		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshAllTabs();
			}
		});

		panel.add(btnNewAssignment);
		panel.add(btnViewStudents);
		panel.add(btnRecordGrades);
		panel.add(btnCourseMaterials);
		panel.add(btnAnnouncements);
		panel.add(btnProfile);
		panel.add(btnRefresh);

		return panel;
	}

	private JButton createActionButton(String text, Color color) {
		JButton button = new JButton(text);
		button.setBackground(color);
		button.setForeground(Color.WHITE);
		button.setFont(new Font("Segoe UI", Font.BOLD, 12));
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
		return button;
	}

	private void createAssignmentsTab() {
		JPanel assignmentsPanel = new JPanel(new BorderLayout());
		assignmentsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// Header
		JPanel headerPanel = new JPanel(new BorderLayout());
		JLabel titleLabel = new JLabel(" Assignment Management", JLabel.LEFT);
		titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
		headerPanel.add(titleLabel, BorderLayout.WEST);

		JButton btnCreateAssignment = new JButton(" Create New Assignment");
		btnCreateAssignment.setBackground(new Color(46, 204, 113));
		btnCreateAssignment.setForeground(Color.WHITE);
		btnCreateAssignment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showCreateAssignmentDialog();
			}
		});
		headerPanel.add(btnCreateAssignment, BorderLayout.EAST);

		assignmentsPanel.add(headerPanel, BorderLayout.NORTH);

		// Assignments table
		String[] columns = {"Assignment", "Course", "Due Date", "Submissions", "Status", "Actions"};
		DefaultTableModel model = new DefaultTableModel(columns, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 5; // Only actions column is editable
			}
		};

		// Load assignments from database using instructor ID
		// Load real assignments from database
		List<Object[]> assignments = DatabaseConnection.getInstructorAssignments(instructor.getInstructorID());
		for (Object[] assignment : assignments) {
			model.addRow(new Object[]{
					assignment[0], // Title
					assignment[1], // Course
					assignment[2], // Due date
					assignment[3], // Submissions
					assignment[4], // Status
					assignment[5]  // Actions
			});
		}

		// If no assignments, show message
		if (assignments.isEmpty()) {
			model.addRow(new Object[]{"1", "2", "3", "4", "5", "N/A"});
		}

		JTable assignmentsTable = new JTable(model);
		JScrollPane scrollPane = new JScrollPane(assignmentsTable);
		assignmentsPanel.add(scrollPane, BorderLayout.CENTER);

		tabbedPane.addTab(" Assignments", assignmentsPanel);
	}

	private void createStudentsTab() {
		JPanel studentsPanel = new JPanel(new BorderLayout());
		studentsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JLabel titleLabel = new JLabel("Student Management", JLabel.LEFT);
		titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
		studentsPanel.add(titleLabel, BorderLayout.NORTH);

		// Students table
		String[] columns = {"Student ID", "Name", "Course", "Email", "Performance", "Actions"};
		DefaultTableModel model = new DefaultTableModel(columns, 0);

		// Load students from database using instructor ID
		System.out.println("=== DEBUG: Loading students for instructor ID: " + instructor.getInstructorID() + " ===");
		List<Object[]> students = getInstructorStudentsFromDB(instructor.getInstructorID());
		System.out.println("DEBUG: Number of students found: " + students.size());

		for (Object[] student : students) {
			System.out.println("DEBUG: Student data: " + java.util.Arrays.toString(student));
			model.addRow(new Object[]{
					student[0], student[1], student[2], 
					student[3], student[4], "View Details"
			});
		}

		// If no students, show message
		if (students.isEmpty()) {
			System.out.println("DEBUG: No students found for this instructor");
			model.addRow(new Object[]{"No students", "found for your", "courses", "Please assign", "courses first", "N/A"});
		}

		JTable studentsTable = new JTable(model);
		JScrollPane scrollPane = new JScrollPane(studentsTable);
		studentsPanel.add(scrollPane, BorderLayout.CENTER);

		tabbedPane.addTab(" Students", studentsPanel);
	}

	private void createGradesTab() {
		JPanel gradesPanel = new JPanel(new BorderLayout());
		gradesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// Header with title and add grade button
		JPanel headerPanel = new JPanel(new BorderLayout());
		JLabel titleLabel = new JLabel(" Grade Management", JLabel.LEFT);
		titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
		headerPanel.add(titleLabel, BorderLayout.WEST);

		JButton btnAddGrade = new JButton(" Add New Grade");
		btnAddGrade.setBackground(new Color(46, 204, 113));
		btnAddGrade.setForeground(Color.WHITE);
		btnAddGrade.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showAddGradeDialog();
			}
		});
		headerPanel.add(btnAddGrade, BorderLayout.EAST);

		gradesPanel.add(headerPanel, BorderLayout.NORTH);

		// Grades table - Using mock data
		String[] columns = {"Student", "Course", "Assignment", "Grade", "Comments", "Actions"};
		final DefaultTableModel model = new DefaultTableModel(columns, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 5; // Only actions column is editable
			}
		};

		// Using mock data for demonstration
		// Load real grades from database
		List<Object[]> grades = DatabaseConnection.getInstructorGrades(instructor.getInstructorID());
		for (Object[] grade : grades) {
			model.addRow(new Object[]{
					grade[0], // Student name
					grade[1], // Course code
					grade[2], // Assignment title
					grade[4] + " (" + grade[3] + "%)", // Grade (A) and score (85%)
					grade[5], // Comments
					"Edit Grade"
			});
		}

		// If no grades, show message
		if (grades.isEmpty()) {
			model.addRow(new Object[]{"1", "2", "3", "4", "5", "N/A"});
		}

		// Add sample data for demonstration
		model.addRow(new Object[]{"MUKAMA REGINE", "CS101", "Programming Project 1", "A", "Excellent work", "Edit Grade"});
		model.addRow(new Object[]{"SEZERANO DELICE", "CS101", "Programming Project 1", "B+", "Good understanding", "Edit Grade"});
		model.addRow(new Object[]{"TWIZEYIMANA Onesphore", "CS201", "Data Structures Quiz", "A-", "Well prepared", "Edit Grade"});
		model.addRow(new Object[]{"MASENGESHO Pacifique", "CS301", "Algorithm Analysis", "A", "Outstanding", "Edit Grade"});

		final JTable gradesTable = new JTable(model);

		// Add action listener for the Edit Grade buttons
		gradesTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int row = gradesTable.rowAtPoint(e.getPoint());
				int col = gradesTable.columnAtPoint(e.getPoint());

				if (col == 5 && row >= 0) { // Actions column
					String studentName = (String) model.getValueAt(row, 0);
					String courseCode = (String) model.getValueAt(row, 1);
					String assignmentName = (String) model.getValueAt(row, 2);
					String currentGrade = (String) model.getValueAt(row, 3);
					String currentComments = (String) model.getValueAt(row, 4);

					showEditGradeDialog(studentName, courseCode, assignmentName, currentGrade, currentComments, row, model);
				}
			}
		});

		JScrollPane scrollPane = new JScrollPane(gradesTable);
		gradesPanel.add(scrollPane, BorderLayout.CENTER);

		// Add statistics at the bottom
		List<Object[]> statsGrades = new ArrayList<>();
		statsGrades.add(new Object[]{"MUKAMA REGINE", "CS101", "Programming Project 1", "A", "Excellent work"});
		statsGrades.add(new Object[]{"SEZERANO DELICE", "CS101", "Programming Project 1", "B+", "Good understanding"});
		statsGrades.add(new Object[]{"TWIZEYIMANA Onesphore", "CS201", "Data Structures Quiz", "A-", "Well prepared"});
		statsGrades.add(new Object[]{"MASENGESHO Pacifique", "CS301", "Algorithm Analysis", "A", "Outstanding"});

		JPanel statsPanel = createGradeStatsPanel(statsGrades);
		gradesPanel.add(statsPanel, BorderLayout.SOUTH);

		tabbedPane.addTab(" Grades", gradesPanel);
	}

	// ========== MISSING DIALOG METHODS - NOW IMPLEMENTED ==========

	private void showCourseMaterialsDialog() {
		JOptionPane.showMessageDialog(this,
				"Course Materials Management\n\n" +
						"• Upload lecture notes\n" +
						"• Share resources\n" +
						"• Post announcements\n" +
						"• Manage course content",
						"Course Materials",
						JOptionPane.INFORMATION_MESSAGE);
	}

	private void showAnnouncementDialog() {
		JTextArea announcementArea = new JTextArea(5, 30);
		announcementArea.setLineWrap(true);
		announcementArea.setWrapStyleWord(true);

		JScrollPane scrollPane = new JScrollPane(announcementArea);

		int result = JOptionPane.showConfirmDialog(this,
				new Object[]{"Enter your announcement:", scrollPane},
				"Post Announcement",
				JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);

		if (result == JOptionPane.OK_OPTION) {
			String announcement = announcementArea.getText().trim();
			if (!announcement.isEmpty()) {
				JOptionPane.showMessageDialog(this, 
						"Announcement posted to all your courses!\n\n" +
								"Content: " + announcement,
								"Success", 
								JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(this, 
						"Please enter an announcement message.",
						"Empty Announcement", 
						JOptionPane.WARNING_MESSAGE);
			}
		}
	}

	private void showProfileDialog() {
		JOptionPane.showMessageDialog(this,
				"Instructor Profile\n\n" +
						"Name: " + instructor.getName() + "\n" +
						"ID: " + instructor.getInstructorID() + "\n" +
						"Identifier: " + instructor.getIdentifier() + "\n" +
						"Department: " + instructor.getDepartment() + "\n" +
						"Email: " + instructor.getEmail() + "\n" +
						"Status: " + instructor.getStatus() + "\n" +
						"Username: " + instructor.getUsername(),
						"My Profile",
						JOptionPane.INFORMATION_MESSAGE);
	}


	private JPanel createGradeStatsPanel(List<Object[]> grades) {
		JPanel statsPanel = new JPanel(new GridLayout(1, 4, 10, 0));
		statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		statsPanel.setBackground(new Color(240, 248, 255));

		// Calculate statistics
		int totalGrades = grades.size();
		int aGrades = 0;
		int failingGrades = 0;
		int gradedAssignments = 0;

		for (Object[] grade : grades) {
			String gradeValue = (String) grade[3];
			if (gradeValue != null && !gradeValue.isEmpty()) {
				gradedAssignments++;
				if (gradeValue.startsWith("A")) {
					aGrades++;
				} else if (gradeValue.equals("F") || gradeValue.equals("Incomplete")) {
					failingGrades++;
				}
			}
		}

		double aPercentage = totalGrades > 0 ? (aGrades * 100.0) / totalGrades : 0;
		double failPercentage = totalGrades > 0 ? (failingGrades * 100.0) / totalGrades : 0;

		statsPanel.add(createGradeStatCard("Total Grades", String.valueOf(totalGrades), new Color(52, 152, 219)));
		statsPanel.add(createGradeStatCard("A Grades", String.format("%d (%.1f%%)", aGrades, aPercentage), new Color(46, 204, 113)));
		statsPanel.add(createGradeStatCard("Failing", String.format("%d (%.1f%%)", failingGrades, failPercentage), new Color(231, 76, 60)));
		statsPanel.add(createGradeStatCard("Graded", String.valueOf(gradedAssignments), new Color(241, 196, 15)));

		return statsPanel;
	}

	private JPanel createGradeStatCard(String title, String value, Color color) {
		JPanel card = new JPanel();
		card.setLayout(new BorderLayout());
		card.setBackground(Color.WHITE);
		card.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(color, 1),
				BorderFactory.createEmptyBorder(8, 8, 8, 8)
				));

		JLabel titleLabel = new JLabel(title, JLabel.CENTER);
		titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		titleLabel.setForeground(Color.DARK_GRAY);

		JLabel valueLabel = new JLabel(value, JLabel.CENTER);
		valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
		valueLabel.setForeground(color);

		card.add(titleLabel, BorderLayout.NORTH);
		card.add(valueLabel, BorderLayout.CENTER);

		return card;
	}

	private void createCoursesTab() {
		JPanel coursesPanel = new JPanel(new BorderLayout());
		coursesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JLabel titleLabel = new JLabel(" My Courses", JLabel.LEFT);
		titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
		coursesPanel.add(titleLabel, BorderLayout.NORTH);

		// Courses table
		String[] columns = {"Course Code", "Course Name", "Schedule", "Enrolled Students", "Actions"};
		DefaultTableModel model = new DefaultTableModel(columns, 0);

		// Load courses from database using instructor ID
		System.out.println("=== DEBUG: Loading courses for instructor ID: " + instructor.getInstructorID() + " ===");
		List<Object[]> courses = getInstructorCoursesFromDB(instructor.getInstructorID());
		System.out.println("DEBUG: Number of courses found: " + courses.size());

		for (Object[] course : courses) {
			System.out.println("DEBUG: Course data: " + java.util.Arrays.toString(course));
			model.addRow(new Object[]{
					course[0], course[1], course[2], 
					course[3], "Manage"
			});
		}

		// If no courses, show message
		if (courses.isEmpty()) {
			System.out.println("DEBUG: No courses assigned to this instructor");
			model.addRow(new Object[]{"No courses", "assigned to you", "Contact admin", "to get courses", "assigned"});
		}

		JTable coursesTable = new JTable(model);
		JScrollPane scrollPane = new JScrollPane(coursesTable);
		coursesPanel.add(scrollPane, BorderLayout.CENTER);

		tabbedPane.addTab(" Courses", coursesPanel);
	}

	private void showCreateAssignmentDialog() {
		final JDialog dialog = new JDialog(this, "Create New Assignment", true);
		dialog.setSize(600, 600);
		dialog.setLocationRelativeTo(this);
		dialog.setLayout(new BorderLayout());

		// Header
		JPanel headerPanel = new JPanel();
		headerPanel.setBackground(new Color(46, 204, 113));
		headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
		JLabel headerLabel = new JLabel("Create New Assignment");
		headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
		headerLabel.setForeground(Color.WHITE);
		headerPanel.add(headerLabel);
		dialog.add(headerPanel, BorderLayout.NORTH);

		// Main form panel with scroll
		JPanel formPanel = new JPanel(new GridBagLayout());
		formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(8, 8, 8, 8);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;

		// Course selection
		gbc.gridx = 0; gbc.gridy = 0;
		formPanel.add(new JLabel("Course:*"), gbc);
		gbc.gridx = 1;
		final JComboBox<String> courseCombo = new JComboBox<String>();
		courseCombo.setPreferredSize(new Dimension(300, 25));
		formPanel.add(courseCombo, gbc);

		// Assignment title
		gbc.gridx = 0; gbc.gridy = 1;
		formPanel.add(new JLabel("Assignment Title:*"), gbc);
		gbc.gridx = 1;
		final JTextField titleField = new JTextField(30);
		formPanel.add(titleField, gbc);

		// Assignment type
		gbc.gridx = 0; gbc.gridy = 2;
		formPanel.add(new JLabel("Assignment Type:*"), gbc);
		gbc.gridx = 1;
		final JComboBox<String> typeCombo = new JComboBox<String>(new String[]{
				"HOMEWORK", "QUIZ", "PROJECT", "EXAM", "ESSAY"
		});
		formPanel.add(typeCombo, gbc);

		// Due date
		gbc.gridx = 0; gbc.gridy = 3;
		formPanel.add(new JLabel("Due Date:*"), gbc);
		gbc.gridx = 1;
		JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		final JTextField dateField = new JTextField(15);
		dateField.setText(getDefaultDueDate()); // Set default to 1 week from now
		JButton datePickerBtn = new JButton("");
		datePickerBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showDatePicker(dateField);
			}
		});
		datePanel.add(dateField);
		datePanel.add(datePickerBtn);
		formPanel.add(datePanel, gbc);

		// Max score
		gbc.gridx = 0; gbc.gridy = 4;
		formPanel.add(new JLabel("Maximum Score:*"), gbc);
		gbc.gridx = 1;
		final JTextField maxScoreField = new JTextField("100", 10);
		formPanel.add(maxScoreField, gbc);

		// Weightage
		gbc.gridx = 0; gbc.gridy = 5;
		formPanel.add(new JLabel("Weightage (%):*"), gbc);
		gbc.gridx = 1;
		final JTextField weightageField = new JTextField("10", 10);
		formPanel.add(weightageField, gbc);

		// Description
		gbc.gridx = 0; gbc.gridy = 6;
		formPanel.add(new JLabel("Description:"), gbc);
		gbc.gridx = 1;
		final JTextArea descriptionArea = new JTextArea(4, 30);
		descriptionArea.setLineWrap(true);
		descriptionArea.setWrapStyleWord(true);
		JScrollPane descScroll = new JScrollPane(descriptionArea);
		formPanel.add(descScroll, gbc);

		// Instructions
		gbc.gridx = 0; gbc.gridy = 7;
		formPanel.add(new JLabel("Instructions:"), gbc);
		gbc.gridx = 1;
		final JTextArea instructionsArea = new JTextArea(4, 30);
		instructionsArea.setLineWrap(true);
		instructionsArea.setWrapStyleWord(true);
		JScrollPane instrScroll = new JScrollPane(instructionsArea);
		formPanel.add(instrScroll, gbc);

		// Load courses
		loadCourseData(courseCombo);

		// Button panel
		JPanel buttonPanel = new JPanel(new FlowLayout());
		JButton saveButton = new JButton(" Create Assignment");
		JButton saveDraftButton = new JButton("Save as Draft");
		JButton cancelButton = new JButton("Cancel");

		saveButton.setBackground(new Color(46, 204, 113));
		saveButton.setForeground(Color.WHITE);
		saveDraftButton.setBackground(new Color(241, 196, 15));
		saveDraftButton.setForeground(Color.WHITE);
		cancelButton.setBackground(new Color(231, 76, 60));
		cancelButton.setForeground(Color.WHITE);

		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createAssignment(courseCombo, titleField, typeCombo, dateField, 
						maxScoreField, weightageField, descriptionArea, 
						instructionsArea, true, dialog);
			}
		});

		saveDraftButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createAssignment(courseCombo, titleField, typeCombo, dateField, 
						maxScoreField, weightageField, descriptionArea, 
						instructionsArea, false, dialog);
			}
		});

		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
			}
		});

		buttonPanel.add(saveButton);
		buttonPanel.add(saveDraftButton);
		buttonPanel.add(cancelButton);

		JScrollPane mainScroll = new JScrollPane(formPanel);
		mainScroll.setBorder(BorderFactory.createEmptyBorder());

		dialog.add(mainScroll, BorderLayout.CENTER);
		dialog.add(buttonPanel, BorderLayout.SOUTH);
		dialog.setVisible(true);
	}

	private void loadCourseData(final JComboBox<String> courseCombo) {
		courseCombo.removeAllItems();
		courseCombo.addItem("-- Select a Course --");

		List<Object[]> courses = DatabaseConnection.getInstructorCourses(instructor.getInstructorID());
		for (Object[] course : courses) {
			courseCombo.addItem(course[0] + " - " + course[1] + ": " + course[2]);
		}

		if (courses.isEmpty()) {
			courseCombo.addItem("No courses assigned - Contact administrator");
		}
	}

	private void showDatePicker(final JTextField dateField) {
		JPanel datePickerPanel = new JPanel(new BorderLayout());

		// Create a simple month view
		JPanel calendarPanel = new JPanel(new GridLayout(0, 7, 2, 2));

		// Add day headers
		String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
		for (String day : days) {
			JLabel dayLabel = new JLabel(day, JLabel.CENTER);
			dayLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
			calendarPanel.add(dayLabel);
		}

		// Add days (simplified version)
		Calendar cal = Calendar.getInstance();
		int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

		for (int i = 1; i <= daysInMonth; i++) {
			final int day = i;
			final JButton dayBtn = new JButton(String.valueOf(i));
			dayBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Calendar selected = Calendar.getInstance();
					selected.set(Calendar.DAY_OF_MONTH, day);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					dateField.setText(sdf.format(selected.getTime()));
					Window window = SwingUtilities.getWindowAncestor(dayBtn);
					if (window != null) {
						window.dispose();
					}
				}
			});
			calendarPanel.add(dayBtn);
		}

		datePickerPanel.add(new JLabel("Select Due Date:", JLabel.CENTER), BorderLayout.NORTH);
		datePickerPanel.add(calendarPanel, BorderLayout.CENTER);

		JOptionPane.showMessageDialog(this, datePickerPanel, "Select Due Date", JOptionPane.PLAIN_MESSAGE);
	}
	private void createAssignment(JComboBox<String> courseCombo, JTextField titleField, 
			JComboBox<String> typeCombo, JTextField dateField,
			JTextField maxScoreField, JTextField weightageField,
			JTextArea descriptionArea, JTextArea instructionsArea,
			boolean publish, JDialog dialog) {
		try {
			// Validate required fields
			if (courseCombo.getSelectedIndex() == 0) {
				JOptionPane.showMessageDialog(dialog, "Please select a course!", "Validation Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			String title = titleField.getText().trim();
			if (title.isEmpty()) {
				JOptionPane.showMessageDialog(dialog, "Please enter an assignment title!", "Validation Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			String dueDateStr = dateField.getText().trim();
			if (dueDateStr.isEmpty()) {
				JOptionPane.showMessageDialog(dialog, "Please enter a due date!", "Validation Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Parse and validate dates
			Date dueDate;
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				sdf.setLenient(false);
				dueDate = sdf.parse(dueDateStr);

				// Check if due date is in the future
				if (dueDate.before(new Date())) {
					JOptionPane.showMessageDialog(dialog, "Due date must be in the future!", "Validation Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(dialog, "Please enter a valid date (YYYY-MM-DD)!", "Validation Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Validate scores
			double maxScore, weightage;
			try {
				maxScore = Double.parseDouble(maxScoreField.getText().trim());
				if (maxScore <= 0) {
					JOptionPane.showMessageDialog(dialog, "Maximum score must be greater than 0!", "Validation Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(dialog, "Please enter a valid maximum score!", "Validation Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			try {
				weightage = Double.parseDouble(weightageField.getText().trim());
				if (weightage <= 0 || weightage > 100) {
					JOptionPane.showMessageDialog(dialog, "Weightage must be between 1 and 100!", "Validation Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(dialog, "Please enter a valid weightage!", "Validation Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Extract course ID
			String courseSelection = (String) courseCombo.getSelectedItem();
			int courseID = Integer.parseInt(courseSelection.split(" - ")[0]);

			String assignmentType = (String) typeCombo.getSelectedItem();
			String description = descriptionArea.getText().trim();
			String instructions = instructionsArea.getText().trim();

			// FIXED: Convert java.util.Date to java.sql.Date
			java.sql.Date sqlDueDate = new java.sql.Date(dueDate.getTime());

			// FIXED: Create assignment in database with correct parameter types
			boolean success = DatabaseConnection.createAssignment(
					courseID, title, description, instructions, sqlDueDate, 
					maxScore, assignmentType, weightage, instructor.getInstructorID()
					);

			if (success) {
				String status = publish ? "published" : "saved as draft";
				JOptionPane.showMessageDialog(dialog,
						" Assignment " + status + " successfully!\n\n" +
								"Title: " + title + "\n" +
								"Course: " + courseSelection.split(": ")[1] + "\n" +
								"Due Date: " + dueDateStr + "\n" +
								"Type: " + assignmentType + "\n" +
								"Max Score: " + maxScore + "\n" +
								"Weightage: " + weightage + "%",
								"Success",
								JOptionPane.INFORMATION_MESSAGE);

				// Refresh assignments tab
				refreshAssignmentsTab();
				dialog.dispose();
			} else {
				JOptionPane.showMessageDialog(dialog,
						" Failed to create assignment.\n" +
								"Please try again or contact administrator.",
								"Creation Failed",
								JOptionPane.ERROR_MESSAGE);
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(dialog,
					" Error creating assignment: " + e.getMessage(),
					"Error",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}



	private String getDefaultDueDate() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, 7); // Default due date: 1 week from now
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(cal.getTime());
	}

	private void refreshAssignmentsTab() {
		// This will be called after creating a new assignment
		JOptionPane.showMessageDialog(this, 
				"Assignments list updated! Changes will be visible when you reopen the assignments tab.", 
				"Refresh", 
				JOptionPane.INFORMATION_MESSAGE);
	}

	@SuppressWarnings("unused")
	private boolean isValidDate(String dateStr) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			sdf.setLenient(false);
			sdf.parse(dateStr);
			return true;
		} catch (Exception e) {
			return false;
		}
	}


	private void refreshGradesTab() {
		// This will refresh the grades tab when new grades are added
		// You might want to implement a proper refresh mechanism
		JOptionPane.showMessageDialog(this, 
				"Grades data refreshed! Please reopen the grades tab to see changes.", 
				"Refresh", 
				JOptionPane.INFORMATION_MESSAGE);
	}

	private void showEditGradeDialog(String studentName, String courseCode, String assignmentName, 
			String currentGrade, String currentComments, int row, DefaultTableModel model) {
		// Parse current grade to get score and letter grade
		String[] gradeParts = currentGrade.split(" \\(");
		String letterGrade = gradeParts[0];
		String scoreStr = gradeParts[1].replace("%)", "");

		JPanel editPanel = new JPanel(new GridLayout(4, 2, 5, 5));
		editPanel.add(new JLabel("Student:"));
		editPanel.add(new JLabel(studentName));
		editPanel.add(new JLabel("Course:"));
		editPanel.add(new JLabel(courseCode));
		editPanel.add(new JLabel("Assignment:"));
		editPanel.add(new JLabel(assignmentName));
		editPanel.add(new JLabel("Score:"));
		JTextField scoreField = new JTextField(scoreStr);
		editPanel.add(scoreField);
		editPanel.add(new JLabel("Letter Grade:"));
		JComboBox<String> gradeCombo = new JComboBox<>(new String[]{
				"A", "A-", "B+", "B", "B-", "C+", "C", "C-", "D+", "D", "D-", "F"
		});
		gradeCombo.setSelectedItem(letterGrade);
		editPanel.add(gradeCombo);
		editPanel.add(new JLabel("Comments:"));
		JTextArea commentsArea = new JTextArea(currentComments, 3, 20);
		commentsArea.setLineWrap(true);
		commentsArea.setWrapStyleWord(true);
		JScrollPane commentsScroll = new JScrollPane(commentsArea);
		editPanel.add(commentsScroll);

		int result = JOptionPane.showConfirmDialog(this, editPanel, 
				"Edit Grade for " + studentName, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

		if (result == JOptionPane.OK_OPTION) {
			try {
				double newScore = Double.parseDouble(scoreField.getText().trim());
				String newLetterGrade = (String) gradeCombo.getSelectedItem();
				String newComments = commentsArea.getText().trim();

				if (newScore >= 0 && newScore <= 100) {
					model.setValueAt(newLetterGrade + " (" + newScore + "%)", row, 3);
					model.setValueAt(newComments, row, 4);
					JOptionPane.showMessageDialog(this, 
							"Grade updated successfully!",
							"Success",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(this, 
							"Score must be between 0 and 100!",
							"Invalid Score",
							JOptionPane.ERROR_MESSAGE);
				}
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, 
						"Please enter a valid numeric score!",
						"Invalid Input",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void refreshAllTabs() {
		refreshAssignmentsTab();
		JOptionPane.showMessageDialog(this, 
				"All data refreshed!", 
				"Refresh Complete", 
				JOptionPane.INFORMATION_MESSAGE);
	}

	private void showAddGradeDialog() {
		final JDialog dialog = new JDialog(this, "Add New Grade", true);
		dialog.setSize(500, 400);
		dialog.setLocationRelativeTo(this);
		dialog.setLayout(new BorderLayout());

		// Header
		JPanel headerPanel = new JPanel();
		headerPanel.setBackground(new Color(52, 152, 219));
		headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
		JLabel headerLabel = new JLabel(" Add New Grade");
		headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
		headerLabel.setForeground(Color.WHITE);
		headerPanel.add(headerLabel);
		dialog.add(headerPanel, BorderLayout.NORTH);

		// Main form panel
		JPanel formPanel = new JPanel(new GridBagLayout());
		formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;

		// Student selection
		gbc.gridx = 0; gbc.gridy = 0;
		formPanel.add(new JLabel("Student:"), gbc);
		gbc.gridx = 1;
		final JComboBox<String> studentCombo = new JComboBox<>();
		studentCombo.setPreferredSize(new Dimension(250, 25));
		formPanel.add(studentCombo, gbc);

		// Assignment selection
		gbc.gridx = 0; gbc.gridy = 1;
		formPanel.add(new JLabel("Assignment:"), gbc);
		gbc.gridx = 1;
		final JComboBox<String> assignmentCombo = new JComboBox<>();
		assignmentCombo.setPreferredSize(new Dimension(250, 25));
		formPanel.add(assignmentCombo, gbc);

		// Score input
		gbc.gridx = 0; gbc.gridy = 2;
		formPanel.add(new JLabel("Score:"), gbc);
		gbc.gridx = 1;
		final JTextField scoreField = new JTextField(10);
		formPanel.add(scoreField, gbc);

		// Letter grade
		gbc.gridx = 0; gbc.gridy = 3;
		formPanel.add(new JLabel("Letter Grade:"), gbc);
		gbc.gridx = 1;
		final JComboBox<String> gradeCombo = new JComboBox<>(new String[]{
				"A", "A-", "B+", "B", "B-", "C+", "C", "C-", "D+", "D", "D-", "F"
		});
		formPanel.add(gradeCombo, gbc);

		// Remarks
		gbc.gridx = 0; gbc.gridy = 4;
		formPanel.add(new JLabel("Remarks:"), gbc);
		gbc.gridx = 1;
		final JTextArea remarksArea = new JTextArea(3, 20);
		remarksArea.setLineWrap(true);
		remarksArea.setWrapStyleWord(true);
		JScrollPane remarksScroll = new JScrollPane(remarksArea);
		formPanel.add(remarksScroll, gbc);

		// Load data
		loadGradeDialogData(studentCombo, assignmentCombo);

		// Button panel
		JPanel buttonPanel = new JPanel(new FlowLayout());
		JButton saveButton = new JButton("Save Grade");
		JButton cancelButton = new JButton(" Cancel");

		saveButton.setBackground(new Color(46, 204, 113));
		saveButton.setForeground(Color.WHITE);
		cancelButton.setBackground(new Color(231, 76, 60));
		cancelButton.setForeground(Color.WHITE);

		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveGrade(studentCombo, assignmentCombo, scoreField, gradeCombo, remarksArea, dialog);
			}
		});

		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
			}
		});

		buttonPanel.add(saveButton);
		buttonPanel.add(cancelButton);

		dialog.add(formPanel, BorderLayout.CENTER);
		dialog.add(buttonPanel, BorderLayout.SOUTH);
		dialog.setVisible(true);
	}

	private void loadGradeDialogData(JComboBox<String> studentCombo, JComboBox<String> assignmentCombo) {
		// Clear existing items
		studentCombo.removeAllItems();
		assignmentCombo.removeAllItems();

		// Load students
		List<Object[]> students = DatabaseConnection.getInstructorStudentsForGrading(instructor.getInstructorID());
		for (Object[] student : students) {
			studentCombo.addItem(student[0] + " - " + student[1] + " (" + student[3] + ")");
		}

		// Load assignments
		List<Object[]> assignments = DatabaseConnection.getInstructorAssignmentsForGrading(instructor.getInstructorID());
		for (Object[] assignment : assignments) {
			assignmentCombo.addItem(assignment[0] + " - " + assignment[1] + " (" + assignment[2] + ")");
		}

		if (students.isEmpty()) {
			studentCombo.addItem("No students found");
		}
		if (assignments.isEmpty()) {
			assignmentCombo.addItem("No assignments found");
		}
	}

	private void saveGrade(JComboBox<String> studentCombo, JComboBox<String> assignmentCombo,
			JTextField scoreField, JComboBox<String> gradeCombo,
			JTextArea remarksArea, JDialog dialog) {
		try {
			// Validate inputs
			if (studentCombo.getSelectedItem() == null || studentCombo.getSelectedIndex() == 0) {
				JOptionPane.showMessageDialog(dialog, "Please select a student!", "Validation Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			if (assignmentCombo.getSelectedItem() == null || assignmentCombo.getSelectedIndex() == 0) {
				JOptionPane.showMessageDialog(dialog, "Please select an assignment!", "Validation Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			String scoreText = scoreField.getText().trim();
			if (scoreText.isEmpty()) {
				JOptionPane.showMessageDialog(dialog, "Please enter a score!", "Validation Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			double score;
			try {
				score = Double.parseDouble(scoreText);
				if (score < 0 || score > 100) {
					JOptionPane.showMessageDialog(dialog, "Score must be between 0 and 100!", "Validation Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(dialog, "Please enter a valid numeric score!", "Validation Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Extract IDs from combo boxes
			String studentSelection = (String) studentCombo.getSelectedItem();
			String assignmentSelection = (String) assignmentCombo.getSelectedItem();

			int studentID = Integer.parseInt(studentSelection.split(" - ")[0]);
			int assignmentID = Integer.parseInt(assignmentSelection.split(" - ")[0]);

			// Get course ID from student data
			List<Object[]> students = DatabaseConnection.getInstructorStudentsForGrading(instructor.getInstructorID());
			int courseID = 0;
			for (Object[] student : students) {
				if ((int) student[0] == studentID) {
					courseID = (int) student[2];
					break;
				}
			}

			String letterGrade = (String) gradeCombo.getSelectedItem();
			String remarks = remarksArea.getText().trim();

			// Save to database
			boolean success = DatabaseConnection.addGrade(assignmentID, studentID, courseID, score, letterGrade, remarks, instructor.getInstructorID());

			if (success) {
				JOptionPane.showMessageDialog(dialog,
						" Grade saved successfully!\n\n" +
								"Student ID: " + studentID + "\n" +
								"Assignment ID: " + assignmentID + "\n" +
								"Score: " + score + "\n" +
								"Grade: " + letterGrade,
								"Success",
								JOptionPane.INFORMATION_MESSAGE);

				// Refresh grades tab
				refreshGradesTab();
				dialog.dispose();
			} else {
				JOptionPane.showMessageDialog(dialog,
						" Failed to save grade.\n" +
								"Please check if this grade already exists.",
								"Save Failed",
								JOptionPane.ERROR_MESSAGE);
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(dialog,
					" Error saving grade: " + e.getMessage(),
					"Error",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}



	public static void main(String[] args) {
		// Test with a real instructor object
		final Instructor demoInstructor = new Instructor();
		demoInstructor.setInstructorID(1); // Set instructor ID
		demoInstructor.setName("Dr. BUGINGO Emmanuel");
		demoInstructor.setIdentifier("INST001");
		demoInstructor.setDepartment("programing with java");
		demoInstructor.setLocation("Computer Science Department");
		demoInstructor.setContact("Emmanuelgmail.com");
		demoInstructor.setEmail("Emmanuel@gmail.com");
		demoInstructor.setStatus("ACTIVE");
		demoInstructor.setUsername("Bugingo");
		demoInstructor.setAssignedSince(new java.util.Date());

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new InstructorControlPanel(demoInstructor).setVisible(true);
			}
		});
	}


}