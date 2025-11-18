package view.admin.components;

import javax.swing.*;
import java.awt.*;

public class TestAdminButtons {
    
    public static void main(String[] args) {
        // Create test frame
        JFrame testFrame = new JFrame("Admin Buttons Test");
        testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        testFrame.setSize(800, 600);
        testFrame.setLocationRelativeTo(null);
        
        // Create tabbed pane for testing
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Students", new JPanel());
        tabbedPane.addTab("Instructors", new JPanel());
        tabbedPane.addTab("Courses", new JPanel());
        
        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Create dashboard buttons grid
        JPanel gridPanel = new JPanel(new GridLayout(2, 4, 15, 15));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        gridPanel.setBackground(new Color(240, 245, 255));
        
        // Get all dashboard buttons
        java.util.List<AdminButton> buttons = AdminButtonFactory.createDashboardButtons(tabbedPane);
        
        // Create cards for each button
        for (AdminButton button : buttons) {
            gridPanel.add(button.createCard());
        }
        
        mainPanel.add(gridPanel, BorderLayout.CENTER);
        
        // Add tabbed pane
        mainPanel.add(tabbedPane, BorderLayout.SOUTH);
        
        testFrame.add(mainPanel);
        testFrame.setVisible(true);
        
        System.out.println("✅ Admin Button Test Framework Loaded Successfully!");
        System.out.println("✅ Total Buttons Created: " + buttons.size());
    }
}