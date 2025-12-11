package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import model.Admin;
import model.Instructor;
import model.Student;
import control.AuthenticationController;

@SuppressWarnings("serial")
public class LoginForm extends JFrame {
	private JComboBox<String> roleCombo;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JButton loginButton;
	private JButton registerButton;
	private AuthenticationController authController;

	public LoginForm() {
		this.authController = new AuthenticationController();
		setTitle("Education Monitoring System - Login");
		setSize(400, 350);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(null);
		initComponents();
	}

	private void initComponents() {
		// Title label
		JLabel titleLabel = new JLabel("Education Monitoring System");
		titleLabel.setBounds(80, 20, 250, 30);
		titleLabel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
		titleLabel.setHorizontalAlignment(JLabel.CENTER);
		add(titleLabel);

		JLabel lblRole = new JLabel("Role:");
		lblRole.setBounds(60, 70, 80, 25);
		add(lblRole);

		roleCombo = new JComboBox<>(new String[]{"Admin", "Instructor", "Student"});
		roleCombo.setBounds(150, 70, 160, 25);
		add(roleCombo);

		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setBounds(60, 110, 80, 25);
		add(lblUsername);

		usernameField = new JTextField(15);
		usernameField.setBounds(150, 110, 160, 25);
		add(usernameField);

		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(60, 150, 80, 25);
		add(lblPassword);

		passwordField = new JPasswordField(15);
		passwordField.setBounds(150, 150, 160, 25);
		add(passwordField);

		// Register Button
		registerButton = new JButton("Register");
		registerButton.setBounds(60, 200, 100, 30);
		registerButton.setBackground(new java.awt.Color(46, 204, 113));
		registerButton.setForeground(java.awt.Color.WHITE);
		add(registerButton);

		// Login Button
		loginButton = new JButton("Login");
		loginButton.setBounds(210, 200, 100, 30);
		loginButton.setBackground(new java.awt.Color(52, 152, 219));
		loginButton.setForeground(java.awt.Color.WHITE);
		add(loginButton);

		// Info label for new users
		JLabel infoLabel = new JLabel("New user? Click Register to create an account.");
		infoLabel.setBounds(60, 240, 280, 25);
		infoLabel.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 11));
		infoLabel.setForeground(java.awt.Color.GRAY);
		add(infoLabel);

		// Action Listener for Register Button
		registerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String selectedRole = (String) roleCombo.getSelectedItem();
				
				// Admin registration is typically not public
				if ("Admin".equals(selectedRole)) {
					JOptionPane.showMessageDialog(LoginForm.this,
							"Admin accounts cannot be created publicly.\n" +
							"Please contact system administrator.",
							"Admin Registration Restricted",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				// Open Registration Form for Student or Instructor
				try {
					// Create and show registration form
					RegistrationForm regForm = new RegistrationForm(selectedRole);
					regForm.setVisible(true);
					
					// Optional: Center the registration form relative to login form
					regForm.setLocationRelativeTo(LoginForm.this);
					
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(LoginForm.this,
							"Error opening registration form: " + ex.getMessage(),
							"Registration Error",
							JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				}
			}
		});

		// Action Listener for Login Button
		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String role = (String) roleCombo.getSelectedItem();
				String username = usernameField.getText().trim();
				String password = new String(passwordField.getPassword()).trim();

				if (username.isEmpty() || password.isEmpty()) {
					JOptionPane.showMessageDialog(LoginForm.this,
							"Please enter both username and password!",
							"Validation Error",
							JOptionPane.WARNING_MESSAGE);
					return;
				}

				try {
					if (role.equals("Admin")) {
						Admin admin = authController.authenticateAdmin(username, password);
						if (admin != null) {
							new AdminControlPanel(admin).setVisible(true);
							dispose();
						} else {
							JOptionPane.showMessageDialog(LoginForm.this,
									"Invalid admin credentials!\n\n" +
									"Please check your username and password.",
									"Login Failed",
									JOptionPane.ERROR_MESSAGE);
						}
					} else if (role.equals("Instructor")) {
						Instructor instructor = authController.authenticateInstructor(username, password);
						if (instructor != null) {
							// Check if instructor is approved
							if ("PENDING".equalsIgnoreCase(instructor.getStatus())) {
								JOptionPane.showMessageDialog(LoginForm.this,
										"Your account is pending approval.\n" +
										"Please wait for admin approval before logging in.",
										"Account Pending",
										JOptionPane.INFORMATION_MESSAGE);
								return;
							} else if ("INACTIVE".equalsIgnoreCase(instructor.getStatus())) {
								JOptionPane.showMessageDialog(LoginForm.this,
										"Your account is inactive.\n" +
										"Please contact system administrator.",
										"Account Inactive",
										JOptionPane.WARNING_MESSAGE);
								return;
							}
							
							new InstructorControlPanel(instructor).setVisible(true);
							dispose();
						} else {
							JOptionPane.showMessageDialog(LoginForm.this,
									"Invalid instructor credentials!\n\n" +
									"Please check your username and password.\n" +
									"New instructors need to register first.",
									"Login Failed",
									JOptionPane.ERROR_MESSAGE);
						}
					} else if (role.equals("Student")) {
						Student student = authController.authenticateStudent(username, password);
						if (student != null) {
							new StudentView(student).setVisible(true);
							dispose();
						} else {
							JOptionPane.showMessageDialog(LoginForm.this,
									"Invalid student credentials!\n\n" +
									"Please check your username and password.\n" +
									"New students need to register first.",
									"Login Failed",
									JOptionPane.ERROR_MESSAGE);
						}
					}
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(LoginForm.this,
							"Login error: " + ex.getMessage(),
							"Login Error",
							JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				}
			}
		});
		
		// Add key listener for Enter key to trigger login
		passwordField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loginButton.doClick();
			}
		});
	}

	public static void main(String[] args) {
		// Set look and feel for better appearance
		try {
			javax.swing.UIManager.setLookAndFeel(
				javax.swing.UIManager.getSystemLookAndFeelClassName()
			);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Initialize database
		util.DatabaseInitializer.initializeDatabase();
		
		// Create and show the login form
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new LoginForm().setVisible(true);
			}
		});
	}
}