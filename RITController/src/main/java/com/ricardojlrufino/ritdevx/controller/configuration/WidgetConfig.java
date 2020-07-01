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
package com.ricardojlrufino.ritdevx.controller.configuration;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JComponent;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import com.ricardojlrufino.ritdevx.controller.configuration.jaxb.KeyValue;
import com.ricardojlrufino.ritdevx.controller.widgets.WidgetInfo;
import com.ricardojlrufino.ritdevx.controller.widgets.factory.DefaultWidgetsFactory;


/**
 * Store values os properties of a {@link JComponent} mapped by {@link WidgetInfo}
 * 
 * @author Ricardo JL Rufino - (ricardo.jl.rufino@gmail.com)
 * @date 5 de jun de 2020
 * @see DefaultWidgetsFactory
 */
@XmlRootElement(name = "widget")
public class WidgetConfig {

  private String widgetType;

  private String name;

  private List<KeyValue> values = new LinkedList<>();

  public String getWidgetType() {
    return widgetType;
  }

  @XmlAttribute(name = "type")
  public void setWidgetType(String widgetType) {
    this.widgetType = widgetType;
  }

  @XmlAttribute(name = "name")
  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  @XmlAnyElement()
  public void setValues(List<KeyValue> values) {
    this.values = values;
  }


  public List<KeyValue> getValues() {
    return values;
  }

  public HashMap<String, Object> getValuesMap() {
    HashMap<String, Object> map = new HashMap<>();
    for (KeyValue keyValue : values) {
      map.put(keyValue.getName(), keyValue.getValue());
    }
    return map;
  }

  public void addValue(String name, Object value) {
    this.values.add(new KeyValue(name, value));
  }


}
