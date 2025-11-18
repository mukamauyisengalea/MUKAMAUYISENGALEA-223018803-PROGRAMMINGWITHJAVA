package view.admin.components;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;
import util.DatabaseConnection;

public class InstructorManagementButton extends AbstractAdminButton {

    public InstructorManagementButton(final JTabbedPane tabbedPane) {
        super(
            "👩‍🏫 Manage Instructors", 
            "Manage instructor accounts and assignments",
            "👩‍🏫",
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
            "👩‍🏫 Manage Instructors", 
            "Manage instructor accounts and assignments",
            "👩‍🏫",
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
        managementDialog.setSize(700, 550);
        managementDialog.setLocationRelativeTo(null);
        managementDialog.setModal(true);
        managementDialog.setLayout(new BorderLayout());

        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(46, 204, 113));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        JLabel headerLabel = new JLabel("👩‍🏫 Instructor Management Dashboard");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);
        managementDialog.add(headerPanel, BorderLayout.NORTH);

        // Main content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Get instructor data from DATABASE
        String[] columns = {"ID", "Name", "Email", "Department", "Courses", "Status"};
        final List<Object[]> instructorData = DatabaseConnection.getAllInstructors();
        
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
        instructorTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tableScrollPane = new JScrollPane(instructorTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Current Instructors"));
        contentPanel.add(tableScrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        // Refresh Button
        JButton refreshButton = new JButton("🔄 Refresh");
        refreshButton.setBackground(new Color(52, 152, 219));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFont(new Font("Arial", Font.BOLD, 12));
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshInstructorTable(tableModel);
                JOptionPane.showMessageDialog(managementDialog, 
                    "✅ Instructor data refreshed successfully!",
                    "Refresh Complete", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Add Instructor Button
        JButton addButton = new JButton("➕ Add Instructor");
        addButton.setBackground(new Color(46, 204, 113));
        addButton.setForeground(Color.WHITE);
        addButton.setFont(new Font("Arial", Font.BOLD, 12));
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddInstructorDialog(managementDialog, tableModel);
            }
        });

