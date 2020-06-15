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
/*******************************************************************************
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
package com.ricardojlrufino.ritdevx.designer.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.Border;

/**
 * Class which provides black border with a small square in right bottom corner
 * of component.
 * @author SirReaver
 */
public class ResizableBorder implements Border {
  private static final int CORECTION = 1;
  private Color color;
  private int size;

  public ResizableBorder() {
    this(Color.RED, 8);
  }

  public ResizableBorder(Color color, int size) {
    super();
    this.color = color;
    this.size = size;
  }

  @Override
  public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
    //Graphics2D g2 = (Graphics2D) g;
    g.setColor(color);
    //g2.setStroke(new BasicStroke(2));
    g.drawRect(x, y, width - CORECTION, height - CORECTION);
    g.fillRect(x + width - size, y + height - size, size, size);
  }

  @Override
  public Insets getBorderInsets(Component c) {
    return new Insets(size, size, size, size);
  }

  @Override
  public boolean isBorderOpaque() {
    return true;
  }

}
