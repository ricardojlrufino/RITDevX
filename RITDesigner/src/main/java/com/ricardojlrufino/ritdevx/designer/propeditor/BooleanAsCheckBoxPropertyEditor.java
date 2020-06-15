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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;

/**
 * BooleanAsCheckBoxPropertyEditor. <br>
 *  
 */
public class BooleanAsCheckBoxPropertyEditor extends AbstractPropertyEditor {

  private JCheckBox checkbox;

  public BooleanAsCheckBoxPropertyEditor() {
    checkbox = new JCheckBox();
    editor = checkbox;
    checkbox.setOpaque(false);
    checkbox.setHorizontalAlignment(JCheckBox.CENTER);
    checkbox.setHorizontalTextPosition(JCheckBox.LEFT);
    checkbox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        firePropertyChange(checkbox.isSelected() ? Boolean.FALSE : Boolean.TRUE,
                           checkbox.isSelected() ? Boolean.TRUE : Boolean.FALSE);
        checkbox.transferFocus();
      }
    });
  }

  public Object getValue() {
    return checkbox.isSelected() ? Boolean.TRUE : Boolean.FALSE;
  }

  public void setValue(Object value) {
    checkbox.setSelected(Boolean.TRUE.equals(value));
  }

}
