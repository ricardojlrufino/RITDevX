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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Locale;
import javax.swing.BoundedRangeModel;
import javax.swing.JComponent;
import javax.swing.event.ChangeListener;
import com.ricardojlrufino.ritdevx.controller.widgets.model.DoubleRangeModel;


/**
 * Gauge rendered using 3 based colors and configurable style ( {@link GaugeStyle} ).
 * You can set Min and Max value range for component using: {@link #setMinValue(double)} and {@link #setMaxValue(double)}
 * <pre>
 * Exra customizations:
 * - This gauge as a visual range of tree colors with can be configured using (percentStep1, percentStep2).
 * - Colos can be defined using: colorStep1, colorStep2, colorStep3, inativeColor
 * - Bar widths: stepsBarWidth, valueBarWidth
 * </pre>
 * @author Ricardo JL Rufino - (ricardo.jl.rufino@gmail.com)
 * @date 20 de jul de 2020
 */
@SuppressWarnings("serial")
public class SimpleGauge extends JComponent implements ComponentListener {

  public enum GaugeStyle {
    HALF, BEST_FIT, CIRCLE, SEMI_CIRCLE_RIGHT, SEMI_CIRCLE_LEFT

  }

  // Colors steps
  private Color colorStep1 = new Color(39, 159, 36);
  private Color colorStep2 = new Color(195, 114, 58);
  private Color colorStep3 = new Color(193, 59, 56);
  private Color inativeColor = new Color(34, 34, 34);

  private int stepsBarWidth = 5;
  private int valueBarWidth = 30;

  private int percentStep1 = 70;
  private int percentStep2 = 90;

  private Font valueFont;
  private int decimals = 0;
  private String unitString;

  private GaugeStyle style = GaugeStyle.BEST_FIT;
  // Angle based on style (runtime only)
  private int minAngle = 10;
  private int maxAngle = 350;

  /** The data model that handles the numeric maximum value, minimum value, and current-position value */
  protected DoubleRangeModel valueModel;

  public SimpleGauge() {
    this(0, 100);
  }

  public SimpleGauge(double minValue, double maxValue) {
    this.valueModel = new DoubleRangeModel(0d, minValue, maxValue);
    this.addComponentListener(this);
  }

  /**
   * Recompute size based on style.
   */
  protected void updateSize() {

    Dimension preferedSize = null;
    Dimension size = getSize();

    if (size != null) {

      if (style == GaugeStyle.HALF) {
        preferedSize = new Dimension(size.width, size.width / 2);
      } else if (style == GaugeStyle.BEST_FIT) {
        preferedSize = new Dimension(size.width, (size.width / 3) * 2);
      } else if (style == GaugeStyle.CIRCLE) {
        preferedSize = new Dimension(size.width, size.width);
      } else if (style == GaugeStyle.SEMI_CIRCLE_RIGHT) {
        preferedSize = new Dimension(size.width, size.width);
      } else if (style == GaugeStyle.SEMI_CIRCLE_LEFT) {
        preferedSize = new Dimension(size.width, size.width);
      }

      setSize(preferedSize);

    }

  }
  
  
  public void setValueFont(Font valueFont) {
    this.valueFont = valueFont;
  }
  
  public Font getValueFont() {
    return valueFont;
  }

  /**
   * Control de max rotation of knob
   * 
   * @param maxAngle
   */
  protected void setMaxAngle(int maxAngle) {
    this.maxAngle = maxAngle;
  }

  public int getMaxAngle() {
    return maxAngle;
  }

  /**
   * Control min rotation of knob.
   * 
   * @param minAngle
   */
  protected void setMinAngle(int minAngle) {
    this.minAngle = minAngle;
  }

  public int getMinAngle() {
    return minAngle;
  }

  public void setDecimals(int decimals) {
    this.decimals = decimals;
  }

  public int getDecimals() {
    return decimals;
  }

  public void setUnitString(String unitString) {
    this.unitString = unitString;
  }

  public String getUnitString() {
    return unitString;
  }

  public void setStyle(GaugeStyle style) {
    this.style = style;
    updateSize();
  }

