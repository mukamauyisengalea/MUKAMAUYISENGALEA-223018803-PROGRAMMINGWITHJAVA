package view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import model.Instructor;
import util.DatabaseConnection;

import java.util.ArrayList;
import java.util.List;

public class InstructorControlPanel extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Instructor instructor;
    private JTabbedPane tabbedPane;

    public InstructorControlPanel(Instructor instructor) {
        this.instructor = instructor;
        
        DatabaseConnection.debugDatabaseState(instructor.getIdentifier());
        
        setTitle("Instructor Dashboard - Education Monitoring Management System");
        setSize(900, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
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

        // 🟦 Title
        JLabel lblTitle = new JLabel("Instructor Dashboard", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(0, 51, 102));
        lblTitle.setBounds(300, 30, 300, 40);
        dashboardPanel.add(lblTitle);

        // 🟨 Welcome message
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

        tabbedPane.addTab("🏠 Dashboard", dashboardPanel);
    }

    private JPanel createInfoCard() {
        JPanel card = new JPanel();
        card.setLayout(new GridLayout(2, 3, 10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        card.add(createInfoField("Instructor ID", instructor.getIdentifier()));
        card.add(createInfoField("Department", instructor.getLocation()));
        card.add(createInfoField("Email", instructor.getContact()));
        card.add(createInfoField("Status", instructor.getStatus()));
        card.add(createInfoField("Assigned Since", instructor.getAssignedSince().toString()));
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

        // Get real statistics from database
        int totalCourses = DatabaseConnection.getInstructorCourses(instructor.getIdentifier()).size();
        int totalStudents = DatabaseConnection.getInstructorStudentCount(instructor.getIdentifier());
        int pendingAssignments = DatabaseConnection.getInstructorActiveAssignments(instructor.getIdentifier());
        int coursesToGrade = getCoursesToGradeCount();

        panel.add(createStatCard("📚 Courses", String.valueOf(totalCourses), new Color(52, 152, 219)));
        panel.add(createStatCard("👥 Students", String.valueOf(totalStudents), new Color(46, 204, 113)));
        panel.add(createStatCard("📝 Assignments", String.valueOf(pendingAssignments), new Color(241, 196, 15)));
        panel.add(createStatCard("📊 To Grade", String.valueOf(coursesToGrade), new Color(155, 89, 182)));

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

        JButton btnNewAssignment = createActionButton("➕ New Assignment", new Color(46, 204, 113));
        JButton btnViewStudents = createActionButton("👥 View Students", new Color(52, 152, 219));
        JButton btnRecordGrades = createActionButton("📊 Record Grades", new Color(155, 89, 182));
        JButton btnCourseMaterials = createActionButton("📚 Course Materials", new Color(241, 196, 15));
        JButton btnAnnouncements = createActionButton("📢 Post Announcement", new Color(230, 126, 34));
        JButton btnProfile = createActionButton("👤 My Profile", new Color(149, 165, 166));
        JButton btnRefresh = createActionButton("🔄 Refresh All", new Color(52, 152, 219));

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
        JLabel titleLabel = new JLabel("📝 Assignment Management", JLabel.LEFT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton btnCreateAssignment = new JButton("➕ Create New Assignment");
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
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Only actions column is editable
            }
        };

        // Load assignments from database
        List<Object[]> assignments = DatabaseConnection.getInstructorAssignments(instructor.getIdentifier());
        for (Object[] assignment : assignments) {
            model.addRow(new Object[]{
                assignment[0], assignment[1], assignment[2], 
                assignment[3], assignment[4], "View/Edit"
            });
        }

        JTable assignmentsTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(assignmentsTable);
        assignmentsPanel.add(scrollPane, BorderLayout.CENTER);

        tabbedPane.addTab("📝 Assignments", assignmentsPanel);
    }

   private void createStudentsTab() {
        JPanel studentsPanel = new JPanel(new BorderLayout());
        studentsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("👥 Student Management", JLabel.LEFT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        studentsPanel.add(titleLabel, BorderLayout.NORTH);

        // Students table
        String[] columns = {"Student ID", "Name", "Course", "Email", "Performance", "Actions"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        // Load students from database WITH DEBUG
        System.out.println("=== DEBUG: Loading students for instructor: " + instructor.getIdentifier() + " ===");
        List<Object[]> students = DatabaseConnection.getInstructorStudents(instructor.getIdentifier());
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

        tabbedPane.addTab("👥 Students", studentsPanel);
    }

   private void createGradesTab() {
        JPanel gradesPanel = new JPanel(new BorderLayout());
        gradesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header with title and add grade button
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("📊 Grade Management", JLabel.LEFT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton btnAddGrade = new JButton("➕ Add New Grade");
        btnAddGrade.setBackground(new Color(46, 204, 113));
        btnAddGrade.setForeground(Color.WHITE);
        btnAddGrade.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showAddGradeDialog();
            }
        });
        headerPanel.add(btnAddGrade, BorderLayout.EAST);

        gradesPanel.add(headerPanel, BorderLayout.NORTH);

        // Grades table
        String[] columns = {"Student", "Course", "Assignment", "Grade", "Comments", "Actions"};
        final DefaultTableModel model = new DefaultTableModel(columns, 0) {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Only actions column is editable
            }
        };

        // ========== ADD DEBUG CODE HERE ==========
        // Load grades from database - ADD DEBUG OUTPUT
        final List<Object[]> grades = DatabaseConnection.getInstructorGrades(instructor.getIdentifier());
        System.out.println("DEBUG: Instructor ID: " + instructor.getIdentifier());
        System.out.println("DEBUG: Number of grades found: " + grades.size());

        for (Object[] grade : grades) {
            System.out.println("DEBUG: Grade data: " + java.util.Arrays.toString(grade));
            model.addRow(new Object[]{
                grade[0], grade[1], grade[2], 
                grade[3], grade[4], "Edit Grade"
            });
        }

        // If no grades, show sample data for testing
        if (grades.isEmpty()) {
            System.out.println("DEBUG: No grades found, showing sample data");
            model.addRow(new Object[]{"John Doe", "CS101", "Programming Project 1", "A", "Excellent work", "Edit Grade"});
            model.addRow(new Object[]{"Jane Smith", "CS101", "Programming Project 1", "B+", "Good understanding", "Edit Grade"});
            model.addRow(new Object[]{"Mike Johnson", "CS207", "Data Structures Quiz", "A", "Well prepared", "Edit Grade"});
            model.addRow(new Object[]{"Sarah Wilson", "CS301", "Algorithm Analysis", "A", "Outstanding", "Edit Grade"});
        }
        // ========== END DEBUG CODE ==========

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

        // Add some statistics at the bottom - UPDATE THIS TOO
        // Use the grades list for statistics, or create a sample list if empty
        List<Object[]> statsGrades = grades;
        if (grades.isEmpty()) {
            statsGrades = new ArrayList<>();
            statsGrades.add(new Object[]{"John Doe", "CS101", "Programming Project 1", "A", "Excellent work"});
            statsGrades.add(new Object[]{"Jane Smith", "CS101", "Programming Project 1", "B+", "Good understanding"});
            statsGrades.add(new Object[]{"Mike Johnson", "CS207", "Data Structures Quiz", "A", "Well prepared"});
            statsGrades.add(new Object[]{"Sarah Wilson", "CS301", "Algorithm Analysis", "A", "Outstanding"});
        }
        JPanel statsPanel = createGradeStatsPanel(statsGrades);
        gradesPanel.add(statsPanel, BorderLayout.SOUTH);

        tabbedPane.addTab("📊 Grades", gradesPanel);
    }

    private void showEditGradeDialog(final String studentName, final String courseCode, final String assignmentName, 
                                    String currentGrade, String currentComments, final int row, final DefaultTableModel model) {
        final JDialog dialog = new JDialog(this, "Edit Grade", true);
        dialog.setSize(450, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(52, 152, 219));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        JLabel headerLabel = new JLabel("✏️ Edit Grade");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);
        dialog.add(headerPanel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Display student info (read-only)
        formPanel.add(new JLabel("Student:"));
        JTextField studentField = new JTextField(studentName);
        studentField.setEditable(false);
        studentField.setBackground(new Color(240, 240, 240));
        formPanel.add(studentField);

        formPanel.add(new JLabel("Course:"));
        JTextField courseField = new JTextField(courseCode);
        courseField.setEditable(false);
        courseField.setBackground(new Color(240, 240, 240));
        formPanel.add(courseField);

        formPanel.add(new JLabel("Assignment:"));
        JTextField assignmentField = new JTextField(assignmentName);
        assignmentField.setEditable(false);
        assignmentField.setBackground(new Color(240, 240, 240));
        formPanel.add(assignmentField);

        // Grade input
        formPanel.add(new JLabel("Grade:*"));
        final JComboBox<String> gradeCombo = new JComboBox<String>(new String[]{
            "A", "A-", "B+", "B", "B-", "C+", "C", "C-", "D+", "D", "D-", "F", "Incomplete"
        });
        if (currentGrade != null && !currentGrade.isEmpty()) {
            gradeCombo.setSelectedItem(currentGrade);
        } else {
            gradeCombo.setSelectedItem("A");
        }
        formPanel.add(gradeCombo);

        // Comments
        formPanel.add(new JLabel("Comments:"));
        final JTextArea commentsArea = new JTextArea(3, 20);
        commentsArea.setLineWrap(true);
        commentsArea.setWrapStyleWord(true);
        if (currentComments != null) {
            commentsArea.setText(currentComments);
        }
        JScrollPane commentsScroll = new JScrollPane(commentsArea);
        formPanel.add(commentsScroll);

        dialog.add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel();
        
        JButton saveButton = new JButton("💾 Save Grade");
        saveButton.setBackground(new Color(46, 204, 113));
        saveButton.setForeground(Color.WHITE);
        
        JButton cancelButton = new JButton("❌ Cancel");
        cancelButton.setBackground(new Color(231, 76, 60));
        cancelButton.setForeground(Color.WHITE);

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String newGrade = (String) gradeCombo.getSelectedItem();
                String newComments = commentsArea.getText().trim();
                
                // Update the table
                model.setValueAt(newGrade, row, 3);
                model.setValueAt(newComments, row, 4);
                
                // Here you would update the grade in the database
                boolean success = updateGradeInDatabase(studentName, courseCode, assignmentName, newGrade, newComments);
                
                if (success) {
                    JOptionPane.showMessageDialog(dialog, 
                        "Grade updated successfully!\n\n" +
                        "Student: " + studentName + "\n" +
                        "Grade: " + newGrade,
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, 
                        "Failed to update grade in database.",
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void showAddGradeDialog() {
        final JDialog dialog = new JDialog(this, "Add New Grade", true);
        dialog.setSize(500, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(46, 204, 113));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        JLabel headerLabel = new JLabel("➕ Add New Grade");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);
        dialog.add(headerPanel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Student selection
        formPanel.add(new JLabel("Student:*"));
        final JComboBox<String> studentCombo = new JComboBox<String>();
        List<String> students = DatabaseConnection.getAllStudents(instructor.getIdentifier());
        for (String student : students) {
            studentCombo.addItem(student);
        }
        formPanel.add(studentCombo);

        // Course selection
        formPanel.add(new JLabel("Course:*"));
        final JComboBox<String> courseCombo = new JComboBox<String>();
        List<Object[]> courses = DatabaseConnection.getInstructorCourses(instructor.getIdentifier());
        for (Object[] course : courses) {
            courseCombo.addItem(course[0] + " - " + course[1]);
        }
        formPanel.add(courseCombo);

        // Assignment selection
        formPanel.add(new JLabel("Assignment:*"));
        final JComboBox<String> assignmentCombo = new JComboBox<String>();
        List<String> assignments = DatabaseConnection.getAllAssignments(instructor.getIdentifier());
        for (String assignment : assignments) {
            assignmentCombo.addItem(assignment);
        }
        formPanel.add(assignmentCombo);

        // Grade input
        formPanel.add(new JLabel("Grade:*"));
        final JComboBox<String> gradeCombo = new JComboBox<String>(new String[]{
            "A", "A-", "B+", "B", "B-", "C+", "C", "C-", "D+", "D", "D-", "F", "Incomplete"
        });
        gradeCombo.setSelectedItem("A");
        formPanel.add(gradeCombo);

        // Comments
        formPanel.add(new JLabel("Comments:"));
        final JTextArea commentsArea = new JTextArea(3, 20);
        commentsArea.setLineWrap(true);
        commentsArea.setWrapStyleWord(true);
        JScrollPane commentsScroll = new JScrollPane(commentsArea);
        formPanel.add(commentsScroll);

        dialog.add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel();
        
        JButton saveButton = new JButton("💾 Save Grade");
        saveButton.setBackground(new Color(46, 204, 113));
        saveButton.setForeground(Color.WHITE);
        
        JButton cancelButton = new JButton("❌ Cancel");
        cancelButton.setBackground(new Color(231, 76, 60));
        cancelButton.setForeground(Color.WHITE);

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedStudent = (String) studentCombo.getSelectedItem();
                String selectedCourse = (String) courseCombo.getSelectedItem();
                String selectedAssignment = (String) assignmentCombo.getSelectedItem();
                String grade = (String) gradeCombo.getSelectedItem();
                String comments = commentsArea.getText().trim();
                
                if (selectedStudent == null || selectedCourse == null || selectedAssignment == null) {
                    JOptionPane.showMessageDialog(dialog, 
                        "Please select student, course, and assignment.",
                        "Validation Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Extract student ID and assignment ID from the combo box values
                String studentId = selectedStudent.split(" - ")[0];
                String courseId = selectedCourse.split(" - ")[0];
                int assignmentId = Integer.parseInt(selectedAssignment.split(" - ")[0]);
                
                // Save to database
                boolean success = DatabaseConnection.updateGrade(studentId, courseId, assignmentId, grade, comments);
                
                if (success) {
                    JOptionPane.showMessageDialog(dialog, 
                        "Grade added successfully!\n\n" +
                        "Student: " + selectedStudent + "\n" +
                        "Course: " + selectedCourse + "\n" +
                        "Grade: " + grade,
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    refreshGradesTab();
                } else {
                    JOptionPane.showMessageDialog(dialog, 
                        "Failed to add grade to database.",
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private boolean updateGradeInDatabase(String studentName, String courseCode, String assignmentName, String grade, String comments) {
        // This method would update the grade in the database
        // For now, we'll simulate success
        System.out.println("Updating grade for: " + studentName + ", Course: " + courseCode + 
                          ", Assignment: " + assignmentName + ", Grade: " + grade);
        
        // In real implementation, you would call DatabaseConnection.updateGrade()
        // with the appropriate parameters
        
        return true; // Simulate success
    }

   private void refreshGradesTab() {
        // Get the grades panel and table model
        JPanel gradesPanel = (JPanel) tabbedPane.getComponentAt(3);
        JScrollPane scrollPane = (JScrollPane) gradesPanel.getComponent(0);
        JViewport viewport = scrollPane.getViewport();
        JTable gradesTable = (JTable) viewport.getView();
        DefaultTableModel model = (DefaultTableModel) gradesTable.getModel();
        
        // Clear existing data
        model.setRowCount(0);
        
        // Reload grades from database
        final List<Object[]> grades = DatabaseConnection.getInstructorGrades(instructor.getIdentifier());
        for (Object[] grade : grades) {
            model.addRow(new Object[]{
                grade[0], grade[1], grade[2], 
                grade[3], grade[4], "Edit Grade"
            });
        }
        
        // Refresh statistics
        JPanel statsPanel = (JPanel) gradesPanel.getComponent(2); // Get the stats panel
        gradesPanel.remove(statsPanel); // Remove old stats
        gradesPanel.add(createGradeStatsPanel(grades), BorderLayout.SOUTH); // Add new stats
        
        // Refresh the display
        model.fireTableDataChanged();
        gradesTable.repaint();
        gradesPanel.revalidate();
        gradesPanel.repaint();
        
        JOptionPane.showMessageDialog(this, 
            "Grades list refreshed with " + grades.size() + " grades!", 
            "Refresh", 
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

        JLabel titleLabel = new JLabel("📚 My Courses", JLabel.LEFT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        coursesPanel.add(titleLabel, BorderLayout.NORTH);

        // Courses table
        String[] columns = {"Course Code", "Course Name", "Schedule", "Enrolled Students", "Actions"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        // Load courses from database WITH DEBUG
        System.out.println("=== DEBUG: Loading courses for instructor: " + instructor.getIdentifier() + " ===");
        List<Object[]> courses = DatabaseConnection.getInstructorCourses(instructor.getIdentifier());
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
            model.addRow(new Object[]{"No courses", "assigned to you", "Contact admin", "to get courses", "assigned", "N/A"});
        }

        JTable coursesTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(coursesTable);
        coursesPanel.add(scrollPane, BorderLayout.CENTER);

        tabbedPane.addTab("📚 Courses", coursesPanel);
    }

    // Dialog methods
    private void showCreateAssignmentDialog() {
        final JDialog dialog = new JDialog(this, "Create New Assignment", true);
        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Assignment Name:"));
        final JTextField nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Course:"));
        final JComboBox<String> courseCombo = new JComboBox<String>();
        final List<Object[]> courses = DatabaseConnection.getInstructorCourses(instructor.getIdentifier());
        for (Object[] course : courses) {
            courseCombo.addItem(course[0] + " - " + course[1]);
        }
        panel.add(courseCombo);

        panel.add(new JLabel("Due Date:"));
        final JTextField dateField = new JTextField();
        panel.add(dateField);

        panel.add(new JLabel("Total Points:"));
        final JTextField pointsField = new JTextField();
        panel.add(pointsField);

        panel.add(new JLabel("Description:"));
        final JTextArea descArea = new JTextArea(3, 20);
        JScrollPane scrollPane = new JScrollPane(descArea);
        panel.add(scrollPane);

        JButton createBtn = new JButton("Create Assignment");
        createBtn.setBackground(new Color(46, 204, 113));
        createBtn.setForeground(Color.WHITE);

        createBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Save assignment to database
                boolean success = DatabaseConnection.createAssignment(
                    nameField.getText(),
                    courses.get(courseCombo.getSelectedIndex())[0].toString(),
                    dateField.getText(),
                    Integer.parseInt(pointsField.getText()),
                    descArea.getText()
                );

                if (success) {
                    JOptionPane.showMessageDialog(dialog, "Assignment created successfully!");
                    dialog.dispose();
                    refreshAssignmentsTab();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to create assignment.");
                }
            }
        });

        dialog.add(panel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(createBtn);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

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
            "ID: " + instructor.getIdentifier() + "\n" +
            "Department: " + instructor.getLocation() + "\n" +
            "Email: " + instructor.getContact() + "\n" +
            "Status: " + instructor.getStatus() + "\n" +
            "Assigned Since: " + instructor.getAssignedSince(),
            "My Profile",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void refreshAssignmentsTab() {
        // Get the assignments panel and table model
        JPanel assignmentsPanel = (JPanel) tabbedPane.getComponentAt(1);
        JScrollPane scrollPane = (JScrollPane) assignmentsPanel.getComponent(0);
        JViewport viewport = scrollPane.getViewport();
        JTable assignmentsTable = (JTable) viewport.getView();
        DefaultTableModel model = (DefaultTableModel) assignmentsTable.getModel();
        
        // Clear existing data
        model.setRowCount(0);
        
        // Reload assignments from database
        List<Object[]> assignments = DatabaseConnection.getInstructorAssignments(instructor.getIdentifier());
        for (Object[] assignment : assignments) {
            model.addRow(new Object[]{
                assignment[0], assignment[1], assignment[2], 
                assignment[3], assignment[4], "View/Edit"
            });
        }
        
        // Refresh the display
        model.fireTableDataChanged();
        assignmentsTable.repaint();
        
        JOptionPane.showMessageDialog(this, 
            "Assignments list refreshed with " + assignments.size() + " assignments!", 
            "Refresh", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    // Logout functionality
    private void setupLogoutButton() {
        JButton btnLogout = new JButton("Logout");
        btnLogout.setBackground(new Color(220, 20, 60));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        
        btnLogout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(
                    InstructorControlPanel.this,
                    "Are you sure you want to logout?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION
                );
                
                if (confirm == JOptionPane.YES_OPTION) {
                    dispose();
                    // Here you would typically return to login screen
                    JOptionPane.showMessageDialog(null, 
                        "Logged out successfully!", 
                        "Logout", 
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        
        // Add logout button to a panel if needed
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutPanel.setBackground(new Color(240, 248, 255));
        logoutPanel.add(btnLogout);
    }

    

    private void refreshAllTabs() {
        refreshAssignmentsTab();
        refreshGradesTab();
        // Add refresh for other tabs if needed
        
        JOptionPane.showMessageDialog(this, 
            "All data refreshed from database!", 
            "Refresh Complete", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void main(String[] args) {
        // Test with a real instructor object
        final Instructor demoInstructor = new Instructor();
        demoInstructor.setName("Dr. Jean Uwimana");
        demoInstructor.setIdentifier("INST001");
        demoInstructor.setLocation("Computer Science Department");
        demoInstructor.setContact("jean.uwimana@university.edu");
        demoInstructor.setStatus("Active");
        demoInstructor.setUsername("juwimana");
        demoInstructor.setAssignedSince(new java.util.Date());
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new InstructorControlPanel(demoInstructor).setVisible(true);
            }
        });
    }
}