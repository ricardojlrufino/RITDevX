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

import java.awt.Color;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditor;

import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import com.ricardojlrufino.ritdevx.designer.propeditor.AbstractPropertyEditor;

/**
 * Allow use  PropertyEditor as {@link TableCellEditor} and {@link TableCellRenderer}
 * @author Ricardo JL Rufino - (ricardo.jl.rufino@gmail.com)
 * @date 3 de jun de 2020
 */
public class PropertyCellAdapter extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {

  private PropertyEditor editor;
  private Color alternateRowColor;
  private Color defaultRowColor;

  public PropertyCellAdapter(PropertyEditor editor) {
    this.editor = editor;
    editor.addPropertyChangeListener(new PropertyChangeListener() {
      boolean hasSet = false;

      @Override
      public void propertyChange(PropertyChangeEvent evt) {
        System.out.println("PropertyCellAdapter change: " + evt.getPropertyName() + ", editor:" + editor);

        if (editor instanceof AbstractPropertyEditor) {
          stopCellEditing();

          // IconCodePropertyEditor
        }

        //        if(evt.getPropertyName().equals("value")) {
        //          if(hasSet) stopCellEditing();
        //          hasSet = true;
        //        }
      }
    });
  }

  @Override
  public Object getCellEditorValue() {
    return editor.getValue();
  }

  @Override
  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
    editor.setValue(value);
    return editor.getCustomEditor();
  }

  @Override
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                 int row, int column) {
    editor.setValue(value);

    JComponent customEditor = (JComponent) editor.getCustomEditor();
    customEditor.setOpaque(true);

    // Default alternative color from LAF
    if (alternateRowColor == null) {
      UIDefaults defaults = UIManager.getLookAndFeelDefaults();
      alternateRowColor = (Color) defaults.getOrDefault("Table.alternateRowColor", Color.LIGHT_GRAY);
      defaultRowColor = (Color) defaults.getOrDefault("Table.cellRenderer.background", Color.WHITE);
    }

    if (customEditor != null) {

      if (isSelected) {

        customEditor.setForeground(table.getSelectionForeground());
        customEditor.setBackground(table.getSelectionBackground());

      } else {

        Color background = defaultRowColor;
        if (row % 2 != 0) {
          background = alternateRowColor;
        }
        customEditor.setForeground(table.getForeground());
        customEditor.setBackground(background);
      }

    }

    return customEditor;
  }

  public PropertyEditor getEditor() {
    return editor;
  }

  public JComponent getCustomEditor() {
    return (JComponent) editor.getCustomEditor();
  }
}