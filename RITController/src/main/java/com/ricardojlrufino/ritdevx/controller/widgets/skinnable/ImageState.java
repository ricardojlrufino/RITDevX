/*******************************************************************************
 * This file is part of RITDevX Controller project. Copyright (c) 2020 Ricardo JL Rufino.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, version 3 with Classpath
 * Exception.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. See
 * the included LICENSE file for details.
 *******************************************************************************/
package com.ricardojlrufino.ritdevx.controller.widgets.skinnable;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.JComponent;
import com.ricardojlrufino.ritdevx.controller.utils.UIHelper;
import com.ricardojlrufino.ritdevx.controller.widgets.OnOffInterface;

/**
 * This component uses two images to represent the ON and OFF states (optional). <br/>
 * It can be used as a normal image, it can be used as a <b>button</b> (responding to clicks) and it
 * can act as a <b>sensor</b>. It also has an optional 'CustomCmd' attribute, which identifies which
 * command should be sent when it is pressed <br/>
 * 
 * @author Ricardo JL Rufino - (ricardo.jl.rufino@gmail.com)
 * @date 11 de jun de 2020
 */
public class ImageState extends JComponent implements OnOffInterface{

  private static final long serialVersionUID = 1L;

  // Properties
  private boolean active;
  private boolean autoScale;
  private File imageOn;
  private File imageOff;
  
  public ImageState() {
    setAutoScale(true);
  }

  // State
  private BufferedImage imageBufferON;
  private BufferedImage imageBufferOFF;


  public boolean isAutoScale() {
    return autoScale;
  }

  public void setAutoScale(boolean autoScale) {
    this.autoScale = autoScale;
    if(!autoScale) updateSize(); // revert original size. 
  }

  public File getImageOn() {
    return imageOn;
  }

  public void setImageOn(File imageOn) {
    this.imageOn = imageOn;
    this.imageBufferON = UIHelper.readImg(imageOn, this);

    // find off image
    if (this.imageOff == null) {
      File offFile = UIHelper.getOffFile(imageOn);
      if (offFile.exists())
        setImageOff(offFile);
    }

    this.updateSize();
  }

  private void updateSize() {
    if (imageBufferON != null) {
      this.setSize(imageBufferON.getWidth(), imageBufferON.getHeight());
    }
  }

  public File getImageOff() {
    return imageOff;
  }

  public void setImageOff(File imageOff) {
    this.imageOff = imageOff;
    if (imageOff != null && imageOff.exists()) { // OFF is optional...
      this.imageBufferOFF = UIHelper.readImg(imageOff, this);
    }
  }

  public void setActive(boolean value) {
    this.active = value;
    repaint();
  }

  public boolean getActive() {
    return active;
  }


  @Override
  protected void paintComponent(Graphics g) {

    if (imageBufferOFF == null)
      imageBufferOFF = imageBufferON;

    if (isOn() && imageBufferON != null) {

      drawImage(g, imageBufferON);

    } else if (imageBufferOFF != null) {

      drawImage(g, imageBufferOFF);

    } else {

      g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

    }

    paintBorder(g);

  }

  private void drawImage(Graphics g, BufferedImage image) {

    if (autoScale) {
      g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
    } else {
      g.drawImage(image, 0, 0, null);
    }

  }

  @Override
  public boolean contains(int x, int y) {

    boolean contains = super.contains(x, y);
    
    // if has focus in current component, ignore transparent shape
    // this is used to allow resize.
    if(this.hasFocus()) return contains;

    // Make component click precise, avoid click in transparent area..
    if (contains && imageBufferON != null) {
      int pixel = imageBufferON.getRGB(x, y);
      if ((pixel >> 24) == 0x00) {
        return false;
      }
    }

    return contains;

  }
  
  //===================================
  // OnOffInterface
  // ===================================

  @Override
  public void on() {
    setActive(true);

  }

  @Override
  public void off() {
    setActive(false);
  }

  @Override
  public void toggle() {
    if (isOn())
      off();
    else
      on();

  }

  @Override
  public boolean isOn() {
    return active;
  }
  
}
