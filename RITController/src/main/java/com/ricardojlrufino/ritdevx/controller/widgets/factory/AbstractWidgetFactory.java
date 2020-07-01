package com.ricardojlrufino.ritdevx.controller.widgets.factory;

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
import com.ricardojlrufino.ritdevx.controller.configuration.WidgetConfig;
import com.ricardojlrufino.ritdevx.controller.widgets.WidgetInfo;

public abstract class AbstractWidgetFactory implements WidgetFactory {

  private static Map<String, AtomicInteger> names = new HashMap<>();

  private List<WidgetInfo> list = new LinkedList<>();
  
  protected WidgetInfo addAndsetDefaults(Class<?> kclass, String name) {
    WidgetInfo info = new WidgetInfo(kclass);
    return addAndsetDefaults(info, name);
  }
  
  protected WidgetInfo addAndsetDefaults(WidgetInfo info, String name) {
    info.setName(name);
    info.setFactory(this);
    info.addProperty("Location");
    info.addProperty("Size");
    info.addVirtualProperty("Layer", Integer.class);
    list.add(info);
    return info;
  }
  
  @Override
  public JComponent createForDesigner(WidgetInfo info, Point point) throws Exception {

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
  
  @Override
  public JComponent createAndRestore(WidgetInfo info, WidgetConfig widgetConfig) throws Exception {
    JComponent component = (JComponent) info.getComponentClass().newInstance();
    info.restoreProperties(widgetConfig, component);
    component.setName(widgetConfig.getName());
    component.putClientProperty(WidgetInfo.PROPERTY_KEY, info);
    return component;
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
  
  @Override
  public List<WidgetInfo> list() {
    return list;
  }

}