        // Delete Instructor Button
        JButton deleteButton = new JButton("🗑️ Delete Instructor");
        deleteButton.setBackground(new Color(231, 76, 60));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFont(new Font("Arial", Font.BOLD, 12));
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = instructorTable.getSelectedRow();
                if (selectedRow != -1) {
                    String instructorId = (String) tableModel.getValueAt(selectedRow, 0);
                    String instructorName = (String) tableModel.getValueAt(selectedRow, 1);
                    showDeleteInstructorDialog(managementDialog, instructorId, instructorName, tableModel);
                } else {
                    JOptionPane.showMessageDialog(managementDialog,
                        "Please select an instructor to delete.",
                        "No Selection",
                        JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // View Details Button
        JButton detailsButton = new JButton("👁️ View Details");
        detailsButton.setBackground(new Color(155, 89, 182));
        detailsButton.setForeground(Color.WHITE);
        detailsButton.setFont(new Font("Arial", Font.BOLD, 12));
        detailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = instructorTable.getSelectedRow();
                if (selectedRow != -1) {
                    String instructorId = (String) tableModel.getValueAt(selectedRow, 0);
                    String instructorName = (String) tableModel.getValueAt(selectedRow, 1);
                    String email = (String) tableModel.getValueAt(selectedRow, 2);
                    String department = (String) tableModel.getValueAt(selectedRow, 3);
                    String courses = (String) tableModel.getValueAt(selectedRow, 4);
                    String status = (String) tableModel.getValueAt(selectedRow, 5);
                    showInstructorDetailsDialog(instructorId, instructorName, email, department, courses, status);
                } else {
                    JOptionPane.showMessageDialog(managementDialog,
                        "Please select an instructor to view details.",
                        "No Selection",
                        JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // Update Status Button
        JButton statusButton = new JButton("📊 Update Status");
        statusButton.setBackground(new Color(241, 196, 15));
        statusButton.setForeground(Color.WHITE);
        statusButton.setFont(new Font("Arial", Font.BOLD, 12));
        statusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = instructorTable.getSelectedRow();
                if (selectedRow != -1) {
                    String instructorId = (String) tableModel.getValueAt(selectedRow, 0);
                    String instructorName = (String) tableModel.getValueAt(selectedRow, 1);
                    String currentStatus = (String) tableModel.getValueAt(selectedRow, 5);
                    showUpdateStatusDialog(managementDialog, instructorId, instructorName, currentStatus, tableModel);
                } else {
                    JOptionPane.showMessageDialog(managementDialog,
                        "Please select an instructor to update status.",
                        "No Selection",
                        JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // Assign Course Button
        JButton assignCourseButton = new JButton("📚 Assign Course");
        assignCourseButton.setBackground(new Color(230, 126, 34));
        assignCourseButton.setForeground(Color.WHITE);
        assignCourseButton.setFont(new Font("Arial", Font.BOLD, 12));
        assignCourseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = instructorTable.getSelectedRow();
                if (selectedRow != -1) {
                    String instructorId = (String) tableModel.getValueAt(selectedRow, 0);
                    String instructorName = (String) tableModel.getValueAt(selectedRow, 1);
                    showAssignCourseDialog(managementDialog, instructorId, instructorName, tableModel);
                } else {
                    JOptionPane.showMessageDialog(managementDialog,
                        "Please select an instructor to assign course.",
                        "No Selection",
                        JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // Close Button
        JButton closeButton = new JButton("❌ Close");
        closeButton.setBackground(new Color(149, 165, 166));
        closeButton.setForeground(Color.WHITE);
        closeButton.setFont(new Font("Arial", Font.BOLD, 12));
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
        
        java.util.Set<String> departments = new java.util.HashSet<>();
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

    private static void refreshInstructorTable(DefaultTableModel tableModel) {
        List<Object[]> instructorData = DatabaseConnection.getAllInstructors();
        tableModel.setRowCount(0);
        for (Object[] row : instructorData) {
            tableModel.addRow(row);
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

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(46, 204, 113));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        JLabel headerLabel = new JLabel("➕ Add New Instructor");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);
        addDialog.add(headerPanel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Form fields
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

        // Button panel
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("💾 Save Instructor");
        JButton cancelButton = new JButton("❌ Cancel");

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

                // SAVE TO DATABASE
                boolean success = DatabaseConnection.addInstructor(instructorId, name, email, department, phone, office);
                
                if (success) {
                    JOptionPane.showMessageDialog(addDialog,
                        "✅ Instructor added successfully to database!\n\n" +
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
                        "❌ Failed to add instructor to database.\n" +
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

    private static void showDeleteInstructorDialog(JDialog parentDialog, String instructorId, String instructorName, final DefaultTableModel tableModel) {
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
            // DELETE FROM DATABASE
            boolean success = DatabaseConnection.deleteInstructor(instructorId);
            
            if (success) {
                JOptionPane.showMessageDialog(parentDialog,
                    "✅ Instructor deleted successfully from database!\n\n" +
                    "Instructor: " + instructorName + "\n" +
                    "ID: " + instructorId,
                    "Deletion Complete",
                    JOptionPane.INFORMATION_MESSAGE);
                
                refreshInstructorTable(tableModel);
            } else {
                JOptionPane.showMessageDialog(parentDialog,
                    "❌ Failed to delete instructor from database.\n" +
                    "Possible reasons:\n" +
                    "• Database connection issue\n" +
                    "• Instructor ID not found",
                    "Deletion Failed",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void showUpdateStatusDialog(final JDialog parentDialog, final String instructorId, final String instructorName, String currentStatus, final DefaultTableModel tableModel) {
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
                        "✅ Status updated successfully!\n\n" +
                        "Instructor: " + instructorName + "\n" +
                        "New Status: " + newStatus,
                        "Status Updated",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    refreshInstructorTable(tableModel);
                    statusDialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(statusDialog,
                        "❌ Failed to update status.",
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

    private static void showAssignCourseDialog(final JDialog parentDialog, final String instructorId, final String instructorName, final DefaultTableModel tableModel) {
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

                boolean success = DatabaseConnection.assignCourseToInstructor(instructorId, courseCode);
                
                if (success) {
                    JOptionPane.showMessageDialog(assignDialog,
                        "✅ Course assigned successfully!\n\n" +
                        "Instructor: " + instructorName + "\n" +
                        "Course: " + courseCode,
                        "Assignment Complete",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    refreshInstructorTable(tableModel);
                    assignDialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(assignDialog,
                        "❌ Failed to assign course.\n" +
                        "Possible reasons:\n" +
                        "• Course code doesn't exist\n" +
                        "• Database connection issue",
                        "Assignment Failed",
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

    private static void showInstructorDetailsDialog(final String id, final String name, String email, 
            String department, String courses, String status) {
// Fetch complete instructor details from database
Object[] fullInstructorDetails = DatabaseConnection.getInstructorDetails(id);

final JDialog detailsDialog = new JDialog();
detailsDialog.setTitle("Instructor Details - " + name);
detailsDialog.setSize(500, 550);
detailsDialog.setLocationRelativeTo(null);
detailsDialog.setModal(true);
detailsDialog.setLayout(new BorderLayout());

// Header
JPanel headerPanel = new JPanel();
headerPanel.setBackground(new Color(52, 152, 219));
headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
JLabel headerLabel = new JLabel("👩‍🏫 Instructor Profile: " + name);
headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
headerLabel.setForeground(Color.WHITE);
headerPanel.add(headerLabel);
detailsDialog.add(headerPanel, BorderLayout.NORTH);

// Main content panel with scroll
JPanel contentPanel = new JPanel();
contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

// Basic Information Section
JPanel basicInfoPanel = createSectionPanel("📋 Basic Information");
basicInfoPanel.add(createDetailRow("Instructor ID:", id));
basicInfoPanel.add(createDetailRow("Full Name:", name));
basicInfoPanel.add(createDetailRow("Email:", email));
basicInfoPanel.add(createDetailRow("Department:", department));

// Status with color coding
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

// Contact Information Section (if available)
if (fullInstructorDetails != null && fullInstructorDetails.length > 5) {
JPanel contactPanel = createSectionPanel("📞 Contact Information");
String phone = fullInstructorDetails[4] != null ? fullInstructorDetails[4].toString() : "Not provided";
String office = fullInstructorDetails[5] != null ? fullInstructorDetails[5].toString() : "Not assigned";

contactPanel.add(createDetailRow("Phone:", phone));
contactPanel.add(createDetailRow("Office:", office));
contentPanel.add(contactPanel);
contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
}

// Course Information Section
JPanel coursePanel = createSectionPanel("📚 Course Assignments");
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

// Add all sections to content panel
contentPanel.add(basicInfoPanel);
contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
contentPanel.add(coursePanel);

// Statistics Section
JPanel statsPanel = createSectionPanel("📊 Teaching Statistics");
int totalCourses = courses.isEmpty() ? 0 : courses.split(",").length;

// Get additional stats from database
int totalStudents = DatabaseConnection.getInstructorStudentCount(id);
int activeAssignments = DatabaseConnection.getInstructorActiveAssignments(id);

statsPanel.add(createDetailRow("Total Courses:", String.valueOf(totalCourses)));
statsPanel.add(createDetailRow("Total Students:", String.valueOf(totalStudents)));
statsPanel.add(createDetailRow("Active Assignments:", String.valueOf(activeAssignments)));

contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
contentPanel.add(statsPanel);

JScrollPane scrollPane = new JScrollPane(contentPanel);
scrollPane.setBorder(BorderFactory.createEmptyBorder());
detailsDialog.add(scrollPane, BorderLayout.CENTER);

// Button panel
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

//Helper method to create section panels
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

//Helper method to create detail rows
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

//Helper method to format courses for better display
private static String formatCourses(String courses) {
if (courses == null || courses.isEmpty()) {
return "No courses assigned";
}

// If courses are comma-separated, format them with line breaks
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

    private static JLabel createDetailLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(Color.DARK_GRAY);
        return label;
    }

    private static JLabel createDetailValue(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        return label;
    }
}