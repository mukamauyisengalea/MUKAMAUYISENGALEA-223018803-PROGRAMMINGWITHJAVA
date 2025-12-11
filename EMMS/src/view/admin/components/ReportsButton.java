package view.admin.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ReportsButton extends AbstractAdminButton {

	public ReportsButton() {
		super(
				" System Reports", 
				"Generate reports and analytics",
				"",
				new Color(231, 76, 60),
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						showReportsDialog();
					}
				}
				);
	}

	private static void showReportsDialog() {
		final JDialog dialog = new JDialog();
		dialog.setTitle("System Reports & Analytics");
		dialog.setSize(500, 450);
		dialog.setLocationRelativeTo(null);
		dialog.setModal(true);

		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		JLabel headerLabel = new JLabel("System Reports & Analytics", JLabel.CENTER);
		headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
		panel.add(headerLabel, BorderLayout.NORTH);

		JPanel reportsPanel = new JPanel(new GridLayout(0, 1, 5, 5));
		reportsPanel.setBorder(BorderFactory.createTitledBorder("Available Reports"));

		String[] reportTypes = {
				" Student Performance Reports",
				" Course Completion Statistics", 
				" Instructor Effectiveness Reports",
				" System Usage Analytics",
				" Enrollment Statistics",
				" Grade Distribution Reports",
				" Attendance Reports",
				" Financial Reports"
		};

		for (String report : reportTypes) {
			JCheckBox checkBox = new JCheckBox(report);
			reportsPanel.add(checkBox);
		}

		JScrollPane scrollPane = new JScrollPane(reportsPanel);
		panel.add(scrollPane, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new FlowLayout());
		JButton generateButton = new JButton("Generate Selected Reports");
		JButton closeButton = new JButton("Close");

		generateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(
						dialog, 
						"Generating selected reports...\nReports will be available in the downloads folder."
						);
			}
		});

		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
			}
		});

		buttonPanel.add(generateButton);
		buttonPanel.add(closeButton);
		panel.add(buttonPanel, BorderLayout.SOUTH);

		dialog.add(panel);
		dialog.setVisible(true);
	}
}
