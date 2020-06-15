//package com.ricardojlrufino.rhmitool.controller.configuration;
//
//import static org.junit.Assert.assertEquals;
//
//import java.awt.Dimension;
//import java.awt.Point;
//import java.io.File;
//import java.io.IOException;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.List;
//
//import javax.xml.bind.JAXBContext;
//import javax.xml.bind.Marshaller;
//import javax.xml.bind.annotation.XmlElement;
//
//import org.junit.Test;
//
//import com.ricardojlrufino.rhmitool.controller.configuration.jaxb.KeyValue;
//
//
//public class HmiConfigTest{
// 
//  @Test
//  public void testSave() throws IOException {
//    
//    HmiConfig config = new HmiConfig();
//    config.setWidgetType("App");
//    
//    List<WidgetConfig> list = new LinkedList<>();
//    
//    WidgetConfig widgetConfig = new WidgetConfig();
//    widgetConfig.setWidgetType("Image");
////    widgetConfig.addValue("Location", new Point(10, 10));
//    widgetConfig.addValue("Date", new Date());
//    widgetConfig.addValue("Value", 1);
//    list.add(widgetConfig);
//    
//    
//    widgetConfig = new WidgetConfig();
//    widgetConfig.setWidgetType("Image2");
//    widgetConfig.addValue("Location", new Point(1, 2));
//    widgetConfig.addValue("Size", new Dimension(100, 200));
//    widgetConfig.addValue("Date", new Date());
//    widgetConfig.addValue("Backgroud", new File("/teste.png"));
//    widgetConfig.addValue("Value", 10);
//    list.add(widgetConfig);
//
//    config.setWidgets(list);
//    
//    File tempFile = File.createTempFile("hiconfig", "rhmi");
//    
//    HmiConfig.save(config, tempFile);
//    
//    
//  }
//  
//  
//  @Test
//  public void testLoad() throws IOException {
//    
//    HmiConfig config = HmiConfig.load(new File(this.getClass().getResource("./config.xml").getFile()));
//    
//    List<WidgetConfig> widgets = config.getWidgets();
//    
//    for (WidgetConfig widgetConfig : widgets) {
//      if("Image2".equals(widgetConfig.getWidgetType())) {
//        List<KeyValue> values = widgetConfig.getValues();
//        HashMap<String,Object> valuesMap = widgetConfig.getValuesMap();
//        assertEquals(5, values.size());
//        
//        Point location = (Point) valuesMap.get("Location");
//        assertEquals(1,location.x);
//        assertEquals(2, location.y);
//        
//        Dimension Size = (Dimension) valuesMap.get("Size");
//        assertEquals(100, Size.width);
//        assertEquals(200, Size.height);
//        
//        System.out.println(values);
//        
//      }
//    }
//    
//    assertEquals(config.getWidgets().size(), 2);
//    
//  }
//
//}
