package com.ricardojlrufino.ritdevx.controller.configuration;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import javax.swing.JComponent;

public class VirtualProperty extends PropertyDescriptor{

  private Class<?> propertyClass;

  public VirtualProperty(String propertyName, Class<?> beanClass, Class<?> propertyClass) throws IntrospectionException {
    super(propertyName, beanClass);
    this.propertyClass = propertyClass;
  }
  
  @Override
  public synchronized Method getWriteMethod() {
    try {
      return JComponent.class.getDeclaredMethod("putClientProperty", Object.class, Object.class);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
  
  @Override
  public synchronized Method getReadMethod() {
    try {
      return JComponent.class.getDeclaredMethod("getClientProperty", Object.class);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
  
  @Override
  public synchronized Class<?> getPropertyType() {
    return propertyClass;
  }

}
