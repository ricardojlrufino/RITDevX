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
package com.ricardojlrufino.ritdevx.designer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.ricardojlrufino.ritdevx.controller.RIController;
import com.ricardojlrufino.ritdevx.controller.WidgetManager;
import com.ricardojlrufino.ritdevx.controller.components.ErrorDialog;
import com.ricardojlrufino.ritdevx.controller.configuration.HmiConfig;
import com.ricardojlrufino.ritdevx.controller.configuration.WidgetConfig;
import com.ricardojlrufino.ritdevx.controller.utils.UIHelper;
import com.ricardojlrufino.ritdevx.controller.widgets.OnOffInterface;
import com.ricardojlrufino.ritdevx.controller.widgets.WidgetInfo;
import com.ricardojlrufino.ritdevx.controller.widgets.factory.DefaultWidgetsFactory;
import com.ricardojlrufino.ritdevx.controller.widgets.factory.WidgetFactory;
import com.ricardojlrufino.ritdevx.controller.widgets.skinnable.IconState;
import com.ricardojlrufino.ritdevx.controller.widgets.skinnable.ImageButton;
import com.ricardojlrufino.ritdevx.controller.widgets.skinnable.ImageState;
import com.ricardojlrufino.ritdevx.designer.components.GridDesignLayer;
import com.ricardojlrufino.ritdevx.designer.events.MouseActionsListener;
import com.ricardojlrufino.ritdevx.designer.events.WidgetKeyListener;
import com.ricardojlrufino.ritdevx.designer.events.WidgetResizeController;
import com.ricardojlrufino.ritdevx.designer.propeditor.FilePropertyEditor;
import com.ricardojlrufino.ritdevx.designer.view.PropertiesPanel;
import com.ricardojlrufino.ritdevx.designer.view.DesignerCanvas;
import com.ricardojlrufino.ritdevx.designer.view.WidgetSelectorPanel;

/**
 * Main class of the Designer application.
 * <p>
 * It manages the main components below:
 * <ul>
 * <li> {@link WidgetSelectorPanel} - List of available widget components  </li>
 * <li> {@link DesignerCanvas} - Draggable area of components, where layout is build  </li>
 * <li> {@link PropertiesPanel} - Editable properties of selected component </li>
 * </ul>
 * Click and some editing features are also provided by {@link MouseActionsListener} with Popup menu.
 * The {@link DesignerCanvas} supports dnd from external files and create a {@link ImageState}
 * </p>
 * The list of available components is defined by the {@link WidgetFactory} classes, 
 * like {@link DefaultWidgetsFactory}, Defined in the <b>RIController</b> module;
 * <br/></br/>
 * The persistence of the layout file (XML) is done by {@link HmiConfig}
 * <br/><br/>
 * The layout can be simulated used {@link RIController}
 * 
 * @author Ricardo JL Rufino (full rewrite)
 * @author Svatek Tomas
 * @version 2.0
 * 
 */
public class RIDesigner extends JFrame {

  private static final long serialVersionUID = 1L;

  private RIController controller;
  private WidgetManager widgetManager;
  private WidgetSelectorPanel widgetSelector;
  private JPanel canvasContainer;
  private DesignerCanvas canvas;
  private JScrollPane widgetSelectorScroll;
  private PropertiesPanel propertiesTable;

  private TreeMap<String, JComponent> addedComponents;
  private JComboBox<String> componentListCombobox;
  private JComponent selectedComponent;

  //  private EventsTable eventsTable;
  private JTabbedPane widgetSettingsPane;
  private JMenuBar menuBar;

  private File lastOpenFile;

  private JPanel propertiesContainer; // store properties table,componentListCombobox, and others 

  private JPanel mainContentPane;
  
  /** Style of application UI {@link #configureLayout3Side()} / {@link #configureLayoutCompact()} */
  private boolean compactLayout = false; 
  
