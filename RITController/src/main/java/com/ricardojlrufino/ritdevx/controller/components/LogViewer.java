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
import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.Timer;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class LogViewer extends JPanel {

  private final JScrollPane scroll;
  private final DefaultStyledDocument document;
  private final JTextPane consoleTextPane;
  private Timer timer;
  private boolean newLinePrinted;
  private SimpleAttributeSet stdOutStyle;

  public LogViewer() {

    this.setLayout(new BorderLayout());

    document = new DefaultStyledDocument();
    consoleTextPane = new JTextPane(document);
    consoleTextPane.setEditable(false);
    consoleTextPane.setBackground(Color.BLACK);

    scroll = new JScrollPane(consoleTextPane);
    scroll.getVerticalScrollBar().setUnitIncrement(7);
    scroll.setBorder(BorderFactory.createEmptyBorder());
    scroll.setBackground(Color.BLACK);

    final Font consoleFont = new Font("Courier New", Font.PLAIN, 13);
    stdOutStyle = new SimpleAttributeSet();
    StyleConstants.setForeground(stdOutStyle, Color.white);
    StyleConstants.setBackground(stdOutStyle, Color.BLACK);
    StyleConstants.setFontSize(stdOutStyle, consoleFont.getSize());
    StyleConstants.setFontFamily(stdOutStyle, consoleFont.getFamily());
    StyleConstants.setBold(stdOutStyle, consoleFont.isBold());
    consoleTextPane.setParagraphAttributes(stdOutStyle, true);

    DefaultCaret caret = (DefaultCaret) consoleTextPane.getCaret();
    caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
    consoleTextPane.setFocusTraversalKeysEnabled(false);

    consoleTextPane.setText("teseeeeeeeeeeeee");

    //    JPanel noWrapPanel = new JPanel(new BorderLayout());
    //    noWrapPanel.setBackground(Color.BLACK);
    //    noWrapPanel.setOpaque(true);
    //    noWrapPanel.add(consoleTextPane);

    //    scroll.setViewportView(noWrapPanel);
    //    noWrapPanel.setBackground(Color.BLACK);
    //    noWrapPanel.setOpaque(true);

    this.timer = new Timer(100, (e) -> {
      if (isShowing() && newLinePrinted) {
        newLinePrinted = false;
        scrollDown();
      }
    });
    timer.setRepeats(false);

    this.add(scroll, BorderLayout.CENTER);
  }

  public void append(String message) {

    try {
      document.insertString(document.getLength(), message.toString(), stdOutStyle);
      newLinePrinted = true;

      if (!timer.isRunning()) {
        timer.restart();
      }

    } catch (BadLocationException e) {
      e.printStackTrace();
    }

  }

  public void scrollDown() {
    scroll.getHorizontalScrollBar().setValue(0);
    scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
  }

  /**
   * Display logs in a new frame
   * 
   * @param logViewer
   * @return {@link JFrame} with {@link LogViewer} in contentPane
   */
  public static JFrame display(LogViewer logViewer) {
    JFrame frame = new JFrame();
    frame.setTitle("Logs");
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.getContentPane().add(logViewer);
    frame.pack();
    frame.setVisible(true);
    return frame;
  }

}
