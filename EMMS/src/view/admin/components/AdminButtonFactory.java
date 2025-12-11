package view.admin.components;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class AdminButtonFactory {
    
    public static List<AdminButton> createDashboardButtons(JTabbedPane tabbedPane) {
        List<AdminButton> buttons = new ArrayList<>();
        
        // Add buttons that need tabbed pane reference
        buttons.add(new StudentManagementButton(tabbedPane));
        buttons.add(new InstructorManagementButton(tabbedPane));
        buttons.add(new CourseManagementButton(tabbedPane));
        
        // Add standalone buttons
        buttons.add(new EnrollmentManagementButton());
        buttons.add(new GradeManagementButton());
        buttons.add(new UserManagementButton());
        buttons.add(new ReportsButton());
        buttons.add(new SystemSettingsButton());
        
        return buttons;
    }
    
    public static List<AdminButton> createQuickActionButtons(JTabbedPane tabbedPane) {
        List<AdminButton> buttons = new ArrayList<>();
        
        buttons.add(new StudentManagementButton(tabbedPane));
        buttons.add(new InstructorManagementButton(tabbedPane));
        buttons.add(new CourseManagementButton(tabbedPane));
        buttons.add(new ReportsButton());
        
        return buttons;
    }
    
    public static List<AdminButton> createAllButtons() {
        List<AdminButton> buttons = new ArrayList<>();
        
        buttons.add(new StudentManagementButton());
        buttons.add(new InstructorManagementButton());
        buttons.add(new CourseManagementButton());
        buttons.add(new EnrollmentManagementButton());
        buttons.add(new GradeManagementButton());
        buttons.add(new UserManagementButton());
        buttons.add(new ReportsButton());
        buttons.add(new SystemSettingsButton());
        
        return buttons;
    }
}