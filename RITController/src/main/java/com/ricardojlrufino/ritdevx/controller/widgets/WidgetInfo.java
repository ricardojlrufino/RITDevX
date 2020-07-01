/*******************************************************************************
 * This file is part of RITDevX Controller project.
 * Copyright (c) 2020 Ricardo JL Rufino.
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
package com.ricardojlrufino.ritdevx.controller.widgets;

import java.awt.Dimension;
import java.awt.datatransfer.DataFlavor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import com.ricardojlrufino.ritdevx.controller.configuration.VirtualProperty;
import com.ricardojlrufino.ritdevx.controller.configuration.WidgetConfig;
import com.ricardojlrufino.ritdevx.controller.widgets.factory.WidgetFactory;
import com.ricardojlrufino.ritdevx.controller.widgets.skinnable.ImageState;

/**
 * Describes the properties available for a given component (like {@link ImageState}), which can be changed by the user. <br/>
 * The component instances related to this widget, are instantiated by {@link WidgetFactory}.
 * @author Ricardo JL Rufino - (ricardo.jl.rufino@gmail.com)
 * @date 14 de jun de 2020
 */
public class WidgetInfo {

  public static DataFlavor dataFlavor = new DataFlavor(WidgetInfo.class, "WidgetInfo");

  public static String PROPERTY_KEY = "widget";
  
  public static String LAYER_PROPERTY = "Layer";

  private Class<?> componentClass;

  private List<PropertyDescriptor> availableProperties = new LinkedList<>();

  private String name;

  private String icon;

  private Dimension size;

  private WidgetFactory factory;

  public WidgetInfo(Class<?> componentClass) {
    this(componentClass, componentClass.getSimpleName());
  }

  public WidgetInfo(Class<?> componentClass, String name) {
    super();
    this.componentClass = componentClass;
    this.name = name;
  }

  public void setSize(Dimension size) {
    this.size = size;
  }

  public void setSize(int width, int height) {
    this.size = new Dimension(width, height);
  }

  public Dimension getSize() {
    return size;
  }

  public Class<?> getComponentClass() {
    return componentClass;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setFactory(WidgetFactory factory) {
    this.factory = factory;
  }

  public void setIcon(String icon) {
    this.icon = icon;
  }

  public String getIcon() {
    if (icon == null)
      return componentClass.getSimpleName();
    return icon;
  }

  public WidgetFactory getFactory() {
    return factory;
  }

  public PropertyDescriptor addProperty(String propertyName) {
    try {
      PropertyDescriptor propertyDescriptor = new PropertyDescriptor(propertyName, componentClass);
      availableProperties.add(propertyDescriptor);
      return propertyDescriptor;
    } catch (IntrospectionException e) {
      e.printStackTrace();
      return null;
    }
  }
  
  public void addVirtualProperty(String propertyName, Class<?> propertyClass) {
    try {
      availableProperties.add(new VirtualProperty(propertyName, componentClass, propertyClass));
    } catch (IntrospectionException e) {
      e.printStackTrace();
    }
  }

  public PropertyDescriptor getProperty(String property) {
    for (PropertyDescriptor propertyDescriptor : availableProperties) {
      if (propertyDescriptor.getName().equals(property)) {
        return propertyDescriptor;
      }
    }
    return null;
  }

  public void setAvailableProperties(List<PropertyDescriptor> availableProperties) {
    this.availableProperties = availableProperties;
  }

  public List<PropertyDescriptor> getAvailableProperties() {
    return availableProperties;
  }

  /**
   * Set default value for this property
   */
  public void setDefaultValue(String property, Object value) {
    PropertyDescriptor propertyDescriptor = getProperty(property);
    if (propertyDescriptor != null)
      propertyDescriptor.setValue("value", value);

  }

  public boolean hasDefaultValue(String property) {
    for (PropertyDescriptor propertyDescriptor : availableProperties) {
      if (propertyDescriptor.getName().equals(property)) {
        return propertyDescriptor.getValue("value") != null;
      }
    }
    return false;
  }

  public WidgetInfo clone() {

    WidgetInfo clone = new WidgetInfo(componentClass);
    clone.setName(name);
    clone.setIcon(icon);
    clone.setSize(size);
    clone.setFactory(factory);

    for (PropertyDescriptor propertyDescriptor : availableProperties) {
      clone.addProperty(propertyDescriptor.getName());
    }

    return clone;
  }

  /**
   * Return WidgetInfo storent in ClientProperty of JComponent
   */
  public static WidgetInfo get(JComponent component) {
    return (WidgetInfo) component.getClientProperty(PROPERTY_KEY);

  }
  
  /**
   * Virtual properties are properties that are not declared in components and are stored as: 'component.putClientProperty'
   */
  public boolean isVirtualPropery(String property) {
    return "layer".equals(property.toLowerCase());
  }

  /**
   * Extract all values from JComponent and create a WidgetConfig for storage.
   */
  public WidgetConfig persistProperties(JComponent component) {
    WidgetConfig config = new WidgetConfig();
    config.setWidgetType(getName());
    config.setName(component.getName());

    for (PropertyDescriptor property : availableProperties) {
      try {
        
        if(isVirtualPropery(property.getName())) {
          Object clientProperty = component.getClientProperty(property.getName());
          config.addValue(property.getName(), clientProperty);
        }else {
          Method readMethod = property.getReadMethod();
          Object value = readMethod.invoke(component, new Object[] {});
          config.addValue(property.getName(), value);
        }
        
      } catch (Exception e) {
        e.printStackTrace();
      }

    }

    return config;
  }

  /**
   * Restore all values from widgetConfig to JComponent.
   * 
   * @throws Exception
   */
  public JComponent restoreProperties(WidgetConfig widgetConfig, JComponent component) throws Exception {
    // Inject values in JComponent
    HashMap<String, Object> valuesMap = widgetConfig.getValuesMap();
    for (String name : valuesMap.keySet()) {
      PropertyDescriptor property = this.getProperty(name);
      try {
        if (property != null) {
          if(isVirtualPropery(property.getName())) {
            component.putClientProperty(property.getName(), valuesMap.get(name));
          }else {
            property.getWriteMethod().invoke(component, valuesMap.get(name));
          }
        } 
      } catch (Exception e) {
        e.printStackTrace();
      }

    }

    return component;
  }
}
