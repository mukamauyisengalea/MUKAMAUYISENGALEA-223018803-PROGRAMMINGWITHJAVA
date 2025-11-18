package view.admin.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SystemSettingsButton extends AbstractAdminButton {
    
    public SystemSettingsButton() {
        super(
            "⚙️ System Settings", 
            "Configure system preferences",
            "⚙️",
            new Color(149, 165, 166), // Gray
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Show system settings dialog
                    showSystemSettingsDialog();
                }
            }
        );
    }
    
    private static void showSystemSettingsDialog() {
        final JDialog dialog = new JDialog();
        dialog.setTitle("System Configuration");
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(null);
        dialog.setModal(true);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header
        JLabel headerLabel = new JLabel("System Configuration Settings", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(headerLabel, BorderLayout.NORTH);
        
        // Settings options
        JPanel settingsPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        settingsPanel.setBorder(BorderFactory.createTitledBorder("System Settings"));
        
        // Academic settings
        JPanel academicPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        academicPanel.setBorder(BorderFactory.createTitledBorder("Academic Settings"));
        academicPanel.add(new JLabel("Academic Year:"));
        academicPanel.add(new JTextField("2024-2025"));
        academicPanel.add(new JLabel("Grading Scale:"));
        academicPanel.add(new JComboBox<>(new String[]{"4.0 Scale", "Percentage", "Letter Grades"}));
        academicPanel.add(new JLabel("Passing Grade:"));
        academicPanel.add(new JTextField("60%"));
        
        // System settings
        JPanel systemPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        systemPanel.setBorder(BorderFactory.createTitledBorder("System Preferences"));
        systemPanel.add(new JLabel("Backup Frequency:"));
        systemPanel.add(new JComboBox<>(new String[]{"Daily", "Weekly", "Monthly"}));
        systemPanel.add(new JLabel("Session Timeout:"));
        systemPanel.add(new JComboBox<>(new String[]{"15 minutes", "30 minutes", "1 hour", "2 hours"}));
        
        settingsPanel.add(academicPanel);
        settingsPanel.add(systemPanel);
        
        JScrollPane scrollPane = new JScrollPane(settingsPanel);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton saveButton = new JButton("Save Settings");
        JButton backupButton = new JButton("System Backup");
        JButton closeButton = new JButton("Close");
        
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(dialog, "System settings saved successfully!");
            }
        });
        
        backupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(dialog, "Starting system backup...");
            }
        });
        
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        
        buttonPanel.add(saveButton);
        buttonPanel.add(backupButton);
        buttonPanel.add(closeButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
}