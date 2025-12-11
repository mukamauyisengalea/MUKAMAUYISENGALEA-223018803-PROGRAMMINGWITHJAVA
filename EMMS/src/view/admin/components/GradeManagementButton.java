package view.admin.components;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GradeManagementButton extends AbstractAdminButton {

	public GradeManagementButton() {
		super(
				" Grade Management", 
				"Handle grade entry and calculations",
				"",
				new Color(22, 160, 133), // Teal
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// Show grade management dialog
						showGradeManagementDialog();
					}
				}
				);
	}

	private static void showGradeManagementDialog() {
		final JDialog dialog = new JDialog();
		dialog.setTitle("Grade Management System");
		dialog.setSize(700, 600);
		dialog.setLocationRelativeTo(null);
		dialog.setModal(true);

		JTabbedPane tabbedPane = new JTabbedPane();

		// Tab 1: Grade Entry
		tabbedPane.addTab("Enter Grades", createGradeEntryPanel());

		// Tab 2: View Grades
		tabbedPane.addTab("View Grades", createViewGradesPanel());

		// Tab 3: GPA Calculation
		tabbedPane.addTab("GPA Calculator", createGPAPanel());

		// Tab 4: Transcripts
		tabbedPane.addTab("Generate Transcripts", createTranscriptsPanel());

		dialog.add(tabbedPane);
		dialog.setVisible(true);
	}

	private static JPanel createGradeEntryPanel() {
		final JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// Header
		JLabel headerLabel = new JLabel("Grade Entry System", JLabel.CENTER);
		headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
		panel.add(headerLabel, BorderLayout.NORTH);

		// Grade entry form
		JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
		formPanel.setBorder(BorderFactory.createTitledBorder("Enter Student Grade"));

		final JComboBox<String> studentCombo = new JComboBox<>(new String[]{
				"Select Student", "John Doe (S1001)", "Jane Smith (S1002)", "Mike Johnson (S1003)"
		});

		final JComboBox<String> courseCombo = new JComboBox<>(new String[]{
				"Select Course", "CS101 - Programming", "MATH201 - Calculus", "ENG101 - English"
		});

		final JComboBox<String> assignmentCombo = new JComboBox<>(new String[]{
				"Select Assignment", "Midterm Exam", "Final Exam", "Assignment 1", "Quiz 1"
		});

		final JComboBox<String> gradeCombo = new JComboBox<>(new String[]{
				"Select Grade", "A", "A-", "B+", "B", "B-", "C+", "C", "C-", "D", "F"
		});

		final JTextField scoreField = new JTextField();
		scoreField.setToolTipText("Enter numerical score (0-100)");

		formPanel.add(new JLabel("Student:"));
		formPanel.add(studentCombo);
		formPanel.add(new JLabel("Course:"));
		formPanel.add(courseCombo);
		formPanel.add(new JLabel("Assignment:"));
		formPanel.add(assignmentCombo);
		formPanel.add(new JLabel("Letter Grade:"));
		formPanel.add(gradeCombo);
		formPanel.add(new JLabel("Numerical Score:"));
		formPanel.add(scoreField);

		panel.add(formPanel, BorderLayout.CENTER);

		// Buttons
		JPanel buttonPanel = new JPanel(new FlowLayout());
		JButton saveGradeButton = new JButton(" Save Grade");
		JButton clearButton = new JButton(" Clear");

		saveGradeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String student = (String) studentCombo.getSelectedItem();
				String course = (String) courseCombo.getSelectedItem();
				String assignment = (String) assignmentCombo.getSelectedItem();
				String grade = (String) gradeCombo.getSelectedItem();
				String score = scoreField.getText();

				if (student.equals("Select Student") || course.equals("Select Course") || 
						assignment.equals("Select Assignment") || grade.equals("Select Grade") || 
						score.isEmpty()) {
					JOptionPane.showMessageDialog(panel, "Please fill in all fields!");
					return;
				}

				try {
					double scoreValue = Double.parseDouble(score);
					if (scoreValue < 0 || scoreValue > 100) {
						JOptionPane.showMessageDialog(panel, "Score must be between 0 and 100!");
						return;
					}

					// Simulate saving grade
					JOptionPane.showMessageDialog(panel, 
							"Grade saved successfully!\n\n" +
									"Student: " + student + "\n" +
									"Course: " + course + "\n" +
									"Assignment: " + assignment + "\n" +
									"Grade: " + grade + "\n" +
									"Score: " + score + "%");

					// Clear form
					studentCombo.setSelectedIndex(0);
					courseCombo.setSelectedIndex(0);
					assignmentCombo.setSelectedIndex(0);
					gradeCombo.setSelectedIndex(0);
					scoreField.setText("");

				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(panel, "Please enter a valid numerical score!");
				}
			}
		});

		clearButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				studentCombo.setSelectedIndex(0);
				courseCombo.setSelectedIndex(0);
				assignmentCombo.setSelectedIndex(0);
				gradeCombo.setSelectedIndex(0);
				scoreField.setText("");
			}
		});

		buttonPanel.add(saveGradeButton);
		buttonPanel.add(clearButton);
		panel.add(buttonPanel, BorderLayout.SOUTH);

		return panel;
	}

	private static JPanel createViewGradesPanel() {
		final JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// Header
		JLabel headerLabel = new JLabel("Student Grades Overview", JLabel.CENTER);
		headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
		panel.add(headerLabel, BorderLayout.NORTH);

		// Filter panel
		JPanel filterPanel = new JPanel(new FlowLayout());
		filterPanel.add(new JLabel("Filter by:"));

		final JComboBox<String> filterCombo = new JComboBox<>(new String[]{
				"All Students", "By Course", "By Assignment", "By Grade"
		});

		JButton filterButton = new JButton("Apply Filter");
		JButton exportButton = new JButton("Export to CSV");

		filterPanel.add(filterCombo);
		filterPanel.add(filterButton);
		filterPanel.add(exportButton);

		panel.add(filterPanel, BorderLayout.NORTH);

		// Grades table
		String[] columns = {"Student ID", "Student Name", "Course", "Assignment", "Grade", "Score %", "Date"};
		Object[][] data = {
				{"S1001", "John Doe", "CS101", "Midterm Exam", "A", "92.5", "2024-12-15"},
				{"S1002", "Jane Smith", "CS101", "Midterm Exam", "B+", "88.0", "2024-12-15"},
				{"S1003", "Mike Johnson", "MATH201", "Quiz 1", "A-", "90.0", "2024-12-10"},
				{"S1001", "John Doe", "MATH201", "Quiz 1", "B", "85.0", "2024-12-10"},
				{"S1004", "Sarah Wilson", "ENG101", "Essay 1", "A", "95.0", "2024-12-12"}
		};

		final DefaultTableModel model = new DefaultTableModel(data, columns);
		final JTable gradesTable = new JTable(model);
		JScrollPane scrollPane = new JScrollPane(gradesTable);

		panel.add(scrollPane, BorderLayout.CENTER);

		// Action buttons for table
		JPanel actionPanel = new JPanel(new FlowLayout());
		JButton editGradeButton = new JButton(" Edit Grade");
		JButton deleteGradeButton = new JButton(" Delete Grade");
		JButton refreshButton = new JButton("Refresh");

		editGradeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = gradesTable.getSelectedRow();
				if (selectedRow >= 0) {
					String studentName = (String) model.getValueAt(selectedRow, 1);
					String course = (String) model.getValueAt(selectedRow, 2);
					String grade = (String) model.getValueAt(selectedRow, 4);

					JOptionPane.showMessageDialog(panel, 
							"Editing grade for:\n" +
									"Student: " + studentName + "\n" +
									"Course: " + course + "\n" +
									"Current Grade: " + grade + "\n\n" +
							"Grade editing feature would open here.");
				} else {
					JOptionPane.showMessageDialog(panel, "Please select a grade to edit!");
				}
			}
		});

		deleteGradeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = gradesTable.getSelectedRow();
				if (selectedRow >= 0) {
					int confirm = JOptionPane.showConfirmDialog(panel,
							"Are you sure you want to delete this grade entry?",
							"Confirm Delete", JOptionPane.YES_NO_OPTION);

					if (confirm == JOptionPane.YES_OPTION) {
						model.removeRow(selectedRow);
						JOptionPane.showMessageDialog(panel, "Grade entry deleted successfully!");
					}
				} else {
					JOptionPane.showMessageDialog(panel, "Please select a grade to delete!");
				}
			}
		});

		refreshButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(panel, "Refreshing grade data...");
				// In real implementation, this would reload from database
			}
		});

		filterButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String filter = (String) filterCombo.getSelectedItem();
				JOptionPane.showMessageDialog(panel, "Applying filter: " + filter);
			}
		});

		exportButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(panel, 
						"Exporting grade data to CSV file...\n" +
						"File will be saved in Downloads folder.");
			}
		});

		actionPanel.add(editGradeButton);
		actionPanel.add(deleteGradeButton);
		actionPanel.add(refreshButton);

		panel.add(actionPanel, BorderLayout.SOUTH);

		return panel;
	}

	private static JPanel createGPAPanel() {
		final JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// Header
		JLabel headerLabel = new JLabel("GPA Calculation System", JLabel.CENTER);
		headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
		panel.add(headerLabel, BorderLayout.NORTH);

		// GPA Calculator
		JPanel calculatorPanel = new JPanel(new GridLayout(6, 2, 10, 10));
		calculatorPanel.setBorder(BorderFactory.createTitledBorder("Calculate GPA"));

		final JComboBox<String> studentCombo = new JComboBox<>(new String[]{
				"Select Student", "John Doe (S1001)", "Jane Smith (S1002)", "Mike Johnson (S1003)"
		});

		final JComboBox<String> semesterCombo = new JComboBox<>(new String[]{
				"Select Semester", "Fall 2024", "Spring 2024", "Fall 2023", "Spring 2023"
		});

		JTextField currentGPALabel = new JTextField("3.45");
		currentGPALabel.setEditable(false);
		currentGPALabel.setBackground(Color.LIGHT_GRAY);

		final JTextField calculatedGPALabel = new JTextField();
		calculatedGPALabel.setEditable(false);
		calculatedGPALabel.setBackground(Color.YELLOW);
		calculatedGPALabel.setFont(new Font("Arial", Font.BOLD, 14));

		calculatorPanel.add(new JLabel("Student:"));
		calculatorPanel.add(studentCombo);
		calculatorPanel.add(new JLabel("Semester:"));
		calculatorPanel.add(semesterCombo);
		calculatorPanel.add(new JLabel("Current Overall GPA:"));
		calculatorPanel.add(currentGPALabel);
		calculatorPanel.add(new JLabel("Calculated Semester GPA:"));
		calculatorPanel.add(calculatedGPALabel);

		// Sample grades for calculation
		calculatorPanel.add(new JLabel("Sample Grades:"));
		JTextArea sampleGrades = new JTextArea("CS101: A (4.0)\nMATH201: B+ (3.3)\nENG101: A- (3.7)");
		sampleGrades.setEditable(false);
		sampleGrades.setBackground(Color.LIGHT_GRAY);

		calculatorPanel.add(new JScrollPane(sampleGrades));

		panel.add(calculatorPanel, BorderLayout.CENTER);

		// Calculation buttons
		JPanel calcButtonPanel = new JPanel(new FlowLayout());
		JButton calculateButton = new JButton(" Calculate GPA");
		JButton resetButton = new JButton("🔄 Reset");

		calculateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String student = (String) studentCombo.getSelectedItem();
				String semester = (String) semesterCombo.getSelectedItem();

				if (student.equals("Select Student") || semester.equals("Select Semester")) {
					JOptionPane.showMessageDialog(panel, "Please select both student and semester!");
					return;
				}

				// Simulate GPA calculation
				double gpa = 3.67; // Sample calculated GPA
				calculatedGPALabel.setText(String.format("%.2f", gpa));
				calculatedGPALabel.setBackground(new Color(144, 238, 144)); // Light green

				JOptionPane.showMessageDialog(panel,
						"GPA Calculation Complete!\n\n" +
								"Student: " + student + "\n" +
								"Semester: " + semester + "\n" +
								"Calculated GPA: " + String.format("%.2f", gpa) + "\n" +
						"Academic Standing: Good");
			}
		});

		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				studentCombo.setSelectedIndex(0);
				semesterCombo.setSelectedIndex(0);
				calculatedGPALabel.setText("");
				calculatedGPALabel.setBackground(Color.YELLOW);
			}
		});

		calcButtonPanel.add(calculateButton);
		calcButtonPanel.add(resetButton);
		panel.add(calcButtonPanel, BorderLayout.SOUTH);

		return panel;
	}

	private static JPanel createTranscriptsPanel() {
		final JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// Header
		JLabel headerLabel = new JLabel("Transcript Generation", JLabel.CENTER);
		headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
		panel.add(headerLabel, BorderLayout.NORTH);

		// Transcript options
		JPanel optionsPanel = new JPanel(new GridLayout(4, 2, 10, 10));
		optionsPanel.setBorder(BorderFactory.createTitledBorder("Transcript Options"));

		final JComboBox<String> studentCombo = new JComboBox<>(new String[]{
				"Select Student", "John Doe (S1001)", "Jane Smith (S1002)", "Mike Johnson (S1003)"
		});

		final JComboBox<String> transcriptTypeCombo = new JComboBox<>(new String[]{
				"Official Transcript", "Unofficial Transcript", "Course Completion"
		});

		JCheckBox includeGradesCheck = new JCheckBox("Include Detailed Grades", true);
		JCheckBox includeGpaCheck = new JCheckBox("Include GPA Calculation", true);
		JCheckBox officialSealCheck = new JCheckBox("Add Official Seal", false);

		JTextField outputFormatField = new JTextField("PDF Document");
		outputFormatField.setEditable(false);

		optionsPanel.add(new JLabel("Student:"));
		optionsPanel.add(studentCombo);
		optionsPanel.add(new JLabel("Transcript Type:"));
		optionsPanel.add(transcriptTypeCombo);
		optionsPanel.add(new JLabel("Options:"));
		optionsPanel.add(includeGradesCheck);
		optionsPanel.add(new JLabel(""));
		optionsPanel.add(includeGpaCheck);
		optionsPanel.add(new JLabel(""));
		optionsPanel.add(officialSealCheck);
		optionsPanel.add(new JLabel("Output Format:"));
		optionsPanel.add(outputFormatField);

		panel.add(optionsPanel, BorderLayout.CENTER);

		// Preview area
		final JTextArea previewArea = new JTextArea(8, 50);
		previewArea.setEditable(false);
		previewArea.setText(
				"UNIVERSITY OF EDUCATION\n" +
						"OFFICIAL TRANSCRIPT\n\n" +
						"Student: John Doe\n" +
						"Student ID: S1001\n" +
						"Program: Computer Science\n" +
						"Enrollment Date: Fall 2023\n\n" +
						"COURSE WORK:\n" +
						"CS101 - Introduction to Programming - A - 4.0\n" +
						"MATH201 - Calculus I - B+ - 3.3\n" +
						"ENG101 - English Composition - A- - 3.7\n\n" +
						"Cumulative GPA: 3.67\n" +
						"Academic Standing: Good"
				);

		JScrollPane previewScroll = new JScrollPane(previewArea);
		previewScroll.setBorder(BorderFactory.createTitledBorder("Transcript Preview"));
		panel.add(previewScroll, BorderLayout.NORTH);

		// Action buttons
		JPanel actionPanel = new JPanel(new FlowLayout());
		JButton generateButton = new JButton(" Generate Transcript");
		JButton previewButton = new JButton(" Preview");
		JButton printButton = new JButton(" Print");
		JButton emailButton = new JButton(" Email Transcript");

		generateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String student = (String) studentCombo.getSelectedItem();
				if (student.equals("Select Student")) {
					JOptionPane.showMessageDialog(panel, "Please select a student!");
					return;
				}

				JOptionPane.showMessageDialog(panel,
						"Transcript Generated Successfully!\n\n" +
								"Student: " + student + "\n" +
								"Type: " + transcriptTypeCombo.getSelectedItem() + "\n" +
								"Format: PDF\n\n" +
								"File saved to: Downloads/" + student.replace(" ", "_") + "_Transcript.pdf");
			}
		});

		previewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String student = (String) studentCombo.getSelectedItem();
				if (!student.equals("Select Student")) {
					previewArea.setText(
							"UNIVERSITY OF EDUCATION\n" +
									"OFFICIAL TRANSCRIPT\n\n" +
									"Student: " + student + "\n" +
									"Student ID: S1001\n" +
									"Program: Computer Science\n" +
									"Enrollment Date: Fall 2023\n\n" +
									"COURSE WORK:\n" +
									"CS101 - Introduction to Programming - A - 4.0\n" +
									"MATH201 - Calculus I - B+ - 3.3\n" +
									"ENG101 - English Composition - A- - 3.7\n\n" +
									"Cumulative GPA: 3.67\n" +
									"Academic Standing: Good\n\n" +
									"Generated on: " + new java.util.Date()
							);
				}
			}
		});

		printButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(panel, "Sending transcript to printer...");
			}
		});

		emailButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String student = (String) studentCombo.getSelectedItem();
				if (student.equals("Select Student")) {
					JOptionPane.showMessageDialog(panel, "Please select a student!");
					return;
				}

				String email = JOptionPane.showInputDialog(panel, 
						"Enter email address to send transcript:", 
						"student@university.edu");

				if (email != null && !email.trim().isEmpty()) {
					JOptionPane.showMessageDialog(panel,
							"Transcript sent successfully!\n\n" +
									"To: " + email + "\n" +
									"Student: " + student + "\n" +
							"Transcript will be delivered within 24 hours.");
				}
			}
		});

		actionPanel.add(generateButton);
		actionPanel.add(previewButton);
		actionPanel.add(printButton);
		actionPanel.add(emailButton);

		panel.add(actionPanel, BorderLayout.SOUTH);

		return panel;
	}

	// Test method
	public static void main(String[] args) {
		// Test the grade management button
		JFrame testFrame = new JFrame("Grade Management Button Test");
		testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		testFrame.setSize(400, 300);
		testFrame.setLocationRelativeTo(null);

		GradeManagementButton gradeButton = new GradeManagementButton();
		JPanel panel = gradeButton.createCard();

		testFrame.add(panel);
		testFrame.setVisible(true);


	}
}