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
package com.ricardojlrufino.ritdevx.controller;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ricardojlrufino.ritdevx.controller.components.BackgroudPanel;
import com.ricardojlrufino.ritdevx.controller.configuration.HmiConfig;
import com.ricardojlrufino.ritdevx.controller.widgets.DefaultWidgetsFactory;
import com.ricardojlrufino.ritdevx.controller.widgets.OnOffInterface;
import com.ricardojlrufino.ritdevx.controller.widgets.WidgetInfo;
import br.com.criativasoft.opendevice.core.LocalDeviceManager;
import br.com.criativasoft.opendevice.core.model.Device;

/**
 * Frame where HMI are executed (controled by {@link RIController}).
 * 
 * @author Ricardo JL Rufino - (ricardo.jl.rufino@gmail.com)
 * @date 5 de jun de 2020
 */
public class RIDisplay extends JLayeredPane {

  private static final long serialVersionUID = 1L;

  private static final Logger log = LoggerFactory.getLogger(RIDisplay.class);

  private WidgetManager widgetManager;
  private String title;
  private BackgroudPanel backgroudPanel;


  private static final int START_LAYER = 5; // user widgets starts in this layer

  private int currentLayer = START_LAYER;
  private HmiConfig config;

  private RIController controller;
  private LocalDeviceManager deviceManager;

  public RIDisplay(RIController controller, HmiConfig config) {
    super();
    this.controller = controller;
    this.config = config;
    this.widgetManager = controller.getWidgetManager();
    this.deviceManager = controller.getDeviceManager();
    this.initLayout();
    this.initWidgets();
  }

  protected void initLayout() {
    setOpaque(true);
    backgroudPanel = new BackgroudPanel();
    add(backgroudPanel, new Integer(1));
  }


  protected void initWidgets() {
    try {

      widgetManager.init(config); // load configuration and initialize widgets

      WidgetInfo appInfo = DefaultWidgetsFactory.createApp(RIDisplay.class); // define settings of application

      // backgroud, etc...
      appInfo.restoreProperties(config, this);

      Collection<JComponent> components = widgetManager.getWidgetsComponents().values();

      for (JComponent component : components) {
        addWidget(component);
      }

    } catch (IOException e) {
      throw new IllegalStateException("Failed to read configuration", e);
    } catch (Exception e) {
      throw new IllegalStateException("Failed to initialize application", e);
    }

  }

  public void addWidget(JComponent comp) {
    // TODO: check layer !!
    add(comp, new Integer(currentLayer++));

    // add lister for TEXT ??? (fow now we not use any text component...)
    comp.addMouseListener(widgetClickEvent);

    if (isClicable(comp)) {
      comp.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

    }

    // Init device

    //    Device device = new Device(deviceID++, DeviceType.DIGITAL);
    //    device.setName(comp.getName());
    //    deviceManager.addDevice(device);

    // Add to eady access.
    //    comp.putClientProperty(DEVICE_PROPERTY_KEY, device);
  }


  private boolean isClicable(JComponent comp) {
    // TODO: Implement, check if is a image only or chat or etc....
    return comp instanceof OnOffInterface;
  }


  protected void handleException(String title, Exception ex) {
    controller.handleException(title, ex);
  }

  public HmiConfig getConfig() {
    return config;
  }

  private MouseListener widgetClickEvent = new MouseAdapter() {

    public void mouseClicked(java.awt.event.MouseEvent e) {

      try {

        JComponent component = (JComponent) e.getSource();

        if (component instanceof OnOffInterface) {

          Device device = (Device) component.getClientProperty(RIController.DEVICE_PROPERTY_KEY);

          OnOffInterface onoff = (OnOffInterface) component;

          if (onoff.hasCustomCmd()) {

            String customCmd = onoff.getCustomCmd();

            String[] cmds = customCmd.split(",");

            deviceManager.sendCommand(cmds[0], cmds[1]);

          } else {

            onoff.toogle();

            if (device != null) {
              device.toggle(); // send coomand
            }

          }



        }

        //        deviceManager.sendCommand("PlayTone", new Object[] {100});

      } catch (Exception e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
    }

  };

  /**
   * Update value on device change.
   */
  public void updateValueForComponent(JComponent component, double value) {

    if (component instanceof OnOffInterface) {

      OnOffInterface state = (OnOffInterface) component;

      if (value > 0)
        state.on();
      else
        state.off();

    }

  }



  /* callend by : appInfo.restoreProperties */
  public void setBackgroudImage(File backgroudImage) {
    this.backgroudPanel.setBackgroudImage(backgroudImage);
    Dimension size = backgroudPanel.getSize();
    if (size.width > 100) {
      this.setMinimumSize(size);
    }
  }

  public File getBackgroudImage() {
    return backgroudPanel.getBackgroudImage();
  }


  public void setTitle(String title) {
    this.title = title;
  }

  public String getTitle() {
    return title;
  }

  public void close() {

  }

  public WidgetManager getWidgetManager() {
    return widgetManager;
  }
}
