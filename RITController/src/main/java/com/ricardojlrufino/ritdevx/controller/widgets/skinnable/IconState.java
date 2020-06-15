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
package com.ricardojlrufino.ritdevx.controller.widgets.skinnable;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import com.ricardojlrufino.ritdevx.controller.widgets.OnOffInterface;
import jiconfont.IconCode;
import jiconfont.IconFont;
import jiconfont.bundle.Bundle;
import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.swing.IconFontSwing;

public class IconState extends JLabel implements OnOffInterface {

  static { // load icon fonts..
    for (IconFont iconFont : Bundle.getIconFonts()) {
      IconFontSwing.register(iconFont);
    }
  }

  private boolean active;
  private String customCmd;

  private IconCode iconOn = FontAwesome.LIGHTBULB_O;
  private Color colorOn = Color.GREEN;

  private Color colorOff = Color.RED;
  private IconCode iconOff = null;

  public IconState() {
    setSize(48, 48);
    setActive(false);
    setHorizontalAlignment(SwingConstants.CENTER);
    updateIcon();
  }

  private void updateIcon() {
    if (isOn()) {
      this.setIcon(IconFontSwing.buildIcon(iconOn, getSize().width, colorOn));
    } else {
      IconCode type2 = (iconOff != null ? iconOff : iconOn);
      this.setIcon(IconFontSwing.buildIcon(type2, getSize().width, colorOff));
    }
  }

  public IconCode getIconOn() {
    return iconOn;
  }

  public void setIconOn(IconCode type) {
    this.iconOn = type;
    updateIcon();
  }

  public Color getColorOn() {
    return colorOn;
  }

  public void setColorOn(Color color) {
    this.colorOn = color;
    updateIcon();
  }

  @Override
  public void setSize(Dimension d) {
    super.setSize(d);
    updateIcon();
  }

  @Override
  public void setSize(int width, int height) {
    super.setSize(width, height);
    updateIcon();
  }

  public void setActive(boolean value) {
    this.active = value;
    updateIcon();
  }

  public boolean getActive() {
    return active;
  }

  public Color getColorOff() {
    return colorOff;
  }

  public void setColorOff(Color colorOff) {
    this.colorOff = colorOff;
    updateIcon();
  }

  public IconCode getIconOff() {
    return iconOff;
  }

  public void setIconOff(IconCode typeOff) {
    this.iconOff = typeOff;
    updateIcon();
  }

  public String getCustomCmd() {
    return customCmd;
  }

  public void setCustomCmd(String customCmd) {
    this.customCmd = customCmd;
  }

  @Override
  public boolean hasCustomCmd() {
    return customCmd != null;
  }

  @Override
  public void on() {
    setActive(true);

  }

  @Override
  public void off() {
    setActive(false);
  }

  @Override
  public void toogle() {
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
