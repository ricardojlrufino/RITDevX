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

import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import com.ricardojlrufino.ritdevx.controller.utils.ReflectionUtils;

/**
 * Base editor for numbers. <br>
 */
public class NumberPropertyEditor extends AbstractPropertyEditor {

  private final Class type;
  private Object lastGoodValue;

  public NumberPropertyEditor(Class type) {
    if (!Number.class.isAssignableFrom(type)) {
      throw new IllegalArgumentException("type must be a subclass of Number");
    }

    editor = new JFormattedTextField();
    this.type = type;
    ((JFormattedTextField) editor).setValue(getDefaultValue());
    // ((JFormattedTextField)editor).setBorder(LookAndFeelTweaks.EMPTY_BORDER);

    ((JFormattedTextField) editor).setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter()));
  }

  public Object getValue() {
    String text = ((JTextField) editor).getText();
    if (text == null || text.trim().length() == 0) {
      return getDefaultValue();
    }

    // allow comma or colon
    text = text.replace(',', '.');

    // collect all numbers from this textfield
    StringBuffer number = new StringBuffer();
    number.ensureCapacity(text.length());
    for (int i = 0, c = text.length(); i < c; i++) {
      char character = text.charAt(i);
      if ('.' == character || '-' == character || (Double.class.equals(type) && 'E' == character)
          || (Float.class.equals(type) && 'E' == character) || Character.isDigit(character)) {
        number.append(character);
      } else if (' ' == character) {
        continue;
      } else {
        break;
      }
    }

    try {
      lastGoodValue = ReflectionUtils.tryToConvert(number.toString(), type, true);
    } catch (Exception e) {
      UIManager.getLookAndFeel().provideErrorFeedback(editor);
    }

    return lastGoodValue;
  }

  public void setValue(Object value) {
    if (value instanceof Number) {
      ((JFormattedTextField) editor).setText(value.toString());
    } else {
      ((JFormattedTextField) editor).setValue(getDefaultValue());
    }
    lastGoodValue = value;
  }

  private Object getDefaultValue() {
    try {
      return type.getConstructor(new Class[] { String.class }).newInstance(new Object[] { "0" });
    } catch (Exception e) {
      // will not happen
      throw new IllegalArgumentException(e);
    }
  }

}