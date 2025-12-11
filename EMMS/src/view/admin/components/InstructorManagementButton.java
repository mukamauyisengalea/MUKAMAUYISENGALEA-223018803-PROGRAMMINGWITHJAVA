package view.admin.components;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import util.DatabaseConnection;

public class InstructorManagementButton extends AbstractAdminButton {

	public InstructorManagementButton(final JTabbedPane tabbedPane) {
		super(
				" Manage Instructors", 
				"Manage instructor accounts and assignments",
				"",
				new Color(46, 204, 113),
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (tabbedPane != null) {
							tabbedPane.setSelectedIndex(2);
							showInstructorManagementDialog();
						} else {
							showInstructorManagementDialog();
						}
					}
				}
				);
	}

	public InstructorManagementButton() {
		super(
				" Manage Instructors", 
				"Manage instructor accounts and assignments",
				"",
				new Color(46, 204, 113),
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						showInstructorManagementDialog();
					}
				}
				);
	}

	private static void showInstructorManagementDialog() {
		// Ensure instructors table exists
		DatabaseConnection.createInstructorsTable();

		final JDialog managementDialog = new JDialog();
		managementDialog.setTitle("Instructor Management System");
		managementDialog.setSize(900, 600);
		managementDialog.setLocationRelativeTo(null);
		managementDialog.setModal(true);
		managementDialog.setLayout(new BorderLayout());

		// Header panel
		JPanel headerPanel = new JPanel();
		headerPanel.setBackground(new Color(46, 204, 113));
		headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
		JLabel headerLabel = new JLabel("Instructor Management Dashboard");
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
		searchPanel.setBorder(BorderFactory.createTitledBorder("Search Instructors"));

		JLabel searchLabel = new JLabel("Search:");
		final JTextField searchField = new JTextField(20);
		final JComboBox<String> searchTypeCombo = new JComboBox<String>(new String[]{
				"All", "ID", "Name", "Email", "Department", "Status", "Courses"
		});
		JButton searchButton = new JButton(" Search");
		JButton clearButton = new JButton("Clear");
		JButton advancedButton = new JButton(" Advanced");

		searchPanel.add(searchLabel);
		searchPanel.add(searchField);
		searchPanel.add(new JLabel("By:"));
		searchPanel.add(searchTypeCombo);
		searchPanel.add(searchButton);
		searchPanel.add(clearButton);
		searchPanel.add(advancedButton);

		// Get instructor data from DATABASE
		String[] columns = {"ID", "Name", "Email", "Department", "Courses", "Status"};
		final List<Object[]> instructorData = DatabaseConnection.getAllInstructors();

		@SuppressWarnings("serial")
		final DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		for (Object[] row : instructorData) {
			tableModel.addRow(row);
		}

		final JTable instructorTable = new JTable(tableModel);
		final TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<DefaultTableModel>(tableModel);
		instructorTable.setRowSorter(sorter);
		instructorTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane tableScrollPane = new JScrollPane(instructorTable);
		tableScrollPane.setBorder(BorderFactory.createTitledBorder("Current Instructors"));

		// Button panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

		// Create buttons
		JButton refreshButton = new JButton(" Refresh");
		JButton addButton = new JButton(" Add Instructor");
		JButton deleteButton = new JButton(" Delete Instructor");
		JButton detailsButton = new JButton(" View Details");
		JButton statusButton = new JButton("Update Status");
		JButton assignCourseButton = new JButton("Assign Course");
		JButton closeButton = new JButton(" Close");

		// Style buttons
		Color[] buttonColors = {
				new Color(52, 152, 219),    // Refresh - Blue
				new Color(46, 204, 113),    // Add - Green
				new Color(231, 76, 60),     // Delete - Red
				new Color(155, 89, 182),    // Details - Purple
				new Color(241, 196, 15),    // Status - Yellow
				new Color(230, 126, 34),    // Assign Course - Orange
				new Color(149, 165, 166)    // Close - Gray
		};

		JButton[] buttons = {refreshButton, addButton, deleteButton, detailsButton, statusButton, assignCourseButton, closeButton};
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].setBackground(buttonColors[i]);
			buttons[i].setForeground(Color.WHITE);
			buttons[i].setFont(new Font("Arial", Font.BOLD, 12));
		}

		// Add action listeners for search functionality
		refreshButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				refreshInstructorTable(tableModel);
				searchField.setText("");
				sorter.setRowFilter(null);
				JOptionPane.showMessageDialog(managementDialog, 
						"Instructor data refreshed successfully!",
						"Refresh Complete", 
						JOptionPane.INFORMATION_MESSAGE);
			}
		});

		searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				performInstructorSearch(searchField.getText().trim(), 
						(String) searchTypeCombo.getSelectedItem(), 
						sorter, tableModel, managementDialog);
			}
		});

		clearButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				searchField.setText("");
				sorter.setRowFilter(null);
				JOptionPane.showMessageDialog(managementDialog, 
						"Search cleared! Showing all instructors.",
						"Clear Search", 
						JOptionPane.INFORMATION_MESSAGE);
			}
		});

		advancedButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showAdvancedSearchDialog(managementDialog, sorter);
			}
		});

		// Add key listener for real-time search
		searchField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (searchField.getText().trim().length() >= 2) {
					performInstructorSearch(searchField.getText().trim(), 
							(String) searchTypeCombo.getSelectedItem(), 
							sorter, tableModel, managementDialog);
				} else if (searchField.getText().trim().isEmpty()) {
					sorter.setRowFilter(null);
				}
			}
		});

		// Action listeners for other buttons
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showAddInstructorDialog(managementDialog, tableModel);
			}
		});

		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = instructorTable.getSelectedRow();
				if (selectedRow != -1) {
					int modelRow = instructorTable.convertRowIndexToModel(selectedRow);
					Integer instructorId = (Integer) tableModel.getValueAt(modelRow, 0);
					String instructorName = (String) tableModel.getValueAt(modelRow, 1);
					showDeleteInstructorDialog(managementDialog, instructorId, instructorName, tableModel);
				} else {
					JOptionPane.showMessageDialog(managementDialog,
							"Please select an instructor to delete.",
							"No Selection",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		detailsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = instructorTable.getSelectedRow();
				if (selectedRow != -1) {
					int modelRow = instructorTable.convertRowIndexToModel(selectedRow);
					Integer instructorId = (Integer) tableModel.getValueAt(modelRow, 0);
					String instructorName = (String) tableModel.getValueAt(modelRow, 1);
					String email = (String) tableModel.getValueAt(modelRow, 2);
					String department = (String) tableModel.getValueAt(modelRow, 3);
					String courses = (String) tableModel.getValueAt(modelRow, 4);
					String status = (String) tableModel.getValueAt(modelRow, 5);
					showInstructorDetailsDialog(instructorId, instructorName, email, department, courses, status);
				} else {
					JOptionPane.showMessageDialog(managementDialog,
							"Please select an instructor to view details.",
							"No Selection",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		statusButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = instructorTable.getSelectedRow();
				if (selectedRow != -1) {
					int modelRow = instructorTable.convertRowIndexToModel(selectedRow);
					Integer instructorId = (Integer) tableModel.getValueAt(modelRow, 0);
					String instructorName = (String) tableModel.getValueAt(modelRow, 1);
					String currentStatus = (String) tableModel.getValueAt(modelRow, 5);
					showUpdateStatusDialog(managementDialog, instructorId, instructorName, currentStatus, tableModel);
				} else {
					JOptionPane.showMessageDialog(managementDialog,
							"Please select an instructor to update status.",
							"No Selection",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		assignCourseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = instructorTable.getSelectedRow();
				if (selectedRow != -1) {
					int modelRow = instructorTable.convertRowIndexToModel(selectedRow);
					Integer instructorId = (Integer) tableModel.getValueAt(modelRow, 0);
					String instructorName = (String) tableModel.getValueAt(modelRow, 1);
					showAssignCourseDialog(managementDialog, instructorId, instructorName, tableModel);
				} else {
					JOptionPane.showMessageDialog(managementDialog,
							"Please select an instructor to assign course.",
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
		buttonPanel.add(deleteButton);
		buttonPanel.add(detailsButton);
		buttonPanel.add(statusButton);
		buttonPanel.add(assignCourseButton);
		buttonPanel.add(closeButton);

		// Add components to content panel
		contentPanel.add(searchPanel, BorderLayout.NORTH);
		contentPanel.add(tableScrollPane, BorderLayout.CENTER);
		contentPanel.add(buttonPanel, BorderLayout.SOUTH);

		managementDialog.add(contentPanel, BorderLayout.CENTER);

		// Statistics panel
		JPanel statsPanel = new JPanel(new GridLayout(1, 4, 10, 0));
		statsPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

		// Calculate real statistics
		int totalInstructors = instructorData.size();
		int activeCount = 0;
		int onLeaveCount = 0;
		int departmentCount = 0;

		java.util.Set<String> departments = new java.util.HashSet<String>();
		for (Object[] row : instructorData) {
			String status = (String) row[5];
			String department = (String) row[3];
			departments.add(department);

			if ("Active".equals(status)) {
				activeCount++;
			} else if ("On Leave".equals(status)) {
				onLeaveCount++;
			}
		}
		departmentCount = departments.size();

		statsPanel.add(createStatCard("Total Instructors", String.valueOf(totalInstructors), new Color(52, 152, 219)));
		statsPanel.add(createStatCard("Active", String.valueOf(activeCount), new Color(46, 204, 113)));
		statsPanel.add(createStatCard("On Leave", String.valueOf(onLeaveCount), new Color(241, 196, 15)));
		statsPanel.add(createStatCard("Departments", String.valueOf(departmentCount), new Color(155, 89, 182)));

		managementDialog.add(statsPanel, BorderLayout.SOUTH);
		managementDialog.setVisible(true);
	}

	// Search functionality method
	private static void performInstructorSearch(String searchText, String searchType, 
			TableRowSorter<DefaultTableModel> sorter, 
			DefaultTableModel tableModel, JDialog parentDialog) {
		if (searchText.isEmpty()) {
			sorter.setRowFilter(null);
			return;
		}

		try {
			RowFilter<DefaultTableModel, Object> rowFilter = null;

			if ("ID".equals(searchType)) {
				rowFilter = RowFilter.regexFilter("(?i)" + searchText, 0);
			} else if ("Name".equals(searchType)) {
				rowFilter = RowFilter.regexFilter("(?i)" + searchText, 1);
			} else if ("Email".equals(searchType)) {
				rowFilter = RowFilter.regexFilter("(?i)" + searchText, 2);
			} else if ("Department".equals(searchType)) {
				rowFilter = RowFilter.regexFilter("(?i)" + searchText, 3);
			} else if ("Status".equals(searchType)) {
				rowFilter = RowFilter.regexFilter("(?i)" + searchText, 5);
			} else if ("Courses".equals(searchType)) {
				rowFilter = RowFilter.regexFilter("(?i)" + searchText, 4);
			} else {
				rowFilter = RowFilter.regexFilter("(?i)" + searchText);
			}

			sorter.setRowFilter(rowFilter);

			int resultCount = sorter.getViewRowCount();
			if (resultCount == 0) {
				JOptionPane.showMessageDialog(parentDialog, 
						" No instructors found matching: '" + searchText + "'\n\n" +
								"Search Type: " + searchType + "\n" +
								"Try:\n" +
								"• Using different keywords\n" +
								"• Checking spelling\n" +
								"• Searching in 'All' fields", 
								"Search Results - No Matches", 
								JOptionPane.INFORMATION_MESSAGE);
			} else {
				if (searchText.length() >= 2) {
					String message = "Found " + resultCount + " instructor(s) matching: '" + searchText + "'";
					if (resultCount == 1) {
						message += "\nTip: Select the instructor to view details or perform actions.";
					}
					showTemporaryStatus(message, parentDialog, 3000);
				}
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(parentDialog, 
					" Invalid search pattern!\n\n" +
							"Please use simple text search without special characters.", 
							"Search Error", 
							JOptionPane.ERROR_MESSAGE);
		}
	}

	// Helper method to show temporary status message
	private static void showTemporaryStatus(String message, final JDialog parent, int duration) {
		final JLabel statusLabel = new JLabel(message, JLabel.CENTER);
		statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		statusLabel.setForeground(new Color(46, 204, 113));

		final JPanel statusPanel = new JPanel(new BorderLayout());
		statusPanel.setBackground(new Color(240, 248, 255));
		statusPanel.setBorder(BorderFactory.createLineBorder(new Color(46, 204, 113), 1));
		statusPanel.add(statusLabel, BorderLayout.CENTER);

		parent.add(statusPanel, BorderLayout.NORTH);
		parent.revalidate();
		parent.repaint();

		Timer timer = new Timer(duration, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.remove(statusPanel);
				parent.revalidate();
				parent.repaint();
			}
		});
		timer.setRepeats(false);
		timer.start();
	}

	// Enhanced refresh method
	private static void refreshInstructorTable(DefaultTableModel tableModel) {
		List<Object[]> instructorData = DatabaseConnection.getAllInstructors();
		tableModel.setRowCount(0);
		for (Object[] row : instructorData) {
			tableModel.addRow(row);
		}
	}

	// Advanced search dialog
	private static void showAdvancedSearchDialog(final JDialog parentDialog, final TableRowSorter<DefaultTableModel> sorter) {
		final JDialog searchDialog = new JDialog(parentDialog, "Advanced Instructor Search", true);
		searchDialog.setSize(400, 300);
		searchDialog.setLocationRelativeTo(parentDialog);
		searchDialog.setLayout(new BorderLayout());

		JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
		formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		formPanel.add(new JLabel("Department:"));
		final JComboBox<String> deptCombo = new JComboBox<String>(new String[]{
				"Any", "Computer Science", "Mathematics", "Physics", "Chemistry", 
				"Biology", "Engineering", "Business", "Arts"
		});
		formPanel.add(deptCombo);

		formPanel.add(new JLabel("Status:"));
		final JComboBox<String> statusCombo = new JComboBox<String>(new String[]{
				"Any", "Active", "On Leave", "Inactive"
		});
		formPanel.add(statusCombo);

		formPanel.add(new JLabel("Has Courses:"));
		final JComboBox<String> courseCombo = new JComboBox<String>(new String[]{
				"Any", "With Courses", "No Courses"
		});
		formPanel.add(courseCombo);

		formPanel.add(new JLabel("Name Contains:"));
		final JTextField nameField = new JTextField();
		formPanel.add(nameField);

		formPanel.add(new JLabel("Email Domain:"));
		final JTextField domainField = new JTextField();
		formPanel.add(domainField);

		searchDialog.add(formPanel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		JButton searchButton = new JButton("Advanced Search");
		JButton clearButton = new JButton(" Clear Filters");
		JButton cancelButton = new JButton(" Cancel");

		searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				performAdvancedSearch(deptCombo, statusCombo, courseCombo, nameField, domainField, sorter, searchDialog);
			}
		});

		clearButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sorter.setRowFilter(null);
				deptCombo.setSelectedIndex(0);
				statusCombo.setSelectedIndex(0);
				courseCombo.setSelectedIndex(0);
				nameField.setText("");
				domainField.setText("");
				JOptionPane.showMessageDialog(searchDialog, "All filters cleared!", "Clear", JOptionPane.INFORMATION_MESSAGE);
			}
		});

		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				searchDialog.dispose();
			}
		});

		buttonPanel.add(searchButton);
		buttonPanel.add(clearButton);
		buttonPanel.add(cancelButton);
		searchDialog.add(buttonPanel, BorderLayout.SOUTH);

		searchDialog.setVisible(true);
	}

	private static void performAdvancedSearch(JComboBox<String> deptCombo, JComboBox<String> statusCombo,
			JComboBox<String> courseCombo, JTextField nameField,
			JTextField domainField, TableRowSorter<DefaultTableModel> sorter,
			JDialog searchDialog) {
		try {
			// Create individual filter variables
			RowFilter<DefaultTableModel, Object> deptFilter = null;
			RowFilter<DefaultTableModel, Object> statusFilter = null;
			RowFilter<DefaultTableModel, Object> courseFilterObj = null;
			RowFilter<DefaultTableModel, Object> nameFilter = null;
			RowFilter<DefaultTableModel, Object> domainFilter = null;

			String dept = (String) deptCombo.getSelectedItem();
			String status = (String) statusCombo.getSelectedItem();
			String courseFilterType = (String) courseCombo.getSelectedItem();
			String name = nameField.getText().trim();
			String domain = domainField.getText().trim();

			// Create individual filters
			if (!"Any".equals(dept)) {
				deptFilter = RowFilter.regexFilter("(?i)" + dept, 3);
			}

			if (!"Any".equals(status)) {
				statusFilter = RowFilter.regexFilter("(?i)" + status, 5);
			}

			if ("With Courses".equals(courseFilterType)) {
				courseFilterObj = RowFilter.regexFilter("^(?!No Courses).*$", 4);
			} else if ("No Courses".equals(courseFilterType)) {
				courseFilterObj = RowFilter.regexFilter("(?i)^No Courses$", 4);
			}

			if (!name.isEmpty()) {
				nameFilter = RowFilter.regexFilter("(?i)" + name, 1);
			}

			if (!domain.isEmpty()) {
				domainFilter = RowFilter.regexFilter("(?i)" + domain, 2);
			}

			// Combine all non-null filters
			java.util.List<RowFilter<DefaultTableModel, Object>> allFilters = new java.util.ArrayList<>();
			if (deptFilter != null) allFilters.add(deptFilter);
			if (statusFilter != null) allFilters.add(statusFilter);
			if (courseFilterObj != null) allFilters.add(courseFilterObj);
			if (nameFilter != null) allFilters.add(nameFilter);
			if (domainFilter != null) allFilters.add(domainFilter);

			if (!allFilters.isEmpty()) {
				RowFilter<DefaultTableModel, Object> combinedFilter = RowFilter.andFilter(allFilters);
				sorter.setRowFilter(combinedFilter);

				int resultCount = sorter.getViewRowCount();
				JOptionPane.showMessageDialog(searchDialog,
						"Advanced search completed!\n\n" +
								"Found " + resultCount + " instructor(s) matching your criteria.",
								"Advanced Search Results",
								JOptionPane.INFORMATION_MESSAGE);

				searchDialog.dispose();
			} else {
				JOptionPane.showMessageDialog(searchDialog,
						"Please select at least one search criteria.",
						"No Criteria Selected",
						JOptionPane.WARNING_MESSAGE);
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(searchDialog,
					"Error performing advanced search: " + e.getMessage(),
					"Search Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private static JPanel createStatCard(String title, String value, Color color) {
		JPanel card = new JPanel();
		card.setLayout(new BorderLayout());
		card.setBackground(Color.WHITE);
		card.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(color, 2),
				BorderFactory.createEmptyBorder(10, 10, 10, 10)
				));

		JLabel titleLabel = new JLabel(title, JLabel.CENTER);
		titleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		titleLabel.setForeground(Color.DARK_GRAY);

		JLabel valueLabel = new JLabel(value, JLabel.CENTER);
		valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
		valueLabel.setForeground(color);

		card.add(titleLabel, BorderLayout.NORTH);
		card.add(valueLabel, BorderLayout.CENTER);

		return card;
	}

	private static void showAddInstructorDialog(final JDialog parentDialog, final DefaultTableModel tableModel) {
		final JDialog addDialog = new JDialog(parentDialog, "Add New Instructor", true);
		addDialog.setSize(450, 500);
		addDialog.setLocationRelativeTo(parentDialog);
		addDialog.setLayout(new BorderLayout());

		JPanel headerPanel = new JPanel();
		headerPanel.setBackground(new Color(46, 204, 113));
		headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
		JLabel headerLabel = new JLabel(" Add New Instructor");
		headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
		headerLabel.setForeground(Color.WHITE);
		headerPanel.add(headerLabel);
		addDialog.add(headerPanel, BorderLayout.NORTH);

		JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
		formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		formPanel.add(new JLabel("Instructor ID:*"));
		final JTextField idField = new JTextField();
		idField.setText("INST" + (100 + (int)(Math.random() * 900)));
		idField.setEditable(false);
		formPanel.add(idField);

		formPanel.add(new JLabel("Full Name:*"));
		final JTextField nameField = new JTextField();
		formPanel.add(nameField);

		formPanel.add(new JLabel("Email:*"));
		final JTextField emailField = new JTextField();
		formPanel.add(emailField);

		formPanel.add(new JLabel("Department:*"));
		final JComboBox<String> departmentCombo = new JComboBox<String>(new String[]{
				"Computer Science", "Mathematics", "Physics", "Chemistry", 
				"Biology", "Engineering", "Business", "Arts"
		});
		formPanel.add(departmentCombo);

		formPanel.add(new JLabel("Phone:"));
		final JTextField phoneField = new JTextField();
		formPanel.add(phoneField);

		formPanel.add(new JLabel("Office:"));
		final JTextField officeField = new JTextField();
		formPanel.add(officeField);

		addDialog.add(formPanel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		JButton saveButton = new JButton("Save Instructor");
		JButton cancelButton = new JButton(" Cancel");

		saveButton.setBackground(new Color(46, 204, 113));
		saveButton.setForeground(Color.WHITE);
		cancelButton.setBackground(new Color(231, 76, 60));
		cancelButton.setForeground(Color.WHITE);

		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String instructorId = idField.getText();
				String name = nameField.getText().trim();
				String email = emailField.getText().trim();
				String department = (String) departmentCombo.getSelectedItem();
				String phone = phoneField.getText().trim();
				String office = officeField.getText().trim();

				if (name.isEmpty() || email.isEmpty()) {
					JOptionPane.showMessageDialog(addDialog,
							"Please fill in all required fields (Name and Email).",
							"Validation Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				boolean success = DatabaseConnection.addInstructor(name, instructorId, email, department, phone, office);

				if (success) {
					JOptionPane.showMessageDialog(addDialog,
							"Instructor added successfully to database!\n\n" +
									"Name: " + name + "\n" +
									"ID: " + instructorId + "\n" +
									"Email: " + email + "\n" +
									"Department: " + department,
									"Success",
									JOptionPane.INFORMATION_MESSAGE);

					refreshInstructorTable(tableModel);
					addDialog.dispose();
				} else {
					JOptionPane.showMessageDialog(addDialog,
							" Failed to add instructor to database.\n" +
									"Possible reasons:\n" +
									"• Email already exists\n" +
									"• Database connection issue",
									"Database Error",
									JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addDialog.dispose();
			}
		});

		buttonPanel.add(saveButton);
		buttonPanel.add(cancelButton);
		addDialog.add(buttonPanel, BorderLayout.SOUTH);

		addDialog.setVisible(true);
	}

	private static void showDeleteInstructorDialog(JDialog parentDialog, int instructorId, String instructorName, final DefaultTableModel tableModel) {
		int confirm = JOptionPane.showConfirmDialog(parentDialog,
				"Are you sure you want to delete this instructor?\n\n" +
						"Instructor: " + instructorName + "\n" +
						"ID: " + instructorId + "\n\n" +
						"⚠️ This action cannot be undone!\n" +
						"• Course assignments will be removed\n" +
						"• Instructor record will be deleted permanently",
						"Confirm Deletion",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE);

		if (confirm == JOptionPane.YES_OPTION) {
			boolean success = DatabaseConnection.deleteInstructor(instructorId);

			if (success) {
				JOptionPane.showMessageDialog(parentDialog,
						" Instructor deleted successfully from database!\n\n" +
								"Instructor: " + instructorName + "\n" +
								"ID: " + instructorId,
								"Deletion Complete",
								JOptionPane.INFORMATION_MESSAGE);

				refreshInstructorTable(tableModel);
			} else {
				JOptionPane.showMessageDialog(parentDialog,
						" Failed to delete instructor from database.\n" +
								"Possible reasons:\n" +
								"• Database connection issue\n" +
								"• Instructor ID not found",
								"Deletion Failed",
								JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private static void showUpdateStatusDialog(final JDialog parentDialog, final int instructorId, final String instructorName, String currentStatus, final DefaultTableModel tableModel) {
		final JDialog statusDialog = new JDialog(parentDialog, "Update Instructor Status", true);
		statusDialog.setSize(350, 200);
		statusDialog.setLocationRelativeTo(parentDialog);
		statusDialog.setLayout(new BorderLayout());

		JPanel contentPanel = new JPanel(new BorderLayout());
		contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		JLabel infoLabel = new JLabel("Update status for: " + instructorName + " (" + instructorId + ")");
		infoLabel.setFont(new Font("Arial", Font.BOLD, 14));
		contentPanel.add(infoLabel, BorderLayout.NORTH);

		JPanel formPanel = new JPanel(new FlowLayout());
		formPanel.add(new JLabel("New Status:"));
		final JComboBox<String> statusCombo = new JComboBox<String>(new String[]{"Active", "On Leave", "Inactive"});
		statusCombo.setSelectedItem(currentStatus);
		formPanel.add(statusCombo);

		contentPanel.add(formPanel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		JButton updateButton = new JButton("Update Status");
		JButton cancelButton = new JButton("Cancel");

		updateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String newStatus = (String) statusCombo.getSelectedItem();
				boolean success = DatabaseConnection.updateInstructorStatus(instructorId, newStatus);

				if (success) {
					JOptionPane.showMessageDialog(statusDialog,
							" Status updated successfully!\n\n" +
									"Instructor: " + instructorName + "\n" +
									"New Status: " + newStatus,
									"Status Updated",
									JOptionPane.INFORMATION_MESSAGE);

					refreshInstructorTable(tableModel);
					statusDialog.dispose();
				} else {
					JOptionPane.showMessageDialog(statusDialog,
							" Failed to update status.",
							"Update Failed",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				statusDialog.dispose();
			}
		});

		buttonPanel.add(updateButton);
		buttonPanel.add(cancelButton);
		contentPanel.add(buttonPanel, BorderLayout.SOUTH);

		statusDialog.add(contentPanel);
		statusDialog.setVisible(true);
	}

	private static void showAssignCourseDialog(final JDialog parentDialog, final int instructorId, final String instructorName, final DefaultTableModel tableModel) {
		final JDialog assignDialog = new JDialog(parentDialog, "Assign Course to Instructor", true);
		assignDialog.setSize(400, 250);
		assignDialog.setLocationRelativeTo(parentDialog);
		assignDialog.setLayout(new BorderLayout());

		JPanel contentPanel = new JPanel(new BorderLayout());
		contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		JLabel infoLabel = new JLabel("Assign course to: " + instructorName + " (" + instructorId + ")");
		infoLabel.setFont(new Font("Arial", Font.BOLD, 14));
		contentPanel.add(infoLabel, BorderLayout.NORTH);

		JPanel formPanel = new JPanel(new FlowLayout());
		formPanel.add(new JLabel("Course Code:"));
		final JTextField courseField = new JTextField(10);
		formPanel.add(courseField);

		contentPanel.add(formPanel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		JButton assignButton = new JButton("Assign Course");
		JButton cancelButton = new JButton("Cancel");

		assignButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String courseCode = courseField.getText().trim();
				if (courseCode.isEmpty()) {
					JOptionPane.showMessageDialog(assignDialog,
							"Please enter a course code.",
							"Validation Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				try {
					int courseId = Integer.parseInt(courseCode);
					boolean success = DatabaseConnection.assignCourseToInstructor(instructorId, courseId);

					if (success) {
						JOptionPane.showMessageDialog(assignDialog,
								"Course assigned successfully!\n\n" +
										"Instructor: " + instructorName + "\n" +
										"Course: " + courseCode,
										"Assignment Complete",
										JOptionPane.INFORMATION_MESSAGE);

						refreshInstructorTable(tableModel);
						assignDialog.dispose();
					} else {
						JOptionPane.showMessageDialog(assignDialog,
								" Failed to assign course.\n" +
										"Possible reasons:\n" +
										"• Course code doesn't exist\n" +
										"• Database connection issue",
										"Assignment Failed",
										JOptionPane.ERROR_MESSAGE);
					}
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(assignDialog,
							" Invalid course code format.\n" +
									"Please enter a numeric course ID.",
									"Invalid Input",
									JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				assignDialog.dispose();
			}
		});

		buttonPanel.add(assignButton);
		buttonPanel.add(cancelButton);
		contentPanel.add(buttonPanel, BorderLayout.SOUTH);

		assignDialog.add(contentPanel);
		assignDialog.setVisible(true);
	}

	private static void showInstructorDetailsDialog(final int id, final String name, String email, 
			String department, String courses, String status) {
		Object[] fullInstructorDetails = DatabaseConnection.getInstructorDetails(id);
		int totalStudents = DatabaseConnection.getInstructorStudentCount(id);
		int activeAssignments = DatabaseConnection.getInstructorActiveAssignments(id);

		final JDialog detailsDialog = new JDialog();
		detailsDialog.setTitle("Instructor Details - " + name);
		detailsDialog.setSize(500, 550);
		detailsDialog.setLocationRelativeTo(null);
		detailsDialog.setModal(true);
		detailsDialog.setLayout(new BorderLayout());

		JPanel headerPanel = new JPanel();
		headerPanel.setBackground(new Color(52, 152, 219));
		headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
		JLabel headerLabel = new JLabel("Instructor Profile: " + name);
		headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
		headerLabel.setForeground(Color.WHITE);
		headerPanel.add(headerLabel);
		detailsDialog.add(headerPanel, BorderLayout.NORTH);

		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		JPanel basicInfoPanel = createSectionPanel("Basic Information");
		basicInfoPanel.add(createDetailRow("Instructor ID:", String.valueOf(id)));
		basicInfoPanel.add(createDetailRow("Full Name:", name));
		basicInfoPanel.add(createDetailRow("Email:", email));
		basicInfoPanel.add(createDetailRow("Department:", department));

		JPanel statusPanel = createDetailRow("Status:", status);
		JLabel statusLabel = (JLabel) ((JPanel) statusPanel.getComponent(1)).getComponent(0);
		if ("Active".equals(status)) {
			statusLabel.setForeground(new Color(46, 204, 113));
		} else if ("On Leave".equals(status)) {
			statusLabel.setForeground(new Color(230, 126, 34));
		} else {
			statusLabel.setForeground(new Color(231, 76, 60));
		}
		basicInfoPanel.add(statusPanel);

		if (fullInstructorDetails != null && fullInstructorDetails.length > 5) {
			JPanel contactPanel = createSectionPanel("Contact Information");
			String phone = fullInstructorDetails[4] != null ? fullInstructorDetails[4].toString() : "Not provided";
			String office = fullInstructorDetails[5] != null ? fullInstructorDetails[5].toString() : "Not assigned";

			contactPanel.add(createDetailRow("Phone:", phone));
			contactPanel.add(createDetailRow("Office:", office));
			contentPanel.add(contactPanel);
			contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		}

		JPanel coursePanel = createSectionPanel(" Course Assignments");
		String courseText = courses.isEmpty() ? "No courses assigned" : formatCourses(courses);
		JTextArea coursesArea = new JTextArea(courseText);
		coursesArea.setEditable(false);
		coursesArea.setLineWrap(true);
		coursesArea.setWrapStyleWord(true);
		coursesArea.setBackground(new Color(248, 249, 250));
		coursesArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		coursesArea.setFont(new Font("Arial", Font.PLAIN, 12));

		JScrollPane coursesScroll = new JScrollPane(coursesArea);
		coursesScroll.setPreferredSize(new Dimension(400, 80));
		coursePanel.add(coursesScroll);

		contentPanel.add(basicInfoPanel);
		contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		contentPanel.add(coursePanel);

		JPanel statsPanel = createSectionPanel("Teaching Statistics");
		int totalCourses = courses.isEmpty() ? 0 : courses.split(",").length;

		statsPanel.add(createDetailRow("Total Courses:", String.valueOf(totalCourses)));
		statsPanel.add(createDetailRow("Total Students:", String.valueOf(totalStudents)));
		statsPanel.add(createDetailRow("Active Assignments:", String.valueOf(activeAssignments)));

		contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		contentPanel.add(statsPanel);

		JScrollPane scrollPane = new JScrollPane(contentPanel);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		detailsDialog.add(scrollPane, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

		JButton closeButton = new JButton("Close");
		closeButton.setBackground(new Color(149, 165, 166));
		closeButton.setForeground(Color.WHITE);
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				detailsDialog.dispose();
			}
		});

		JButton editButton = new JButton("Edit Profile");
		editButton.setBackground(new Color(52, 152, 219));
		editButton.setForeground(Color.WHITE);
		editButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(detailsDialog,
						"Edit functionality would open here.\n\n" +
								"Instructor: " + name + "\n" +
								"ID: " + id,
								"Edit Instructor",
								JOptionPane.INFORMATION_MESSAGE);
			}
		});

		buttonPanel.add(editButton);
		buttonPanel.add(closeButton);
		detailsDialog.add(buttonPanel, BorderLayout.SOUTH);

		detailsDialog.setVisible(true);
	}

	private static JPanel createSectionPanel(String title) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(
						BorderFactory.createLineBorder(new Color(200, 200, 200)), 
						title
						),
						BorderFactory.createEmptyBorder(10, 10, 10, 10)
				));
		panel.setBackground(Color.WHITE);
		return panel;
	}

	private static JPanel createDetailRow(String label, String value) {
		JPanel rowPanel = new JPanel(new BorderLayout());
		rowPanel.setBackground(Color.WHITE);

		JLabel labelComp = new JLabel(label);
		labelComp.setFont(new Font("Arial", Font.BOLD, 12));
		labelComp.setForeground(Color.DARK_GRAY);
		labelComp.setPreferredSize(new Dimension(150, 25));

		JLabel valueComp = new JLabel(value);
		valueComp.setFont(new Font("Arial", Font.PLAIN, 12));

		rowPanel.add(labelComp, BorderLayout.WEST);
		rowPanel.add(valueComp, BorderLayout.CENTER);
		rowPanel.setMaximumSize(new Dimension(400, 30));

		return rowPanel;
	}

	private static String formatCourses(String courses) {
		if (courses == null || courses.isEmpty()) {
			return "No courses assigned";
		}

		String[] courseArray = courses.split(",");
		StringBuilder formatted = new StringBuilder();
		for (int i = 0; i < courseArray.length; i++) {
			formatted.append("• ").append(courseArray[i].trim());
			if (i < courseArray.length - 1) {
				formatted.append("\n");
			}
		}
		return formatted.toString();
	}
}