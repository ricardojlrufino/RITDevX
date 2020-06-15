/*******************************************************************************
 * This file is part of RITDevX Designer project.
 * Copyright (c) 2020 Ricardo JL Rufino and others stated in this file.
 *
 * This program is free software: you can redistribute it and/or modify  
 * it under the terms of the GNU General Public License as published by  
 * the Free Software Foundation, version 3 with Classpath Exception.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License 
 * along with this program. See the included LICENSE file for details.
 *******************************************************************************/
package com.ricardojlrufino.ritdevx.designer.propeditor.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LabelColor extends JPanel {

  private JLabel label;
  private JLabel lColor;

  public LabelColor() {
    setLayout(new BorderLayout());
    label = new JLabel();
    lColor = new JLabel();
    lColor.setText("##");
    lColor.setToolTipText("Change");
    lColor.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

    this.setOpaque(false);
    label.setOpaque(false);
    lColor.setOpaque(true);

    lColor.setMinimumSize(new Dimension(50, 0));
    lColor.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
    label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

    add(label, BorderLayout.CENTER);
    add(lColor, BorderLayout.EAST);
  }

  public void setColor(Color color) {
    lColor.setBackground(color);
    lColor.setForeground(color);
    if (color != null)
      label.setText(colorToText(color));
  }

  public String colorToText(Color color) {
    return color.getRed() + "," + color.getGreen() + "," + color.getBlue();
  }

  @Override
  public synchronized void addMouseListener(MouseListener l) {
    super.addMouseListener(l);
    lColor.addMouseListener(l);
  }
}
