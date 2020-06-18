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

import static com.ricardojlrufino.ritdevx.controller.utils.I18n.tr;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import org.apache.log4j.Appender;
import org.apache.log4j.LogManager;
import com.ricardojlrufino.ritdevx.controller.components.ErrorDialog;
import com.ricardojlrufino.ritdevx.controller.components.LogViewer;
import com.ricardojlrufino.ritdevx.controller.configuration.HmiConfig;
import com.ricardojlrufino.ritdevx.controller.utils.StatusMessageAppender;
import com.ricardojlrufino.ritdevx.controller.widgets.xchart.XChartWrapper;
import br.com.criativasoft.opendevice.connection.ConnectionListener;
import br.com.criativasoft.opendevice.connection.ConnectionStatus;
import br.com.criativasoft.opendevice.connection.DeviceConnection;
import br.com.criativasoft.opendevice.connection.StreamConnection;
import br.com.criativasoft.opendevice.connection.StreamConnectionFactory;
import br.com.criativasoft.opendevice.connection.message.Message;
import br.com.criativasoft.opendevice.core.LocalDeviceManager;
import br.com.criativasoft.opendevice.core.command.GetDevicesRequest;
import br.com.criativasoft.opendevice.core.command.UserCommand;
import br.com.criativasoft.opendevice.core.listener.DeviceListener;
import br.com.criativasoft.opendevice.core.model.Device;

/**
 * Here the application is managed and controlled. It can be run from the command line (via
 * {@link RIControllerMain}), or it can be used to create integrations in other IDEs or in the
 * Designer project. <br/>
 * <br/>
 * The visual components defined in the layout, are executed in {@link RIDisplay}
 * 
 * @author Ricardo JL Rufino - (ricardo.jl.rufino@gmail.com)
 * @date 6 de jun de 2020
 */
@SuppressWarnings("serial")
public class RIController extends JFrame {

  /** JComponent client propery to reference a Device. */
  public static final String DEVICE_PROPERTY_KEY = "deviceRef";

  private RIDisplay display;
  private LogViewer logViewer;
  private JPopupMenu popup;

  /** This handle physical device communication (usb, tcp/ip, etc.) */
  private LocalDeviceManager deviceManager;
  private WidgetManager widgetManager;

  /**
   * Constructor
   * @param config - layout configuration file created in RIDesigner (.ritx).
   */
  public RIController(HmiConfig config) {
    this.widgetManager = new WidgetManager();
    this.deviceManager = new LocalDeviceManager();

    display = new RIDisplay(this, config);

    setTitle(display.getTitle());
    setLayout(new BorderLayout());

    Dimension minimumSize = display.getMinimumSize();
    if (minimumSize.width < 100)
      minimumSize = new Dimension(400, 200);
    setMinimumSize(minimumSize);

    initComponents();
    initPopup();
    initLogs();
    initConnections();
    connect();

    pack();

    setVisible(true);
  }

  protected void initComponents() {
    getContentPane().add(display, BorderLayout.CENTER);
    logViewer = new LogViewer();
    logViewer.setPreferredSize(new Dimension((int) this.getMinimumSize().getWidth(), 200));
    //    getContentPane().add(logViewer, BorderLayout.SOUTH);
  }

  protected void initPopup() {
    popup = new JPopupMenu();

    popup.add(new JMenuItem(connectAction));
    popup.add(new JMenuItem(disconnectAction));
    popup.add(new JMenuItem(logsAction));
    popup.add(new JMenuItem(settingsAction));
    //    popup.addComponentListener(l);

    popup.addPopupMenuListener(new PopupMenuListener() {

      @Override
      public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
        connectAction.setEnabled(!deviceManager.isConnected());
        disconnectAction.setEnabled(deviceManager.isConnected());
      }

      @Override
      public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
        // ignore.
      }

      @Override
      public void popupMenuCanceled(PopupMenuEvent e) {
        // ignore.
      }
    });
    
    this.addMouseListener(new MouseAdapter() {
      public void mouseReleased(MouseEvent e) {
        if (e.getButton() == e.BUTTON3) {
          popup.show(e.getComponent(), e.getX(), e.getY());
        }
      }
    });
  }

