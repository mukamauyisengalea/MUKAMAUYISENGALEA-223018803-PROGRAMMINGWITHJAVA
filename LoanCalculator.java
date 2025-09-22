package com.LoanCalculator;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class LoanCalculator implements ActionListener{

	JFrame frame;
	JLabel amountLabel = new JLabel("Amount requested");
	JLabel durationLabel = new JLabel("Duration (year)");
	JLabel totalLabel = new JLabel("Total to return");
	JTextField amountField = new JTextField();
	JTextField durationField = new JTextField();
	JTextField totalField = new JTextField();

	JButton calculateBtn = new JButton("Calculate");

	public LoanCalculator() {
		createWindow();
		setLocationAndSize();
		addComponentsToFrame();
		addEventListeners();
	}



	private void createWindow() {
		frame =new JFrame();
		frame.setTitle("====Loancalculator====");
		frame.setBounds(10,10,380,600);
		frame.getContentPane().setBackground(Color.BLUE);
		frame.getContentPane().setLayout(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
	}


	private void setLocationAndSize() {
		amountLabel.setBounds(20, 30, 150, 30);
		amountField.setBounds(180, 30, 150, 30);

		durationLabel.setBounds(20, 80, 150, 30);
		durationField.setBounds(180, 80, 150, 30);

		totalLabel.setBounds(20, 130, 150, 30);
		totalField.setBounds(180, 130, 150, 30);
		totalField.setEditable(false);

		calculateBtn.setBounds(120, 190, 150, 40);	

	}
	private void addComponentsToFrame() {
		frame.add(amountLabel);
		frame.add(durationLabel);
		frame.add(totalLabel);
		frame.add(amountField);
		frame.add(durationField);
		frame.add(totalField);
		frame.add(calculateBtn);
	}
	private void addEventListeners() {
		calculateBtn.addActionListener((ActionListener) this);
	}

	public void actionPerformed(ActionEvent e) {
		try {
			double amount = Double.parseDouble(amountField.getText());
			int years = Integer.parseInt(durationField.getText());

			// Example: 0.6% interest per year
			double interestRate = 0.006;  
			double total = amount + (amount * interestRate * years);

			totalField.setText(String.valueOf(total)); 
		} catch (NumberFormatException ex) {
			totalField.setText("Invalid input");
		}
	}

	public static void main(String[] args) {
		new LoanCalculator();
	}
}
