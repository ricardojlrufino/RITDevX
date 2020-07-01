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
package com.ricardojlrufino.ritdevx.controller.utils;

import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import com.ricardojlrufino.ritdevx.controller.configuration.HmiConfig;


public final class UIHelper {

  public static File getOffFile(File file) {
    if (file == null)
      return null;
    String[] names = file.getName().split("\\.");
    String name = names[0];
    String ext = names[1];

    File offFile = new File(file.getParent(), name + "-off." + ext);
    return offFile;
  }

  public static BufferedImage readImg(File file, JComponent component) {
    try {
      
      if (file == null) return null;
      
      // Check if is a relative path.
      if (!file.exists() && HmiConfig.getLastLoaded() != null) {
        
        File lastLoaded = HmiConfig.getLastLoaded();
        
        File relative = new File(lastLoaded.getParentFile(), file.getPath());
        
        if(relative.exists()) {
          return ImageIO.read(relative);
        }
        
      }
      
      return ImageIO.read(file);
      
    } catch (IOException e) {
      System.err.println("file: " + file);
      e.printStackTrace();
      return null;
    } finally {
      component.repaint();
    }
  }


  /**
   *
   * @param text
   * @return
   */
  public static JButton createButton(String text) {
    JButton button = new JButton(text);
    button.setFocusPainted(true);
    button.setBorderPainted(true);
    button.setContentAreaFilled(true);
    return button;
  }

  /**
   *
   * @param text
   * @param icon
   * @return
   */
  public static JButton createButton(String text, String icon) {
    return createButton(text, icon, false);
  }

  /**
   *
   * @param text
   * @param icon
   * @param flat
   * @return
   */
  public static JButton createButton(String text, String icon, boolean flat) {
    ImageIcon iconNormal = readImageIcon(icon + ".png");
    ImageIcon iconHighlight = readImageIcon(icon + ".png");
    ImageIcon iconPressed = readImageIcon(icon + ".png");

    JButton button = new JButton(text, iconNormal);
    button.setFocusPainted(!flat);
    button.setBorderPainted(!flat);
    button.setContentAreaFilled(!flat);
    if (iconHighlight != null) {
      button.setRolloverEnabled(true);
      button.setRolloverIcon(iconHighlight);
    }
    if (iconPressed != null)
      button.setPressedIcon(iconPressed);
    return button;
  }

  /**
   *
   * @param text
   * @param icon
   * @return
   */
  public static JLabel createLabel(String text, String icon) {
    ImageIcon iconNormal = readImageIcon(icon + ".png");
    JLabel label = new JLabel(text, iconNormal, JLabel.CENTER);
    return label;
  }

  /**
   *
   * @param filename
   * @return
   */
  public static ImageIcon readImageIcon(String filename) {

    URL resource = UIHelper.class.getResource("/icons/widgets/" + filename);

    if (resource == null)
      resource = UIHelper.class.getResource("/icons/widgets/no-image.png");

    return new ImageIcon(Toolkit.getDefaultToolkit().getImage(resource));
  }
  
  /**
   * Repaint parent region of component
   * @param component
   */
  public static void repaintParent(JComponent component) {
    if( component.getParent() != null) {
      Rectangle bounds = component.getBounds();
      component.getParent().repaint(bounds.x - 2, bounds.y - 2, component.getWidth() + 4, component.getHeight() + 4);
    }
  }

}
