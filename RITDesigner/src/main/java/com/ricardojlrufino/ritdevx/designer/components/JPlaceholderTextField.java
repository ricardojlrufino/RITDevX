package com.ricardojlrufino.ritdevx.designer.components;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JTextField;

public class JPlaceholderTextField extends JTextField {

	private String ph;
	private Font phFont;
 
	public JPlaceholderTextField(String ph) {
		this.ph = ph;
		phFont = getFont().deriveFont(Font.ITALIC);
	}
	
	public JPlaceholderTextField() {
		this.ph = null;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (super.getText().length() > 0 || ph == null) {
			return;
		}
		
		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(super.getDisabledTextColor());
		g2.setFont(phFont);
		g2.drawString(ph, getInsets().left, g.getFontMetrics().getMaxAscent() + getInsets().top);
	}
}