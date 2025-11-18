package main;

import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import util.DatabaseConnection;
import view.LoginForm;



public class MainApplication {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			if (DatabaseConnection.getConnection() != null) {
				System.out.println("Application started successfully!");

				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						LoginForm loginForm = new LoginForm();
						loginForm.setVisible(true);
					}
				});
			} else {
				JOptionPane.showMessageDialog(null,
						"Database connection failed! Please check your configuration.",
						"Connection Error",
						JOptionPane.ERROR_MESSAGE);
				System.exit(1);
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null,
					"An error occurred while connecting to the database:\n" + e.getMessage(),
					"Database Error",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			System.exit(1);
		}
	}
}


