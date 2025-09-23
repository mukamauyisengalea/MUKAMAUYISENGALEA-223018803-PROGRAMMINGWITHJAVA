package com.form;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

class TrianglePanel extends JPanel implements ActionListener {
	JLabel baseLbl = new JLabel("Base:");
	JLabel heightLbl = new JLabel("Height:");
	JTextField baseTxt = new JTextField();
	JTextField heightTxt = new JTextField();
	JButton calcBtn = new JButton("Calculate");
	JButton clearBtn = new JButton("Clear");

	JTable table;
	DefaultTableModel model;

	public TrianglePanel() {
		setLayout(null);

		baseLbl.setBounds(30, 20, 80, 25);
		heightLbl.setBounds(30, 60, 80, 25);
		baseTxt.setBounds(120, 20, 100, 25);
		heightTxt.setBounds(120, 60, 100, 25);
		calcBtn.setBounds(50, 100, 100, 30);
		clearBtn.setBounds(160, 100, 100, 30);

		add(baseLbl);
		add(baseTxt);
		add(heightLbl);
		add(heightTxt);
		add(calcBtn);
		add(clearBtn);

		String[] cols = {"Base", "Height", "Area"};
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
				double b = Double.parseDouble(baseTxt.getText());
				double h = Double.parseDouble(heightTxt.getText());
				double area = 0.5 * b * h;
				model.addRow(new Object[]{b, h, String.format("%.2f", area)});
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} else if (e.getSource() == clearBtn) {
			baseTxt.setText("");
			heightTxt.setText("");
			model.setRowCount(0);
		}
	}
}

