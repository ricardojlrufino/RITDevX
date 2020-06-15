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
package com.ricardojlrufino.ritdevx.controller.configuration.jaxb;

import java.util.HashMap;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class KeyValueAdapter extends XmlAdapter<Element, KeyValue> {
  private ClassLoader classLoader;
  private DocumentBuilder documentBuilder;
  private JAXBContext jaxbContext;

  private HashMap<Class, CustomSerializer> serializers = new HashMap<>();

  public KeyValueAdapter() {
    classLoader = Thread.currentThread().getContextClassLoader();
  }

  public KeyValueAdapter(JAXBContext jaxbContext) {
    this();
    this.jaxbContext = jaxbContext;
  }

  public <T> void addSerializer(Class<T> key, CustomSerializer<T> value) {
    serializers.put(key, value);
  }

  private DocumentBuilder getDocumentBuilder() throws Exception {
    // Lazy load the DocumentBuilder as it is not used for unmarshalling.
    if (null == documentBuilder) {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      documentBuilder = dbf.newDocumentBuilder();
    }
    return documentBuilder;
  }

  private JAXBContext getJAXBContext(Class<?> type) throws Exception {
    if (null == jaxbContext) {
      // A JAXBContext was not set, so create a new one based  on the type.
      return JAXBContext.newInstance(type);
    }
    return jaxbContext;
  }

  @Override
  public Element marshal(KeyValue parameter) throws Exception {
    if (null == parameter) {
      return null;
    }

    Object value = parameter.getValue();

    if (value == null)
      return null;

    Class type = value.getClass();

    // Custom serializers..
    CustomSerializer customSerializer = serializers.get(type);
    if (customSerializer != null) {
      value = customSerializer.serialize(value);
    }

    // 1. Build the JAXBElement to wrap the instance of Parameter.
    QName rootElement = new QName(parameter.getName());
    JAXBElement jaxbElement = new JAXBElement(rootElement, type, value);
    // 2.  Marshal the JAXBElement to a DOM element.
    Document document = getDocumentBuilder().newDocument();
    Marshaller marshaller = getJAXBContext(type).createMarshaller();
    marshaller.marshal(jaxbElement, document);
    Element element = document.getDocumentElement();

    // 3.  Set the type attribute based on the value's type.
    element.setAttribute("type", type.getName());
    return element;
  }

  @Override
  public KeyValue unmarshal(Element element) throws Exception {
    if (null == element) {
      return null;
    }
    // 1. Determine the values type from the type attribute.
    Class<?> type = classLoader.loadClass(element.getAttribute("type"));

    // Custom serializers..
    CustomSerializer customSerializer = serializers.get(type);
    if (customSerializer != null) {
      Object value = customSerializer.deserialize(element.getTextContent());
      KeyValue parameter = new KeyValue();
      parameter.setName(element.getLocalName());
      parameter.setValue(value);
      return parameter;
    } else {
      // 2. Unmarshal the element based on the value's type.
      DOMSource source = new DOMSource(element);
      Unmarshaller unmarshaller = getJAXBContext(type).createUnmarshaller();
      JAXBElement jaxbElement = unmarshaller.unmarshal(source, type);
      // 3. Build the instance of Parameter
      KeyValue parameter = new KeyValue();
      parameter.setName(element.getLocalName());
      parameter.setValue(jaxbElement.getValue());
      return parameter;
    }

  }


}
