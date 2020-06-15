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
package com.ricardojlrufino.ritdevx.designer.view;

//package com.ricardojlrufino.hmidesigner.view;
//
//import java.awt.Component;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.util.TreeMap;
//import javax.swing.JButton;
//import javax.swing.JOptionPane;
//import javax.swing.JScrollPane;
//import javax.swing.JTable;
//import javax.swing.UIManager;
//import javax.swing.table.AbstractTableModel;
//import javax.swing.table.TableCellRenderer;
//
//import com.ricardojlrufino.hmidesigner.EventsInit;
//
///**
// * Class which provides table with buttons for events setting. Part of this class was taken from Charles
// * <a href="http://www.cordinc.com/blog/2010/01/jbuttons-in-a-jtable.html">[online]</a>
// * 
// * @author Charles
// * @author Svatek Tomáš
// * @see EventsInit
// */
//public class EventsTable extends JScrollPane{
//
//  private JTable table;
//  private String component = "";
//  private TreeMap<String, EventsInit> componentEvents;
//
//  /**
//   * Class constructor
//   * 
//   * @param componentEvents
//   *          list of component events
//   */
//  public EventsTable(TreeMap<String, EventsInit> componentEvents) {
//    this.componentEvents = componentEvents;
//    this.table = new JTable(new JTableModel());
//    this.table.setFillsViewportHeight(true);
//    setViewportView(table);
//    TableCellRenderer buttonRenderer = new JTableButtonRenderer();
//    this.table.getColumn("Button1").setCellRenderer(buttonRenderer);
//    this.table.getColumn("Button1").setMaxWidth(40);
//    // table.getColumn("Button2").setCellRenderer(buttonRenderer);
//    this.table.addMouseListener(new JTableButtonMouseListener(table));
//    this.table.setRowHeight(22);
//
//  }
//
//  /**
//   * Set component name (here is component presented by string
//   * 
//   * @param name
//   */
//  public void setComponentName(String name) {
//    this.component = name;
//  }
//
//  /**
//   * Return component name
//   * 
//   * @return string
//   */
//  public String getComponentName() {
//    return component;
//  }
//
//  /**
//   * Return table with buttons
//   * 
//   * @return Jtable
//   */
//  public JTable getTable() {
//    return table;
//  }
//
//  /**
//   *
//   * @return
//   */
//  public TreeMap<String, EventsInit> getTree() {
//    return componentEvents;
//  }
//
//  /**
//   * Table model class for table. Contains column names and add button in to table.
//   * 
//   * @author Charles
//   */
//  class JTableModel extends AbstractTableModel {
//    private static final long serialVersionUID = 1L;
//    private final String[] COLUMN_NAMES = new String[] { "Events", "Button1" };
//    private final Class<?>[] COLUMN_TYPES = new Class<?>[] { String.class, JButton.class };
//    private final String[] TABLE_NAMES = { "actionPerformed", "keyPressed", "keyReleased", "keyTyped", "mouseClicked",
//        "mouseEntered", "mouseExited", "mousePressed", "mouseRelesed", "mouseDragged", "mouseMoved",
//        "mouseWheelMoved" };
//
//    public JTableModel() {
//
//    }
//
//    @Override
//    public int getColumnCount() {
//      return COLUMN_NAMES.length;
//    }
//
//    @Override
//    public int getRowCount() {
//      return TABLE_NAMES.length;
//    }
//
//    @Override
//    public String getColumnName(int columnIndex) {
//      return COLUMN_NAMES[columnIndex];
//    }
//
//    @Override
//    public Class<?> getColumnClass(int columnIndex) {
//      return COLUMN_TYPES[columnIndex];
//    }
//
//    /**
//     * Return value at row and column. Contains action for buton
//     * 
//     * @param rowIndex
//     * @param columnIndex
//     * @return
//     */
//    @Override
//    public Object getValueAt(final int rowIndex, final int columnIndex) {
//      switch (columnIndex) {
//      case 0:
//        return TABLE_NAMES[rowIndex];
//      case 1:
//        final JButton button = new JButton("...");
//        button.addActionListener(new ActionListener() {
//          public void actionPerformed(ActionEvent arg0) {
//            String name = "";
//            boolean exist = true;
//            String name2 = componentEvents.get(component).getActionValue(rowIndex);
//            name = JOptionPane.showInputDialog("Enter method name", name2);
//            cyklus: for (String s : componentEvents.keySet()) {
//              for (int i = 0; i < 12; i++) {
//                String actionValue = componentEvents.get(s).getActionValue(i);
//                if (actionValue.equals(name) && !actionValue.equals("")) {
//                  JOptionPane.showMessageDialog(button,
//                                                "Method name already exists.\nPlease change name of this method.",
//                                                "Warning", JOptionPane.WARNING_MESSAGE);
//                  name = JOptionPane.showInputDialog("Enter method name",
//                                                     componentEvents.get(component).getActionValue(rowIndex));
//                  break cyklus;
//                }
//              }
//            }
//            if (name == null)
//              name = name2;
//            componentEvents.get(component).setActionValue(rowIndex, name);
//          }
//        });
//        return button;
//
//      default:
//        return "Error";
//      }
//    }
//
//  }
//
//  /**
//   * Table cell renderer class which add buttons instead text field in to table
//   * 
//   * @author Charles
//   */
//  private class JTableButtonRenderer implements TableCellRenderer {
//
//    @Override
//    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
//                                                   int row, int column) {
//      JButton button = (JButton) value;
//      if (isSelected) {
//        button.setForeground(table.getSelectionForeground());
//        button.setBackground(table.getSelectionBackground());
//      } else {
//        button.setForeground(table.getForeground());
//        button.setBackground(UIManager.getColor("Button.background"));
//      }
//      return button;
//    }
//  }
//
//  /**
//   * Class for mouse action above event tables
//   * 
//   * @author Charles
//   */
//  private static class JTableButtonMouseListener extends MouseAdapter {
//    private final JTable table;
//
//    public JTableButtonMouseListener(JTable table) {
//      this.table = table;
//    }
//
//    public void mouseClicked(MouseEvent e) {
//      int column = table.getColumnModel().getColumnIndexAtX(e.getX());
//      int row = e.getY() / table.getRowHeight();
//
//      if (row < table.getRowCount() && row >= 0 && column < table.getColumnCount() && column >= 0) {
//        Object value = table.getValueAt(row, column);
//        if (value instanceof JButton) {
//          ((JButton) value).doClick();
//        }
//      }
//    }
//  }
//}
