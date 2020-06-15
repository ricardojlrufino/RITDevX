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
package com.ricardojlrufino.ritdevx.designer.view;

import java.beans.PropertyEditor;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

import com.ricardojlrufino.ritdevx.controller.widgets.WidgetInfo;
import com.ricardojlrufino.ritdevx.designer.components.BeanPropertiesTabeModel;
import com.ricardojlrufino.ritdevx.designer.components.JTableProperty;
import com.ricardojlrufino.ritdevx.designer.components.PropertyCellAdapter;
import com.ricardojlrufino.ritdevx.designer.propeditor.AllowPopupEditor;
import com.ricardojlrufino.ritdevx.designer.propeditor.PropertyEditorManagerInitializer;

/**
 * Class which store JTable for properties this uses {@link JTableProperty} and {@link BeanPropertiesTabeModel}. <br/>
 * 
 * The 'active' component for editing is defined used {@link #setCurrentComponent(JComponent)}
 * 
 * @author Ricardo JL Rufino
 */
public class PropertiesPanel extends JScrollPane {

  private static final long serialVersionUID = 1L;

  private JComponent currentComp;
  private JTableProperty table;
  private String[] col_names;

  public PropertiesPanel() {
    table = new JTableProperty();
    setViewportView(table);

    col_names = new String[] { "Properties", "Value" };

    // Register custom Editors
    PropertyEditorManagerInitializer.init();

    table.setModel(new DefaultTableModel());
    // model.addTableModelListener(getListener());
    table.setRowHeight(22);

    table.enbleFocusOnEditableCells();

    table.configureDefaultEditors();

  }

  /**
   * REturn table
   * 
   * @return
   */
  public JTableProperty getTable() {
    return table;
  }

  /**
   * Return component
   * 
   * @return
   */
  public JComponent getComponentForTable() {
    return currentComp;
  }

  /**
   * Set component for table, when is just added or clicked
   * 
   * @param comp
   *          component
   */
  public void setComponentForTable(JComponent comp) {
    this.currentComp = comp;
  }

  /**
   * This method update table with calues of clicked or just added component.
   * 
   * @param c
   *          component
   */
  public void setCurrentComponent(JComponent c) {

    // End of cell edit when clicked out
    TableCellEditor cellEditor = table.getCellEditor();
    if (cellEditor != null) {
      if (!cellEditor.stopCellEditing()) {
        cellEditor.cancelCellEditing();
      }
    }

    this.currentComp = c;

    WidgetInfo widgetInfo = (WidgetInfo) currentComp.getClientProperty(WidgetInfo.PROPERTY_KEY);

    if (widgetInfo != null) {

      BeanPropertiesTabeModel tabeModel = new BeanPropertiesTabeModel(currentComp, widgetInfo.getAvailableProperties(),
          col_names);
      table.setModel(tabeModel);

    }

  }

  public void fireEditProperty(String property) {

    BeanPropertiesTabeModel tabeModel = (BeanPropertiesTabeModel) table.getModel();

    int propertyRow = tabeModel.getPropertyRow(property);
    table.editCellAt(propertyRow, 1);

    TableCellEditor cellEditor = table.getCellEditor();
    if (cellEditor instanceof PropertyCellAdapter) {
      PropertyCellAdapter adapter = (PropertyCellAdapter) cellEditor;
      PropertyEditor editor = adapter.getEditor();
      if (editor instanceof AllowPopupEditor) {
        adapter.getCustomEditor().requestFocus();
        ((AllowPopupEditor) editor).openSelector();
      }

    }

  }

  public void clear() {
    table.setModel(new DefaultTableModel());
  }

}
