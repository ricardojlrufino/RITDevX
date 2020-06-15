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
/*******************************************************************************
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import com.ricardojlrufino.ritdevx.designer.propeditor.BooleanAsCheckBoxPropertyEditor;
import com.ricardojlrufino.ritdevx.designer.propeditor.ColorPropertyEditor;
import com.ricardojlrufino.ritdevx.designer.propeditor.DimensionPropertyEditor;
import com.ricardojlrufino.ritdevx.designer.propeditor.PointPropertyEditor;

/**
 * Table with use {@link BeanPropertiesTabeModel}, and individual cell editors/render <br/>
 * for each line {@link #getCellRenderer(int, int)}, provided by {@link PropertyCellAdapter} <br/>
 * 
 * Standard editors are initialized in {@link #configureDefaultEditors()}
 * 
 * @author Ricardo JL Rufino - (ricardo.jl.rufino@gmail.com)
 * @date 13 de jun de 2020
 */
public class JTableProperty extends JTable {

  private static final long serialVersionUID = 1L;

  /**
   * Class constructor
   */
  public JTableProperty() {
    super();
  }

  public JTableProperty(TableModel tm) {
    super(tm);
  }

  public JTableProperty(TableModel tm, TableColumnModel cm) {
    super(tm, cm);
  }

  public JTableProperty(TableModel tm, TableColumnModel cm, ListSelectionModel sm) {
    super(tm, cm, sm);
  }

  public void enbleFocusOnEditableCells() {
    getColumnModel().getSelectionModel().addListSelectionListener(new ExploreSelectionListener());
  }

  /**
   * Initializes default the bean property editors
   */
  public void configureDefaultEditors() {

    setDefaultRenderer(Color.class, new PropertyCellAdapter(new ColorPropertyEditor()));
    setDefaultRenderer(Boolean.class, new PropertyCellAdapter(new BooleanAsCheckBoxPropertyEditor()));
    setDefaultRenderer(boolean.class, new PropertyCellAdapter(new BooleanAsCheckBoxPropertyEditor()));
    setDefaultRenderer(Point.class, new PropertyCellAdapter(new PointPropertyEditor()));
    setDefaultRenderer(Dimension.class, new PropertyCellAdapter(new DimensionPropertyEditor()));

  }

  /**
   * Return cell editor for cell
   * 
   * @param row
   *          row position
   * @param col
   *          col position
   * @return TableCellEditor
   */
  public TableCellEditor getCellEditor(int row, int col) {
    TableCellEditor tmpEditor = null;

    if (getModel() instanceof BeanPropertiesTabeModel) {

      tmpEditor = ((BeanPropertiesTabeModel) getModel()).getCellEditor(row, col);

      if (tmpEditor != null)
        return tmpEditor;
    }

    return super.getCellEditor(row, col);
  }

  @Override
  public TableCellRenderer getCellRenderer(int row, int column) {

    if (getModel() instanceof BeanPropertiesTabeModel) {
      BeanPropertiesTabeModel model = (BeanPropertiesTabeModel) getModel();

      Class<?> cellClass = model.getCellClass(row, column);

      TableCellRenderer defaultRenderer = getDefaultRenderer(cellClass);

      if (defaultRenderer != null) {
        return defaultRenderer;
      }

    }

    return super.getCellRenderer(row, column);
  }

  public void fireBeanPropertyUpdated(String property) {
    BeanPropertiesTabeModel model = (BeanPropertiesTabeModel) getModel();
    model.fireBeanPropertyUpdated(property);
  }

  /**
   * Force focus in editable cells. 
   */
  private class ExploreSelectionListener implements ListSelectionListener {
    public void valueChanged(ListSelectionEvent e) {
      if (!e.getValueIsAdjusting()) {
        int row = getSelectedRow();
        int col = getSelectedColumn();
        // Make sure we start with legal values.
        while (col < 0)
          col++;
        while (row < 0)
          row++;
        // Find the next editable cell.
        while (!isCellEditable(row, col)) {
          col++;
          if (col > getColumnCount() - 1) {
            col = 1;
            row = (row == getRowCount() - 1) ? 1 : row + 1;
          }
        }
        // Select the cell in the table.
        final int r = row, c = col;
        EventQueue.invokeLater(new Runnable() {
          public void run() {
            changeSelection(r, c, false, false);
          }
        });
      }
    }
  }
}