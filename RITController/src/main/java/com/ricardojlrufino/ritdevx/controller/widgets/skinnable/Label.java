package com.ricardojlrufino.ritdevx.controller.widgets.skinnable;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JLabel;
import javax.swing.border.Border;
import com.ricardojlrufino.ritdevx.controller.utils.UIHelper;

public class Label extends JLabel {
  
  private int cornerRadius;
  private Color backgroud;
  
  public Label() {
    configureDefaults();
  }
  
  protected void configureDefaults() {
    setHorizontalAlignment(JLabel.CENTER);
    setText("Add text");
    setForeground(Color.RED);
    setBackground(Color.WHITE);
  }
  
  public void setCornerRadius(int cornerRadius) {
    this.cornerRadius = cornerRadius;
  }
  
  public int getCornerRadius() {
    return cornerRadius;
  }
  
  @Override
  public void setForeground(Color fg) {
    super.setForeground(fg);
  }
  
  @Override
  public void setBackground(Color bg) {
    this.backgroud = bg;
  }
  
  @Override
  public Color getBackground() {
    return backgroud;
  }
  
  @Override
  public void setOpaque(boolean isOpaque) {
    super.setOpaque(isOpaque);
  }
  
  @Override
  public void setBorder(Border border) {
    super.setBorder(border);
    fixBackgroud();
  }
  
  @Override
  public void setText(String text) {
    super.setText(text);
    fixBackgroud();
  }
  
  private void fixBackgroud() {
    // HACK to fix repaint after set resize border..
    // See: https://github.com/ricardojlrufino/RITDevX/issues/2
    if( getParent() != null) {
      UIHelper.repaintParent(this);
    }
  }
  
  @Override
  protected void paintComponent(Graphics g) {
    if(cornerRadius > 0 && isOpaque()) {
      
      RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
      
      Graphics2D g2d = (Graphics2D) g;
      g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
      g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
      g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
      g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
      g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
      g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
      g2d.setColor(backgroud);
      g2d.fill(roundedRectangle);
       
      setOpaque(false);
      super.paintComponent(g);
      setOpaque(true);
      
    }else {
      if(isOpaque()) {
        g.setColor(backgroud);
        g.fillRect(0, 0, getWidth(), getHeight());
      }
      super.paintComponent(g);
    }
  }

}
