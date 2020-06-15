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
package com.ricardojlrufino.ritdevx.designer.propeditor.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import jiconfont.IconCode;
import jiconfont.IconFont;
import jiconfont.bundle.Bundle;
import jiconfont.swing.IconFontSwing;

public class IconCodeDialog extends JDialog {

  private Map<String, IconCollection> icons = new HashMap<>();
  private JPanel grid;
  private int size = 24;

  private JComboBox iconsComboBox;
  private JComboBox<Integer> sizeComboBox;
  private JCheckBox showNames = new JCheckBox();

  private Border iconBorder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);

  private IconCode selectedIcon;
  private MouseListener onSelectIconMouse;
  private JScrollPane scrollPane;

  public IconCodeDialog() {
    setTitle("Icons");
    setModal(true);
    setLayout(new BorderLayout());
    setPreferredSize(new Dimension(500, 500));

    for (IconFont iconFont : Bundle.getIconFonts()) {
      IconFontSwing.register(iconFont);
    }

    for (IconCode icon : Bundle.getIcons()) {
      register(icon);
    }

    initComponents();
    updateIcons();
    pack();

  }

  public IconCode getSelectedIcon() {
    return selectedIcon;
  }

  private void updateIcons() {

    Object selectedItem = iconsComboBox.getSelectedItem();
    load(grid, icons.get(selectedItem));
  }

  public void load(final JPanel panel, IconCollection iconCollection) {

    final boolean showNamesOpt = showNames.isSelected();
    SwingWorker<Boolean, JComponent> sw1 = new SwingWorker<Boolean, JComponent>() {

      @Override
      protected Boolean doInBackground() throws Exception {

        grid.removeAll();

        for (final IconCode icon : iconCollection.getIcons()) {
          JLabel label = new JLabel();
          label.setIcon(IconFontSwing.buildIcon(icon, size, Color.BLACK));
          label.putClientProperty("IconCode", icon);
          label.addMouseListener(onSelectIconMouse);
          label.setToolTipText(icon.toString());

          if (showNamesOpt) {
            label.setText(icon.toString());
            label.setPreferredSize(new Dimension(120, size + 10));
          } else {
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setVerticalAlignment(JLabel.CENTER);
            label.setPreferredSize(new Dimension(size + 10, size + 10));
          }

          //          label.setHorizontalTextPosition(JLabel.CENTER);
          //          label.setVerticalTextPosition(JLabel.BOTTOM);

          label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
          label.setBorder(iconBorder);

          publish(label);
        }

        return true;
      }

      @Override
      protected void process(List<JComponent> chunks) {
        for (Object object : chunks) {
          panel.add((JComponent) object);
        }
      }

      @Override
      protected void done() {
        scrollPane.revalidate();
      }
    };

    // executes the swingworker on worker thread
    sw1.execute();
  }

  private void initComponents() {

    JPanel topPanel = new JPanel(new FlowLayout());
    iconsComboBox = new JComboBox<Object>(icons.keySet().toArray());
    sizeComboBox = new JComboBox<Integer>(new Integer[] { 24, 30, 45, 50 });

    iconsComboBox.setSelectedIndex(0);
    sizeComboBox.setSelectedIndex(0);

    showNames.setText("Show Names");
    showNames.setSelected(false);

    topPanel.add(iconsComboBox);
    topPanel.add(showNames);
    topPanel.add(sizeComboBox);

    //    JPanel butons = new JPanel(new FlowLayout());
    //    butons.add(new JButton("OK"));

    grid = new JPanel();
    grid.setLayout(new GridLayout(0, 11));

    scrollPane = new JScrollPane(grid);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    scrollPane.getVerticalScrollBar().setUnitIncrement(20);
    //    scrollPane.setSize(new Dimension(800, 600));

    add(topPanel, BorderLayout.NORTH);
    add(scrollPane, BorderLayout.CENTER);
    //    add(butons, BorderLayout.SOUTH);

    ActionListener updateIconsAction = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        size = (int) sizeComboBox.getSelectedItem();
        updateIcons();
      }
    };

    iconsComboBox.addActionListener(updateIconsAction);
    showNames.addActionListener(updateIconsAction);
    sizeComboBox.addActionListener(updateIconsAction);

    onSelectIconMouse = new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        JComponent component = (JComponent) e.getSource();
        selectedIcon = (IconCode) component.getClientProperty("IconCode");
        dispose();
      }
    };

  }

  protected JRootPane createRootPane() {
    KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
    JRootPane rootPane = new JRootPane();

    ActionListener actionListener = new ActionListener() {
      public void actionPerformed(ActionEvent actionEvent) {
        dispose();
      }
    };

    rootPane.registerKeyboardAction(actionListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
    return rootPane;
  }

  public void register(IconCode icon) {
    IconCollection iconCollection = icons.get(icon.getFontFamily());
    if (iconCollection == null) {
      iconCollection = new IconCollection(icon.getClass().getSimpleName());
      icons.put(icon.getFontFamily(), iconCollection);
    }
    iconCollection.add(icon);
  }

  private static class IconCollection {
    private String name;
    private List<IconCode> icons;

    public IconCollection(String name) {
      this.name = name;
      this.icons = new ArrayList<>();
    }

    public void add(IconCode icon) {
      this.icons.add(icon);
    }

    public List<IconCode> getIcons() {
      return icons;
    }

    public String getName() {
      return name;
    }
  }

  public static IconCode showDialog(Component component, String title) {
    IconCodeDialog iconFontDialog = new IconCodeDialog();
    iconFontDialog.setTitle(title);
    iconFontDialog.setVisible(true);
    return iconFontDialog.getSelectedIcon();
  }

  public static void main(String[] args) {
    IconCodeDialog iconFontDialog = new IconCodeDialog();
    iconFontDialog.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });
    iconFontDialog.setVisible(true);
  }

}
