package view.admin.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public abstract class AbstractAdminButton implements AdminButton {
	protected String buttonText;
	protected String description;
	protected String icon;
	protected Color color;
	protected ActionListener actionListener;

	public AbstractAdminButton(String buttonText, String description, String icon, Color color, ActionListener actionListener) {
		this.buttonText = buttonText;
		this.description = description;
		this.icon = icon;
		this.color = color;
		this.actionListener = actionListener;
	}

	@Override
	public String getButtonText() {
		return buttonText;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getIcon() {
		return icon;
	}

	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public ActionListener getActionListener() {
		return actionListener;
	}

	@Override
	public JButton createButton() {
		JButton button = new JButton(getButtonText());
		button.setBackground(getColor());
		button.setForeground(Color.WHITE);
		button.setFont(new Font("SansSerif", Font.BOLD, 14));
		button.setFocusPainted(false);
		button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		button.setPreferredSize(new Dimension(180, 80));

		if (getActionListener() != null) {
			button.addActionListener(getActionListener());
		}

		return button;
	}

	@Override
	public JPanel createCard() {
		JPanel cardPanel = new JPanel(new BorderLayout());
		cardPanel.setBackground(Color.WHITE);
		cardPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(200, 200, 200)),
				BorderFactory.createEmptyBorder(12, 12, 12, 12)
				));

		JButton button = createButton();
		JLabel descriptionLabel = new JLabel("<html><center><small>" + getDescription() + "</small></center></html>", SwingConstants.CENTER);
		descriptionLabel.setForeground(new Color(80, 80, 80));
		descriptionLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));

		cardPanel.add(button, BorderLayout.CENTER);
		cardPanel.add(descriptionLabel, BorderLayout.SOUTH);

		return cardPanel;
	}
}