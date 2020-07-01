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
package com.ricardojlrufino.ritdevx.designer.events;

import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import com.ricardojlrufino.ritdevx.designer.components.BeanPropertiesTabeModel;
import com.ricardojlrufino.ritdevx.designer.components.GridDesignLayer;
import com.ricardojlrufino.ritdevx.designer.components.ResizableBorder;
import com.ricardojlrufino.ritdevx.designer.view.PropertiesPanel;

/**
 * Class which provides Mouse motin functionality.
 * 
 * @author Svatek Tomáš
 * @author Ricardo JL Rufino
 */
public class WidgetResizeController extends MouseAdapter implements MouseMotionListener {

  private static final int RESIZE_AREA = 8;
//  private static final int BORDER_SIZE = 1;
//  private static final int ANCHOR_LOC = 2;

  private JComponent comp;
  private JComponent parent;

  private Border borderDefault;
  private Border resizableBorder;
  
  private PropertiesPanel properties;
  boolean resizing = false;

  /**
   * ¨Class constructor
   * 
   * @param component
   *          component which has this listener
   * @param table
   *          properties table for update
   */
  public WidgetResizeController(JComponent component, PropertiesPanel table) {

    this.comp = component;
    this.comp.setDoubleBuffered(true);
    this.borderDefault = comp.getBorder();
    this.resizableBorder = new ResizableBorder();
    this.parent = (JComponent) comp.getParent();
    this.properties = table;

  }

  private void showResizing() {
    comp.setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
//    comp.setBorder(resizableBorder);
  }

  private boolean isOnBorder(MouseEvent e) {
    int x = comp.getBounds().width;
    int y = comp.getBounds().height;
    return e.getX() > (x - RESIZE_AREA) && e.getX() <= (x + RESIZE_AREA) && e.getY() > (y - RESIZE_AREA)
           && e.getY() < (y + RESIZE_AREA);
  }

  @Override
  public void mousePressed(MouseEvent e) {
    if (isOnBorder(e)) {
      resizing = true;
      showResizing();
    }
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    resizing = false;
    comp.setCursor(Cursor.getDefaultCursor());
    comp.setBorder(borderDefault);
    GridDesignLayer.setDragComponent(null);
    comp.getParent().repaint();
  }

  /**
   * When mouse move in left bottom corner of components, this method change border and show a resizable square
   * 
   * @param e
   */
  @Override
  public void mouseMoved(MouseEvent e) {
    if (isOnBorder(e)) {
      showResizing();
    } else {
      comp.setCursor(Cursor.getDefaultCursor());
    }
  }

  /**
   * When mouse is dragged in possition near left bottom corner, this method provide resizing of component.
   * 
   * @param e
   */
  @Override
  public void mouseDragged(MouseEvent e) {
    if (SwingUtilities.isLeftMouseButton(e) && resizing) {
      try {
        comp.setSize(e.getX(), e.getY());

        GridDesignLayer.setDragComponent(comp);

        // Set min size...
        int minSize = 15;
        if (comp.getSize().width < minSize)
          comp.setSize(minSize, comp.getSize().height);
        if (comp.getSize().height < minSize)
          comp.setSize(comp.getSize().width, minSize);

        showResizing();

        BeanPropertiesTabeModel model = (BeanPropertiesTabeModel) properties.getTable().getModel();
        model.fireBeanPropertyUpdated("Size");

        parent.repaint();
      } catch (NullPointerException ex) {
        // todo
      }
    }
  }

}
