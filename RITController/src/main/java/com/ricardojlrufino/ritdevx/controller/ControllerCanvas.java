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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ricardojlrufino.ritdevx.controller.components.BackgroudPanel;
import com.ricardojlrufino.ritdevx.controller.components.NotificationPanel;
import com.ricardojlrufino.ritdevx.controller.configuration.HmiConfig;
import com.ricardojlrufino.ritdevx.controller.widgets.ButtonInterface;
import com.ricardojlrufino.ritdevx.controller.widgets.OnOffInterface;
import com.ricardojlrufino.ritdevx.controller.widgets.OperationMode;
import com.ricardojlrufino.ritdevx.controller.widgets.WidgetInfo;
import com.ricardojlrufino.ritdevx.controller.widgets.factory.DefaultWidgetsFactory;
import com.ricardojlrufino.ritdevx.controller.widgets.xchart.XChartWrapper;
import br.com.criativasoft.opendevice.core.LocalDeviceManager;
import br.com.criativasoft.opendevice.core.model.Device;
import eu.hansolo.steelseries.extras.Led;
import eu.hansolo.steelseries.gauges.AbstractGauge;
import eu.hansolo.steelseries.gauges.DisplaySingle;
import eu.hansolo.steelseries.gauges.SparkLine;

/**
 * Component where HMI are displayed (controled by {@link RIController}).
 * 
 * @author Ricardo JL Rufino - (ricardo.jl.rufino@gmail.com)
 * @date 5 de jun de 2020
 */
public class ControllerCanvas extends JLayeredPane {

  private static final long serialVersionUID = 1L;
  private static final int START_LAYER = 5; // user widgets starts in this layer

  private static final Logger log = LoggerFactory.getLogger(ControllerCanvas.class);

  private WidgetManager widgetManager;
  private String title;
  private BackgroudPanel backgroudPanel;
  private NotificationPanel notificationPanel; 

  private int currentLayer = START_LAYER;
  private HmiConfig config;

  private RIController controller;
  private LocalDeviceManager deviceManager;

  public ControllerCanvas(RIController controller, HmiConfig config) {
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
    notificationPanel =  new NotificationPanel();
    add(backgroudPanel, new Integer(1));
    add(notificationPanel, new Integer(2));
  }


  protected void initWidgets() {
    try {

      widgetManager.init(config); // load configuration and initialize widgets

      WidgetInfo appInfo = DefaultWidgetsFactory.createApp(ControllerCanvas.class); // define settings of application

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
    
    Integer layer = (Integer) comp.getClientProperty(WidgetInfo.LAYER_PROPERTY);
    
    if(layer == null) layer = new Integer(START_LAYER);
    else layer = new Integer(layer + START_LAYER);
    
    add(comp, new Integer(layer));
    
    if (isClickable(comp)) {
      
      comp.addMouseListener(widgetClickEvent);

      comp.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

    }
    
    // Ony on Controler, the images are loaded using defaut ON state
    if (comp instanceof OnOffInterface) {
      OnOffInterface state = (OnOffInterface) comp;
      state.off();
    }


  }


  private boolean isClickable(JComponent comp) {
    
    if(comp instanceof ButtonInterface) {
      
      ButtonInterface btn = (ButtonInterface) comp;
      
      if(btn.getOperationMode() == OperationMode.SWITCH || btn.getOperationMode() == OperationMode.PUSH_BUTTON) {
        return true;
      }
      
    }
    
    return false;
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

        if (component instanceof ButtonInterface) {

          Device device = (Device) component.getClientProperty(RIController.DEVICE_PROPERTY_KEY);

          ButtonInterface onoff = (ButtonInterface) component;


          if(onoff.getOperationMode() == OperationMode.PUSH_BUTTON ) {
            
            onoff.on(); // update UI
            if (device != null) device.on(); // send coomand
            
            // turn off after some delay
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
              @Override
              public void run() {
                onoff.off(); // update UI
                if (device != null) device.off(); // send coomand
              }
            }, 100);
            
          }else {
            
            onoff.toogle(); // update UI

            if (device != null) {
              device.toggle(); // send coomand
            }
            
          }
          
          // Send custom command
          
          if (onoff.hasCustomCmd()) {

            String customCmd = onoff.getCustomCmd();
            
            customCmd = customCmd.replaceAll("\\$\\{value\\}", (onoff.isOn() ? 1 : 0)+"");

            String[] cmds = customCmd.split(",");
            
            String[] values =  Arrays.copyOfRange(cmds, 1, cmds.length);

            deviceManager.sendCommand(cmds[0], values);

          } 

        }

      } catch (Exception ex) {
        handleException(ex.getMessage(), ex);
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

    } else if (component instanceof AbstractGauge) {
      
      ((AbstractGauge) component).setValue(value);
      
    } else if (component instanceof eu.hansolo.steelseries.extras.Led) {
      
      Led led = (Led) component;
      
      if (value > 0)
        led.setLedOn(true);
      else
        led.setLedOn(false);
      
    } else if (component instanceof DisplaySingle) {
      
      ((DisplaySingle) component).setLcdValue(value);
      
    } else if (component instanceof SparkLine) {
      
      ((SparkLine) component).addDataPoint(value);
      
    } 

  }

  /**
   * Received from Custom Commands
   * @param component
   * @param params
   */
  public void updateValueForComponent(JComponent component, List<Object> params) {
    
    if (component instanceof XChartWrapper) {
      
      XChartWrapper wrapper = (XChartWrapper) component;
      
      wrapper.addValues(params);
      
    } else if (params.size() == 1) {
      
      updateValueForComponent(component, Double.parseDouble(params.get(0).toString()));
      
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
  
  public NotificationPanel getNotificationPanel() {
    return notificationPanel;
  }

 
}
