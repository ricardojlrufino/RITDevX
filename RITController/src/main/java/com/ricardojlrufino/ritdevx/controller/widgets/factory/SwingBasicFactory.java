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
package com.ricardojlrufino.ritdevx.controller.widgets.factory;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import com.ricardojlrufino.ritdevx.controller.configuration.WidgetConfig;
import com.ricardojlrufino.ritdevx.controller.widgets.WidgetInfo;

public class SwingBasicFactory implements WidgetFactory {

  private Map<String, Class> availableProperties = new LinkedHashMap<>();

  private static final int START = 1;
  private final Dimension PANES_DIMENSION = new Dimension(150, 100);
  private final Dimension BUTTONS_DIMENSION = new Dimension(90, 35);
  private final Border BORDER = BorderFactory.createBevelBorder(1);

  private int buttonName = START;
  private int checkBoxName = START;
  private int comboBoxName = START;
  private int editorPaneName = START;
  private int labelName = START;
  private int listName = START;
  private int menuName = START;
  private int panelName = START;
  private int radioButtonName = START;
  private int tabbedPanelName = START;
  private int tableName = START;
  private int textAreaName = START;
  private int textBoxName = START;
  private int textFieldName = START;
  private int passwordFieldName = START;


  public SwingBasicFactory() {

    availableProperties.put("Size", Dimension.class);
    availableProperties.put("Backgroud", Color.class);
    availableProperties.put("Foreground", Color.class);
    availableProperties.put("Font", Font.class);
    availableProperties.put("Text", String.class);
    availableProperties.put("Enabled", Boolean.class);
    availableProperties.put("Visible", Boolean.class);

  }


  private final Class[] COMPONENTS_ = {JButton.class, JLabel.class, JRadioButton.class, JCheckBox.class, JList.class,
      JPanel.class, JPasswordField.class,
      JTextArea.class, JTextField.class, JComboBox.class};

  @Override
  public List<WidgetInfo> list() {
    List<WidgetInfo> list = new LinkedList<>();

    for (int i = 0; i < COMPONENTS_.length; i++) {
      WidgetInfo widgetInfo = new WidgetInfo(COMPONENTS_[i]);
      list.add(widgetInfo);
      widgetInfo.setFactory(this);
    }

    return list;
  }

  @Override
  public JComponent createForDesigner(WidgetInfo info, Point point) {
    String name = null;
    
    String componentClass = info.getName();
    JComponent component = null;

    switch (componentClass) {
      case "Button":
        JButton button = new JButton("Button" + buttonName);
        component = (JButton) setSizeBounds(button, point, BUTTONS_DIMENSION);
        name = componentClass + "" + buttonName;
        buttonName++;
        break;
      case "CheckBox":
        JCheckBox checkBox = new JCheckBox("CheckBox");
        component = (JCheckBox) setSizeBounds(checkBox, point, BUTTONS_DIMENSION);
        name = (componentClass + "" + checkBoxName);
        checkBoxName++;
        break;
      case "ComboBox":
        JComboBox<String> comboBox = new JComboBox<>();
        component = (JComboBox<String>) setSizeBounds(comboBox, point, BUTTONS_DIMENSION);
        name = (componentClass + "" + comboBoxName);
        comboBoxName++;
        break;
      case "EditorPane":
        JEditorPane editorPane = new JEditorPane();
        component = (JEditorPane) setSizeBounds(editorPane, point, PANES_DIMENSION);
        editorPane.setBorder(BORDER);
        editorPane.setText("JEditorPane");
        // editorPane.setEnabled(false);
        name = (componentClass + "" + editorPaneName);
        editorPaneName++;
        break;
      case "Label":
        JLabel label = new JLabel("label");
        component = (JLabel) setSizeBounds(label, point, BUTTONS_DIMENSION);
        name = (componentClass + "" + labelName);
        labelName++;
        break;
      case "List":
        JList<String> list = new JList<>();
        component = (JList<String>) setSizeBounds(list, point, PANES_DIMENSION);
        list.setBorder(BORDER);
        DefaultListModel lm = new DefaultListModel();
        lm.addElement("Jlist");
        list.setModel(lm);
        name = (componentClass + "" + listName);
        listName++;
        break;
      case "PasswordField":
        JPasswordField passwordField = new JPasswordField();
        component = (JPasswordField) setSizeBounds(passwordField, point, BUTTONS_DIMENSION);
        passwordField.setText("PasswordField");
        name = (componentClass + "" + passwordFieldName);
        passwordFieldName++;
        break;
      case "RadioButton":
        JRadioButton radioButton = new JRadioButton("JRadioButton");
        component = (JRadioButton) setSizeBounds(radioButton, point, BUTTONS_DIMENSION);
        name = (componentClass + "" + radioButtonName);
        radioButtonName++;
        break;
      case "TextArea":
        JTextArea textArea = new JTextArea();
        component = (JTextArea) setSizeBounds(textArea, point, PANES_DIMENSION);
        textArea.setText("JTextArea");
        textArea.setBorder(BORDER);
        textArea.setBackground(Color.WHITE);
        name = (componentClass + "" + textAreaName);
        textAreaName++;
        break;
      case "TextField":
        JTextField textField = new JTextField("JTextField");
        component = (JTextField) setSizeBounds(textField, point, BUTTONS_DIMENSION);
        name = (componentClass + "" + textFieldName);
        textFieldName++;
        break;
    }

    if (component != null) {
      component.setName(name);
    }

    return component;
  }
  
  @Override
  public JComponent createAndRestore(WidgetInfo info, WidgetConfig widgetConfig) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  public JComponent setSizeBounds(JComponent comp, Point p, Dimension size) {
    Point newP = new Point(p.x - size.width / 2, p.y - size.height / 2);
    comp.setBounds(newP.x, newP.y, size.width, size.height);
    return comp;
  }

}
