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
package com.ricardojlrufino.ritdevx.designer.propeditor;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

/**
 * ComboBoxPropertyEditor. <br>
 *  
 */
public class ComboBoxPropertyEditor extends AbstractPropertyEditor {

  private Object oldValue;
  private Icon[] icons;

  public ComboBoxPropertyEditor() {
    editor = new JComboBox() {
      public void setSelectedItem(Object anObject) {
        oldValue = getSelectedItem();
        super.setSelectedItem(anObject);
      }
    };

    final JComboBox combo = (JComboBox) editor;

    combo.setRenderer(new Renderer());
    combo.addPopupMenuListener(new PopupMenuListener() {
      public void popupMenuCanceled(PopupMenuEvent e) {
        // ignore.
      }

      public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
        ComboBoxPropertyEditor.this.firePropertyChange(oldValue, combo.getSelectedItem());
      }

      public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
        // ignore.
      }
    });
    combo.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          ComboBoxPropertyEditor.this.firePropertyChange(oldValue, combo.getSelectedItem());
        }
      }
    });
    combo.setSelectedIndex(-1);
  }

  public Object getValue() {
    Object selected = ((JComboBox) editor).getSelectedItem();
    if (selected instanceof Value) {
      return ((Value) selected).value;
    } else {
      return selected;
    }
  }

  public void setValue(Object value) {
    JComboBox combo = (JComboBox) editor;
    Object current = null;
    int index = -1;
    for (int i = 0, c = combo.getModel().getSize(); i < c; i++) {
      current = combo.getModel().getElementAt(i);
      if (value == current || (current != null && current.equals(value))) {
        index = i;
        break;
      }
    }
    ((JComboBox) editor).setSelectedIndex(index);
  }

  public void setAvailableValues(Object[] values) {
    ((JComboBox) editor).setModel(new DefaultComboBoxModel(values));
  }

  public void setAvailableIcons(Icon[] icons) {
    this.icons = icons;
  }

  public class Renderer extends DefaultListCellRenderer {
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                                                  boolean cellHasFocus) {
      Component component = super.getListCellRendererComponent(list,
                                                               (value instanceof Value) ? ((Value) value).visualValue
                                                                                        : value,
                                                               index, isSelected, cellHasFocus);
      if (icons != null && index >= 0 && component instanceof JLabel)
        ((JLabel) component).setIcon(icons[index]);
      return component;
    }
  }

  public static final class Value {
    private Object value;
    private Object visualValue;

    public Value(Object value, Object visualValue) {
      this.value = value;
      this.visualValue = visualValue;
    }

    public boolean equals(Object o) {
      if (o == this)
        return true;
      if (value == o || (value != null && value.equals(o)))
        return true;
      return false;
    }

    public int hashCode() {
      return value == null ? 0 : value.hashCode();
    }
  }
}
