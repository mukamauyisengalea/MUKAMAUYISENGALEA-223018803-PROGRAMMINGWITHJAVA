package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import control.AuthenticationController;
import control.InstructorController;
import model.Admin;
import model.Instructor;
import model.Student;

public class LoginForm extends JFrame {
	private JComboBox<String> roleCombo;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JButton loginButton;
	private AuthenticationController authController;

	public LoginForm() {
		this.authController = new AuthenticationController();
		setTitle("Education Monitoring System - Login");
		setSize(400, 350);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(null); // <-- ukoresha absolute layout
		initComponents();
	}

	private void initComponents() {
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

		loginButton = new JButton("Login");
		loginButton.setBounds(210, 200, 100, 30);
		add(loginButton);

		// ActionListener
		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String role = (String) roleCombo.getSelectedItem();
				String username = usernameField.getText().trim();
				String password = new String(passwordField.getPassword()).trim();

				if (username.isEmpty() || password.isEmpty()) {
					JOptionPane.showMessageDialog(LoginForm.this,
							"Please enter both username and password!");
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
									"Invalid admin credentials!");
						}
					} else if (role.equals("Instructor")) {
					    Instructor instructor = authController.authenticateInstructor(username, password);
					    if (instructor != null) {
					        new InstructorControlPanel(instructor).setVisible(true);  // ✅ CORRECT
					        dispose();
					    } else {
					        JOptionPane.showMessageDialog(LoginForm.this,
					                "Invalid instructor credentials!");
					    }
					} else if (role.equals("Student")) {
						Student student = authController.authenticateStudent(username, password);
						if (student != null) {
							new StudentView(student).setVisible(true);
							dispose();
						} else {
							JOptionPane.showMessageDialog(LoginForm.this,
									"Invalid student credentials!");
						}
					}
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(LoginForm.this,
							"Login error: " + ex.getMessage());
					ex.printStackTrace();
				}
			}
		});
	}

	public static void main(String[] args) {
		util.DatabaseInitializer.initializeDatabase();
		new LoginForm().setVisible(true);
	}
}
