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

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JColorChooser;

import com.ricardojlrufino.ritdevx.designer.propeditor.components.LabelColor;

/**
 * ColorPropertyEditor. <br>
 * 
 */
public class ColorPropertyEditor extends AbstractPropertyEditor implements AllowPopupEditor {

  private Color color;
  private LabelColor labelColor;

  public ColorPropertyEditor() {
    labelColor = new LabelColor();
    editor = labelColor;

    labelColor.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        openSelector();
      }
    });

    // ((JPanel)editor).setLayout(new BoxLayout((JComponent)editor, BoxLayout.X_AXIS));
    // ((JPanel)editor).add("*", label = new JLabel());
    // label.setOpaque(true);
    // ((JPanel)editor).add(button = createMiniButton());
    // button.addActionListener(new ActionListener() {
    // public void actionPerformed(ActionEvent e) {
    // selectColor();
    // }
    // });
    // ((JPanel)editor).add(button = createMiniButton());
    // button.setText("X");
    // button.addActionListener(new ActionListener() {
    // public void actionPerformed(ActionEvent e) {
    // selectNull();
    // }
    // });
    // ((JPanel)editor).setOpaque(false);
  }

  public Object getValue() {
    return color;
  }

  public void setValue(Object value) {
    color = (Color) value;
    if (value != null)
      labelColor.setColor(color);
  }

  public void openSelector() {
    String title = "Color";
    Color selectedColor = JColorChooser.showDialog(editor, title, color);

    if (selectedColor != null) {
      Color oldColor = color;
      Color newColor = selectedColor;
      color = newColor;
      labelColor.setColor(newColor);
      firePropertyChange(oldColor, newColor);
    }
  }

  public static class AsInt extends ColorPropertyEditor {
    public void setValue(Object arg0) {
      if (arg0 instanceof Integer) {
        super.setValue(new Color(((Integer) arg0).intValue()));
      } else {
        super.setValue(arg0);
      }
    }

    public Object getValue() {
      Object value = super.getValue();
      if (value == null) {
        return null;
      } else {
        return new Integer(((Color) value).getRGB());
      }
    }

    protected void firePropertyChange(Object oldValue, Object newValue) {
      if (oldValue instanceof Color) {
        oldValue = new Integer(((Color) oldValue).getRGB());
      }
      if (newValue instanceof Color) {
        newValue = new Integer(((Color) newValue).getRGB());
      }
      super.firePropertyChange(oldValue, newValue);
    }
  }
}
