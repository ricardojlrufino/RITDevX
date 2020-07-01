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
package com.ricardojlrufino.ritdevx.controller.widgets.factory;

import java.awt.Dimension;
import java.awt.Point;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import com.ricardojlrufino.ritdevx.controller.widgets.WidgetInfo;
import com.ricardojlrufino.ritdevx.controller.widgets.steelseries.LinearBargraphImpl;
import eu.hansolo.steelseries.extras.AirCompass;
import eu.hansolo.steelseries.gauges.AbstractGauge;
import eu.hansolo.steelseries.gauges.DisplaySingle;
import eu.hansolo.steelseries.gauges.RadialBargraph;
import eu.hansolo.steelseries.gauges.SparkLine;
import eu.hansolo.steelseries.tools.BackgroundColor;
import eu.hansolo.steelseries.tools.NumberFormat;

/**
 * Factory for components from:
 * 
 * @author Ricardo JL Rufino - (ricardo.jl.rufino@gmail.com)
 * @date 3 de jun de 2020
 */
public class GaugesSteelSeriesFactory extends AbstractWidgetFactory {


  public GaugesSteelSeriesFactory() {

    WidgetInfo radialBargraph = new WidgetInfo(RadialBargraph.class);
    radialBargraph.addProperty("Value");
    radialBargraph.addProperty("BarGraphColor");
    addAndsetDefaults(radialBargraph,"Radial Bar");
    addAbstractRadial(radialBargraph, true);
    radialBargraph.setSize(230, 230);

    WidgetInfo led = new WidgetInfo(eu.hansolo.steelseries.extras.Led.class);
    addAndsetDefaults(led, "Led");
    led.addProperty("LedOn");
    led.addProperty("LedColor");
    led.addProperty("CustomLedColor");
    led.addProperty("LedType");
    led.addProperty("LedBlinking");

    WidgetInfo sparkLine = new WidgetInfo(SparkLine.class);
    addAndsetDefaults(sparkLine, "SparkLine");
    sparkLine.addProperty("TimeFrame");
    sparkLine.addProperty("Filled");
    sparkLine.addProperty("Smoothing");
    sparkLine.addProperty("SmoothFunction");
    sparkLine.addProperty("SparkLineColor");
    sparkLine.addProperty("BackgroundVisible");
    sparkLine.addProperty("LineColor");
    sparkLine.addProperty("AverageColor");
    sparkLine.addProperty("NormalAreaColor");
    sparkLine.addProperty("AreaFill");
    sparkLine.addProperty("LineWidth");
    sparkLine.addProperty("LineShadow");
    sparkLine.addProperty("StartStopIndicatorVisible");
    sparkLine.addProperty("HiLoIndicatorVisible");
    sparkLine.addProperty("InfoLabelsVisible");
    sparkLine.addProperty("BaseLineVisible");
    sparkLine.addProperty("AverageVisible");
    sparkLine.addProperty("NormalAreaVisible");


    // Ref: https://harmoniccode.blogspot.com/2012/03/steelseries-3920.html
    WidgetInfo displaySingle = new WidgetInfo(DisplaySingle.class);
    displaySingle.addProperty("LcdValue");
    addAndsetDefaults(displaySingle, "Simple Display");
    displaySingle.setSize(160, 60);
    displaySingle.addProperty("BargraphVisible");
    //    DisplaySingle.addProperty("CustomLcdBackground");
    //    DisplaySingle.addProperty("CustomLcdForeground");
    //    DisplaySingle.addProperty("CustomLcdUnitFont");
    //    DisplaySingle.addProperty("CustomLcdUnitFontEnabled");
    displaySingle.addProperty("DigitalFont");
    displaySingle.addProperty("GlowColor");
    displaySingle.addProperty("GlowVisible");
    displaySingle.addProperty("Glowing");
    displaySingle.addProperty("LcdMinValue");
    displaySingle.addProperty("LcdMaxValue");
    displaySingle.addProperty("LcdBackgroundVisible");
    displaySingle.addProperty("LcdBlinking");
    displaySingle.addProperty("LcdColor");
    displaySingle.addProperty("LcdDecimals");
    displaySingle.addProperty("LcdInfoFont");
    displaySingle.addProperty("LcdInfoString");
    displaySingle.addProperty("LcdNumberSystem");
    displaySingle.addProperty("LcdNumericValues");
    displaySingle.addProperty("LcdText");
    displaySingle.addProperty("LcdTextScrolling");
    displaySingle.addProperty("LcdThreshold");
    displaySingle.addProperty("LcdThresholdBehaviourInverted");
    displaySingle.addProperty("LcdThresholdVisible");
    displaySingle.addProperty("LcdUnitFont");
    displaySingle.addProperty("LcdUnitString");
    displaySingle.addProperty("LcdUnitStringVisible");
    displaySingle.addProperty("LcdValueFont");
    displaySingle.addProperty("PlainBargraphSegments");


    WidgetInfo LinearBargraph = new WidgetInfo(LinearBargraphImpl.class);
    addAndsetDefaults(LinearBargraph,"LevelBar");
    LinearBargraph.setIcon("LevelBar");
    LinearBargraph.addProperty("Value");
    LinearBargraph.addProperty("MaxValue");
    LinearBargraph.addProperty("MinValue");


    LinearBargraph.addProperty("BackgroundColor").setValue("value", BackgroundColor.MUD);
    LinearBargraph.addProperty("BackgroundVisible");
    LinearBargraph.addProperty("BarGraphColor");
    LinearBargraph.addProperty("DigitalFont");
    LinearBargraph.addProperty("ForegroundVisible");
    LinearBargraph.addProperty("FrameVisible").setValue("value", false);
    LinearBargraph.addProperty("FrameDesign");
    LinearBargraph.addProperty("FrameEffect");

    LinearBargraph.addProperty("LabelColor");
    LinearBargraph.addProperty("LabelColorFromThemeEnabled");
    LinearBargraph.addProperty("LabelNumberFormat").setValue("value", NumberFormat.STANDARD);

    LinearBargraph.addProperty("LcdColor");
    LinearBargraph.addProperty("LcdDecimals");
    LinearBargraph.addProperty("LcdNumberSystem");

    LinearBargraph.addProperty("LcdUnitFont");
    LinearBargraph.addProperty("LcdUnitString");
    LinearBargraph.addProperty("LcdUnitStringVisible");
    LinearBargraph.addProperty("LcdVisible");

    LinearBargraph.addProperty("MajorTickSpacing");
    LinearBargraph.addProperty("MajorTickmarkType");
    LinearBargraph.addProperty("MajorTickmarkVisible");
    LinearBargraph.addProperty("MaxMeasuredValueVisible").setValue("value", false);
    LinearBargraph.addProperty("MinMeasuredValueVisible").setValue("value", false);
    LinearBargraph.addProperty("MaxNoOfMajorTicks");
    LinearBargraph.addProperty("MaxNoOfMinorTicks");


    LinearBargraph.addProperty("MinorTickSpacing");
    LinearBargraph.addProperty("MinorTickmarkType");
    LinearBargraph.addProperty("MinorTickmarkVisible");
    LinearBargraph.addProperty("NiceScale");
    LinearBargraph.addProperty("Orientation");

    LinearBargraph.addProperty("Threshold");
    LinearBargraph.addProperty("ThresholdBehaviourInverted");
    LinearBargraph.addProperty("ThresholdColor");
    LinearBargraph.addProperty("ThresholdType");
    LinearBargraph.addProperty("ThresholdVisible");
    LinearBargraph.addProperty("TicklabelsVisible");
    LinearBargraph.addProperty("TickmarkColor");
    LinearBargraph.addProperty("TickmarkColorFromThemeEnabled").setValue("value", false);
    LinearBargraph.addProperty("TickmarksVisible");
    LinearBargraph.addProperty("Title");
    LinearBargraph.addProperty("TitleAndUnitFont");
    LinearBargraph.addProperty("TitleAndUnitFontEnabled");
    LinearBargraph.addProperty("TitleVisible");

    LinearBargraph.addProperty("TrackVisible");
    LinearBargraph.addProperty("TrackStart");
    LinearBargraph.addProperty("TrackStartColor");
    LinearBargraph.addProperty("TrackSection");
    LinearBargraph.addProperty("TrackSectionColor");
    LinearBargraph.addProperty("TrackStop");
    LinearBargraph.addProperty("TrackStopColor");
    LinearBargraph.addProperty("UnitString");
    LinearBargraph.setSize(150, 350);

    //    WidgetInfo Altimeter = new WidgetInfo(eu.hansolo.steelseries.extras.Altimeter.class);
    //    Altimeter.setName("Altimeter"); 
    //    Altimeter.addProperty("Value");
    //    setDefaults(Altimeter);
    //    addAbstractRadial(Altimeter);
    //    Altimeter.setSize(230, 230);

    // Commented Because: No digital option.
    //    WidgetInfo Clock = new WidgetInfo(eu.hansolo.steelseries.extras.Clock.class);
    //    Clock.setName("Clock"); 
    //    Clock.addProperty("Value");
    //    Clock.addProperty("Automatic");
    //    setDefaults(Clock);
    //    addAbstractRadial(Clock);
    //    Clock.setSize(230, 230);
    //    
    WidgetInfo compass = new WidgetInfo(eu.hansolo.steelseries.extras.Compass.class);
    compass.addProperty("Value");
    addAndsetDefaults(compass,"Compass");
    addAbstractRadial(compass, false);
    compass.setSize(230, 230);

    //    WidgetInfo DigitalRadial = new WidgetInfo(eu.hansolo.steelseries.gauges.DigitalRadial.class);
    //    DigitalRadial.setName("DigitalRadial"); 
    //    DigitalRadial.addProperty("Value");
    //    setDefaults(DigitalRadial);
    //    addAbstractRadial(DigitalRadial);
    //    DigitalRadial.setSize(230, 230);

    // Commented: DigitalSingle is suficient
    //    WidgetInfo DisplayCircular = new WidgetInfo(eu.hansolo.steelseries.gauges.DisplayCircular.class);
    //    DisplayCircular.setName("DisplayCircular"); 
    //    DisplayCircular.addProperty("Value");
    //    setDefaults(DisplayCircular);
    //    addAbstractRadial(DisplayCircular);
    //    DisplayCircular.setSize(230, 230);

    WidgetInfo horizon = new WidgetInfo(eu.hansolo.steelseries.extras.Horizon.class);
    horizon.addProperty("Roll");
    horizon.addProperty("Pitch");
    addAndsetDefaults(horizon,"Horizon");
    addAbstractRadial(horizon, false);
    horizon.setSize(230, 230);


    // See demo: https://www.youtube.com/watch?v=Iz-MOatYEkY
    WidgetInfo level = new WidgetInfo(eu.hansolo.steelseries.extras.Level.class);
    level.addProperty("Value");
    level.addProperty("TextOrientationFixed");
    level.addProperty("DecimalVisible");
    addAndsetDefaults(level, "Level");
    addAbstractRadial(level, true);
    level.setSize(230, 230);

    //    WidgetInfo Radar = new WidgetInfo(eu.hansolo.steelseries.extras.Radar.class);
    //    Radar.setName("Radar"); 
    //    Radar.addProperty("Value");
    //    setDefaults(Radar);
    //    addAbstractRadial(Radar);
    //    Radar.setSize(230, 230);

    WidgetInfo radial = new WidgetInfo(eu.hansolo.steelseries.gauges.Radial.class);
    radial.addProperty("Value");
    addAndsetDefaults(radial, "Radial");
    addAbstractRadial(radial, true);
    radial.setSize(230, 230);

    //    WidgetInfo Radial1Square = new WidgetInfo(eu.hansolo.steelseries.gauges.Radial1Square.class);
    //    Radial1Square.setName("Radial1Square"); 
    //    Radial1Square.addProperty("Value");
    //    setDefaults(Radial1Square);
    //    addAbstractRadial(Radial1Square);
    //    Radial1Square.setSize(230, 230);

    WidgetInfo radial1Vertical = new WidgetInfo(eu.hansolo.steelseries.gauges.Radial1Vertical.class);
    radial1Vertical.addProperty("Value");
    addAndsetDefaults(radial1Vertical, "Radial1Vertical");
    addAbstractRadial(radial1Vertical, true);
    radial1Vertical.setSize(230, 230);


    //    
    //    WidgetInfo Radial2Top = new WidgetInfo(eu.hansolo.steelseries.gauges.Radial2Top.class);
    //    Radial2Top.setName("Radial2Top"); 
    //    Radial2Top.addProperty("Value");
    //    setDefaults(Radial2Top);
    //    addAbstractRadial(Radial2Top);
    //    Radial2Top.setSize(230, 230);

    WidgetInfo radialCounter = new WidgetInfo(eu.hansolo.steelseries.gauges.RadialCounter.class);
    radialCounter.addProperty("Value");
    addAndsetDefaults(radialCounter, "RadialCounter");
    addAbstractRadial(radialCounter, true);
    radialCounter.setSize(230, 230);

    // Comented becouse: Bugged
    //    WidgetInfo StopWatch = new WidgetInfo(eu.hansolo.steelseries.extras.StopWatch.class);
    //    StopWatch.setName("StopWatch"); 
    //    StopWatch.addProperty("Value");
    //    StopWatch.addProperty("Running");
    //    setDefaults(StopWatch);
    //    addAbstractRadial(StopWatch);
    //    StopWatch.setSize(230, 230);

    //    WidgetInfo WindDirection = new WidgetInfo(eu.hansolo.steelseries.extras.WindDirection.class);
    //    WindDirection.setName("WindDirection"); 
    //    WindDirection.addProperty("Value");
    //    WindDirection.addProperty("Value2");
    //    setDefaults(WindDirection);
    //    addAbstractRadial(WindDirection);
    //    WindDirection.setSize(230, 230);


    WidgetInfo airCompass = new WidgetInfo(AirCompass.class);
    airCompass.setName("Air Compass");
    airCompass.addProperty("Value");
    airCompass.addProperty("RotateTickmarks");
    addAndsetDefaults(airCompass, "Air Compass");
    airCompass.addProperty("BackgroundColor");
    airCompass.addProperty("BackgroundVisible");
    airCompass.addProperty("ForegroundType");
    airCompass.addProperty("ForegroundVisible");
    airCompass.addProperty("FrameDesign");
    airCompass.addProperty("FrameEffect");
    airCompass.addProperty("FrameType");
    airCompass.addProperty("FrameVisible");
    airCompass.setSize(230, 230);

    // LcdValueFont, LcdUnitFon, LcdInfoString, LcdInfoFont
  }

