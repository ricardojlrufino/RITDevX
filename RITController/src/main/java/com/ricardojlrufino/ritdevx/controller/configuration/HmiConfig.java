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
package com.ricardojlrufino.ritdevx.controller.configuration;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ricardojlrufino.ritdevx.controller.configuration.jaxb.ColorSerializer;
import com.ricardojlrufino.ritdevx.controller.configuration.jaxb.DimensionSerializer;
import com.ricardojlrufino.ritdevx.controller.configuration.jaxb.FontSerializer;
import com.ricardojlrufino.ritdevx.controller.configuration.jaxb.KeyValueAdapter;
import com.ricardojlrufino.ritdevx.controller.configuration.jaxb.PointSerializer;
import com.ricardojlrufino.ritdevx.controller.widgets.OperationMode;

/**
 * Represents the data saved in the configuration file that describes the HMI / interface
 * 
 * @author Ricardo JL Rufino - (ricardo.jl.rufino@gmail.com)
 * @date 5 de jun de 2020
 */
@XmlRootElement(name = "config")
public class HmiConfig extends WidgetConfig {

  private static final Logger log = LoggerFactory.getLogger(HmiConfig.class);

  public static final String APP_NAME = "App";

  private List<WidgetConfig> widgets = new LinkedList<>();


  private static JAXBContext createJAXBContext() throws JAXBException {
    return JAXBContext.newInstance(
        HmiConfig.class,
        Point.class,
        java.awt.geom.Point2D.Double.class,
        Dimension.class,
        java.awt.Color.class,
        java.awt.Font.class,
        jiconfont.icons.font_awesome.FontAwesome.class,
        jiconfont.icons.typicons.Typicons.class,
        eu.hansolo.steelseries.tools.LedColor.class,
        eu.hansolo.steelseries.tools.LedType.class,
        eu.hansolo.steelseries.tools.ColorDef.class,
        eu.hansolo.steelseries.tools.BackgroundColor.class,
        eu.hansolo.steelseries.tools.CustomColorDef.class,
        eu.hansolo.steelseries.tools.ForegroundType.class,
        eu.hansolo.steelseries.tools.FrameDesign.class,
        eu.hansolo.steelseries.tools.FrameEffect.class,
        eu.hansolo.steelseries.tools.FrameType.class,
        eu.hansolo.steelseries.tools.GaugeType.class,
        eu.hansolo.steelseries.tools.KnobStyle.class,
        eu.hansolo.steelseries.tools.KnobType.class,
        eu.hansolo.steelseries.tools.NumberFormat.class,
        eu.hansolo.steelseries.tools.LcdColor.class,
        eu.hansolo.steelseries.tools.NumberSystem.class,
        eu.hansolo.steelseries.tools.TickmarkType.class,
        eu.hansolo.steelseries.tools.Orientation.class,
        eu.hansolo.steelseries.tools.PointerType.class,
        eu.hansolo.steelseries.tools.ThresholdType.class,
        eu.hansolo.steelseries.tools.TicklabelOrientation.class,
        eu.hansolo.steelseries.tools.Direction.class,
        //
        OperationMode.class);
  }

  private static KeyValueAdapter createKeyValueAdapter(JAXBContext jaxbContext) throws JAXBException {
    KeyValueAdapter adapter = new KeyValueAdapter(jaxbContext);
    adapter.addSerializer(Point.class, new PointSerializer());
    adapter.addSerializer(Dimension.class, new DimensionSerializer());
    adapter.addSerializer(Color.class, new ColorSerializer());
    adapter.addSerializer(Font.class, new FontSerializer());
    return adapter;
  }

  public static HmiConfig load(File file) throws IOException {

    try {

      log.debug("loading config: " + file);
      JAXBContext jaxbContext = createJAXBContext();
      Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
      unmarshaller.setAdapter(createKeyValueAdapter(jaxbContext));

      return (HmiConfig) unmarshaller.unmarshal(file);

    } catch (Exception e) {
      log.error("Fail read file", e);
      throw new IOException("Fail read file", e);
    }

  }

  public static void save(HmiConfig config, File file) throws IOException {
    try {

      config.setName(APP_NAME);
      config.setWidgetType(APP_NAME);

      JAXBContext jaxbContext = createJAXBContext();

      Marshaller marshaller = jaxbContext.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

      marshaller.setAdapter(createKeyValueAdapter(jaxbContext));


      StringWriter stringWriter = new StringWriter();
      marshaller.marshal(config, stringWriter);

      // Remove ugly code generated by JABX because of use KeyValueAdapter
      StringBuffer buffer = stringWriter.getBuffer();
      String configStr = buffer.toString();
      configStr = configStr.replaceAll("xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" ", "");
      configStr = configStr.replaceAll("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"", "");
      configStr = configStr.replaceAll("xsi:type=\"xs:string\" ", "");

      FileOutputStream outputStream = new FileOutputStream(file);
      outputStream.write(configStr.getBytes());
      outputStream.close();

      //      marshaller.marshal(config, System.out);
      //      marshaller.marshal(config, file);

    } catch (Exception e) {
      log.error("Fail to save file", e);
      throw new IOException("Fail to save file", e);
    }

  }

  public void addWidget(WidgetConfig element) {
    widgets.add(element);
  }

  public List<WidgetConfig> getWidgets() {
    return widgets;
  }

  @XmlElement(name = "widgets")
  public void setWidgets(List<WidgetConfig> widgets) {
    this.widgets = widgets;
  }

}
