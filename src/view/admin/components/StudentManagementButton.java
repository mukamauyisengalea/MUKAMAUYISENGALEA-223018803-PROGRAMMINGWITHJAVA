package view.admin.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class StudentManagementButton extends AbstractAdminButton {
    
    public StudentManagementButton(final JTabbedPane tabbedPane) {
        super(
            "👨‍🎓 Manage Students", 
            "Manage student records and profiles",
            "👨‍🎓",
            new Color(52, 152, 219), // Blue
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (tabbedPane != null) {
                        // Switch to Students tab (index 1)
                        tabbedPane.setSelectedIndex(1);
                        JOptionPane.showMessageDialog(null, 
                            "Navigating to Student Management...\n\n" +
                            "• View all students\n" +
                            "• Add new students\n" +
                            "• Edit student information\n" +
                            "• Delete student records");
                    } else {
                        JOptionPane.showMessageDialog(null, 
                            "Opening Student Management Panel...");
                    }
                }
            }
        );
    }
    
    // Alternative constructor for standalone use
    public StudentManagementButton() {
        super(
            "👨‍🎓 Manage Students", 
            "Manage student records and profiles",
            "👨‍🎓",
            new Color(52, 152, 219),
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(null, 
                        "Student Management Features:\n\n" +
                        "• View Student Database\n" +
                        "• Add New Students\n" +
                        "• Update Student Information\n" +
                        "• Manage Student Enrollment\n" +
                        "• Generate Student Reports");
                }
            }
        );
    }
}