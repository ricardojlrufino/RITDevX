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
package com.ricardojlrufino.ritdevx.designer.view;

import java.awt.Color;
import java.awt.Insets;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.TransferHandler;

import com.ricardojlrufino.ritdevx.controller.configuration.HmiConfig;
import com.ricardojlrufino.ritdevx.controller.widgets.DefaultWidgetsFactory;
import com.ricardojlrufino.ritdevx.controller.widgets.WidgetInfo;
import com.ricardojlrufino.ritdevx.designer.RIDesigner;
import com.ricardojlrufino.ritdevx.designer.components.GridDesignLayer;
import com.ricardojlrufino.ritdevx.designer.events.ComponentMover;

/**
 * This is the container where componentes are gragged. <br/>
 * <br/>
 * It allows dropping external images, and {@link WidgetSelectorPanel} components. <br/>
 * See {@link DropComponentHandler}
 * <p>
 * It contains the properties of the final application, such as Title, Background Image, etc.. <br/>
 * that are defined by {@link DefaultWidgetsFactory#createApp(Class)}
 * </p>
 * @author Ricardo JL Rufino - (ricardo.jl.rufino@gmail.com)
 * @date 13 de jun de 2020
 */
public class RIDesignerCanvas extends JLayeredPane {

  private static final long serialVersionUID = 1L;

  public static final String CANVAS_NAME = HmiConfig.APP_NAME;
  private static final int START_LAYER = 5;

  private RIDesigner designer;
  private ComponentMover dragger;
  private GridDesignLayer gridDesignLayer;

  // Application Properties
  // =======================
  private int gridSize = 20;
  private int currentLayer = 1;
  private String title;
  // SNAPtoGrid ?!?!

  public RIDesignerCanvas(RIDesigner designer) {
    this.designer = designer;
    setName(CANVAS_NAME);
    setOpaque(true);
    setBackground(Color.LIGHT_GRAY);
    setTransferHandler(new DropComponentHandler());

    // Set editable propertis to edit by user.
    WidgetInfo info = DefaultWidgetsFactory.createApp(RIDesignerCanvas.class);
    info.addProperty("ShowGrid");
    info.addProperty("GridSize");
    info.addProperty("GridColor");
    info.addProperty("CurrentLayer");

    //DefaultWidgetsFactory

    // Please add same properties for Application

    putClientProperty(WidgetInfo.PROPERTY_KEY, info);

    dragger = new ComponentMover(designer.getPropertiesTable().getTable());
    dragger.setDragInsets(new Insets(2, 2, 2, 2));
    dragger.setEdgeInsets(new Insets(0, 0, 0, 0));

    initLayers();
  }

  protected void initLayers() {
    gridDesignLayer = new GridDesignLayer(this);
    setGridSize(gridSize);
    add(gridDesignLayer, new Integer(1));
  }

  public void setCurrentLayer(int currentLayer) {
    this.currentLayer = currentLayer;
  }

  public int getCurrentLayer() {
    return currentLayer;
  }

  public void setGridColor(Color gridColor) {
    this.gridDesignLayer.setGridColor(gridColor);
  }

  public Color getGridColor() {
    return gridDesignLayer.getGridColor();
  }

  public void setShowGrid(boolean showGrid) {
    this.gridDesignLayer.setShowGrid(showGrid);
  }

  public boolean isShowGrid() {
    return this.gridDesignLayer.isShowGrid();
  }

  public void setBackgroudImage(File backgroudImage) {
    this.gridDesignLayer.setBackgroudImage(backgroudImage);
  }

  public File getBackgroudImage() {
    return gridDesignLayer.getBackgroudImage();
  }

  public void setGridSize(int size) {
    gridDesignLayer.setGridSize(size);
    this.gridSize = size;
  }

  public int getGridSize() {
    return gridSize;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getTitle() {
    return title;
  }

  /**
   * Called by {@link DropComponentHandler} on new component added to Canvas by drop
   * @param comp
   * @throws Exception 
   */
  public void onDropWidget(WidgetInfo widget, Point point) throws Exception {

    JComponent comp = widget.getFactory().create(widget, point);
    comp.putClientProperty(WidgetInfo.PROPERTY_KEY, widget);

    if (comp.getName() == null) {
      JOptionPane.showMessageDialog(RIDesignerCanvas.this, "Name for Component is required, check WidgetFactory");
      throw new IllegalStateException("Invalid component");
    }

    designer.addWidget(comp, true); // next call this addWidget

  }

  public void addWidget(WidgetInfo widget, JComponent comp) {

    dragger.registerComponent(comp);

    add(comp, new Integer(START_LAYER));

  }

  /**
   * TransferHandler class for panel.
   * In this class is component added to panel and has set all
   * mouse and keys events which we need for funcionality.
   */
  private class DropComponentHandler extends TransferHandler {

    @Override
    public boolean canImport(TransferHandler.TransferSupport info) {
      if (!(info.isDataFlavorSupported(WidgetInfo.dataFlavor)
            || info.isDataFlavorSupported(DataFlavor.javaFileListFlavor))) {
        return false;
      }
      return true;
    }

    /**
     * Method which accept or reject droped object on panel.
     * @param info object which is transfered
     * @return 
     */
    @Override
    public boolean importData(TransferHandler.TransferSupport info) {
      if (!info.isDrop()) {
        return false;
      }

      WidgetInfo widget = null;

      try {

        // Local drop
        if (info.isDataFlavorSupported(WidgetInfo.dataFlavor)) {

          widget = (WidgetInfo) info.getTransferable().getTransferData(WidgetInfo.dataFlavor);

          onDropWidget(widget, info.getDropLocation().getDropPoint());

        } else if (info.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {

          List<File> files = (List<File>) info.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);

          List<WidgetInfo> list = designer.onDropFilesInCancas(files);

          for (WidgetInfo widgetInfo : list) {

            onDropWidget(widgetInfo, info.getDropLocation().getDropPoint());

          }

        }

      } catch (UnsupportedFlavorException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(null, "Drag Fail : " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        return false;
      } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(null, "Drag Fail : " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        return false;
      }
      return true;
    }

  }

  public void resetSettings() {
    setBackgroudImage(null);
    removeAll();
    initLayers();
  }

}