  private void addAbstractRadial(WidgetInfo info, boolean lcd) {

    //    RadialBargraph bargraph = new RadialBargraph();
    //    bargraph.setInnerFrameColor(INNER_FRAME_COLOR);

    info.addProperty("Title");
    info.addProperty("UnitString");
    info.addProperty("MinValue");
    info.addProperty("MaxValue");


//    info.addProperty("ValueCoupled");
    //    info.addProperty("AutoResetToZero");
    info.addProperty("BackgroundColor");
    info.addProperty("BackgroundVisible");
    //    info.addProperty("CustomLayer");
    //    info.addProperty("CustomLayerVisible");
    //    info.addProperty("CustomLcdUnitFont");
    //    info.addProperty("CustomLcdUnitFontEnabled");
    //    info.addProperty("CustomLedColor");
    //    info.addProperty("CustomPointerColor");
    //    info.addProperty("Design1");
    //    info.addProperty("Design2");
    info.addProperty("DigitalFont");
    info.addProperty("ForegroundType");
    info.addProperty("ForegroundVisible");
    //    info.addProperty("FrameBaseColor");
    //    info.addProperty("FrameBaseColorEnabled");
    info.addProperty("FrameDesign");
    info.addProperty("FrameEffect");
    info.addProperty("FrameType");
    info.addProperty("FrameVisible");
    info.addProperty("GaugeType");
    
//    info.addProperty("GlowColor");
//    info.addProperty("GlowPulsating");
//    info.addProperty("GlowVisible");
//    info.addProperty("Glowing");

    info.addProperty("KnobStyle");
    info.addProperty("KnobType");
    info.addProperty("LabelColor");
    info.addProperty("LabelColorFromThemeEnabled");
    info.addProperty("LabelNumberFormat").setValue("value", NumberFormat.STANDARD);
    
    if(lcd) {
      info.addProperty("LcdBackgroundVisible");
      info.addProperty("LcdBlinking");
      info.addProperty("LcdColor");
      info.addProperty("LcdDecimals");
      info.addProperty("LcdInfoFont");
      info.addProperty("LcdInfoString");
      info.addProperty("LcdNumberSystem");
      info.addProperty("LcdScientificFormat");
      //    info.addProperty("LcdTextVisible");
      info.addProperty("LcdThreshold");
      info.addProperty("LcdThresholdBehaviourInverted");
      info.addProperty("LcdThresholdVisible");
      info.addProperty("LcdUnitFont");
      info.addProperty("LcdUnitString");
      info.addProperty("LcdUnitStringVisible");
      info.addProperty("LcdValue");
      //    info.addProperty("LcdValueAnimated");
      info.addProperty("LcdValueFont");
      info.addProperty("LcdVisible");
    }
    
    info.addProperty("LedBlinking");
    info.addProperty("LedColor");
    info.addProperty("LedPosition");
    info.addProperty("LedVisible");
    
//    info.addProperty("LogScale");
    info.addProperty("MajorTickSpacing");
    info.addProperty("MajorTickmarkType");
    info.addProperty("MajorTickmarkVisible");
    info.addProperty("MinMeasuredValueVisible");
    info.addProperty("MaxMeasuredValueVisible");
    info.addProperty("MaxNoOfMajorTicks");
    info.addProperty("MaxNoOfMinorTicks");
    info.addProperty("MinorTickSpacing");
    info.addProperty("MinorTickmarkType");
    info.addProperty("MinorTickmarkVisible");
    info.addProperty("NiceScale");
    info.addProperty("Orientation");
    info.addProperty("OuterFrameColor");
    info.addProperty("PeakValue");
    //    info.addProperty("PeakValueEnabled");
    info.addProperty("PeakValueVisible");
    info.addProperty("PointerColor");
    info.addProperty("PointerShadowVisible");
    info.addProperty("PointerType");
    info.addProperty("PostsVisible");
    info.addProperty("RangeOfMeasuredValuesVisible");
    info.addProperty("TextureColor");
    info.addProperty("Threshold");
    info.addProperty("ThresholdBehaviourInverted");
    info.addProperty("ThresholdColor");
    info.addProperty("ThresholdType");
    info.addProperty("ThresholdVisible");
    info.addProperty("TicklabelOrientation");
    info.addProperty("TicklabelsVisible");
    info.addProperty("TickmarkColor");
    info.addProperty("TickmarkColorFromThemeEnabled");
    info.addProperty("TickmarkDirection");
    info.addProperty("TickmarksVisible");

    info.addProperty("TitleAndUnitFont");
    info.addProperty("TitleAndUnitFontEnabled");
    info.addProperty("TrackSection");
    info.addProperty("TrackSectionColor");
    info.addProperty("TrackStart");
    info.addProperty("TrackStartColor");
    info.addProperty("TrackStop");
    info.addProperty("TrackStopColor");
    info.addProperty("TrackVisible");
    info.addProperty("TransparentAreasEnabled");
    info.addProperty("TransparentSectionsEnabled");

    //    info.addProperty("UserLedBlinking");
    //    info.addProperty("UserLedColor");
    //    info.addProperty("UserLedOn");
    //    info.addProperty("UserLedPosition");
    //    info.addProperty("UserLedVisible");
    //    info.addProperty("VerticalAlignment");
  }
  
  @Override
  public JComponent createForDesigner(WidgetInfo info, Point point) throws Exception {

//    // FIX: Allow set default NumberFormat
//    // Not working to set using "addProperty and setValue"
    JComponent component = super.createForDesigner(info, point);
//    if(component instanceof AbstractGauge) {
//      ((AbstractGauge) component).setLabelNumberFormat(NumberFormat.STANDARD);
//    }
    
    
    if(component instanceof SparkLine) {
      
      SparkLine line = (SparkLine) component;
      
      ((SparkLine) component).setTimeFrame(100);
      
      Random random = new Random();
      
      for (int i = 0; i < 100; i++) {
        line.addDataPoint(random.nextInt(100));
      }
      
    }
    
    return component;
  }


}
