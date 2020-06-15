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
import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyEditor;

/**
 * AbstractPropertyEditor. <br>
 *  
 */
public class AbstractPropertyEditor implements PropertyEditor {

  protected Component editor;
  private PropertyChangeSupport listeners = new PropertyChangeSupport(this);

  public boolean isPaintable() {
    return false;
  }

  public boolean supportsCustomEditor() {
    return true;
  }

  public Component getCustomEditor() {
    return editor;
  }

  public void addPropertyChangeListener(PropertyChangeListener listener) {
    listeners.addPropertyChangeListener(listener);
  }

  public void removePropertyChangeListener(PropertyChangeListener listener) {
    listeners.removePropertyChangeListener(listener);
  }

  protected void firePropertyChange(Object oldValue, Object newValue) {
    listeners.firePropertyChange("value", oldValue, newValue);
  }

  public Object getValue() {
    return null;
  }

  public void setValue(Object value) {
  }

  public String getAsText() {
    return null;
  }

  public String getJavaInitializationString() {
    return null;
  }

  public String[] getTags() {
    return null;
  }

  public void setAsText(String text) throws IllegalArgumentException {
  }

  public void paintValue(Graphics gfx, Rectangle box) {
  }

}