  public GaugeStyle getStyle() {
    return style;
  }


  public int getStepsBarWidth() {
    return stepsBarWidth;
  }

  public void setStepsBarWidth(int stepsBarWidth) {
    this.stepsBarWidth = stepsBarWidth;
  }

  public int getValueBarWidth() {
    return valueBarWidth;
  }

  public void setValueBarWidth(int valueBarWidth) {
    this.valueBarWidth = valueBarWidth;
  }

  public int getPercentStep1() {
    return percentStep1;
  }

  public void setPercentStep1(int valueStep1) {
    this.percentStep1 = valueStep1;
  }

  public int getPercentStep2() {
    return percentStep2;
  }

  public void setPercentStep2(int valueStep2) {
    this.percentStep2 = valueStep2;
  }

  public Color getColorStep1() {
    return colorStep1;
  }

  public void setColorStep1(Color colorStep1) {
    this.colorStep1 = colorStep1;
  }

  public Color getColorStep2() {
    return colorStep2;
  }

  public void setColorStep2(Color colorStep2) {
    this.colorStep2 = colorStep2;
  }

  public Color getColorStep3() {
    return colorStep3;
  }

  public void setColorStep3(Color colorStep3) {
    this.colorStep3 = colorStep3;
  }

  public Color getInativeColor() {
    return inativeColor;
  }

  public void setInativeColor(Color inativeColor) {
    this.inativeColor = inativeColor;
  }

  /**
   * Return the minimum size that the knob would like to be. This is the same size as the preferred
   * size so the knob will be of a fixed size.
   *
   * @return the minimum size of the JKnob.
   */
  public Dimension getMinimumSize() {
    return getPreferredSize();
  }


  /**
   * Adds a ChangeListener.
   *
   * @param l the ChangeListener to add
   * @see #fireStateChanged
   * @see #removeChangeListener
   */
  public void addChangeListener(ChangeListener l) {
    listenerList.add(ChangeListener.class, l);
  }

  /**
   * Removes a ChangeListener from.
   */
  public void removeChangeListener(ChangeListener l) {
    listenerList.remove(ChangeListener.class, l);
  }


  public void setMinValue(double min) {
    valueModel.setMinimum(min);
  }

  public double getMinValue() {
    return valueModel.getMinimum();
  }

  public void setMaxValue(double max) {
    valueModel.setMaximum(max);
  }

  public double getMaxValue() {
    return valueModel.getMaximum();
  }

  public DoubleRangeModel getModel() {
    return valueModel;
  }


  /**
   * Sets the current value to {@code n}. This method forwards the new value to the model.
   * <p>
   * The data model (an instance of {@code BoundedRangeModel}) handles any mathematical issues arising
   * from assigning faulty values. See the {@code BoundedRangeModel} documentation for details.
   * <p>
   * If the new value is different from the previous value, all change listeners are notified.
   *
   * @param n the new value
   * @see #getValue
   * @see #addChangeListener
   * @see BoundedRangeModel#setValue
   * @beaninfo preferred: true description: The sliders current value.
   */
  public void setValue(double n) {
    DoubleRangeModel m = valueModel;
    double oldValue = m.getValue();
    if (oldValue == n) {
      return;
    }
    m.setValue(n);

    repaint();
  }

  /**
   * Returns the slider's current value from the {@code BoundedRangeModel}.
   *
   * @return the current value of the slider
   * @see #setValue
   * @see BoundedRangeModel#getValue
   */
  public double getValue() {
    return getModel().getValue();
  }



  /**
   * Convert value to angle using minAngle and maxAngle
   */
  private int valueToAngle(double value) {
    int angle = (int) mapValues(value, valueModel.getMinimum(), valueModel.getMaximum(), minAngle, maxAngle);
    return angle;
  }

  /**
   * Re-maps a number from one range to another. That is, a value of fromLow would get mapped to
   * toLow, a value of fromHigh to toHigh, values in-between to values in-between, etc.
   * 
   * @return
   */
  private double mapValues(double x, double in_min, double in_max, double out_min, double out_max) {
    return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
  }


