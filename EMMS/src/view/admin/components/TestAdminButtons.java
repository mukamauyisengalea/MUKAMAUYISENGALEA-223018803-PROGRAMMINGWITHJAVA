package view.admin.components;

import javax.swing.*;
import java.awt.*;

public class TestAdminButtons {

	public static void main(String[] args) {
		JFrame testFrame = new JFrame("Admin Buttons Test");
		testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		testFrame.setSize(800, 600);
		testFrame.setLocationRelativeTo(null);

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Students", new JPanel());
		tabbedPane.addTab("Instructors", new JPanel());
		tabbedPane.addTab("Courses", new JPanel());

		JPanel mainPanel = new JPanel(new BorderLayout());

		JPanel gridPanel = new JPanel(new GridLayout(2, 4, 15, 15));
		gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		gridPanel.setBackground(new Color(240, 245, 255));

		java.util.List<AdminButton> buttons = AdminButtonFactory.createDashboardButtons(tabbedPane);

		for (AdminButton button : buttons) {
			gridPanel.add(button.createCard());
		}

		mainPanel.add(gridPanel, BorderLayout.CENTER);
		mainPanel.add(tabbedPane, BorderLayout.SOUTH);

		testFrame.add(mainPanel);
		testFrame.setVisible(true);

		System.out.println("Admin Button Test Framework Loaded Successfully!");
		System.out.println("Total Buttons Created: " + buttons.size());
	}
}
