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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import com.ricardojlrufino.ritdevx.controller.widgets.WidgetInfo;
import com.ricardojlrufino.ritdevx.controller.widgets.skinnable.SimpleGauge;
import com.ricardojlrufino.ritdevx.controller.widgets.skinnable.TitlePanel;
import com.ricardojlrufino.ritdevx.controller.widgets.skinnable.IconState;
import com.ricardojlrufino.ritdevx.controller.widgets.skinnable.ImageButton;
import com.ricardojlrufino.ritdevx.controller.widgets.skinnable.ImageState;
import com.ricardojlrufino.ritdevx.controller.widgets.skinnable.Label;

public class DefaultWidgetsFactory extends AbstractWidgetFactory {

  private static Map<String, AtomicInteger> names = new HashMap<>();

  public DefaultWidgetsFactory() {

    WidgetInfo imageState = addAndsetDefaults(ImageState.class, "Image");
    imageState.addProperty("Active");
    imageState.addProperty("ImageOn");
    imageState.addProperty("ImageOff");
    imageState.addProperty("AutoScale");
    imageState.setSize(100, 100);

    WidgetInfo iconState = addAndsetDefaults(IconState.class, "Icon");
    iconState.addProperty("Active");
    iconState.addProperty("IconOn");
    iconState.addProperty("ColorOn");
    iconState.addProperty("IconOff");
    iconState.addProperty("ColorOff");
    iconState.addProperty("OperationMode");
    iconState.addProperty("CustomCmd");
    iconState.setSize(48, 48);

    WidgetInfo button = addAndsetDefaults(ImageButton.class, "Button");
    button.addProperty("Active");
    button.addProperty("ImageOn");
    button.addProperty("ImageOff");
    button.addProperty("AutoScale");
    button.addProperty("OperationMode");
    button.addProperty("CustomCmd");
    button.setSize(64, 64);

    WidgetInfo label = addAndsetDefaults(Label.class, "Label");
    label.addProperty("Text");
    label.addProperty("Background");
    label.addProperty("Foreground");
//    label.addProperty("Font");
    label.addProperty("Opaque");
    label.addProperty("CornerRadius");
    label.setSize(100, 48);


    WidgetInfo simpleGauge = addAndsetDefaults(SimpleGauge.class, "SimpleGauge");
    simpleGauge.addProperty("Value");
    simpleGauge.addProperty("Decimals");
    simpleGauge.addProperty("UnitString");
    simpleGauge.addProperty("MinValue");
    simpleGauge.addProperty("MaxValue");
    simpleGauge.addProperty("ValueFont");
    simpleGauge.addProperty("Style");
    simpleGauge.addProperty("ColorStep1");
    simpleGauge.addProperty("ColorStep2");
    simpleGauge.addProperty("ColorStep3");
    simpleGauge.addProperty("InativeColor");
    simpleGauge.addProperty("PercentStep1");
    simpleGauge.addProperty("PercentStep2");
    simpleGauge.addProperty("StepsBarWidth");
    simpleGauge.addProperty("ValueBarWidth");
    
    simpleGauge.setSize(200, 100);

    WidgetInfo titlePanel = addAndsetDefaults(TitlePanel.class, "TitlePanel");
    titlePanel.addProperty("Title");
    titlePanel.addProperty("TitleFont");
    titlePanel.addProperty("TitleColor");
    titlePanel.addProperty("BorderColor");
    titlePanel.addProperty("Background");
    titlePanel.addProperty("CornerRadius");
    titlePanel.setSize(270, 200);
    
  }


 
  /**
   * Create application properties shared between Designer and Controller. Designer will add more
   * properties to store user preferences...
   * 
   * @param kclass
   * @return
   */
  public static WidgetInfo createApp(Class<?> kclass) {
    WidgetInfo info = new WidgetInfo(kclass);
    info.addProperty("Title");
    info.addProperty("Background");
    info.addProperty("BackgroudImage");
    return info;
  }

}
