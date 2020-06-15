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
package com.ricardojlrufino.ritdevx.designer.components;

import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.util.List;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;

import com.ricardojlrufino.ritdevx.designer.propeditor.ComboBoxPropertyEditor;

/**
 * A tablemodel where each line represents a property of a bean being edited. <br/>
 * The properties are defined as 'PropertyDescriptor', and allow the integration of changed attributes. <br/>
 * Its use is in conjunction with {@link JTableProperty}
 * @author Ricardo JL Rufino - (ricardo.jl.rufino@gmail.com)
 * @date 13 de jun de 2020
 */
public class BeanPropertiesTabeModel extends AbstractTableModel {

  private static final long serialVersionUID = 1L;

  private Object bean;

  private List<PropertyDescriptor> properties;

  private String[] colNames;

  public BeanPropertiesTabeModel(Object bean, List<PropertyDescriptor> properties, String[] colNames) {
    super();
    this.bean = bean;
    this.properties = properties;
    this.colNames = colNames;

    for (PropertyDescriptor propertyDescriptor : properties) {
      Class<?> propertyEditorClass = propertyDescriptor.getPropertyEditorClass();
      if (propertyEditorClass == null) {
        PropertyEditor findEditor = PropertyEditorManager.findEditor(propertyDescriptor.getPropertyType());
        if (findEditor != null) {
          propertyDescriptor.setPropertyEditorClass(findEditor.getClass());
        }
      }
    }

  }

  @Override
  public int getRowCount() {
    return properties.size();
  }

  @Override
  public int getColumnCount() {
    return colNames.length;
  }

  @Override
  public String getColumnName(int column) {
    return colNames[column];
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    PropertyDescriptor descriptor = properties.get(rowIndex);
    if (columnIndex == 0)
      return descriptor.getName();

    try {
      return descriptor.getReadMethod().invoke(bean);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  public void fireBeanPropertyUpdated(String property) {
    int row = getPropertyRow(property);
    fireTableCellUpdated(row, 1);
  }

  @Override
  public void setValueAt(Object value, int rowIndex, int columnIndex) {
    if (columnIndex == 0)
      return;
    PropertyDescriptor descriptor = properties.get(rowIndex);

    try {
      descriptor.getWriteMethod().invoke(bean, value);
    } catch (Exception e) {
      System.err.println("Fail setting property: " + descriptor.getName() + ", value: " + value);
      e.printStackTrace();
    }
    //    descriptor.setValue(descriptor.getName(), aValue);
  }

  @Override
  public boolean isCellEditable(int rowIndex, int columnIndex) {
    if (columnIndex == 0)
      return false;
    PropertyDescriptor descriptor = properties.get(rowIndex);
    return descriptor.getPropertyEditorClass() != null;
  }

  public TableCellEditor getCellEditor(int row, int col) {
    if (col == 0)
      return null;
    PropertyDescriptor descriptor = properties.get(row);

    PropertyEditor propertyEditor = PropertyEditorManager.findEditor(descriptor.getPropertyType());

    // For enuns...
    if (descriptor.getPropertyType().isEnum()) {
      propertyEditor = new ComboBoxPropertyEditor();
      ((ComboBoxPropertyEditor) propertyEditor).setAvailableValues(descriptor.getPropertyType().getEnumConstants());
    }

    //    if(descriptor.getPropertyType().isPrimitive() ||  descriptor.getPropertyType().isAssignableFrom(Number.class)) {
    //      return new DefaultCellEditor(new JTextField());
    //    }
    //    System.out.println("propertyEditor : " + propertyEditor);
    //    System.out.println("getCustomEditor : " + propertyEditor.getCustomEditor());

    if (propertyEditor != null && propertyEditor.supportsCustomEditor()) {
      PropertyCellAdapter propertyEditorCell = new PropertyCellAdapter(propertyEditor);
      return propertyEditorCell;

    } else {
      return null;
    }

  }

  public Class<?> getCellClass(int row, int col) {
    if (col == 0)
      return String.class;
    PropertyDescriptor descriptor = properties.get(row);
    return descriptor.getPropertyType();
  }

  public int getPropertyRow(String property) {
    int row = 0;
    for (PropertyDescriptor propertyDescriptor : properties) {
      if (propertyDescriptor.getName().equals(property)) {
        break;
      }
      row++;
    }
    return row;
  }

}
