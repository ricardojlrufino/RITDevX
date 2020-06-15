/*******************************************************************************
 * This file is part of RITDevX Designer project.
 * Copyright (c) 2020 Ricardo JL Rufino and others stated in this file.
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
package com.ricardojlrufino.ritdevx.designer.propeditor;

import java.awt.Point;

public class PointPropertyEditor extends StringPropertyEditor {

  @Override
  protected Object convertFromString(String text) {
    String[] split = text.split(",");
    return new Point(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
  }

  @Override
  protected String convertToString(Object value) {
    Point point = (Point) value;
    return point.x + "," + point.y;
  }

}
