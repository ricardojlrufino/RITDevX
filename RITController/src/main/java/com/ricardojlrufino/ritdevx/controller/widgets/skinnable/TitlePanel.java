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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A Panel has a title, border and can be rouded. 
 * @author Ricardo JL Rufino - (ricardo.jl.rufino@gmail.com)
 * @date 20 de jul de 2020
 */
public class TitlePanel extends JComponent{
  
  public enum TitlePosition{
    TOP_CENTER,
    BOTTOM_CENTER
  }
  
  private String title;
  
  private Font titleFont;
  
  private TitlePosition titlePosition;
  
  private Color borderColor;
  
  private int cornerRadius;
  
  // runtime variables
  private Stroke borderStroke = new BasicStroke(2);

  public TitlePanel() {
    configureDefaults();
  }
  
  protected void configureDefaults() {
    setOpaque(true);
    title = "Title";
    cornerRadius = 10;
    borderColor = new Color(41, 41, 41);
    setBackground(new Color(31, 29, 30));
    setForeground(new Color(217, 217, 217));
    
    int fontSize = 14;
    Font defaultFont = new JLabel().getFont();
    titleFont = new Font(defaultFont.getName(), Font.BOLD, fontSize);
    titlePosition = TitlePosition.TOP_CENTER;
  }
  
  public void setTitleFont(Font titleFont) {
    this.titleFont = titleFont;
  }
  
  public Font getTitleFont() {
    return titleFont;
  }
  
  public int getCornerRadius() {
    return cornerRadius;
  }

  public void setCornerRadius(int cornerRadius) {
    this.cornerRadius = cornerRadius;
  }

  public void setTitlePosition(TitlePosition titlePosition) {
    this.titlePosition = titlePosition;
  }
  
  public TitlePosition getTitlePosition() {
    return titlePosition;
  }
  
  @Override
  public Color getBackground() {
    return super.getBackground();
  }
  
  public void setTitleColor(Color color) {
    setForeground(color);
  }
  
  public Color getTitleColor() {
    return getForeground();
  }
  
  public void setBorderColor(Color borderColor) {
    this.borderColor = borderColor;
  }
  
  public Color getBorderColor() {
    return borderColor;
  }
  
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  
  @Override
  protected void paintComponent(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
    
    int w = getWidth();
    int h = getHeight();
    
    borderStroke = new BasicStroke(2);
    
    g2.setStroke(borderStroke);

    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    
    if(cornerRadius > 0 && isOpaque()) {
      
      RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
      
      g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
      g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
      g2.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
      g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
      g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
      g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
      g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
      g2.setColor(getBackground());
      g2.fill(roundedRectangle);
      
      if(borderColor != null) {
        g2.setColor(borderColor);
        g2.draw(roundedRectangle);
      }
      
    }else {
      
        g2.setColor(getBackground());
        g2.fillRect(0, 0, getWidth(), getHeight());
        
        if(borderColor != null) {
          g2.setColor(borderColor);
          g2.drawRect(0, 0, getWidth()-1, getHeight()-1);
        }
        
//      super.paintComponent(g);
    }
    
    if(title != null) {
      
      g2.setFont(titleFont);
      Rectangle2D fontUnitSize = g2.getFontMetrics().getStringBounds(title, g2);
      
      if(titlePosition == TitlePosition.TOP_CENTER) {
        
        int padding = 5; 
        g2.setColor(getForeground());
        g2.drawString(title, (int) (w/2 - fontUnitSize.getWidth()/2), (int) fontUnitSize.getHeight() + padding);
        
      }
      
    }
    
    
  }

}
