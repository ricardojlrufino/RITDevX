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

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

/**
 * StringConverterPropertyEditor. <br>A comma separated list of values.
 */
public abstract class StringPropertyEditor extends AbstractPropertyEditor {

  private Object oldValue;

  public StringPropertyEditor() {
    editor = new JTextField();
    ((JTextField) editor).setBorder(BorderFactory.createEmptyBorder());
  }

  public Object getValue() {
    String text = ((JTextComponent) editor).getText();
    if (text == null || text.trim().length() == 0) {
      return null;
    } else {
      try {
        return convertFromString(text.trim());
      } catch (Exception e) {
        /*UIManager.getLookAndFeel().provideErrorFeedback(editor);*/
        return oldValue;
      }
    }
  }

  public void setValue(Object value) {
    if (value == null) {
      ((JTextComponent) editor).setText("");
    } else {
      oldValue = value;
      ((JTextComponent) editor).setText(convertToString(value));
    }
  }

  protected abstract Object convertFromString(String text);

  protected abstract String convertToString(Object value);

}
