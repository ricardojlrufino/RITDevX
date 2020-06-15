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

import java.awt.event.MouseListener;
import java.util.List;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import com.ricardojlrufino.ritdevx.controller.utils.CircularBuffer;

public class XChartWrapper extends XChartPanel<XYChart> {

  private XYChart chart;
  private double count = 0;
  private final int size = 100;
  private CircularBuffer xb = new CircularBuffer(size);
  private CircularBuffer yb = new CircularBuffer(size);

  public XChartWrapper() {
    super(QuickChart.getChart("Title", "Time", "Value", "Line", new double[] {0}, new double[] {0}));
    chart = this.getChart();
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

  @Override
  public synchronized void addMouseListener(MouseListener l) {
    // TODO: SIABLE ONLY IF IN DESIGN MODE
    if (!l.getClass().getName().contains("org.knowm.xchart")) {
      super.addMouseListener(l);
    }
  }

  //  public void setValues(List<Object> params) {
  //    double v1 = Double.parseDouble(params.get(0).toString());
  //    double v2 = Double.parseDouble(params.get(1).toString());
  //    double v3 = Double.parseDouble(params.get(2).toString());
  //
  //    count++;
  //    xb.add(count);
  //    yb.add(v1);
  //    
  //    double[] xData = new double[size];
  //    double[] yData = new double[size];
  //    for (int i = 0; i < size; i++) {
  //      if(xb.size() - 1 >= i) {
  //        xData[i] = xb.get(i);
  //        yData[i] = yb.get(i);        
  //      }else {
  //        xData[i] = i;
  //        yData[i] = v1;  
  //      }
  //    }
  //    
  //    chart.updateXYSeries("Line",xData, yData, null);
  //
  //    repaint();
  //  }

  public void addValues(List<Object> params) {
    double v1 = Double.parseDouble(params.get(0).toString());
    //    double v2 = Double.parseDouble(params.get(1).toString());
    //    double v3 = Double.parseDouble(params.get(2).toString());

    count++;
    xb.add(count);
    yb.add(v1);

    double[] xData = new double[xb.size()];
    double[] yData = new double[xb.size()];
    for (int i = 0; i < xb.size(); i++) {
      xData[i] = xb.get(i);
      yData[i] = yb.get(i);
    }

    chart.updateXYSeries("Line", xData, yData, null);

    repaint();
  }
}
