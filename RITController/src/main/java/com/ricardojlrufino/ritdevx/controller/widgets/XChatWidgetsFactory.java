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
import java.awt.Point;
import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import com.ricardojlrufino.ritdevx.controller.widgets.xchart.XChartWrapper;

public class XChatWidgetsFactory implements WidgetFactory {

  private static Map<String, AtomicInteger> names = new HashMap<>();
  private List<WidgetInfo> list = new LinkedList<>();

  public XChatWidgetsFactory() {
    //    XYChart chart;
    //    chart.setYAxisTitle(yAxisTitle);

    WidgetInfo XYChart = addAndsetDefaults(XChartWrapper.class, "XYChart");
    XYChart.addProperty("Title");
    XYChart.addProperty("XAxisTitle");
    XYChart.addProperty("YAxisTitle");
    XYChart.setSize(500, 250);

    //    XYChart chart = QuickChart.getChart("Simple XChart Real-time Demo", "Radians", "Sine", "sine", initdata[0], initdata[1]);
    //    final SwingWrapper<XYChart> sw = new SwingWrapper<XYChart>(chart);
  }

  private WidgetInfo addAndsetDefaults(Class<?> kclass, String name) {
    WidgetInfo info = new WidgetInfo(kclass);
    info.setName(name);
    info.setFactory(this);
    info.addProperty("Location");
    info.addProperty("Size");
    list.add(info);
    return info;
  }


  @Override
  public List<WidgetInfo> list() {
    return list;
  }


  @Override
  public JComponent create(WidgetInfo info, Point point) throws Exception {

    Class<?> componentClass = info.getComponentClass();

    JComponent newInstance = (JComponent) componentClass.newInstance();

    // Init with saved values..
    List<PropertyDescriptor> availableProperties = info.getAvailableProperties();
    for (PropertyDescriptor property : availableProperties) {
      Object value = property.getValue("value");
      if (value != null) {
        property.getWriteMethod().invoke(newInstance, value);
      }
    }

    setName(info, newInstance);

    // Check if size has set.
    Dimension defaultSize = newInstance.getSize();
    if (defaultSize != null && defaultSize.width < 20) {
      defaultSize = info.getSize();
      if (defaultSize == null)
        defaultSize = new Dimension(200, 100);
    }

    setSizeBounds(newInstance, point, defaultSize);
    newInstance.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

    return newInstance;
  }

  public void setName(WidgetInfo info, JComponent component) {
    AtomicInteger counter = names.get(info.getName());
    if (counter == null) {
      counter = new AtomicInteger(0);
      names.put(info.getName(), counter);
    }
    int next = counter.incrementAndGet();
    component.setName(info.getName() + next);
  }

  public JComponent setSizeBounds(JComponent comp, Point p, Dimension size) {
    Point newP = new Point(p.x - size.width / 2, p.y - size.height / 2);
    comp.setBounds(newP.x, newP.y, size.width, size.height);
    return comp;
  }

}
