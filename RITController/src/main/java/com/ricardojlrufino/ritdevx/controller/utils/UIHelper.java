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
      if (file == null || !file.exists())
        return null;
      return ImageIO.read(file);
    } catch (IOException e) {
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

}
