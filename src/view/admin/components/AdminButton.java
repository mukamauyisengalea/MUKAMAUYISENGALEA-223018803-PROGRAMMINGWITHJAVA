package view.admin.components;

import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

public interface AdminButton {
    String getButtonText();
    String getDescription();
    String getIcon();
    java.awt.Color getColor();
    ActionListener getActionListener();
    JButton createButton();
    JPanel createCard();
}