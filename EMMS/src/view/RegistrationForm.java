package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import util.DatabaseConnection;
import model.Student;
import model.Instructor;

@SuppressWarnings("serial")
public class RegistrationForm extends JFrame {
    private JPanel registrationPanel;
    private JTextField idField;
    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField officeField;
    private JComboBox<String> departmentCombo;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private String role;

    public RegistrationForm(String role) {
        this.role = role;
        setTitle("Registration Form - " + role + " Registration");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        initComponents();
    }

    private void initComponents() {
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(46, 204, 113));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        JLabel headerLabel = new JLabel("➕ Register as " + role);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Create registration panel based on role
        registrationPanel = createRegistrationPanel();
        JScrollPane scrollPane = new JScrollPane(registrationPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        JButton registerButton = new JButton("Register");
        registerButton.setBackground(new Color(46, 204, 113));
        registerButton.setForeground(Color.WHITE);
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processRegistration();
            }
        });
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBackground(new Color(231, 76, 60));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createRegistrationPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        int row = 0;

        // ID Field
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel((role.equals("Student") ? "Student ID:" : "Instructor ID:") + "*"), gbc);
        gbc.gridx = 1;
        idField = new JTextField(15);
        if (role.equals("Student")) {
            idField.setText("STU" + (1000 + (int)(Math.random() * 9000)));
        } else {
            idField.setText("INST" + (100 + (int)(Math.random() * 900)));
        }
        idField.setEditable(false);
        panel.add(idField, gbc);
        row++;

        // Full Name
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Full Name:*"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(15);
        panel.add(nameField, gbc);
        row++;

        // Email
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Email:*"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(15);
        panel.add(emailField, gbc);
        row++;

        // Phone
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        phoneField = new JTextField(15);
        panel.add(phoneField, gbc);
        row++;

        // Office (only for instructors)
        if (role.equals("Instructor")) {
            gbc.gridx = 0; gbc.gridy = row;
            panel.add(new JLabel("Office:"), gbc);
            gbc.gridx = 1;
            officeField = new JTextField(15);
            panel.add(officeField, gbc);
            row++;
        }

        // Department
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Department:*"), gbc);
        gbc.gridx = 1;
        departmentCombo = new JComboBox<String>(new String[]{
            "Computer Science", "Mathematics", "Physics", "Chemistry", 
            "Biology", "Engineering", "Business", "Arts"
        });
        panel.add(departmentCombo, gbc);
        row++;

        // Password
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Password:*"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        panel.add(passwordField, gbc);
        row++;

        // Confirm Password
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Confirm Password:*"), gbc);
        gbc.gridx = 1;
        confirmPasswordField = new JPasswordField(15);
        panel.add(confirmPasswordField, gbc);

        return panel;
    }

    private void processRegistration() {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String office = role.equals("Instructor") ? officeField.getText().trim() : "";
        String department = (String) departmentCombo.getSelectedItem();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        // Validation
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please fill in all required fields (*).",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this,
                "Passwords do not match!",
                "Password Mismatch",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this,
                "Password must be at least 6 characters long.",
                "Weak Password",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Check if email already exists
        if (DatabaseConnection.checkUserExists(email, role.toLowerCase())) {
            JOptionPane.showMessageDialog(this,
                "Email already registered! Please use a different email.",
                "Duplicate Email",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            boolean success = false;
            
            if (role.equals("Student")) {
                // Create student object
                Student student = new Student();
                
                // Parse student ID or generate one
                int studentId = 0;
                if (id.startsWith("STU")) {
                    try {
                        studentId = Integer.parseInt(id.substring(3));
                    } catch (NumberFormatException ex) {
                        // Use generated ID
                    }
                }
                
                // Split name into first and last name
                String[] nameParts = name.split(" ", 2);
                String firstName = nameParts[0];
                String lastName = nameParts.length > 1 ? nameParts[1] : "";
                
                // Generate username from email
                String username = email.split("@")[0];
                
                student.setStudentID(studentId);
                student.setFirstName(firstName);
                student.setLastName(lastName);
                student.setUsername(username);
                student.setEmail(email);
                student.setPhone(phone);
                student.setPassword(password);
                student.setCreatedAt(new java.util.Date());
                
                success = DatabaseConnection.registerStudent(student);
                
            } else if (role.equals("Instructor")) {
                // Create instructor object
                Instructor instructor = new Instructor();
                
                // Parse instructor ID or generate one
                int instructorId = 0;
                if (id.startsWith("INST")) {
                    try {
                        instructorId = Integer.parseInt(id.substring(4));
                    } catch (NumberFormatException ex) {
                        // Use generated ID
                    }
                }
                
                // Generate identifier
                String identifier = "INST" + (instructorId > 0 ? instructorId : "NEW");
                
                // Generate username from email
                String username = email.split("@")[0];
                
                instructor.setInstructorID(instructorId);
                instructor.setName(name);
                instructor.setIdentifier(identifier);
                instructor.setEmail(email);
                instructor.setPhone(phone);
                instructor.setDepartment(department);
                instructor.setLocation(office);
                instructor.setUsername(username);
                instructor.setPassword(password);
                instructor.setStatus("PENDING");
                
                success = DatabaseConnection.registerInstructor(instructor);
            }

            if (success) {
                String message = " " + role + " registration successful!\n\n" +
                               (role.equals("Student") ? "Student" : "Instructor") + " ID: " + id + "\n" +
                               "Name: " + name + "\n" +
                               "Email: " + email + "\n" +
                               "Department: " + department + "\n";
                
                if (role.equals("Instructor")) {
                    message += "\nNote: Your account is pending admin approval.\n" +
                              "You will be able to login once approved.";
                } else {
                    message += "\nYou can now login with your credentials.";
                }
                
                JOptionPane.showMessageDialog(this,
                    message,
                    "Registration Successful",
                    JOptionPane.INFORMATION_MESSAGE);
                
                dispose(); // Close registration form
            } else {
                JOptionPane.showMessageDialog(this,
                    " Registration failed. Please try again.",
                    "Registration Failed",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error during registration: " + ex.getMessage(),
                "Registration Error",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}