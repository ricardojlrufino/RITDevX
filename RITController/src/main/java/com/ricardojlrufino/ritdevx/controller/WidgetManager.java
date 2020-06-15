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
package com.ricardojlrufino.ritdevx.controller;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.JComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ricardojlrufino.ritdevx.controller.configuration.HmiConfig;
import com.ricardojlrufino.ritdevx.controller.configuration.WidgetConfig;
import com.ricardojlrufino.ritdevx.controller.widgets.DefaultWidgetsFactory;
import com.ricardojlrufino.ritdevx.controller.widgets.GaugesSteelSeriesFactory;
import com.ricardojlrufino.ritdevx.controller.widgets.WidgetFactory;
import com.ricardojlrufino.ritdevx.controller.widgets.WidgetInfo;
import com.ricardojlrufino.ritdevx.controller.widgets.XChatWidgetsFactory;

/**
 * Store widgets definitions ({@link WidgetInfo}) and inialized representation of widgets (JComponent).
 * 
 * @author Ricardo JL Rufino - (ricardo.jl.rufino@gmail.com)
 * @date 5 de jun de 2020
 */
public class WidgetManager {

  private static final Logger log = LoggerFactory.getLogger(WidgetManager.class);

  // Widget definition (from WidgetFactory)
  private Map<String, WidgetInfo> availableWidgets = new LinkedHashMap<>();

  // Widget instances as JComponent (from file ou designer)
  private HashMap<String, JComponent> widgetsComponents = new LinkedHashMap<>();


  public WidgetManager() {

    // Load all widgets..
    WidgetFactory[] widgetFactories = new WidgetFactory[] {
        new DefaultWidgetsFactory(),
        new XChatWidgetsFactory(),
        new GaugesSteelSeriesFactory()
    };

    for (WidgetFactory widgetFactory : widgetFactories) {

      List<WidgetInfo> list = widgetFactory.list();
      for (WidgetInfo widgetInfo : list) {
        availableWidgets.put(widgetInfo.getName(), widgetInfo);
      }
    }

  }

  public Map<String, WidgetInfo> getAvailableWidgets() {
    return availableWidgets;
  }

  public HashMap<String, JComponent> getWidgetsComponents() {
    return widgetsComponents;
  }

  /**
   * Load settings forn previous loaded config and initialize components
   */
  public HmiConfig init(HmiConfig config) throws IOException {

    List<WidgetConfig> widgetsList = new LinkedList<>(config.getWidgets());

    for (WidgetConfig widgetConfig : widgetsList) {
      addComponent(widgetConfig);
    }

    return config;

  }

  /**
   * Load settings forn file and initialize components
   * 
   * @param file
   */
  public HmiConfig configure(File file) throws IOException {

    HmiConfig config = HmiConfig.load(file);

    return init(config);

  }

  /**
   * Create JComponent and restore settings from widgetConfig
   * 
   * @param widgetConfig
   * @return
   */
  public JComponent addComponent(WidgetConfig widgetConfig) {
    WidgetInfo widgetInfo = availableWidgets.get(widgetConfig.getWidgetType());

    try {
      Class<?> componentClass = widgetInfo.getComponentClass();

      // TODO: use fractory ???
      JComponent component = (JComponent) componentClass.newInstance();
      widgetInfo.restoreProperties(widgetConfig, component);

      log.debug("Loading widget: " + widgetConfig.getName() + ", Class: " + componentClass);

      component.setName(widgetConfig.getName());
      component.putClientProperty(WidgetInfo.PROPERTY_KEY, widgetInfo);

      registerComponent(component);

      return component;
    } catch (Exception e) {
      e.printStackTrace();
      throw new IllegalStateException(
          "Can not create JComponent for:" + widgetConfig.getName() + ", " + widgetConfig.getWidgetType());
    }
  }

  public void registerComponent(JComponent component) {
    widgetsComponents.put(component.getName(), component);
  }

  public HmiConfig saveConfig(JComponent applicationLayer, Collection<JComponent> widgets)
      throws IOException {

    HmiConfig config = new HmiConfig();

    WidgetInfo appInfo = WidgetInfo.get(applicationLayer);
    WidgetConfig appProperties = appInfo.persistProperties(applicationLayer);
    config.setValues(appProperties.getValues());

    for (JComponent component : widgets) {

      if (HmiConfig.APP_NAME.equals(component.getName()))
        continue;

      WidgetInfo widgetInfo = WidgetInfo.get(component);
      WidgetConfig widget = widgetInfo.persistProperties(component);
      widget.setName(component.getName());
      config.addWidget(widget);

    }

    return config;
  }

  public void save(File file, JComponent applicationLayer, Collection<JComponent> widgets)
      throws IOException {

    HmiConfig config = saveConfig(applicationLayer, widgets);

    HmiConfig.save(config, file);

  }

  public void remove(JComponent comp) {
    widgetsComponents.remove(comp.getName());
  }

  public JComponent getWidgetComponent(String name) {
    return widgetsComponents.get(name);
  }

  /**
   * Clear widget components
   */
  public void clear() {
    widgetsComponents.clear();
    
  }


}
