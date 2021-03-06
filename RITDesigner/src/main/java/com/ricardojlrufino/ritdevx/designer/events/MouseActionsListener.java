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

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import com.ricardojlrufino.ritdevx.controller.widgets.OnOffInterface;
import com.ricardojlrufino.ritdevx.controller.widgets.WidgetInfo;
import com.ricardojlrufino.ritdevx.designer.RIDesigner;
import com.ricardojlrufino.ritdevx.designer.components.GridDesignLayer;
import com.ricardojlrufino.ritdevx.designer.components.ResizableBorder;
import com.ricardojlrufino.ritdevx.designer.view.DesignerCanvas;

import jiconfont.IconCode;

/**
 * Class which provides Mouse functionality (click / popup). Not Mouse move or wheel events.
 * 
 * @author Svatek Tomáš
 * @author Ricardo JL Rufino
 */
public final class MouseActionsListener implements MouseListener {
  private JComponent comp;

  //  private Container parent;
  //  private TreeMap<String, JComponent> addedComponentsMap;
  //  private TreeMap<String, EventsInit> componentEvents;
  private JPopupMenu popup;
  //  private PropertiesPanel propertiesTable;
  //  private JComboBox<String> comboBox;
  //  private JTabbedPane events;
  //  private EventsTable eventsTable;
  //  private ArrayList<JComponent> panels;
  private RIDesigner designer;

  public MouseActionsListener(JComponent component, RIDesigner designer) {

    this.comp = component;
    this.designer = designer;

    if (!(comp instanceof JLayeredPane)) {
      comp.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
    }

  }

  /**
   * When left mouse is clicked, componets show border with a little black square for resize. When we click on righ
   * mouse button, we will see a pop-up menu.
   * 
   * @param e
   */
  @Override
  public void mouseClicked(MouseEvent e) {
    
    int btn = e.getButton();
    
    if (btn == 1) {
      // System.out.println(parent.getComponentZOrder(comp));
      comp.requestFocus();

      // Change state on designer to preview 
      if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
        if (comp instanceof OnOffInterface) {
          OnOffInterface offInterface = (OnOffInterface) comp;
          offInterface.toggle();
        }
      }

      try {
        
        designer.setSelectedComponent(comp);
        
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    } else if (btn == 3) {
      createPopupMenu();
      popup.show(e.getComponent(), e.getX(), e.getY());
    }

  }

  
  @Override
  public void mouseEntered(MouseEvent e) {
    if (!(comp instanceof JLayeredPane)) {
      GridDesignLayer.setHoverComponent(comp);
    }
  }
  
  /**
   * Set back to component default border
   * 
   * @param e
   */
  @Override
  public void mouseExited(MouseEvent e) {
    if(comp != designer.getSelectedComponent()) {
      GridDesignLayer.setHoverComponent(null);
    }
  }

  /**
   * When mouse is pressed tab name of tabb panel with events change name and set properties in properties table of
   * choosen component.
   * 
   * @param e
   */
  @Override
  public void mousePressed(MouseEvent e) {
    designer.setSelectedComponent(comp);
    GridDesignLayer.setDragComponent(comp); // show reference lines.
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    GridDesignLayer.setDragComponent(null); // clear guide lines...
  }

  /**
   * Method which generate pop-up menu. Create submenus for select Icon and File types...
   */
  public void createPopupMenu() {
    JMenuItem menuItem;

    // Create the popup menu.
    popup = new JPopupMenu();

    WidgetInfo widgetInfo = (WidgetInfo) comp.getClientProperty(WidgetInfo.PROPERTY_KEY);

    ActionListener triggerPropertyChange = new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        designer.getPropertiesTable().fireEditProperty(e.getActionCommand());
      }
    };

    List<PropertyDescriptor> availableProperties = widgetInfo.getAvailableProperties();
    for (PropertyDescriptor propertyDescriptor : availableProperties) {

      if (propertyDescriptor.getPropertyType() == File.class
          || propertyDescriptor.getPropertyType() == IconCode.class) {
        menuItem = new JMenuItem("Change " + propertyDescriptor.getName());
        menuItem.setActionCommand(propertyDescriptor.getName());
        menuItem.addActionListener(triggerPropertyChange);
        popup.add(menuItem);
      }

    }

    if (!DesignerCanvas.CANVAS_NAME.equals(comp.getName())) {

      popup.addSeparator();

      menuItem = new JMenuItem("Rename");
      menuItem.addActionListener(e -> designer.renameComponent(comp));
      popup.add(menuItem);

      menuItem = new JMenuItem("Clone");
      menuItem.addActionListener(e -> designer.cloneComponent(comp));
      popup.add(menuItem);

      menuItem = new JMenuItem("Delete");
      menuItem.addActionListener(e -> designer.deleteComponent(comp));
      popup.add(menuItem);
      
      popup.addSeparator();
      
      menuItem = new JMenuItem("Layer Up");
      menuItem.addActionListener(e -> designer.moveUpDown(comp, true));
      popup.add(menuItem);

      menuItem = new JMenuItem("Layer Down");
      menuItem.addActionListener(e -> designer.moveUpDown(comp, false));
      popup.add(menuItem);

    }

  }


}
