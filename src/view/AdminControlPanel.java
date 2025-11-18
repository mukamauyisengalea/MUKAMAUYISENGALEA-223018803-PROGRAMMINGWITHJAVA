package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import control.CourseController;
import control.InstructorController;
import control.StudentController;
import model.Admin;
import model.Course;
import model.Instructor;
import model.Student;
import view.admin.components.AdminButton;
import view.admin.components.AdminButtonFactory;

public class AdminControlPanel extends JFrame {
	
	private Admin admin;
	private JTabbedPane tabbedPane;
	private StudentController studentController;
	private InstructorController instructorController;
	private CourseController courseController;

	public AdminControlPanel(Admin admin) {
		this.admin = admin;
		this.studentController = new StudentController();
		this.instructorController = new InstructorController();
		this.courseController = new CourseController();

		setTitle("Admin Control Panel - " + admin.getName());
		setSize(1000, 700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		initComponents();
	}

	private void initComponents() {
		tabbedPane = new JTabbedPane();

		tabbedPane.addTab("Dashboard", createDashboardPanel());
		tabbedPane.addTab("Students", createStudentPanel());
		tabbedPane.addTab("Instructors", createInstructorPanel());
		tabbedPane.addTab("Courses", createCoursePanel());
		tabbedPane.addTab("System", createSystemPanel());

		add(tabbedPane, BorderLayout.CENTER);

		// Top panel with admin info and logout
		JPanel topPanel = new JPanel(new BorderLayout());
		JLabel adminLabel = new JLabel("Administrator: " + admin.getName());
		adminLabel.setFont(new Font("Arial", Font.BOLD, 16));
		topPanel.add(adminLabel, BorderLayout.WEST);

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

	private JPanel createDashboardPanel() {
		JPanel panel = new JPanel(new BorderLayout());

		// Welcome message
		JLabel welcomeLabel = new JLabel("Education Management System - Administrator Dashboard", JLabel.CENTER);
		welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
		welcomeLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
		panel.add(welcomeLabel, BorderLayout.NORTH);

		// Main dashboard buttons grid
		JPanel dashboardPanel = createDashboardButtonsGrid();
		panel.add(dashboardPanel, BorderLayout.CENTER);

		// Quick actions panel
		JPanel quickActionsPanel = createQuickActionsPanel();
		panel.add(quickActionsPanel, BorderLayout.SOUTH);

		return panel;
	}

	private JPanel createDashboardButtonsGrid() {
		JPanel gridPanel = new JPanel(new GridLayout(2, 4, 15, 15));
		gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		gridPanel.setBackground(new Color(240, 245, 255));

		// Get all dashboard buttons
		java.util.List<AdminButton> buttons = AdminButtonFactory.createDashboardButtons(tabbedPane);

		// Create cards for each button
		for (AdminButton button : buttons) {
			gridPanel.add(button.createCard());
		}

		return gridPanel;
	}

	private JPanel createQuickActionsPanel() {
		JPanel quickPanel = new JPanel();
		quickPanel.setBorder(BorderFactory.createTitledBorder("Quick Actions"));
		quickPanel.setBackground(Color.WHITE);

		// Get quick action buttons
		java.util.List<AdminButton> quickButtons = AdminButtonFactory.createQuickActionButtons(tabbedPane);

		// Create regular buttons (not cards) for quick actions
		for (AdminButton adminButton : quickButtons) {
			javax.swing.JButton button = adminButton.createButton();
			button.setPreferredSize(new Dimension(160, 40)); // Smaller for quick actions
			quickPanel.add(button);
		}

		return quickPanel;
	}

	private JPanel createStudentPanel() {
		JPanel panel = new JPanel(new BorderLayout());

		String[] columns = {"ID", "First Name", "Last Name", "Username", "Sex", "Birthdate"};
		final DefaultTableModel model = new DefaultTableModel(columns, 0);
		final JTable table = new JTable(model);
		JScrollPane scrollPane = new JScrollPane(table);

		JPanel buttonPanel = new JPanel(new FlowLayout());
		JButton refreshButton = new JButton("Refresh");
		JButton addButton = new JButton("Add Student");
		JButton deleteButton = new JButton("Delete Student");
		JButton editButton = new JButton("Edit Student");

		refreshButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadStudents(model);
			}
		});

		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showAddStudentDialog(model);
			}
		});

		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table.getSelectedRow();
				if (selectedRow >= 0) {
					int studentID = (int) model.getValueAt(selectedRow, 0);
					int confirm = JOptionPane.showConfirmDialog(AdminControlPanel.this,
							"Are you sure you want to delete this student?",
							"Confirm Delete", JOptionPane.YES_NO_OPTION);
					if (confirm == JOptionPane.YES_OPTION) {
						if (studentController.deleteStudent(studentID)) {
							JOptionPane.showMessageDialog(AdminControlPanel.this, "Student deleted successfully!");
							loadStudents(model);
						} else {
							JOptionPane.showMessageDialog(AdminControlPanel.this, "Failed to delete student!");
						}
					}
				} else {
					JOptionPane.showMessageDialog(AdminControlPanel.this, "Please select a student to delete!");
				}
			}
		});

		editButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table.getSelectedRow();
				if (selectedRow >= 0) {
					JOptionPane.showMessageDialog(AdminControlPanel.this, 
							"Student editing feature coming soon!");
				} else {
					JOptionPane.showMessageDialog(AdminControlPanel.this, 
							"Please select a student to edit!");
				}
			}
		});

		buttonPanel.add(refreshButton);
		buttonPanel.add(addButton);
		buttonPanel.add(editButton);
		buttonPanel.add(deleteButton);

		panel.add(scrollPane, BorderLayout.CENTER);
		panel.add(buttonPanel, BorderLayout.SOUTH);

		loadStudents(model);
		return panel;
	}

	protected void loadStudents(DefaultTableModel model) {
		model.setRowCount(0);
		List<Student> students = studentController.getAllStudents();
		if (students != null && !students.isEmpty()) {
			for (Student student : students) {
				model.addRow(new Object[]{
						student.getStudentID(),
						student.getFirstName(),
						student.getLastName(),
						student.getUsername(),
						student.getSex(),
						student.getBirthdate()
				});
			}
		} else {
			// Add sample data for demonstration
			model.addRow(new Object[]{1, "John", "Doe", "john.doe", "M", "2000-05-15"});
			model.addRow(new Object[]{2, "Jane", "Smith", "jane.smith", "F", "2001-08-22"});
			model.addRow(new Object[]{3, "Mike", "Johnson", "mike.johnson", "M", "1999-12-10"});
		}
	}

	protected void showAddStudentDialog(final DefaultTableModel model) {
		final JDialog dialog = new JDialog(this, "Add New Student", true);
		dialog.setSize(400, 350);
		dialog.setLocationRelativeTo(this);

		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		final JTextField firstNameField = new JTextField(20);
		final JTextField lastNameField = new JTextField(20);
		final JTextField usernameField = new JTextField(20);
		final JPasswordField passwordField = new JPasswordField(20);
		final JComboBox<String> sexCombo = new JComboBox<>(new String[]{"M", "F"});
		final JTextField birthdateField = new JTextField(20);
		birthdateField.setToolTipText("Format: YYYY-MM-DD");

		gbc.gridx = 0; gbc.gridy = 0;
		panel.add(new JLabel("First Name:"), gbc);
		gbc.gridx = 1;
		panel.add(firstNameField, gbc);

		gbc.gridx = 0; gbc.gridy = 1;
		panel.add(new JLabel("Last Name:"), gbc);
		gbc.gridx = 1;
		panel.add(lastNameField, gbc);

		gbc.gridx = 0; gbc.gridy = 2;
		panel.add(new JLabel("Username:"), gbc);
		gbc.gridx = 1;
		panel.add(usernameField, gbc);

		gbc.gridx = 0; gbc.gridy = 3;
		panel.add(new JLabel("Password:"), gbc);
		gbc.gridx = 1;
		panel.add(passwordField, gbc);

		gbc.gridx = 0; gbc.gridy = 4;
		panel.add(new JLabel("Sex:"), gbc);
		gbc.gridx = 1;
		panel.add(sexCombo, gbc);

		gbc.gridx = 0; gbc.gridy = 5;
		panel.add(new JLabel("Birthdate:"), gbc);
		gbc.gridx = 1;
		panel.add(birthdateField, gbc);

		JButton saveButton = new JButton("Save");
		JButton cancelButton = new JButton("Cancel");

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(saveButton);
		buttonPanel.add(cancelButton);

		gbc.gridx = 0; gbc.gridy = 6;
		gbc.gridwidth = 2;
		panel.add(buttonPanel, gbc);

		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String firstName = firstNameField.getText().trim();
					String lastName = lastNameField.getText().trim();
					String username = usernameField.getText().trim();
					String password = new String(passwordField.getPassword()).trim();
					String birthdate = birthdateField.getText().trim();

					if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || password.isEmpty() || birthdate.isEmpty()) {
						JOptionPane.showMessageDialog(dialog, "Please fill in all fields!");
						return;
					}

					Student student = new Student();
					student.setFirstName(firstName);
					student.setLastName(lastName);
					student.setUsername(username);
					student.setPassword(password);
					student.setSex((String) sexCombo.getSelectedItem());
					student.setBirthdate(java.sql.Date.valueOf(birthdate));

					if (studentController.addStudent(student)) {
						JOptionPane.showMessageDialog(dialog, "Student added successfully!");
						loadStudents(model);
						dialog.dispose();
					} else {
						JOptionPane.showMessageDialog(dialog, "Failed to add student!");
					}
				} catch (IllegalArgumentException ex) {
					JOptionPane.showMessageDialog(dialog, "Invalid date format! Please use YYYY-MM-DD");
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
					ex.printStackTrace();
				}
			}
		});

		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
			}
		});

		dialog.add(panel);
		dialog.setVisible(true);
	}

	private JPanel createInstructorPanel() {
		JPanel panel = new JPanel(new BorderLayout());

		String[] columns = {"ID", "Name", "Identifier", "Status", "Location", "Contact", "Course ID"};
		final DefaultTableModel model = new DefaultTableModel(columns, 0);
		final JTable table = new JTable(model);
		JScrollPane scrollPane = new JScrollPane(table);

		JPanel buttonPanel = new JPanel(new FlowLayout());
		JButton refreshButton = new JButton("Refresh");
		JButton addButton = new JButton("Add Instructor");
		JButton deleteButton = new JButton("Delete Instructor");

		refreshButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadInstructors(model);
			}
		});

		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(AdminControlPanel.this, 
						"Add instructor feature coming soon!");
			}
		});

		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table.getSelectedRow();
				if (selectedRow >= 0) {
					JOptionPane.showMessageDialog(AdminControlPanel.this, 
							"Delete instructor feature coming soon!");
				} else {
					JOptionPane.showMessageDialog(AdminControlPanel.this, 
							"Please select an instructor to delete!");
				}
			}
		});

		buttonPanel.add(refreshButton);
		buttonPanel.add(addButton);
		buttonPanel.add(deleteButton);

		panel.add(scrollPane, BorderLayout.CENTER);
		panel.add(buttonPanel, BorderLayout.SOUTH);

		loadInstructors(model);
		return panel;
	}

	private void loadInstructors(DefaultTableModel model) {
		model.setRowCount(0);
		List<Instructor> instructors = instructorController.getAllInstructors();
		if (instructors != null && !instructors.isEmpty()) {
			for (Instructor instructor : instructors) {
				model.addRow(new Object[]{
						instructor.getInstructorID(),
						instructor.getName(),
						instructor.getIdentifier(),
						instructor.getStatus(),
						instructor.getLocation(),
						instructor.getContact(),
						instructor.getCourseID()
				});
			}
		} else {
			// Add sample data for demonstration
			model.addRow(new Object[]{1, "Dr. Smith", "INS001", "Active", "Building A", "smith@edu.com", 101});
			model.addRow(new Object[]{2, "Prof. Johnson", "INS002", "Active", "Building B", "johnson@edu.com", 102});
			model.addRow(new Object[]{3, "Dr. Williams", "INS003", "On Leave", "Building C", "williams@edu.com", 103});
		}
	}

	private JPanel createCoursePanel() {
		JPanel panel = new JPanel(new BorderLayout());

		String[] columns = {"Course ID", "Course Name", "Course Code", "Created At"};
		final DefaultTableModel model = new DefaultTableModel(columns, 0);
		final JTable table = new JTable(model);
		JScrollPane scrollPane = new JScrollPane(table);

		JPanel buttonPanel = new JPanel(new FlowLayout());
		JButton refreshButton = new JButton("Refresh");
		JButton addButton = new JButton("Add Course");
		JButton deleteButton = new JButton("Delete Course");

		refreshButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadCourses(model);
			}
		});

		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showAddCourseDialog(model);
			}
		});

		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table.getSelectedRow();
				if (selectedRow >= 0) {
					int courseID = (int) model.getValueAt(selectedRow, 0);
					int confirm = JOptionPane.showConfirmDialog(AdminControlPanel.this,
							"Are you sure you want to delete this course?",
							"Confirm Delete", JOptionPane.YES_NO_OPTION);
					if (confirm == JOptionPane.YES_OPTION) {
						if (courseController.deleteCourse(courseID)) {
							JOptionPane.showMessageDialog(AdminControlPanel.this, "Course deleted successfully!");
							loadCourses(model);
						} else {
							JOptionPane.showMessageDialog(AdminControlPanel.this, "Failed to delete course!");
						}
					}
				} else {
					JOptionPane.showMessageDialog(AdminControlPanel.this, "Please select a course to delete!");
				}
			}
		});

		buttonPanel.add(refreshButton);
		buttonPanel.add(addButton);
		buttonPanel.add(deleteButton);

		panel.add(scrollPane, BorderLayout.CENTER);
		panel.add(buttonPanel, BorderLayout.SOUTH);

		loadCourses(model);
		return panel;
	}

	private void loadCourses(DefaultTableModel model) {
		model.setRowCount(0);
		List<Course> courses = courseController.getAllCourses();
		if (courses != null && !courses.isEmpty()) {
			for (Course course : courses) {
				model.addRow(new Object[]{
						course.getCourseID(),
						course.getCourseName(),
						course.getCourseCode(),
						course.getCreatedAt()
				});
			}
		} else {
			// Add sample data for demonstration
			model.addRow(new Object[]{101, "Introduction to Programming", "CS101", "2024-01-15"});
			model.addRow(new Object[]{102, "Data Structures", "CS201", "2024-01-15"});
			model.addRow(new Object[]{103, "Calculus I", "MATH101", "2024-01-15"});
		}
	}

	private void showAddCourseDialog(final DefaultTableModel model) {
		final JDialog dialog = new JDialog(this, "Add New Course", true);
		dialog.setSize(400, 200);
		dialog.setLocationRelativeTo(this);

		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		final JTextField courseNameField = new JTextField(20);
		final JTextField courseCodeField = new JTextField(20);

		gbc.gridx = 0; gbc.gridy = 0;
		panel.add(new JLabel("Course Name:"), gbc);
		gbc.gridx = 1;
		panel.add(courseNameField, gbc);

		gbc.gridx = 0; gbc.gridy = 1;
		panel.add(new JLabel("Course Code:"), gbc);
		gbc.gridx = 1;
		panel.add(courseCodeField, gbc);

		JButton saveButton = new JButton("Save");
		JButton cancelButton = new JButton("Cancel");

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(saveButton);
		buttonPanel.add(cancelButton);

		gbc.gridx = 0; gbc.gridy = 2;
		gbc.gridwidth = 2;
		panel.add(buttonPanel, gbc);

		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String courseName = courseNameField.getText().trim();
				String courseCode = courseCodeField.getText().trim();

				if (courseName.isEmpty() || courseCode.isEmpty()) {
					JOptionPane.showMessageDialog(dialog, "Please fill in all fields!");
					return;
				}

				Course course = new Course();
				course.setCourseName(courseName);
				course.setCourseCode(courseCode);

				if (courseController.addCourse(course)) {
					JOptionPane.showMessageDialog(dialog, "Course added successfully!");
					loadCourses(model);
					dialog.dispose();
				} else {
					JOptionPane.showMessageDialog(dialog, "Failed to add course!");
				}
			}
		});

		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
			}
		});

		dialog.add(panel);
		dialog.setVisible(true);
	}

	private JPanel createSystemPanel() {
		JPanel panel = new JPanel(new BorderLayout());

		JLabel headerLabel = new JLabel("System Administration", JLabel.CENTER);
		headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
		headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
		panel.add(headerLabel, BorderLayout.NORTH);

		// System controls panel
		JPanel controlsPanel = new JPanel(new GridLayout(4, 2, 10, 10));
		controlsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		JButton backupButton = new JButton("Database Backup");
		JButton restoreButton = new JButton("Restore Backup");
		JButton logsButton = new JButton("View System Logs");
		JButton usersButton = new JButton("Manage System Users");
		JButton settingsButton = new JButton("System Settings");
		JButton reportsButton = new JButton("System Reports");
		JButton maintenanceButton = new JButton("Maintenance");
		JButton aboutButton = new JButton("About System");

		// Add action listeners for system buttons
		ActionListener systemButtonListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(AdminControlPanel.this, 
						"System feature: " + ((JButton)e.getSource()).getText() + " - Coming soon!");
			}
		};

		backupButton.addActionListener(systemButtonListener);
		restoreButton.addActionListener(systemButtonListener);
		logsButton.addActionListener(systemButtonListener);
		usersButton.addActionListener(systemButtonListener);
		settingsButton.addActionListener(systemButtonListener);
		reportsButton.addActionListener(systemButtonListener);
		maintenanceButton.addActionListener(systemButtonListener);
		aboutButton.addActionListener(systemButtonListener);

		controlsPanel.add(backupButton);
		controlsPanel.add(restoreButton);
		controlsPanel.add(logsButton);
		controlsPanel.add(usersButton);
		controlsPanel.add(settingsButton);
		controlsPanel.add(reportsButton);
		controlsPanel.add(maintenanceButton);
		controlsPanel.add(aboutButton);

		panel.add(controlsPanel, BorderLayout.CENTER);

		return panel;
	}

	// Main method for testing
	public static void main(String[] args) {
		// Create a sample admin for testing
		final Admin testAdmin = new Admin();
		testAdmin.setId(1);
		testAdmin.setName("System Administrator");
		testAdmin.setUsername("admin");

		// Create and show the admin control panel
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new AdminControlPanel(testAdmin).setVisible(true);
			}
		});
	}
}