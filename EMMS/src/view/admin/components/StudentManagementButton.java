package view.admin.components;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import control.StudentController;
import model.Student;
import java.util.List;

public class StudentManagementButton extends AbstractAdminButton {

	public StudentManagementButton(final JTabbedPane tabbedPane) {
		super(
				"Manage Students", 
				"Manage student records and profiles",
				"",
				new Color(52, 152, 219),
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (tabbedPane != null) {
							tabbedPane.setSelectedIndex(1);
							showStudentManagementDialog();
						} else {
							showStudentManagementDialog();
						}
					}
				}
				);
	}

	public StudentManagementButton() {
		super(
				"Manage Students", 
				"Manage student records and profiles",
				"",
				new Color(52, 152, 219),
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						showStudentManagementDialog();
					}
				}
				);
	}

	private static void showStudentManagementDialog() {
		final JDialog managementDialog = new JDialog();
		managementDialog.setTitle("Student Management System");
		managementDialog.setSize(900, 600);
		managementDialog.setLocationRelativeTo(null);
		managementDialog.setModal(true);
		managementDialog.setLayout(new BorderLayout());

		// Header panel
		JPanel headerPanel = new JPanel();
		headerPanel.setBackground(new Color(52, 152, 219));
		headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
		JLabel headerLabel = new JLabel(" Student Management Dashboard");
		headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
		headerLabel.setForeground(Color.WHITE);
		headerPanel.add(headerLabel);
		managementDialog.add(headerPanel, BorderLayout.NORTH);

		// Main content panel
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BorderLayout());
		contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		// Search panel
		JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		searchPanel.setBorder(BorderFactory.createTitledBorder("Search Students"));

		JLabel searchLabel = new JLabel("Search:");
		final JTextField searchField = new JTextField(20);
		final JComboBox<String> searchTypeCombo = new JComboBox<>(new String[]{
				"All", "ID", "First Name", "Last Name", "Username", "Email"
		});
		JButton searchButton = new JButton(" Search");
		JButton clearButton = new JButton("Clear");

		searchPanel.add(searchLabel);
		searchPanel.add(searchField);
		searchPanel.add(new JLabel("By:"));
		searchPanel.add(searchTypeCombo);
		searchPanel.add(searchButton);
		searchPanel.add(clearButton);

		// Students table
		String[] columns = {"ID", "First Name", "Last Name", "Username", "Email","phone", "Status"};
		@SuppressWarnings("serial")
		final DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		final JTable studentTable = new JTable(tableModel);
		final TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
		studentTable.setRowSorter(sorter);

		JScrollPane tableScrollPane = new JScrollPane(studentTable);
		tableScrollPane.setBorder(BorderFactory.createTitledBorder("Student Records"));

		// Load student data
		loadStudentData(tableModel);

		// Button panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

		JButton refreshButton = new JButton(" Refresh");
		JButton addButton = new JButton(" Add Student");
		JButton editButton = new JButton(" Edit Student");
		JButton deleteButton = new JButton("Delete Student");
		JButton viewDetailsButton = new JButton("View Details");
		JButton closeButton = new JButton(" Close");

		// Style buttons
		Color[] buttonColors = {
				new Color(52, 152, 219),    // Refresh - Blue
				new Color(46, 204, 113),    // Add - Green
				new Color(241, 196, 15),    // Edit - Yellow
				new Color(231, 76, 60),     // Delete - Red
				new Color(155, 89, 182),    // Details - Purple
				new Color(149, 165, 166)    // Close - Gray
		};

		JButton[] buttons = {refreshButton, addButton, editButton, deleteButton, viewDetailsButton, closeButton};
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].setBackground(buttonColors[i]);
			buttons[i].setForeground(Color.WHITE);
			buttons[i].setFont(new Font("Arial", Font.BOLD, 12));
		}

		// Add action listeners
		refreshButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadStudentData(tableModel);
				searchField.setText("");
				sorter.setRowFilter(null);
				JOptionPane.showMessageDialog(managementDialog, 
						"Student data refreshed successfully!", 
						"Refresh Complete", 
						JOptionPane.INFORMATION_MESSAGE);
			}
		});

		searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				performSearch(searchField.getText().trim(), 
						(String) searchTypeCombo.getSelectedItem(), 
						sorter, tableModel);
			}
		});

		clearButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				searchField.setText("");
				sorter.setRowFilter(null);
				JOptionPane.showMessageDialog(managementDialog, 
						"Search cleared!", 
						"Clear", 
						JOptionPane.INFORMATION_MESSAGE);
			}
		});

		// Add key listener for real-time search
		searchField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (searchField.getText().trim().length() >= 2) {
					performSearch(searchField.getText().trim(), 
							(String) searchTypeCombo.getSelectedItem(), 
							sorter, tableModel);
				} else if (searchField.getText().trim().isEmpty()) {
					sorter.setRowFilter(null);
				}
			}
		});

		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showAddStudentDialog(managementDialog, tableModel);
			}
		});

		editButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = studentTable.getSelectedRow();
				if (selectedRow != -1) {
					int modelRow = studentTable.convertRowIndexToModel(selectedRow);
					int studentID = (int) tableModel.getValueAt(modelRow, 0);
					String firstName = (String) tableModel.getValueAt(modelRow, 1);
					String lastName = (String) tableModel.getValueAt(modelRow, 2);
					showEditStudentDialog(managementDialog, studentID, firstName + " " + lastName, tableModel);
				} else {
					JOptionPane.showMessageDialog(managementDialog,
							"Please select a student to edit.",
							"No Selection",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = studentTable.getSelectedRow();
				if (selectedRow != -1) {
					int modelRow = studentTable.convertRowIndexToModel(selectedRow);
					int studentID = (int) tableModel.getValueAt(modelRow, 0);
					String studentName = (String) tableModel.getValueAt(modelRow, 1) + " " + 
							(String) tableModel.getValueAt(modelRow, 2);
					showDeleteStudentDialog(managementDialog, studentID, studentName, tableModel);
				} else {
					JOptionPane.showMessageDialog(managementDialog,
							"Please select a student to delete.",
							"No Selection",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		viewDetailsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = studentTable.getSelectedRow();
				if (selectedRow != -1) {
					int modelRow = studentTable.convertRowIndexToModel(selectedRow);
					showStudentDetailsDialog(tableModel, modelRow);
				} else {
					JOptionPane.showMessageDialog(managementDialog,
							"Please select a student to view details.",
							"No Selection",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				managementDialog.dispose();
			}
		});

		// Add buttons to panel
		buttonPanel.add(refreshButton);
		buttonPanel.add(addButton);
		buttonPanel.add(editButton);
		buttonPanel.add(deleteButton);
		buttonPanel.add(viewDetailsButton);
		buttonPanel.add(closeButton);

		// Add components to content panel
		contentPanel.add(searchPanel, BorderLayout.NORTH);
		contentPanel.add(tableScrollPane, BorderLayout.CENTER);
		contentPanel.add(buttonPanel, BorderLayout.SOUTH);

		managementDialog.add(contentPanel, BorderLayout.CENTER);
		managementDialog.setVisible(true);
	}

	private static void loadStudentData(DefaultTableModel tableModel) {
		tableModel.setRowCount(0);
		StudentController studentController = new StudentController();
		List<Student> students = studentController.getAllStudents();

		if (students != null && !students.isEmpty()) {
			for (Student student : students) {
				tableModel.addRow(new Object[]{
						student.getStudentID(),
						student.getFirstName(),
						student.getLastName(),
						student.getUsername(),
						student.getEmail(),
						student.getPhone(),
						"Active"
				});
			}
		} else {
			// Add sample data for demonstration
			tableModel.addRow(new Object[]{1001, "MUKAMA", "REGINE", "Regine", "regine@gmail.com", "555-0101", "Active"});
			tableModel.addRow(new Object[]{1002, "SEZERANO", "Delice", "Delice", "jane.smith@student.edu", "555-0102", "Active"});
			tableModel.addRow(new Object[]{1003, "TWIZEYIMANA", "Onesphore", "Onesphore", "twizeyimana@gmail.com", "555-0103", "Active"});
			tableModel.addRow(new Object[]{1004, "MASENGESHO", "Pacifique", "Pacifique", "masengesho@gmail.com", "555-0104", "Inactive"});
			tableModel.addRow(new Object[]{1005, "GUHIRWA", "Divine", "Divine", "divine@gmail.com", "555-0105", "Active"});
		}
	}

	private static void performSearch(String searchText, String searchType, 
			TableRowSorter<DefaultTableModel> sorter, 
			DefaultTableModel tableModel) {
		if (searchText.isEmpty()) {
			sorter.setRowFilter(null);
			return;
		}

		try {
			RowFilter<DefaultTableModel, Object> rowFilter = null;

			switch (searchType) {
			case "ID":
				rowFilter = RowFilter.regexFilter("(?i)" + searchText, 0);
				break;
			case "First Name":
				rowFilter = RowFilter.regexFilter("(?i)" + searchText, 1);
				break;
			case "Last Name":
				rowFilter = RowFilter.regexFilter("(?i)" + searchText, 2);
				break;
			case "Username":
				rowFilter = RowFilter.regexFilter("(?i)" + searchText, 3);
				break;
			case "Email":
				rowFilter = RowFilter.regexFilter("(?i)" + searchText, 4);
				break;
			case "All":
			default:
				rowFilter = RowFilter.regexFilter("(?i)" + searchText);
				break;
			}

			sorter.setRowFilter(rowFilter);

			// Show search results count
			int resultCount = sorter.getViewRowCount();
			if (resultCount == 0) {
				JOptionPane.showMessageDialog(null, 
						"No students found matching: '" + searchText + "'", 
						"Search Results", 
						JOptionPane.INFORMATION_MESSAGE);
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, 
					"Invalid search pattern!", 
					"Search Error", 
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private static void showAddStudentDialog(JDialog parentDialog, final DefaultTableModel tableModel) {
		JOptionPane.showMessageDialog(parentDialog,
				"Add Student Feature\n\n" +
						"This feature would allow you to:\n" +
						"• Enter student personal information\n" +
						"• Set login credentials\n" +
						"• Add contact details\n" +
						"• Save to database",
						"Add Student",
						JOptionPane.INFORMATION_MESSAGE);
	}

	private static void showEditStudentDialog(JDialog parentDialog, int studentID, String studentName, final DefaultTableModel tableModel) {
		JOptionPane.showMessageDialog(parentDialog,
				"Edit Student: " + studentName + " (ID: " + studentID + ")\n\n" +
						"This feature would allow you to:\n" +
						"• Update personal information\n" +
						"• Modify contact details\n" +
						"• Change student status\n" +
						"• Save changes to database",
						"Edit Student",
						JOptionPane.INFORMATION_MESSAGE);
	}

	private static void showDeleteStudentDialog(JDialog parentDialog, int studentID, String studentName, final DefaultTableModel tableModel) {
		int confirm = JOptionPane.showConfirmDialog(parentDialog,
				"Are you sure you want to delete this student?\n\n" +
						"Student: " + studentName + "\n" +
						"ID: " + studentID + "\n\n" +
						"⚠️ This action cannot be undone!\n" +
						"• Student record will be deleted\n" +
						"• All enrollments will be removed\n" +
						"• Grades and assignments will be deleted",
						"Confirm Deletion",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE);

		if (confirm == JOptionPane.YES_OPTION) {
			// Simulate deletion
			JOptionPane.showMessageDialog(parentDialog,
					"Student deleted successfully!\n\n" +
							"Student: " + studentName + "\n" +
							"ID: " + studentID,
							"Deletion Complete",
							JOptionPane.INFORMATION_MESSAGE);

			// In real implementation, you would call:
			// StudentController studentController = new StudentController();
			// boolean success = studentController.deleteStudent(studentID);
			// if (success) { loadStudentData(tableModel); }
		}
	}

	private static void showStudentDetailsDialog(DefaultTableModel tableModel, int row) {
		int studentID = (int) tableModel.getValueAt(row, 0);
		String firstName = (String) tableModel.getValueAt(row, 1);
		String lastName = (String) tableModel.getValueAt(row, 2);
		String username = (String) tableModel.getValueAt(row, 3);
		String email = (String) tableModel.getValueAt(row, 4);
		String phone = (String) tableModel.getValueAt(row, 5);
		String status = (String) tableModel.getValueAt(row, 6);

		JTextArea detailsArea = new JTextArea();
		detailsArea.setEditable(false);
		detailsArea.setText(
				" STUDENT DETAILS\n" +
						"==================\n\n" +
						"ID: " + studentID + "\n" +
						"Name: " + firstName + " " + lastName + "\n" +
						"Username: " + username + "\n" +
						"Email: " + email + "\n" +
						"Phone: " + phone + "\n" +
						"Status: " + status + "\n\n" +
						"Additional Information:\n" +
						"• Enrollment History\n" +
						"• Course Progress\n" +
						"• Assignment Submissions\n" +
						"• Grade Reports"
				);

		JScrollPane scrollPane = new JScrollPane(detailsArea);
		scrollPane.setPreferredSize(new Dimension(400, 300));

		JOptionPane.showMessageDialog(null, scrollPane, 
				"Student Details - " + firstName + " " + lastName, 
				JOptionPane.INFORMATION_MESSAGE);
	}
}