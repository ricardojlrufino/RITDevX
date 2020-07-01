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

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.ricardojlrufino.ritdevx.controller.utils.UIHelper;
import com.ricardojlrufino.ritdevx.designer.view.PropertiesPanel;

/**
 * Class which provides keyboard functionality for component on canvas.
 * <ul>
 * <li>Delete component on DELETE key</li>
 * <li>Move components by 1px with arrow keys</li>
 * </ul>
 * 
 * @author Svatek Tomáš
 */
public class WidgetKeyListener implements KeyListener {

  private JComponent comp;
  private JComponent parent;
  private PropertiesPanel propertiesTable;
  private TreeMap<String, JComponent> addedComponentsMap;
  private int step = -1;
  private ArrayList<JComponent> panels;
  private JComboBox<String> combobox;

  /**
   * Class constructor
   * 
   * @param propertiesTable
   *          table of propertičes for update when deleted
   * @param addedComponentsMap
   *          list of added components for update when deleted
   * @param combobox
   *          combobox for update when deleted
   */
  public WidgetKeyListener(PropertiesPanel propertiesTable, TreeMap<String, JComponent> addedComponentsMap,
                           JComboBox<String> combobox) {

    this.propertiesTable = propertiesTable;
    this.addedComponentsMap = addedComponentsMap;
    this.combobox = combobox;
    panels = new ArrayList<>();

  }

  @Override
  public void keyTyped(KeyEvent e) {
    // ignore.
  }

  /**
   * Method which provides components movement when arrow key is pressed.
   * 
   * @param e
   */
  @Override
  public void keyPressed(KeyEvent e) {
    JComponent c = (JComponent) e.getSource();
    int currX = c.getLocation().x;
    int currY = c.getLocation().y;
    int key = e.getKeyCode();
    int steps = e.isControlDown() ? 5 : 1;
    
    switch (key) {
    case KeyEvent.VK_RIGHT:
      c.setLocation(currX+steps, currY);
      UIHelper.repaintParent(c);
      propertiesTable.getTable().fireBeanPropertyUpdated("Location");
      break;
    case KeyEvent.VK_LEFT:
      c.setLocation(currX-steps, currY);
      UIHelper.repaintParent(c);
      propertiesTable.getTable().fireBeanPropertyUpdated("Location");
      break;
    case KeyEvent.VK_UP:
      c.setLocation(currX, currY-steps);
      UIHelper.repaintParent(c);
      propertiesTable.getTable().fireBeanPropertyUpdated("Location");
      break;
    case KeyEvent.VK_DOWN:
      c.setLocation(currX, currY+steps);
      UIHelper.repaintParent(c);
      propertiesTable.getTable().fireBeanPropertyUpdated("Location");
      break;
    }

  }

  /**
   * Method for deleteing component after DELETE key is pressed. Call method deleteComponent()
   */
  @Override
  public void keyReleased(KeyEvent e) {
    comp = (JComponent) e.getSource();
    parent = (JComponent) comp.getParent();
    if (e.getKeyCode() == KeyEvent.VK_DELETE) {
      deleteComponent();
    }

  }

  /**
   * Method which delete component and components on that component Taht means if we have panel on panel which has
   * another panel, we delete all components on that panels and panel itself.
   */
  public void deleteComponent() {
    findPanels(comp);
    if (comp instanceof JPanel && comp.getComponents().length > 0) {
      for (Component c : comp.getComponents()) {
        addedComponentsMap.remove(c.getName());
        combobox.removeItem(c.getName());
        comp.remove(c);
      }
    }
    for (JComponent c : panels) {
      for (Component k : c.getComponents()) {
        addedComponentsMap.remove(k.getName());
        combobox.removeItem(k.getName());
        c.remove(k);
      }
      comp.remove(c);
    }

    addedComponentsMap.remove(comp.getName());
    combobox.removeItem(comp.getName());
    parent.remove(comp);
    parent.revalidate();
    parent.repaint();
  }

  /**
   * Find all panels on panel component.
   * 
   * @param panel
   *          panel component
   */
  public void findPanels(JComponent panel) {
    step++;
    for (Component comp : panel.getComponents()) {
      if (comp instanceof JPanel) {
        panels.add((JComponent) comp);
      }
    }
    if (panels.size() > 0 && step < panels.size()) {
      findPanels(panels.get(step));
    }

  }

}
