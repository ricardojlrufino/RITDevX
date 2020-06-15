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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.JPanel;
import com.ricardojlrufino.ritdevx.controller.utils.UIHelper;

/**
 * Simple Panel with image background.
 * 
 * @author Ricardo JL Rufino - (ricardo.jl.rufino@gmail.com)
 * @date 13 de jun de 2020
 */
public class BackgroudPanel extends JPanel {

  private File backgroudImage;
  private BufferedImage backgroudImageBuffer;

  public void setBackgroudImage(File backgroudImage) {
    this.backgroudImage = backgroudImage;
    this.backgroudImageBuffer = UIHelper.readImg(backgroudImage, this);
    if (backgroudImageBuffer != null) {
      this.setSize(new Dimension(backgroudImageBuffer.getWidth(), backgroudImageBuffer.getHeight()));
      this.repaint();
    }
  }

  public File getBackgroudImage() {
    return backgroudImage;
  }

  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    // Draw backgroud image of user
    if (backgroudImageBuffer != null) {
      g.drawImage(backgroudImageBuffer, 0, 0, this);
    }


  }
}
