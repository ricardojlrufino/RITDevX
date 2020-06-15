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
package com.ricardojlrufino.ritdevx.controller.components;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;


// Retired from https://github.com/freehep/freehep-vectorgraphics

/**
 * A utility for displaying errors in dialogs.
 * 
 * @author Tony Johnson
 */
public class ErrorDialog {
  private ErrorDialog() {}

  /**
   * Creates a dialog which will display a message to the user. If a Throwable is provided the user
   * can click the "Details" button to get a full stack trace, including any nested errors ("caused
   * by").
   * 
   * @param source The parent component to be used for the dialog (may be null)
   * @param message The message to be displayed in the dialog
   * @param detail The exception that caused the error (may be null)
   */
  public static void showErrorDialog(Component source, Object message, final Throwable detail) {

    detail.printStackTrace();

    final JButton details = new JButton("Details...");
    details.setEnabled(detail != null);
    details.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JDialog owner = (JDialog) SwingUtilities.getAncestorOfClass(JDialog.class, details);
        ErrorDetailsDialog dlg = new ErrorDetailsDialog(owner, detail);
        dlg.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dlg.pack();
        dlg.setLocationRelativeTo(owner);
        dlg.setVisible(true);
      }
    });
    if (source != null)
      source.getToolkit().beep();
    Object[] options = {"OK", details};
    JOptionPane.showOptionDialog(source, message, "Error...", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE,
        null, options, options[0]);
  }

  public static class ErrorDetailsDialog extends JDialog {
    public ErrorDetailsDialog(JDialog owner, Throwable detail) {
      super(owner);
      JComponent messageComponent = null;
      JTabbedPane tabs = null;
      Throwable ex = detail;
      for (; ex != null;) {
        JTextArea ta = new JTextArea();
        ta.append(ex + "\n");
        StackTraceElement[] trace = ex.getStackTrace();
        for (int i = 0; i < trace.length; i++) {
          ta.append("    at " + trace[i] + "\n");
        }
        JScrollPane scroll = new JScrollPane(ta);
        ta.setCaretPosition(0);
        ta.setEditable(false);
        scroll.setPreferredSize(new Dimension(400, 300));
        ex = ex.getCause();
        if (ex != null) {
          if (messageComponent == null) {
            messageComponent = tabs = new JTabbedPane();
            tabs.addTab("Exception", scroll);
          } else {
            tabs.addTab("Caused by", scroll);
          }
        } else {
          if (messageComponent == null)
            messageComponent = scroll;
          else
            tabs.addTab("Caused by", scroll);
          break;
        }
      }
      getContentPane().add(messageComponent);
      JButton close = new JButton("Close") {
        public void fireActionPerformed(ActionEvent event) {
          dispose();
        }
      };
      JPanel panel = new JPanel();
      panel.add(close);
      getContentPane().add(panel, BorderLayout.SOUTH);
    }
  }

  public static void main(String[] args) {
    ErrorDialog.showErrorDialog(null, "erro", new Exception("ok"));
  }
}
