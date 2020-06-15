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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.CellRendererPane;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.JToolTip;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicToolTipUI;

/**
 * Implements a multi line tooltip for GUI components
 * Copied from http://www.codeguru.com/java/articles/122.shtml
 *
 * @author Zafir Anjum
 */
public class JMultiLineToolTip extends JToolTip {

  private static final long serialVersionUID = 7813662474312183098L;

  protected int columns = 0;
  protected int fixedwidth = 0;
  
  public JMultiLineToolTip() {
    updateUI();
  }

  public void updateUI() {
    setUI(MultiLineToolTipUI.createUI(this));
  }

  public void setColumns(int columns) {
    this.columns = columns;
    this.fixedwidth = 0;
  }

  public int getColumns() {
    return columns;
  }

  public void setFixedWidth(int width) {
    this.fixedwidth = width;
    this.columns = 0;
  }

  public int getFixedWidth() {
    return fixedwidth;
  }

}

/**
 * UI for multi line tool tip
 */
class MultiLineToolTipUI extends BasicToolTipUI {

  private static MultiLineToolTipUI sharedInstance = new MultiLineToolTipUI();
  protected CellRendererPane rendererPane;

  private static JTextArea textArea;

  public static ComponentUI createUI(JComponent c) {
    return sharedInstance;
  }

  public MultiLineToolTipUI() {
    super();
  }

  public void installUI(JComponent c) {
    super.installUI(c);
    rendererPane = new CellRendererPane();
    c.add(rendererPane);
  }

  public void uninstallUI(JComponent c) {
    super.uninstallUI(c);

    c.remove(rendererPane);
    rendererPane = null;
  }

  public void paint(Graphics g, JComponent c) {
    Dimension size = c.getSize();
    textArea.setBackground(c.getBackground());
    rendererPane.paintComponent(g, textArea, c, 1, 1, size.width - 1, size.height - 1, true);
  }

  public Dimension getPreferredSize(JComponent c) {
    String tipText = ((JToolTip) c).getTipText();
    if (tipText == null)
      return new Dimension(0, 0);
    textArea = new JTextArea(tipText);
    rendererPane.removeAll();
    rendererPane.add(textArea);
    textArea.setWrapStyleWord(true);
    int width = ((JMultiLineToolTip) c).getFixedWidth();
    int columns = ((JMultiLineToolTip) c).getColumns();

    if (columns > 0) {
      textArea.setColumns(columns);
      textArea.setSize(0, 0);
      textArea.setLineWrap(true);
      textArea.setSize(textArea.getPreferredSize());
    } else if (width > 0) {
      textArea.setLineWrap(true);
      Dimension d = textArea.getPreferredSize();
      d.width = width;
      d.height++;
      textArea.setSize(d);
    } else
      textArea.setLineWrap(false);

    Dimension dim = textArea.getPreferredSize();

    dim.height += 1;
    dim.width += 1;
    return dim;
  }

  public Dimension getMinimumSize(JComponent c) {
    return getPreferredSize(c);
  }

  public Dimension getMaximumSize(JComponent c) {
    return getPreferredSize(c);
  }
}