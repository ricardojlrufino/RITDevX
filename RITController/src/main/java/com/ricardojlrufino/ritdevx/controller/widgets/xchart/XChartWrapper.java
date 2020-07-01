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
package com.ricardojlrufino.ritdevx.controller.widgets.xchart;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseMotionListener;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.swing.JPanel;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.internal.chartpart.ToolTips;
import org.knowm.xchart.style.XYStyler;
import org.knowm.xchart.style.markers.None;

/**
 * Container for {@link XYChart} 
 * @author Ricardo JL Rufino - (ricardo.jl.rufino@gmail.com)
 * @date 23 de jun de 2020
 */
public class XChartWrapper extends JPanel{
  // extends XChartPanel<XYChart> (removed becoude of ctrl + z - imput map) 

  private static final long serialVersionUID = 1L;
  
  private XYChart chart;
  private double count = 0;
  private int dataPoints;
  
  private LinkedHashMap<String, List<Double>> dataSeriesMap = new LinkedHashMap();
  private List<Double> xData = new LinkedList<>();
  
  private String dataSeries;
  private XYStyler styler;

  public XChartWrapper() {
//    super(QuickChart.getChart("Title", "Time", "Value", "Line", new double[] {0}, new double[] {0}));
//    chart = this.getChart();
    chart = QuickChart.getChart("Title", "Time", "Value", "Line", new double[] {0}, new double[] {0});
    styler = chart.getStyler();
    
    configureDefaults();
    initEvents();
  }
  
  protected void initEvents() {
    
    // Mouse motion listener for data label popup
    ToolTips toolTips = chart.getToolTips();
    if (toolTips != null) {
      MouseMotionListener mml = toolTips.getMouseMotionListener();
      if (mml != null) {
        this.addMouseMotionListener(mml);
      }
    }
    
    // Mouse motion listener for Cursor
    this.addMouseMotionListener(chart.getCursor());
    
  }
  
  protected void configureDefaults() {
    XYStyler styler = chart.getStyler();
    styler.setLegendVisible(false);
    styler.setCursorEnabled(true);
//    styler.setlabel
    
    // Set the minimum and maximum values of the Y axis.
    setYAxisMax(100d);
    setYAxisMin(0d);
    setDataPoints(100);
    
    setDataSeries("Line");
  }
  
  public void setDataPoints(int dataLength) {
    this.dataPoints = dataLength;
  }
  
  public int getDataPoints() {
    return dataPoints;
  }

  public Double getYAxisMin() {
    return styler.getYAxisMin();
  }

  public void setYAxisMin(Double yAxisMin) {
    styler.setYAxisMin(yAxisMin);
  }

  public Double getYAxisMax() {
    return styler.getYAxisMax();
  }

  public void setYAxisMax(Double yAxisMax) {
    styler.setYAxisMax(yAxisMax);
  }
  

  public String getTitle() {
    return chart.getTitle();
  }

  public void setTitle(String title) {
    chart.setTitle(title);
  }

  public String getXAxisTitle() {
    return chart.getXAxisTitle();
  }

  public void setXAxisTitle(String xAxisTitle) {
    chart.setXAxisTitle(xAxisTitle);
  }

  public String getYAxisTitle() {
    return chart.getYAxisTitle();
  }

  public void setYAxisTitle(String yAxisTitle) {
    chart.setYAxisTitle(yAxisTitle);
  }
  
  /**
   * Set multiple data series. Use comma ',', to serpare multiple values.
   * @param dataSeries
   */
  public void setDataSeries(String dataSeries) {
    this.dataSeries = dataSeries;
  }
  
  public String getDataSeries() {
    return dataSeries;
  }

//  @Override
//  public synchronized void addMouseListener(MouseListener l) {
//    // TODO: DISABLE ONLY IF IN DESIGN MODE
//    if (!l.getClass().getName().contains("org.knowm.xchart")) {
//      super.addMouseListener(l);
//    }
//  }
  
  
  private void initDataSeries() {
    chart.removeSeries("Line");

    for (String name : dataSeries.trim().split(",")) {
      dataSeriesMap.put(name.trim(), new LinkedList<>());
      xData = new LinkedList<>();
      XYSeries series = chart.addSeries(name, new double[] {0});
      series.setMarker(new None());
    }

    // Show legent if as multiple values.
    if (dataSeriesMap.values().size() > 1) {
      styler.setLegendVisible(true);
    }
  }

  /**
   * Add a single value or multiple values.
   * For multiple values, use also {@link #setDataSeries(String)}
   * @param params
   */
  public void addValues(List<Object> params) {

    if (dataSeriesMap.isEmpty())
      initDataSeries();

    count++;
    xData.add(count);
    if (xData.size() > dataPoints) xData.remove(0);

    Set<String> series = dataSeriesMap.keySet();

    int paramCount = 0;
    for (String serie : series) {
      List<Double> yData = dataSeriesMap.get(serie);
      yData.add(Double.parseDouble(params.get(paramCount++).toString()));
      if (yData.size() > dataPoints) yData.remove(0);

      chart.updateXYSeries(serie, xData, yData, null);

    }

    if (xData.size() < dataPoints) {
      chart.getStyler().setXAxisMin(xData.get(0));
      chart.getStyler().setXAxisMax((double) dataPoints);
    } else {
      chart.getStyler().setXAxisMin(xData.get(0));
      chart.getStyler().setXAxisMax((double) xData.get(dataPoints - 1));
    } 

    repaint();
  }
  
  
  @Override
  protected void paintComponent(Graphics g) {

    super.paintComponent(g);

    Graphics2D g2d = (Graphics2D) g.create();
    chart.paint(g2d, getWidth(), getHeight());
    g2d.dispose();
  }
  

}
