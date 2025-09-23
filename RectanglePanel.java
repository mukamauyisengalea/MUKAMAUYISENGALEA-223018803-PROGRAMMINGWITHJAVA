package com.form;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

class RectanglePanel extends JPanel implements ActionListener {
	JLabel lengthLbl = new JLabel("Length:");
	JLabel widthLbl = new JLabel("Width:");
	JTextField lengthTxt = new JTextField();
	JTextField widthTxt = new JTextField();
	JButton calcBtn = new JButton("Calculate");
	JButton clearBtn = new JButton("Clear");

	JTable table;
	DefaultTableModel model;

	public RectanglePanel() {
		setLayout(null);

		lengthLbl.setBounds(30, 20, 80, 25);
		widthLbl.setBounds(30, 60, 80, 25);
		lengthTxt.setBounds(120, 20, 100, 25);
		widthTxt.setBounds(120, 60, 100, 25);
		calcBtn.setBounds(50, 100, 100, 30);
		clearBtn.setBounds(160, 100, 100, 30);

		add(lengthLbl);
		add(lengthTxt);
		add(widthLbl);
		add(widthTxt);
		add(calcBtn);
		add(clearBtn);

		String[] cols = {"Length", "Width", "Area", "Perimeter"};
		model = new DefaultTableModel(cols, 0);
		table = new JTable(model);
		JScrollPane scroll = new JScrollPane(table);
		scroll.setBounds(30, 150, 500, 250);
		add(scroll);

		calcBtn.addActionListener(this);
		clearBtn.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == calcBtn) {
			try {
				double l = Double.parseDouble(lengthTxt.getText());
				double w = Double.parseDouble(widthTxt.getText());
				double area = l * w;
				double peri = 2 * (l + w);
				model.addRow(new Object[]{l, w, String.format("%.2f", area), String.format("%.2f", peri)});
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} else if (e.getSource() == clearBtn) {
			lengthTxt.setText("");
			widthTxt.setText("");
			model.setRowCount(0);
		}
	}
}