//  /**
//   * Provides component hierarchy traversal to set menu.
//   * 
//   * @param container start node for the traversal.
//   */
//  private void setComponentPopupMenu(Container container, JPopupMenu menu) {
//    for (final Component comp : container.getComponents()) {
//      if (comp instanceof JComponent) {
//        ((JComponent) comp).setComponentPopupMenu(menu);
//      }
//      if (comp instanceof Container) {
//        setComponentPopupMenu((Container) comp, menu);
//      }
//    }
//  }

  protected void initConnections() {
    //    ODev.getConfig().setBindLocalVariables(false); // must call AddDevice

    deviceManager.addListener(deviceListener);

    deviceManager.addConnectionListener(connectionListener);
  }


  public void connect() {
    
    if (deviceManager.getConnections().isEmpty()) {
      StreamConnection usb = StreamConnectionFactory.createUsb();
      usb.setSerializer(null); // TODO: REMOVE THIS, precisou para forçar o padrão, mas é bug.
      deviceManager.addConnection(usb);
    }

    Thread thread = new Thread() {
      @Override
      public void run() {
        try {
          deviceManager.connect();
        } catch (IOException e) {
          handleException(tr("Connection fail"), e);
        }
      }
    };

    thread.start();

   
  }

  // ========================================================================
  // DeviceListener - receive events from device / conection
  // ========================================================================

  private DeviceListener deviceListener = new DeviceListener() {

    @Override
    public void onDeviceChanged(Device device) {

      JComponent component = widgetManager.getWidgetComponent(device.getName());

      if (component != null) {
        display.updateValueForComponent(component, device.getValue());
      }

    }

    @Override
    public void onDeviceRegistred(Device device) {

      System.err.println("onDeviceRegistred >> " + device);
      JComponent component = widgetManager.getWidgetComponent(device.getName());

      if (component != null)
        component.putClientProperty(DEVICE_PROPERTY_KEY, device);

    }
  };

  private ConnectionListener connectionListener = new ConnectionListener() {

    @Override
    public void onMessageReceived(Message message, DeviceConnection connection) {

      if (message instanceof UserCommand) {

        UserCommand command = (UserCommand) message;

        JComponent component = widgetManager.getWidgetComponent(command.getName());

        if (component instanceof XChartWrapper) {
          XChartWrapper wrapper = (XChartWrapper) component;
          wrapper.addValues(command.getParams());
        }

      }

    }

    @Override
    public void connectionStateChanged(DeviceConnection connection, ConnectionStatus status) {

      //          Collection<JComponent> components = widgetManager.getWidgetsComponents().values();
      //          
      //          for (JComponent jComponent : components) {
      //            if(status == ConnectionStatus.DISCONNECTED) {
      //              jComponent.setEnabled(false);
      //            }
      //            if(status == ConnectionStatus.CONNECTED) {
      //              jComponent.setEnabled(true);
      //            }
      //          }
      //          

    }
  };


  // ========================================================================
  // Popup Actions
  // ========================================================================


  private Action connectAction = new AbstractAction(tr("Connect")) {

    @Override
    public void actionPerformed(ActionEvent e) {

      connect();

    }
  };

  private Action disconnectAction = new AbstractAction(tr("Disconnect")) {

    @Override
    public void actionPerformed(ActionEvent e) {

      try {
        deviceManager.disconnect();
      } catch (IOException ex) {
        handleException("Disconnect fail", ex);
      }

    }
  };

  private Action logsAction = new AbstractAction(tr("Show Logs")) {

    @Override
    public void actionPerformed(ActionEvent e) {

      LogViewer.display(logViewer);

    }
  };

  private Action settingsAction = new AbstractAction(tr("Settings")) {

    @Override
    public void actionPerformed(ActionEvent e) {

      try {
        deviceManager.send(new GetDevicesRequest());
      } catch (IOException e1) {
        e1.printStackTrace();
      }

      JOptionPane.showMessageDialog(RIController.this, "NOT IMPLEMENTED");

    }
  };



  public WidgetManager getWidgetManager() {
    return widgetManager;
  }

  public LocalDeviceManager getDeviceManager() {
    return deviceManager;
  }

  /**
   * Stops running simulation e dipose.
   * 
   * @throws IOException
   */
  public void close() {
    try {
      deviceManager.disconnect();
    } catch (Exception e) {
      e.printStackTrace();
    }
    display.close();
    deviceManager.getCurrentContext().cleanUp();
    dispose();
  }

  /**
   * Confifure Log4j using system property or default configuration file (/config/log4j.properties).
   */
  public static final void configureLog4j() {
    String logSet = System.getProperty("log4j.configuration");
    if (logSet == null) {
      URL resource = RIControllerMain.class.getResource("/config/log4j.properties");
      if (resource != null) {
        System.setProperty("log4j.configuration", resource.toString());
      }
    }
  }

  /**
   * Display exceptions to user.
   */
  public void handleException(String title, Exception ex) {
    ErrorDialog.showErrorDialog(this, title, ex);
  }

  protected final void initLogs() {
    StatusMessageAppender appender = new StatusMessageAppender(logViewer);
    Appender consoleAppender = (Appender) LogManager.getRootLogger().getAllAppenders().nextElement();
    appender.setLayout(consoleAppender.getLayout());
    LogManager.getRootLogger().addAppender(appender);
  }
}
