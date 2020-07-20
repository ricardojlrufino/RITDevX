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
package com.ricardojlrufino.ritdevx.designer.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import com.ricardojlrufino.ritdevx.controller.components.BackgroudPanel;
import com.ricardojlrufino.ritdevx.designer.view.DesignerCanvas;

/**
 * Draw grid, backgroud, and also make line guides for active component when draged.
 * This is usead as layer of {@link DesignerCanvas}  
 * @author Ricardo JL Rufino - (ricardo.jl.rufino@gmail.com)
 * @date 2 de jun de 2020
 */
public class GridDesignLayer extends JPanel {

  private static final long serialVersionUID = 1L;

  private DesignerCanvas canvas;

  private Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 5 }, 0);
  private Stroke normal = new BasicStroke(1);

  private static JComponent dragComponent;
  private static JComponent selectedComponent;
  private static JComponent hoverComponent;

  private boolean showGrid;
  private int gridSize;
  private Color gridColor = new Color(206, 206, 255);
  
  private static GridDesignLayer ref;

  public GridDesignLayer(DesignerCanvas hmiDesignerCanvas) {
    this.canvas = hmiDesignerCanvas;
    ref = this;
    setOpaque(false);
    setBounds(0, 0, canvas.getWidth(), canvas.getHeight());

    hmiDesignerCanvas.addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        canvas.repaint();
      }
    });
  }
  
  @Override
  public boolean contains(int x, int y) {
    // Alow pass mouse events.
    return false;
  }

  public void setGridColor(Color gridColor) {
    this.gridColor = gridColor;
    repaint();
  }

  public Color getGridColor() {
    return gridColor;
  }

  public void setShowGrid(boolean showGrid) {
    this.showGrid = showGrid;
    repaint();
  }

  public boolean isShowGrid() {
    return showGrid;
  }

  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g.create();
    
    int width = getWidth();
    int height = getHeight();

    if (showGrid && gridSize > 1) {
      int x = 0;
      int y = 0;

      g2.setColor(gridColor);
      g2.setStroke(dashed);

      while (x < width) {
        g2.drawLine(x, 0, x, height);
        x += gridSize;
      }

      while (y < height) {
        g2.drawLine(0, y, width, y);
        y += gridSize;
      }
    }
    
    // Draw lines whem move component in canvas
    if (dragComponent != null) {
      drawGuideLines(g2);
    }

    // Draw hover effect
    if (hoverComponent != null) {
      drawSelectedLines(hoverComponent, g2, false);
    }
    
    // Draw selected effect
    if (selectedComponent != null) {
      drawSelectedLines(selectedComponent, g2, true);
    }
    
    g2.dispose();
  }
  
 private void drawSelectedLines(JComponent comp, Graphics2D g2, boolean selected) {
   
    g2.setStroke(normal);
    g2.setColor(Color.BLUE);
    Rectangle bounds = comp.getBounds();
    Insets insets = comp.getInsets();
    int size = insets.top;
    
    bounds.setBounds(bounds.x + size, bounds.y + size, bounds.width - size * 2, bounds.height - size * 2);
    g2.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
    
    // Draw resize marker on corner
    if(selected) {
      int dragSize = 8;
      g2.fillRect(bounds.x + bounds.width - dragSize, bounds.y + bounds.height - dragSize, dragSize, dragSize);
    }
    
  }
 
  private void drawGuideLines(Graphics2D g2) {
    
    int width = getWidth();
    int height = getHeight();
    
    g2.setStroke(normal);
    g2.setColor(Color.RED);
    Rectangle bounds = dragComponent.getBounds();
    Insets insets = dragComponent.getInsets();
    int size = insets.top;
    bounds.setBounds(bounds.x + size, bounds.y + size, bounds.width - size * 2, bounds.height - size * 2);

    g2.drawLine(0, bounds.y, width, bounds.y);
    g2.drawLine(0, bounds.y + bounds.height, width, bounds.y + bounds.height);

    g2.drawLine(bounds.x, 0, bounds.x, height);
    g2.drawLine(bounds.x + bounds.width, 0, bounds.x + bounds.width, height);
    
  }

  public void setGridSize(int size) {
    this.gridSize = size;
  }


  public static void setHoverComponent(JComponent hoverComponent) {
    if(hoverComponent instanceof JLayeredPane) return;
    
    JComponent oldComponent = GridDesignLayer.hoverComponent;
    
    GridDesignLayer.hoverComponent = hoverComponent;
    
    if(hoverComponent != null) {
      Rectangle bounds = hoverComponent.getBounds();
      ref.paintImmediately(new Rectangle(bounds.x, bounds.y, bounds.width + 1, bounds.height + 1));
    }
    
    if(oldComponent != null) {
      Rectangle bounds = oldComponent.getBounds();
      ref.paintImmediately(new Rectangle(bounds.x, bounds.y, bounds.width + 1, bounds.height + 1));
    }
  }
  
  public static void setSelectedComponent(JComponent selectedComponent) {
    if(selectedComponent instanceof JLayeredPane) return;
    GridDesignLayer.selectedComponent = selectedComponent;
    ref.repaint();
  }
  
  public static void setDragComponent(JComponent currentComponent) {
    if(currentComponent instanceof JLayeredPane) return;
    GridDesignLayer.dragComponent = currentComponent;
  }

}
