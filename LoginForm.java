package com.form;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame implements ActionListener {
	JTextField usernameField;
	JPasswordField passwordField;
	JButton loginButton;

	public Login() {
		setTitle("Login");
		setBounds(100, 100, 350, 200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new GridLayout(3, 2, 10, 10));

		JLabel userLabel = new JLabel("Username:");
		JLabel passLabel = new JLabel("Password:");
		usernameField = new JTextField();
		passwordField = new JPasswordField();
		loginButton = new JButton("Login");

		add(userLabel);
		add(usernameField);
		add(passLabel);
		add(passwordField);
		add(new JLabel());
		add(loginButton);

		loginButton.addActionListener(this);

		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		String user = usernameField.getText();
		String pass = new String(passwordField.getPassword());

		if (user.equals("MUKAMA") && pass.equals("45378")) {
			JOptionPane.showMessageDialog(this, "Login successful!");
			dispose();
			new ShapesApp(); // open shapes calculator
		} else {
			JOptionPane.showMessageDialog(this, "Invalid login. Try again.");
		}
	}

	public static void main(String[] args) {
		new Login();
	}
}
