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
package com.ricardojlrufino.ritdevx.designer.propeditor;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileSystemView;

public class FilePropertyEditor extends AbstractPropertyEditor implements AllowPopupEditor {

  private static File lastSelected; // store to reuse.

  private File file;
  private JTextField component;

  private JPanel panel;

  public FilePropertyEditor() {
    component = new JTextField();
    panel = new JPanel(new BorderLayout());
    editor = panel;

    component.setBorder(BorderFactory.createEmptyBorder());

    JLabel lbtn = new JLabel(UIManager.getIcon("FileView.directoryIcon"));
    lbtn.setHorizontalAlignment(JLabel.CENTER);
    lbtn.setVerticalAlignment(JLabel.CENTER);
    lbtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    lbtn.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
    lbtn.setPreferredSize(new Dimension(32, component.getHeight()));

    panel.add(lbtn, BorderLayout.EAST);
    panel.add(component, BorderLayout.CENTER);

    lbtn.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        openSelector();
      }
    });

    component.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String text = component.getText();
        if (text != null && text.length() > 0) {
          File file = new File(text);
          if (file.exists()) {
            onSelect(file);
          }
        }
      }
    });

  }

  public Object getValue() {
    return file;
  }

  public void setValue(Object value) {
    file = (File) value;
    if (value != null)
      component.setText(value.toString());
  }

  public void openSelector() {

    File defaultFile = lastSelected;
    if (defaultFile == null)
      defaultFile = FileSystemView.getFileSystemView().getHomeDirectory();

    JFileChooser jfc = new JFileChooser(defaultFile);

    if (JFileChooser.APPROVE_OPTION == jfc.showOpenDialog(editor)) {
      onSelect(jfc.getSelectedFile());
    }
  }

  protected void onSelect(File newFile) {
    File oldFile = (File) getValue();
    String text = newFile.getAbsolutePath();
    component.setText(text);
    file = newFile;
    lastSelected = newFile; // store to reuse
    firePropertyChange(oldFile, newFile);
  }

  /**
   * Set predefined folder.
   * Note that this action is temporary, and it only works when the application is being started.
   */
  public static void setDefaultFolder(File lastSelected) {
    FilePropertyEditor.lastSelected = lastSelected;
  }

}
