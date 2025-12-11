package view.admin.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class CourseManagementButton extends AbstractAdminButton {

	public CourseManagementButton(final JTabbedPane tabbedPane) {
		super(
				"Manage Courses", 
				"Create and manage course offerings",
				"",
				new Color(155, 89, 182), // Purple
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (tabbedPane != null) {
							// Switch to Courses tab (index 3)
							tabbedPane.setSelectedIndex(3);
							JOptionPane.showMessageDialog(null, 
									"Navigating to Course Management...\n\n" +
											"• View all courses\n" +
											"• Add new courses\n" +
											"• Update course details\n" +
									"• Manage course catalog");
						} else {
							JOptionPane.showMessageDialog(null, 
									"Opening Course Management Panel...");
						}
					}
				}
				);
	}

	public CourseManagementButton() {
		super(
				" Manage Courses", 
				"Create and manage course offerings",
				"",
				new Color(155, 89, 182),
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						JOptionPane.showMessageDialog(null, 
								"Course Management Features:\n\n" +
										"• View Course Catalog\n" +
										"• Add New Courses\n" +
										"• Update Course Information\n" +
										"• Set Course Prerequisites\n" +
								"• Manage Course Schedule");
					}
				}
				);
	}
}