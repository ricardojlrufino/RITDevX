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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

import com.ricardojlrufino.ritdevx.designer.propeditor.components.IconCodeDialog;

import jiconfont.IconCode;

public class IconCodePropertyEditor extends AbstractPropertyEditor implements AllowPopupEditor {

  private IconCode iconCode;
  private JLabel component;

  public IconCodePropertyEditor() {
    component = new JLabel();
    editor = component;

    component.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        openSelector();
      }
    });

  }

  public Object getValue() {
    return iconCode;
  }

  public void setValue(Object value) {
    iconCode = (IconCode) value;
    if (value != null)
      component.setText(value.toString());
  }

  public void openSelector() {
    String title = "Select Icon";
    IconCode selected = IconCodeDialog.showDialog(editor, title);
    if (selected != null) {
      IconCode oldValue = iconCode;
      IconCode newValue = selected;
      iconCode = newValue;
      component.setText(newValue.toString());
      firePropertyChange(oldValue, newValue);
    }
  }

}