  public RIDesigner() {

    this.setTitle("RITDevX Designer Tool");
    this.setPreferredSize(new Dimension(800, 600));

    createMenu();
    setJMenuBar(menuBar);

    widgetManager = new WidgetManager();

    addedComponents = new TreeMap<>();
    componentListCombobox = new JComboBox<>();
    propertiesTable = new PropertiesPanel();

    widgetSettingsPane = new JTabbedPane();
    canvasContainer = new JPanel(null);
    canvasContainer.setName("panel");
//    canvasContainer.setBackground(Color.WHITE);
    canvasContainer.setLayout(new BorderLayout());

    canvas = new DesignerCanvas(this);
    propertiesTable.setSelectedComponent(canvas);

    canvas.addMouseListener(new MouseActionsListener(canvas, this));

    // canvas.addMouseMotionListener(new WidgetMoveController(canvas,propertiesTable));

    componentListCombobox.addItemListener(comboOnSelectComponent);
    componentListCombobox.setName("comboComponents");

    canvasContainer.add(canvas, BorderLayout.CENTER);
    canvasContainer.setBorder(BorderFactory.createTitledBorder("Design"));

    widgetSelector = new WidgetSelectorPanel(this);

    widgetSelectorScroll = new JScrollPane(widgetSelector);
    widgetSelectorScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    widgetSelectorScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

    propertiesContainer = new JPanel(new BorderLayout());
//    propertiesContainer.setBackground(new Color(236, 236, 236));
    propertiesContainer.setPreferredSize(new Dimension(200, 300));


    propertiesTable.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    propertiesTable.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

    widgetSettingsPane.addTab("Properties", propertiesTable);

    propertiesContainer.add(componentListCombobox, BorderLayout.NORTH);
    propertiesContainer.add(widgetSettingsPane, BorderLayout.CENTER);

    JButton screenShot = new JButton("ScreenShot");
    screenShot.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        
        JComponent selectedItem = canvas;
//        JComponent selectedItem = (JComponent) propertiesTable.getComponentForTable();
        
        try {
          BufferedImage image = canvas.printScreen(selectedItem);
          File file = new File("/tmp/" + selectedItem.getClass().getSimpleName() + ".png");
          ImageIO.write(image, "png", file);
          
          JOptionPane.showMessageDialog(RIDesigner.this, "Saved: " + file);
        } catch (IOException e1) {
          e1.printStackTrace();
        }
        
      }
    });
    propertiesContainer.add(screenShot, BorderLayout.SOUTH);

    setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
    configureLayout(compactLayout);
    
    pack();

    this.setLocationRelativeTo(null);
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    this.setVisible(true);

  }
  
  protected void configureLayout(boolean compact) {
    
    // check if has changed.
    if(compact == compactLayout && mainContentPane != null) return;
    
    this.compactLayout = compact;
    
    if(mainContentPane != null) {
      mainContentPane.removeAll();
    }
    
    if(compact) {
      configureLayoutCompact();
    }else {
      configureLayout3Side();
    }
    
    this.doLayout();
  }
  
  /**
   * setup 3 coluns layout
   */
  public void configureLayout3Side() {
    mainContentPane = new JPanel();
    mainContentPane.setLayout(new BorderLayout());

    int sideSize = 285;
    
    // create a splitpane 
    JSplitPane splitCenterRight = new JSplitPane(SwingConstants.VERTICAL, canvasContainer, propertiesContainer); 
    JSplitPane slipts = new JSplitPane(SwingConstants.VERTICAL, widgetSelectorScroll, splitCenterRight); 
    
    slipts.setDividerLocation(sideSize);
    
    // HACH: set size on open...
    this.addWindowListener(new WindowAdapter() {
      @Override
      public void windowOpened(WindowEvent e) {
        splitCenterRight.setDividerLocation(RIDesigner.this.getSize().width - (sideSize * 2));
      }
    
    });
    
    mainContentPane.add(slipts);
    this.setContentPane(mainContentPane);
  }
  
  /**
   * Setup panels on left side.
   */
  public void configureLayoutCompact() {
    mainContentPane = new JPanel();
    mainContentPane.setLayout(new BorderLayout());
    
    JSplitPane splitPaneLeftTopDown = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    JSplitPane splitMain = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

    splitMain.setDividerLocation(285);
    // splitMain.addPropertyChangeListener("dividerLocation", evt -> System.out.println("SPLIT. " +
    // splitMain.getDividerLocation()));

    splitPaneLeftTopDown.setDividerLocation(this.getSize().height / 2);
    
    JScrollPane leftBottomScrollPane = new JScrollPane(propertiesContainer);
    leftBottomScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    leftBottomScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    leftBottomScrollPane.setPreferredSize(new Dimension(300, 370));
    
    splitPaneLeftTopDown.setTopComponent(widgetSelectorScroll);
    splitPaneLeftTopDown.setBottomComponent(leftBottomScrollPane);
    splitPaneLeftTopDown.setDividerLocation(200);

    splitMain.setLeftComponent(splitPaneLeftTopDown);
    splitMain.setRightComponent(canvasContainer);
    
    mainContentPane.add(splitMain);
    this.setContentPane(mainContentPane);
  }

  /**
   * Create menu In this time just save and exit action.
   */
  public void createMenu() {
    menuBar = new JMenuBar();

    final JMenu fileMenu = new JMenu("File");

    JMenuItem newAction = new JMenuItem("New");
    newAction.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Event.CTRL_MASK));

    JMenuItem openAction = new JMenuItem("Open");
    openAction.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Event.CTRL_MASK));
    
    JMenuItem openTestAction = new JMenuItem("Open (Test)");
    openTestAction.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, Event.CTRL_MASK));

    JMenuItem saveAction = new JMenuItem("Save");
    saveAction.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK));

    JMenuItem saveAsAction = new JMenuItem("Save as");
