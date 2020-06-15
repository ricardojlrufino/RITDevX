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

import java.awt.Dimension;

public class DimensionSerializer implements CustomSerializer<Dimension> {

  @Override
  public String serialize(Dimension value) {
    return ((int) value.getWidth()) + "," + ((int) value.getHeight());
  }

  @Override
  public Dimension deserialize(String value) {
    String[] values = value.split(",");
    return new Dimension(Integer.parseInt(values[0]), Integer.parseInt(values[1]));
  }

}
