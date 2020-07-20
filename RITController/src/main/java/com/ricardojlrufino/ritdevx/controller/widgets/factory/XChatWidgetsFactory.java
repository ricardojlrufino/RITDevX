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
package com.ricardojlrufino.ritdevx.controller.widgets.factory;

import com.ricardojlrufino.ritdevx.controller.widgets.WidgetInfo;
import com.ricardojlrufino.ritdevx.controller.widgets.xchart.XChartWrapper;

public class XChatWidgetsFactory extends AbstractWidgetFactory {

  public XChatWidgetsFactory() {

    WidgetInfo XYChart = addAndsetDefaults(XChartWrapper.class, "LineChart");
    XYChart.addProperty("Title"); 
    XYChart.addProperty("DataSeries");
    XYChart.addProperty("DataPoints");
    XYChart.addProperty("XAxisTitle");
    XYChart.addProperty("YAxisTitle");
    XYChart.addProperty("YAxisMin");
    XYChart.addProperty("YAxisMax");
    XYChart.addProperty("DarkStyle");
    XYChart.setIcon("LineChart");
    XYChart.setSize(500, 250);  
  }

}