//    saveAsAction.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.SHIFT_MASK));

    JMenuItem exitAction = new JMenuItem("Exit");
    exitAction.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, Event.CTRL_MASK));

    fileMenu.add(newAction);
    fileMenu.add(openAction);
    fileMenu.add(openTestAction);
    fileMenu.add(saveAction);
    fileMenu.add(saveAsAction);
    fileMenu.addSeparator();
    fileMenu.add(exitAction);
    menuBar.add(fileMenu);

    // listeners
    exitAction.addActionListener(e -> handleCloseAcion());
    saveAction.addActionListener(e -> handleSaveAction(false));
    saveAsAction.addActionListener(e -> handleSaveAction(true));
    newAction.addActionListener(e -> handleNewAction());
    openAction.addActionListener(e -> handleOpenAction());
    openTestAction.addActionListener(e -> {
      File selectedFile = new File("/media/ricardo/Dados/Workspace/Arduino/RITDevX-Project/PRIVATE_NOTES/interface.ritd");
      handleOpenAction(selectedFile);
    });
    
    // ===============

    JMenu runMenu = new JMenu("Run");

    JMenuItem startAction = new JMenuItem("Start");
    startAction.setAccelerator(KeyStroke.getKeyStroke("F9"));
    startAction.addActionListener(e -> handleRunStartAction());
    runMenu.add(startAction);

    menuBar.add(runMenu);
    
    // ===============
    
    JMenu windowMenu = new JMenu("Window");
    
    
    JMenuItem layoutCompactAction = new JMenuItem("Layout: Compact");
    JMenuItem layout2Action = new JMenuItem("Layout: 3 Colums");
    
    windowMenu.add(layoutCompactAction);
    windowMenu.add(layout2Action);
    
    // FIXME: change layout in runtime is causing Jframe Freeze
    layoutCompactAction.setEnabled(false);
    
    layoutCompactAction.addActionListener(e -> {
      configureLayout(true);
    });
    
    layout2Action.addActionListener(e -> {
      configureLayout(false);
    });
    
    
    menuBar.add(windowMenu);

  }

  /**
   * Called after new component aded on canvas or on loaded from life.
   * 
   * @param comp
   * @param askSelect - true for drag - some dragged components ask for select a image.
   */
  public void addWidget(JComponent comp, boolean askSelect) {

    WidgetInfo widget = WidgetInfo.get(comp);

    canvas.addWidget(widget, comp);

    widgetManager.registerComponent(comp);

    propertiesTable.setSelectedComponent((JComponent) comp);

    comp.requestFocus();

    addedComponents.put(comp.getName(), comp);
    componentListCombobox.addItem(comp.getName());
    componentListCombobox.setSelectedItem(comp.getName());

    WidgetResizeController widgetResizeController = new WidgetResizeController(comp, propertiesTable);

    comp.addMouseMotionListener(widgetResizeController);
    comp.addMouseListener(widgetResizeController);
    comp.addMouseListener(new MouseActionsListener(comp, this));
    comp.addKeyListener(new WidgetKeyListener(propertiesTable, addedComponents, componentListCombobox));

    // Ony on Designer, the images are loaded using defaut OFF state
    if (comp instanceof OnOffInterface) {
      OnOffInterface state = (OnOffInterface) comp;
      state.off();
    }

    if (askSelect) {
      // Show dialog to select default property.
      SwingUtilities.invokeLater(() -> {

        if (widget.getComponentClass().equals(ImageState.class)) {
          if (!widget.hasDefaultValue("ImageOn"))
            propertiesTable.fireEditProperty("ImageOn");
        }
        
        if (widget.getComponentClass().equals(ImageButton.class)) {
          if (!widget.hasDefaultValue("ImageOn"))
            propertiesTable.fireEditProperty("ImageOn");
        }

        if (widget.getComponentClass().equals(IconState.class)) {
          if (!widget.hasDefaultValue("Type"))
            propertiesTable.fireEditProperty("Type");
        }
      });
    }

  }

  public List<WidgetInfo> onDropFilesInCancas(List<File> files) {

    List<WidgetInfo> list = new LinkedList<>();

    for (File file : files) {
      
      WidgetInfo widgetInfo = null;
      
      File offFile = UIHelper.getOffFile(file);
      
      if(offFile != null) {
        widgetInfo = widgetSelector.getWidgetForButton();
      }else {
        widgetInfo = widgetSelector.getWidgetForImage();
      }

      widgetInfo.setDefaultValue("ImageOn", file);

      list.add(widgetInfo);

    }

    return list;

  }

  /**
   * Listener for : componentListCheckBox
   */
  private ItemListener comboOnSelectComponent = new ItemListener() {

    @Override
    public void itemStateChanged(ItemEvent e) {

      JComponent c = null;
      if (e.getStateChange() == ItemEvent.SELECTED) {
        String selected = (String) e.getItem();
        c = widgetManager.getWidgetComponent(selected);
        if (c != null) {
          
          setSelectedComponent(c);
          
          //          eventsTable.setComponentName(c.getName());
        }
      }
    }
  };

 
  public JComboBox<String> getComponentListCombobox() {
    return componentListCombobox;
  }

  public PropertiesPanel getPropertiesTable() {
    return propertiesTable;
  }

  public WidgetManager getWidgetManager() {
    return widgetManager;
  }

  private void handleRunStartAction() {

    try {

      if (controller != null) {
        controller.close();
      }

      if (widgetManager.getWidgetsComponents().size() == 0) {
        JOptionPane.showMessageDialog(this, "You have not configured any components !");
        return;
      }

      HmiConfig config = widgetManager.saveConfig(canvas, addedComponents.values());
      controller = new RIController(config);
      controller.setTitle("Simulation");
      controller.setLocationRelativeTo(this);

      controller.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
          controller.close();
        }
      });

    } catch (Exception ex) {
      ErrorDialog.showErrorDialog(this, "Run Error", ex);
    }

  }

  protected void handleCloseAcion() {
    // TODO: contributors welcome !!
    // TODO : ASK berfore close !!!
    // if (checkModified()) {
    //
    // }
    dispose();
  }

  protected void handleSaveAction(boolean askLocation) {

    // If opend file ask before save.
    if (lastOpenFile == null || askLocation) {

      JFileChooser jfc = new JFileChooser();
      jfc.setDialogTitle("Save file as...");
      jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);

      if (lastOpenFile != null)
        jfc.setSelectedFile(lastOpenFile);
      else
        jfc.setSelectedFile(new File("interface.ritd"));

      if (JFileChooser.APPROVE_OPTION == jfc.showSaveDialog(this)) {
        lastOpenFile = jfc.getSelectedFile();
      } else {
        return;
      }
    }

    try {

      widgetManager.save(lastOpenFile, canvas, addedComponents.values());

      JOptionPane.showMessageDialog(this, "File Saved", "Information", JOptionPane.INFORMATION_MESSAGE);

    } catch (Exception ex) {
      ErrorDialog.showErrorDialog(this, "Save Failed", ex);
    }

  }

  protected void handleOpenAction() {

      JFileChooser jfc = new JFileChooser();
      jfc.setDialogTitle("Open");
      jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
      
      if(lastOpenFile != null) jfc.setSelectedFile(lastOpenFile);
      else jfc.setSelectedFile(new File(System.getProperty("user.dir")));

      if (JFileChooser.APPROVE_OPTION == jfc.showOpenDialog(this)) {
        
        File selectedFile = jfc.getSelectedFile();
        
        handleOpenAction(selectedFile);

      }

  }
  
  protected void handleOpenAction(File selectedFile) {
    try {

        handleNewAction(); // clearALL,
      
        HmiConfig config = widgetManager.configure(selectedFile);
      
        FilePropertyEditor.setDefaultFolder(selectedFile);

        lastOpenFile = selectedFile;

        Collection<JComponent> components = widgetManager.getWidgetsComponents().values();

        for (JComponent comp : components) {
          addWidget(comp, false);
        }

        // Restore app settings
        WidgetInfo appInfo = WidgetInfo.get(canvas);
        appInfo.restoreProperties(config, canvas);

    } catch (Exception ex) {
      ErrorDialog.showErrorDialog(this, "Open Failed", ex);
    }
  }

  protected void handleNewAction() {
    // canvas, addedComponents, propertiesTable, widgetSettingsPane,
    // componentEvents, eventsTable, componentListCheckBox

    lastOpenFile = null;
    canvas.resetSettings();
    addedComponents.clear();
    componentListCombobox.removeAllItems();

    propertiesTable.setSelectedComponent(canvas);
    addedComponents.put(canvas.getName(), canvas);
    componentListCombobox.addItem(canvas.getName());
    componentListCombobox.setSelectedItem(canvas.getName());
    widgetManager.clear();
  }

  /**
   * Test method.. ignore.
   */
  public void hotswapTest() {
    System.out.println("frame.hotswapTest [OK]: " + this);
    widgetManager.loadAvailableWidgets(); // force reload
    widgetSelector = new WidgetSelectorPanel(this);
    widgetSelectorScroll.getViewport().setView(widgetSelector);
  }

  public void deleteComponent(JComponent comp) {

    JComponent parent = canvas;
    //    if (comp instanceof JPanel && comp.getComponents().length > 0) {
    //      for (Component c : comp.getComponents()) {
    //        addedComponentsMap.remove(c.getName());
    //        componentEvents.remove(c.getName());
    //        comboBox.removeItem(c.getName());
    //        comp.remove(c);
    //      }
    //    }

    // // remove childs..
    // for (JComponent c : panels) {
    // for (Component k : c.getComponents()) {
    // addedComponentsMap.remove(k.getName());
    // componentEvents.remove(k.getName());
    // comboBox.removeItem(k.getName());
    // c.remove(k);
    // }
    // comp.remove(c);
    // }

    widgetManager.remove(comp);
    addedComponents.remove(comp.getName());
    componentListCombobox.removeItem(comp.getName());
    GridDesignLayer.setSelectedComponent(null);

    parent.remove(comp);
    parent.repaint();

  }
  
  public void setSelectedComponent(JComponent comp) {
    if(comp != selectedComponent) {
      
      comp.requestFocus();
      
      this.selectedComponent = comp;
      
      getComponentListCombobox().setSelectedItem(comp.getName());
      
      propertiesTable.setSelectedComponent(comp);
      
      if(comp != canvas) {
        GridDesignLayer.setSelectedComponent(comp);
      }else {
        GridDesignLayer.setSelectedComponent(null); // clear selection
        GridDesignLayer.setHoverComponent(null);
        GridDesignLayer.setDragComponent(null);
      }
      
    }
  }
  
  public JComponent getSelectedComponent() {
    return selectedComponent;
  }

  public void renameComponent(JComponent comp) {
    String input = JOptionPane.showInputDialog("New Name", comp.getName());
    if (input != null) {
      deleteComponent(comp); // to update all dependent components
      comp.setName(input);
      addWidget(comp, false);
    }
  }

  public void cloneComponent(JComponent comp) {

    WidgetInfo info = WidgetInfo.get(comp);
    WidgetConfig properties = info.persistProperties(comp);
    Point location = (Point) properties.getValuesMap().get("Location");
    Dimension size = (Dimension) properties.getValuesMap().get("Size");

    if (location != null) {
      location.x = location.x + size.width + 2;
    }

    String name = null;
    while (name == null) {
      String input = JOptionPane.showInputDialog("New Name", comp.getName() + "_clone");

      if (input == null || input.length() == 0) {
        JOptionPane.showInputDialog("Name Invalid !");
        continue;
      }

      if (widgetManager.getWidgetComponent(name) != null) {
        JOptionPane.showInputDialog("Name already exist !");
        continue;
      }
      name = input;
    }

    properties.setName(name);
    // Make a new instance and set properties
    JComponent component = widgetManager.addComponent(properties);

    // Add events
    addWidget(component, false);

  }

  /**
   * Move component in Layers UP / Down 
   */
  public void moveUpDown(JComponent comp, boolean up) {
    
    Integer position = (Integer) comp.getClientProperty(WidgetInfo.LAYER_PROPERTY);
    
    if(up) {
      position++;
    }else{
      position--;
    }
    
    comp.putClientProperty(WidgetInfo.LAYER_PROPERTY, position);
    
    propertiesTable.getTable().fireBeanPropertyUpdated("Layer");
  }



}