  @Override
  protected void paintComponent(Graphics g) {

    Graphics2D g2 = (Graphics2D) g.create();

    int w = getSize().width;
    int h = getSize().height;

    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    // Draw steps
    drawArcFor(g2, getMaxValue(), colorStep3, stepsBarWidth, 0);
    drawArcFor(g2, getStepValue(percentStep2), colorStep2, stepsBarWidth, 0);
    drawArcFor(g2, getStepValue(percentStep1), colorStep1, stepsBarWidth, 0);

    // Draw value

    Color valueColor = getValueColor();
    drawArcFor(g2, getMaxValue(), inativeColor, valueBarWidth, stepsBarWidth);
    drawArcFor(g2, getValue(), valueColor, valueBarWidth, stepsBarWidth);

    String valueStr = "" + (int) getValue();
    if (decimals > 0)
      valueStr = String.format(Locale.US, "%." + decimals + "f", getValue());

    int fontSize = 24;
    int unitFontSize = fontSize / 2;

    // TODO: CACHE FONT...
    Font defaultFont = getFont();
    Font valueFont = new Font(defaultFont.getName(), Font.BOLD, fontSize);
    Font unitFont = new Font(defaultFont.getName(), Font.BOLD, unitFontSize);

    g2.setFont(valueFont);
    Rectangle2D valueBounds = g2.getFontMetrics().getStringBounds(valueStr, g2);

    // Draw text value
    int labelOffsetX = 0;
    int labelOffsetY = 0;

    // Ajust offset of text based on style
    if (style == GaugeStyle.HALF) {
    } else if (style == GaugeStyle.BEST_FIT) {
    } else if (style == GaugeStyle.CIRCLE) {
      labelOffsetY = -(h / 2 - (int) valueBounds.getHeight() / 2);
    } else if (style == GaugeStyle.SEMI_CIRCLE_RIGHT) {
      int offset = (w / 4) + 4;
      labelOffsetX = -offset;
      labelOffsetY = -(offset / 2 + (int) valueBounds.getHeight() / 2);
    } else if (style == GaugeStyle.SEMI_CIRCLE_LEFT) {
      int offset = (w / 4) + 4;
      labelOffsetX = offset;
      labelOffsetY = -(offset / 2 + (int) valueBounds.getHeight() / 2);
    }

    // Draw unit string
    if (unitString != null && unitString.length() != 0) {
      g2.setFont(unitFont);
      Rectangle2D fontUnitSize = g2.getFontMetrics().getStringBounds(valueStr, g2);
      int padding = 3; // padding from value
      g2.drawString(unitString,
          (int) (w / 2 + valueBounds.getWidth() / 2 + padding - (fontUnitSize.getWidth() / 2)) + labelOffsetX,
          (int) (h - valueBounds.getHeight() / 3) + labelOffsetY);

      valueBounds.setRect(0, 0, valueBounds.getWidth() + fontUnitSize.getWidth(), valueBounds.getHeight()); // add width of unit string
    }

    g2.setFont(valueFont);
    g2.drawString(valueStr, (int) (w / 2 - valueBounds.getWidth() / 2) + labelOffsetX,
        (int) (h - valueBounds.getHeight() / 3) + labelOffsetY);

    g2.dispose();

  }

  private Color getValueColor() {

    double vStep1 = getStepValue(percentStep1);
    double vStep2 = getStepValue(percentStep2);

    double interpolationZone = getMaxValue() / 12;

    // Intepolation zone (ORANGE > RED)...
    int diffStep2 = (int) (vStep2 - getValue());
    if (diffStep2 <= interpolationZone && diffStep2 > 0) {
      double inter = (((interpolationZone - diffStep2) / interpolationZone) * 100f) / 100f;
      return interpolateColor(colorStep2, colorStep3, (float) inter);
    }

    // Intepolation zone (GREEN > ORANGE)...
    int diffStep1 = (int) (vStep1 - getValue());
    if (diffStep1 <= interpolationZone && diffStep1 > 0) {
      double inter = (((interpolationZone - diffStep1) / interpolationZone) * 100f) / 100f;
      return interpolateColor(colorStep1, colorStep2, (float) inter);
    }

    if (getValue() >= getStepValue(percentStep2))
      return colorStep3;
    if (getValue() >= getStepValue(percentStep1))
      return colorStep2;

    return colorStep1;
  }


