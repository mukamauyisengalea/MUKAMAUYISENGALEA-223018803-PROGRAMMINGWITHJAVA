package view.admin.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class EnrollmentManagementButton extends AbstractAdminButton {

	public EnrollmentManagementButton() {
		super(
				" Manage Enrollments", 
				"Handle student course enrollments",
				"",
				new Color(241, 196, 15), // Yellow
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// Show enrollment management dialog
						showEnrollmentManagementDialog();
					}
				}
				);
	}

	private static void showEnrollmentManagementDialog() {
		final JDialog dialog = new JDialog();
		dialog.setTitle("Enrollment Management");
		dialog.setSize(500, 400);
		dialog.setLocationRelativeTo(null);
		dialog.setModal(true);

		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		// Header
		JLabel headerLabel = new JLabel("Enrollment Management System", JLabel.CENTER);
		headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
		panel.add(headerLabel, BorderLayout.NORTH);

		// Content
		JTextArea contentArea = new JTextArea();
		contentArea.setEditable(false);
		contentArea.setFont(new Font("Arial", Font.PLAIN, 12));
		contentArea.setText(
				"Enrollment Management Features:\n\n" +
						"• Process Enrollment Requests\n" +
						"• Manage Student Course Registrations\n" +
						"• Handle Waitlists\n" +
						"• Resolve Enrollment Conflicts\n" +
						"• Generate Enrollment Reports\n" +
						"• Track Enrollment Statistics\n\n" +
						"Current Statistics:\n" +
						"• Total Enrollments: 2,341\n" +
						"• Active Enrollments: 1,895\n" +
						"• Pending Requests: 156\n" +
						"• Waitlisted: 89"
				);

		JScrollPane scrollPane = new JScrollPane(contentArea);
		panel.add(scrollPane, BorderLayout.CENTER);

		// Buttons
		JPanel buttonPanel = new JPanel(new FlowLayout());
		JButton processButton = new JButton("Process Requests");
		JButton reportsButton = new JButton("Generate Reports");
		JButton closeButton = new JButton("Close");

		processButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(dialog, "Processing enrollment requests...");
			}
		});

		reportsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(dialog, "Generating enrollment reports...");
			}
		});

		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
			}
		});

		buttonPanel.add(processButton);
		buttonPanel.add(reportsButton);
		buttonPanel.add(closeButton);
		panel.add(buttonPanel, BorderLayout.SOUTH);

		dialog.add(panel);
		dialog.setVisible(true);
	}
}