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

// Imports for the GUI classes.
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.accessibility.AccessibleContext;
import javax.imageio.ImageIO;
import javax.swing.BoundedRangeModel;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * JKnob.java - A knob component. The knob can be rotated by dragging a spot on the knob around in a
 * circle. <br/>
 * <br/>
 * 
 * In the basic construction of the visual, the component is defined based on 'minAngle' and
 * 'maxAngle'. <br/>
 * Angle ZERO is in the SOUTH, and runs clockwise. <br/>
 * <br/>
 * 
 * The offset in angles is mapped to values through the minimum and maximum value
 * ({@link #setMinMaxValues(int, int)}), and can be monitored through the
 * {@link #addChangeListener(ChangeListener)}.<br/>
 * <br/>
 * 
 * The look of this component is built entirely with images, 3 in total, but only 2 are necessary
 * ({@link #setImgKnobe(String)} and {@link #setImgBackgroud(String)}). </br>
 * Image 3 can be used for a visual activation effect {@link #setImgTrack(String)}.<br/>
 * It is important that the images are in PNG format, with the same size, and that they are light.
 * <br/>
 * <br/>
 * 
 * The rotation point must be adjusted according to your image ({@link #setControlPointOffset(int)},
 * {@link #setControlPointSize(int)}). </br>
 * </br>
 * To optimize the use of memory, a cache is used for the images already loaded, this helps when
 * more than one instance of the same class is used.
 *
 * @author Grant William Braught
 * @author Dickinson College
 * @author Ricardo JL Rufino
 * @version 12/4/2020
 */

@SuppressWarnings("serial")
public class JKnob extends JComponent implements MouseListener, MouseMotionListener {

  private String imgTrack;
  private String imgKnobe;
  private String imgBackgroud;

  private static final Map<String, BufferedImage> imgCache = new HashMap<>();

  // store drawing buffers
  private BufferedImage imgTrackBuffer;
  private BufferedImage imgKnobeBuffer;
  private BufferedImage imgBackgroudBuffer;

  // Current position
  private double theta;
  private double angle;

  // Configurable positions
  private int minAngle = 10;
  private int maxAngle = 350;
  private int stepAngle = 10;

  // Ajusts
  protected int controlPointOffset = 0;
  protected int controlPointSize = 10;

  /**
   * The data model that handles the numeric maximum value, minimum value, and current-position value
   * for the .
   */
  protected BoundedRangeModel valueModel;

  protected transient ChangeEvent changeEvent = null;

  private ChangeListener changeListener = new ModelListener();

  // Debug controlers
  private Arc2D arcActive = new Arc2D.Float();
  private boolean debug = false;

  // state
  private Point spotCenter;
  private boolean pressedOnSpot;
  private Cursor hoverCursor = new Cursor(Cursor.HAND_CURSOR);
  private Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);

  protected int radius = 50; // calculated from knob image size.

  public JKnob() {
    this(10, 350, 0, 100);
  }

  public JKnob(int minAngle, int maxAngle) {
    this(minAngle, maxAngle, 0, 100);
  }

  /**
   * Constructor that initializes the constraints og Knob. It is mandatory to initialize the image
   * that will serve as the basis for the Knob, where the rotation effect is generated.
   * 
   * @param minAngle min angle with starts min range value of imagem
   * @param maxAngle max angle with represents end value of imagem
   * @param minValue the minimum value of the knob
   * @param maxValue the maximum value of the knob
   * 
   * @see #setImgKnobe(String)
   * @see #setImgBackgroud(String)
   * @see #setImgTrack(String)
   */
  public JKnob(int minAngle, int maxAngle, int minValue, int maxValue) {
    setAngle(minAngle);
    setModel(new DefaultBoundedRangeModel(0, 0, minValue, maxValue));
    this.addMouseListener(this);
    this.addMouseMotionListener(this);
  }


  /**
   * Defines the control point used to rotate the Knob
   * 
   * @param controlPointOffset
   */
  public void setControlPointOffset(int controlPointOffset) {
    int oldValue = this.controlPointOffset;
    this.controlPointOffset = controlPointOffset;
    firePropertyChange("controlPointOffset", oldValue, controlPointOffset);
  }

  public void setControlPointSize(int controlPointSize) {
    this.controlPointSize = controlPointSize;
  }

  /**
   * Control de max rotation of knob
   * 
   * @param maxAngle
   */
  public void setMaxAngle(int maxAngle) {
    this.maxAngle = maxAngle;
    if (debug)
      repaint();
  }

  /**
   * Control min rotation of knob.
   * 
   * @param minAngle
   */
  public void setMinAngle(int minAngle) {
    this.minAngle = minAngle;
    if (angle < minAngle) {
      setAngle(minAngle);
    }
    if (debug)
      repaint();
  }

  public int getMinAngle() {
    return minAngle;
  }

  public void setStepAngle(int stepAngle) {
    this.stepAngle = stepAngle;
  }

  public int getStepAngle() {
    return stepAngle;
  }

  /**
   * Get the current anglular position of the knob.
   *
   * @return the current anglular position of the knob.
   */
  public double getAngle() {
    return angle;
  }

  public void setAngle(double angle) {
    this.angle = angle;
    this.theta = Math.toRadians(angle - 180);
  }


  public void setImgBackgroud(String imgBackgroud) {
    this.imgBackgroud = imgBackgroud;
    this.imgBackgroudBuffer = loadImg(imgBackgroud);
  }

  public String getImgBackgroud() {
    return imgBackgroud;
  }

  /**
   * Set the image of the component that will make the rotation animation, this is the main image and
   * it is from there that the component size calculations are made.
   * 
   * @see #setControlPointOffset
   * @param imgKnobe
   */
  public void setImgKnobe(String imgKnobe) {
    this.imgKnobe = imgKnobe;
    this.imgKnobeBuffer = loadImg(imgKnobe);
    this.radius = imgKnobeBuffer.getWidth() / 2;
    repaint();
  }

  public String getImgKnobe() {
    return imgKnobe;
  }

  /**
   * Optional image to create a circular active effect
   * 
   * @param imgRingActive
   */
  public void setImgTrack(String imgRingActive) {
    this.imgTrack = imgRingActive;
    this.imgTrackBuffer = loadImg(imgRingActive);
  }

  public String getImgTrack() {
    return imgTrack;
  }


  /**
   * Load image from
   */
  protected BufferedImage loadImg(String img) {
    try {

      BufferedImage buffered = imgCache.get(img);
      if (buffered == null) {
        buffered = ImageIO.read(new File(img));
        imgCache.put(img, buffered);
      }
      return buffered;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }


  /**
   * Return the ideal size that the knob would like to be.
   */
  @Override
  public Dimension getPreferredSize() {
    if (imgKnobeBuffer == null)
      throw new IllegalStateException("imgKnobe can't be null !!");
    return new Dimension(imgKnobeBuffer.getWidth(), imgKnobeBuffer.getHeight());
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

  public double getTheta() {
    return theta;
  }



  /**
   * Calculate the x, y coordinates of the center of the spot.
   *
   * @return a Point containing the x,y position of the center of the spot.
   */
  private Point getSpotCenter() {

    // Calculate the center point of the spot RELATIVE to the
    // center of the of the circle.

    int r = (radius - controlPointOffset) - controlPointSize;

    int xcp = (int) (r * Math.sin(theta));
    int ycp = (int) (r * Math.cos(theta));

    // Adjust the center point of the spot so that it is offset
    // from the center of the circle. This is necessary becasue
    // 0,0 is not actually the center of the circle, it is the
    // upper left corner of the component!
    int xc = radius + xcp;
    int yc = radius - ycp;

    if (spotCenter == null)
      spotCenter = new Point(xc, yc);
    else
      spotCenter.setLocation(xc, yc);

    // Create a new Point to return since we can't
    // return 2 values!
    return spotCenter;
  }

  /**
   * Determine if the mouse click was on the spot or not. If it was return true, otherwise return
   * false.
   *
   * @return true if x,y is on the spot and false if not.
   */
  private boolean isOnSpot(Point pt) {
    return (pt.distance(getSpotCenter()) < controlPointSize);
  }

  // Methods from the MouseListener interface.

  /**
   * Empy method because nothing happens on a click.
   *
   * @param e reference to a MouseEvent object describing the mouse click.
   */
  public void mouseClicked(MouseEvent e) {}

  /**
   * Empty method because nothing happens when the mouse enters the Knob.
   *
   * @param e reference to a MouseEvent object describing the mouse entry.
   */
  public void mouseEntered(MouseEvent e) {}

  /**
   * Empty method because nothing happens when the mouse exits the knob.
   *
   * @param e reference to a MouseEvent object describing the mouse exit.
   */
  public void mouseExited(MouseEvent e) {}

  /**
   * When the mouse button is pressed, the dragging of the spot will be enabled if the button was
   * pressed over the spot.
   *
   * @param e reference to a MouseEvent object describing the mouse press.
   */
  public void mousePressed(MouseEvent e) {

    if (!isEnabled())
      return;

    Point mouseLoc = e.getPoint();
    pressedOnSpot = isOnSpot(mouseLoc);

    if (isRequestFocusEnabled()) {
      requestFocus();
    }

  }

  /**
   * When the button is released, the dragging of the spot is disabled.
   *
   * @param e reference to a MouseEvent object describing the mouse release.
   */
  public void mouseReleased(MouseEvent e) {
    pressedOnSpot = false;
    valueModel.setValueIsAdjusting(false);
  }

  // Methods from the MouseMotionListener interface.

  /**
   * Empty method because nothing happens when the mouse is moved if it is not being dragged.
   *
   * @param e reference to a MouseEvent object describing the mouse move.
   */
  public void mouseMoved(MouseEvent e) {
    if (pressedOnSpot || isOnSpot(e.getPoint())) {
      setCursor(hoverCursor);
    } else {
      setCursor(defaultCursor);
    }

  }


  /**
   * Compute the new angle for the spot and repaint the knob. The new angle is computed based on the
   * new mouse position.
   */
  public void mouseDragged(MouseEvent e) {

    if (!isEnabled())
      return;

    if (pressedOnSpot) {

      valueModel.setValueIsAdjusting(true);

      int mx = e.getX();
      int my = e.getY();

      // Compute the x, y position of the mouse RELATIVE
      // to the center of the knob.
      int mxp = mx - radius;
      int myp = radius - my;

      // Compute the new angle of the knob from the
      // new x and y position of the mouse.
      // Math.atan2(...) computes the angle at which
      // x,y lies from the positive y axis with cw rotations
      // being positive and ccw being negative.
      double thetaI = Math.atan2(mxp, myp);

      int angleI = toPositiveDegrees(thetaI);

      if (minAngle != 0 && maxAngle != 0) {

        if (angleI < minAngle)
          angleI = minAngle;
        if (angleI > maxAngle)
          angleI = maxAngle;

        if (Math.abs(angleI - angle) < 100) { // avoid invalid 360 rotation

          if (stepAngle > 0) { // enable step changes...

            if (Math.abs(angleI - angle) >= stepAngle || (angleI == minAngle || angleI == maxAngle)) {
              updateValues(thetaI, angleI);
            }

          } else {
            updateValues(thetaI, angleI);
          }

        }

      } else {
        updateValues(thetaI, angleI);
      }


    }
  }

  /**
   * Adds a ChangeListener to the slider.
   *
   * @param l the ChangeListener to add
   * @see #fireStateChanged
   * @see #removeChangeListener
   */
  public void addChangeListener(ChangeListener l) {
    listenerList.add(ChangeListener.class, l);
  }

  /**
   * Removes a ChangeListener from the slider.
   */
  public void removeChangeListener(ChangeListener l) {
    listenerList.remove(ChangeListener.class, l);
  }

  /**
   * Set min max range for this component.
   * 
   * @param min
   * @param max
   */
  public void setMinMaxValues(int min, int max) {
    setModel(new DefaultBoundedRangeModel(0, 0, min, max));
  }

  public void setMinValue(int min) {
    valueModel.setMinimum(min);
  }

  public int getMinValue() {
    return valueModel.getMinimum();
  }

  public void setMaxValue(int max) {
    valueModel.setMaximum(max);
  }

  public int getMaxValue() {
    return valueModel.getMaximum();
  }

  /**
   * Sets the {@code BoundedRangeModel} that handles the slider's three fundamental properties:
   * minimum, maximum, value.
   * <p>
   * Attempts to pass a {@code null} model to this method result in undefined behavior, and, most
   * likely, exceptions.
   *
   * @param newModel the new, {@code non-null} <code>BoundedRangeModel</code> to use
   *
   * @see #getModel
   * @see BoundedRangeModel
   * @beaninfo bound: true description: The sliders BoundedRangeModel.
   */
  public void setModel(BoundedRangeModel newModel) {
    BoundedRangeModel oldModel = getModel();

    if (oldModel != null) {
      oldModel.removeChangeListener(changeListener);
    }

    valueModel = newModel;

    if (newModel != null) {
      newModel.addChangeListener(changeListener);
    }

    if (accessibleContext != null) {
      accessibleContext.firePropertyChange(
          AccessibleContext.ACCESSIBLE_VALUE_PROPERTY,
          (oldModel == null
              ? null
              : Integer.valueOf(oldModel.getValue())),
          (newModel == null
              ? null
              : Integer.valueOf(newModel.getValue())));
    }

    firePropertyChange("model", oldModel, valueModel);
  }

  /**
   * Returns the {@code BoundedRangeModel} that handles the slider's three fundamental properties:
   * minimum, maximum, value.
   *
   * @return the data model for this component
   * @see #setModel
   * @see BoundedRangeModel
   */
  public BoundedRangeModel getModel() {
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
  public void setValue(int n) {
    BoundedRangeModel m = getModel();
    int oldValue = m.getValue();
    if (oldValue == n) {
      return;
    }
    m.setValue(n);
    updateValueToAngle();
  }

  /**
   * Returns the slider's current value from the {@code BoundedRangeModel}.
   *
   * @return the current value of the slider
   * @see #setValue
   * @see BoundedRangeModel#getValue
   */
  public int getValue() {
    return getModel().getValue();
  }


  /**
   * Enable visual feedback of angles in constraints of component.
   * 
   * @param debug
   */
  public void setDebug(boolean debug) {
    this.debug = debug;
  }


  /**
   * We pass Change events along to the listeners with the the slider (instead of the model itself) as
   * the event source.
   */
  private class ModelListener implements ChangeListener, Serializable {
    public void stateChanged(ChangeEvent e) {
      fireStateChanged();
    }
  }


  protected void fireStateChanged() {
    Object[] listeners = listenerList.getListenerList();
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == ChangeListener.class) {
        if (changeEvent == null) {
          changeEvent = new ChangeEvent(this);
        }
        ((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
      }
    }
  }



  private void updateValues(double theta, int angle) {
    if (this.angle != angle) {
      this.theta = theta;
      this.angle = angle;

      // update value
      int values = mapValues(angle, minAngle, maxAngle, valueModel.getMinimum(), valueModel.getMaximum());
      valueModel.setValue(values);
      repaint();
    }
  }

  private void updateValueToAngle() {
    int angle = mapValues(valueModel.getValue(), valueModel.getMinimum(), valueModel.getMaximum(), minAngle, maxAngle);
    setAngle(angle);
    repaint();
  }

  /**
   * Re-maps a number from one range to another. That is, a value of fromLow would get mapped to
   * toLow, a value of fromHigh to toHigh, values in-between to values in-between, etc.
   * 
   * @return
   */
  private int mapValues(int x, int in_min, int in_max, int out_min, int out_max) {
    return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
  }

  private int toPositiveDegrees(double thetaI) {
    int angleI = (int) Math.toDegrees(thetaI);
    angleI = 180 + angleI;
    return angleI;
  }



  @Override
  protected void paintComponent(Graphics g) {

    Graphics2D g2 = (Graphics2D) g.create();

    int angle = (int) getAngle();

    int w = imgKnobeBuffer.getWidth();
    int h = imgKnobeBuffer.getHeight();



    //    System.out.println("start:" + angle + ", negativeVal: " + negativeVal);
    //    System.out.println("theta: " + theta + ", angle: " + angle);

    //    g2.setPaint(new Color(34, 32, 38));
    //    g2.fillRect(0, 0, getWidth(), getHeight());

    g2.drawImage(imgBackgroudBuffer, 0, 0, null);

    // Draw rotated knob
    drawKnob(g2, angle);

    int negativeVal = (-angle) + minAngle;
    arcActive.setArc(0, 0, w, h, (-minAngle) - 90, negativeVal, Arc2D.PIE);

    if (imgTrackBuffer != null) {

      Area activeArea = new Area(arcActive);
      g2.setClip(activeArea);
      g2.drawImage(imgTrackBuffer, 0, 0, null);
      g2.setClip(null);

    }


    if (debug) {

      // Current angle debug
      g2.setPaint(Color.red);
      g2.draw(arcActive);

      // Draw the knob.
      g.setColor(Color.red);
      g.drawOval(0, 0, 2 * radius, 2 * radius);

      // Find the center of the spot.
      Point pt = getSpotCenter();
      final int xc = (int) pt.getX();
      final int yc = (int) pt.getY();

      // draw max and min angle
      g2.setColor(Color.green);
      negativeVal = (-maxAngle) + minAngle;
      arcActive.setArc(0, 0, w, h, (-minAngle) - 90, negativeVal, Arc2D.PIE);
      g2.draw(arcActive);

      // Draw the spot.
      g.setColor(Color.ORANGE);
      g.fillOval(xc - controlPointSize, yc - controlPointSize, 2 * controlPointSize, 2 * controlPointSize);
    }

    g2.dispose();

  }


  protected void drawKnob(Graphics2D g2d, double angle) {
    AffineTransform orig = g2d.getTransform();
    AffineTransform newXform = (AffineTransform) (orig.clone());
    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    //center of rotation is center of the panel
    int xRot = imgKnobeBuffer.getWidth() / 2;
    int yRot = imgKnobeBuffer.getHeight() / 2;
    newXform.rotate(Math.toRadians(angle), xRot, yRot);
    //draw image centered in panel
    //    int x = ( getWidth() - imgKnobe.getWidth(this))/2;
    //    int y = (getHeight() - imgKnobe.getHeight(this))/2;
    g2d.setTransform(newXform);
    //    g2d.drawImage(imgKnobe, x, y, this);
    g2d.drawImage(imgKnobeBuffer, 0, 0, this);
    g2d.setTransform(orig);

    //    g2d.drawImage(imgKnobe, 0, 0, this);
  }

}
