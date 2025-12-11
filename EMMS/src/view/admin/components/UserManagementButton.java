package view.admin.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class UserManagementButton extends AbstractAdminButton {

	public UserManagementButton() {
		super(
				" Manage Users", 
				"Manage all system user accounts",
				"",
				new Color(230, 126, 34),
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						showUserManagementDialog();
					}
				}
				);
	}

	private static void showUserManagementDialog() {
		final JDialog dialog = new JDialog();
		dialog.setTitle("User Account Management");
		dialog.setSize(500, 400);
		dialog.setLocationRelativeTo(null);
		dialog.setModal(true);

		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		JLabel headerLabel = new JLabel("User Account Management", JLabel.CENTER);
		headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
		panel.add(headerLabel, BorderLayout.NORTH);

		JPanel statsPanel = new JPanel(new GridLayout(3, 2, 10, 10));
		statsPanel.setBorder(BorderFactory.createTitledBorder("User Statistics"));

		statsPanel.add(new JLabel("Total Users:"));
		statsPanel.add(new JLabel("1,341"));

		statsPanel.add(new JLabel("Active Users:"));
		statsPanel.add(new JLabel("1,289"));

		statsPanel.add(new JLabel("User Types:"));
		statsPanel.add(new JLabel("Admin: 5, Instructors: 48, Students: 1,288"));

		panel.add(statsPanel, BorderLayout.NORTH);

		JTextArea contentArea = new JTextArea();
		contentArea.setEditable(false);
		contentArea.setFont(new Font("Arial", Font.PLAIN, 12));
		contentArea.setText(
				"User Management Features:\n\n" +
						"• Create New User Accounts\n" +
						"• Reset User Passwords\n" +
						"• Manage User Permissions\n" +
						"• Audit User Activities\n" +
						"• Deactivate/Activate Accounts\n" +
						"• User Role Management\n\n" +
						"Available Actions:\n" +
						"• Add new administrators\n" +
						"• Reset forgotten passwords\n" +
						"• Modify user privileges\n" +
						"• View user login history"
				);

		JScrollPane scrollPane = new JScrollPane(contentArea);
		panel.add(scrollPane, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new FlowLayout());
		JButton createUserButton = new JButton("Create User");
		JButton resetPasswordButton = new JButton("Reset Password");
		JButton closeButton = new JButton("Close");

		createUserButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(dialog, "Opening user creation wizard...");
			}
		});

		resetPasswordButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(dialog, "Opening password reset tool...");
			}
		});

		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
			}
		});

		buttonPanel.add(createUserButton);
		buttonPanel.add(resetPasswordButton);
		buttonPanel.add(closeButton);
		panel.add(buttonPanel, BorderLayout.SOUTH);

		dialog.add(panel);
		dialog.setVisible(true);
	}
}
