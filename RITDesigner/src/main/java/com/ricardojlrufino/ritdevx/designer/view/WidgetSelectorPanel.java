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

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolTip;
import javax.swing.TransferHandler;
import javax.swing.border.EtchedBorder;

import com.ricardojlrufino.ritdevx.controller.utils.UIHelper;
import com.ricardojlrufino.ritdevx.controller.widgets.WidgetFactory;
import com.ricardojlrufino.ritdevx.controller.widgets.WidgetInfo;
import com.ricardojlrufino.ritdevx.designer.RIDesigner;
import com.ricardojlrufino.ritdevx.designer.components.JMultiLineToolTip;
import com.ricardojlrufino.ritdevx.designer.components.WrapLayout;

/**
 * List of components available to the application, here is where available factories ( {@link WidgetFactory} ) are initialized
 * @author Ricardo JL Rufino - (ricardo.jl.rufino@gmail.com)
 * @author Svatek Tomáš
 * @date 13 de jun de 2020
 */
public class WidgetSelectorPanel extends JPanel {

  private RIDesigner designer;

  private JPanel content;

  private Map<String, WidgetInfo> widgets;

  public WidgetSelectorPanel(RIDesigner designer) {
    this.designer = designer;

    setLayout(new BorderLayout());
    content = new JPanel(new WrapLayout());
    add(content, BorderLayout.CENTER);
    // setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

    // setBackground(new Color(212, 208, 200));
    // setPreferredSize(new Dimension(100, -1));

    widgets = designer.getWidgetManager().getAvailableWidgets();
    createWidgetsIcons();
  }

  /**
   * Set draggable components (top left corner)
   */
  public void createWidgetsIcons() {

    for (WidgetInfo info : widgets.values()) {

      JLabel label = UIHelper.createLabel(info.getName(), info.getIcon());

      label.setHorizontalTextPosition(JLabel.CENTER);
      label.setVerticalTextPosition(JLabel.BOTTOM);
      // label.setIconTextGap(20);
      label.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

      label.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));

      label.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
      label.setPreferredSize(new Dimension(120, 100));
      // label.setBackground(Color.WHITE);
      // label.setOpaque(true);

      label.setTransferHandler(new WidgetTransferHandler(info));
      label.addMouseListener(new MouseHandler());
      label.setDoubleBuffered(true);

      label.setToolTipText("<html><b>[pending]</b> Add good docs...  </html>");

      content.add(label);
      // add(new Box.Filler(minSize, prefSize, maxSize));
    }
  }

  @Override
  public JToolTip createToolTip() {
    return new JMultiLineToolTip();
  }

  public static class WidgetTransferable implements Transferable {
    private WidgetInfo info;

    public WidgetTransferable(WidgetInfo info) {
      super();
      this.info = info;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
      return new DataFlavor[] { WidgetInfo.dataFlavor };
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
      return WidgetInfo.dataFlavor.equals(flavor);
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
      return info;
    }

  }

  /**
   * Class which provide Transferable object, in this case string.
   */
  @SuppressWarnings("serial")
  private class WidgetTransferHandler extends TransferHandler {

    // public static final DataFlavor stringFlavor = DataFlavor.createConstant(java.lang.String.class, "Unicode
    // String");

    private WidgetInfo info;

    public WidgetTransferHandler(WidgetInfo widget) {
      this.info = widget;
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
      return new WidgetTransferable(info);
    }

    @Override
    public int getSourceActions(JComponent c) {
      return COPY;
    }
  }

  /**
   * Class which extends MouseAdapter and it is set to draggable components. Transfer transferable object in action COPY
   */
  class MouseHandler extends MouseAdapter {
    public void mousePressed(MouseEvent e) {
      JComponent c = (JComponent) e.getSource();
      TransferHandler handle = c.getTransferHandler();
      handle.exportAsDrag(c, e, TransferHandler.COPY);
    }
  }

  public WidgetInfo getWidgetForImage() {
    return widgets.get("Image").clone();

  }
}