  private double getStepValue(int stepsPercent) {
    return (stepsPercent * getMaxValue() / 100);
  }

  private void drawArcFor(Graphics2D g2, double value, Color color, int clipWidth, int margin) {

    int w = getSize().width;

    if (style == GaugeStyle.HALF) {
      minAngle = 90;
      maxAngle = 270;
    } else if (style == GaugeStyle.BEST_FIT) {
      minAngle = 73;
      maxAngle = 287;
    } else if (style == GaugeStyle.CIRCLE) {
      minAngle = 0;
      maxAngle = 360;
    } else if (style == GaugeStyle.SEMI_CIRCLE_RIGHT) {
      minAngle = 90;
      maxAngle = 360;
    } else if (style == GaugeStyle.SEMI_CIRCLE_LEFT) {
      minAngle = 0;
      maxAngle = 270;
    }


    int angle = valueToAngle(value);
    int negativeVal = (-angle) + minAngle;
    g2.setPaint(color);

    // no graw area
    BufferedImage clipMask = new BufferedImage(w, w, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2i = clipMask.createGraphics();
    applyQualityRenderingHints(g2i);
    g2i.fillArc(clipWidth, clipWidth, w - clipWidth * 2, w - clipWidth * 2, 0, 360);
    g2i.dispose();


    BufferedImage masked = new BufferedImage(w, w, BufferedImage.TYPE_INT_ARGB);
    g2i = masked.createGraphics();
    applyQualityRenderingHints(g2i);
    g2i.setPaint(color);
    int padding = 2 + margin;
    g2i.fillArc(padding, padding, w - padding * 2, w - padding * 2, (-minAngle) - 90, negativeVal);

    g2i.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_OUT));
    g2i.drawImage(clipMask, 0, 0, null);
    g2i.dispose();

    // TODO: cache masked ???

    g2.drawImage(masked, 0, 0, null);

  }

  public static void applyQualityRenderingHints(Graphics2D g2d) {

    g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
    g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
    g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

  }

  private java.awt.Color interpolateColor(final Color COLOR1, final Color COLOR2, float fraction) {
    final float itof = 1f / 255f;
    fraction = Math.min(fraction, 1f);
    fraction = Math.max(fraction, 0f);

    final float RED1 = COLOR1.getRed() * itof;
    final float GREEN1 = COLOR1.getGreen() * itof;
    final float BLUE1 = COLOR1.getBlue() * itof;
    final float ALPHA1 = COLOR1.getAlpha() * itof;

    final float RED2 = COLOR2.getRed() * itof;
    final float GREEN2 = COLOR2.getGreen() * itof;
    final float BLUE2 = COLOR2.getBlue() * itof;
    final float ALPHA2 = COLOR2.getAlpha() * itof;

    final float DELTA_RED = RED2 - RED1;
    final float DELTA_GREEN = GREEN2 - GREEN1;
    final float DELTA_BLUE = BLUE2 - BLUE1;
    final float DELTA_ALPHA = ALPHA2 - ALPHA1;

    float red = RED1 + (DELTA_RED * fraction);
    float green = GREEN1 + (DELTA_GREEN * fraction);
    float blue = BLUE1 + (DELTA_BLUE * fraction);
    float alpha = ALPHA1 + (DELTA_ALPHA * fraction);

    red = Math.min(red, 1f);
    red = Math.max(red, 0f);
    green = Math.min(green, 1f);
    green = Math.max(green, 0f);
    blue = Math.min(blue, 1f);
    blue = Math.max(blue, 0f);
    alpha = Math.min(alpha, 1f);
    alpha = Math.max(alpha, 0f);

    return new Color(red, green, blue, alpha);
  }

  @Override
  public void componentResized(ComponentEvent e) {

    updateSize();

  }

  @Override
  public void componentMoved(ComponentEvent e) {

  }

  @Override
  public void componentShown(ComponentEvent e) {}

  @Override
  public void componentHidden(ComponentEvent e) {}


}
