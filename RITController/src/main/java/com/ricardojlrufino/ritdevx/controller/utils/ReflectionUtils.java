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
package com.ricardojlrufino.ritdevx.controller.utils;

/**
 * ReflectionUtils
 * @author Ricardo JL Rufino - (ricardo.jl.rufino@gmail.com)
 * @date 13 de jun de 2020
 */
public interface ReflectionUtils {

  public static Object tryToConvert(Object value, Class targetType, boolean tryNumber) {

    String valueStr = null;

    if (value instanceof String) {

      valueStr = (String) value;

    } else if (tryNumber && value instanceof Number) {

      valueStr = value.toString();

    } else {

      return null;
    }

    Object newValue = null;

    String className = targetType.getName();

    if (className.equals("int") || className.equals("java.lang.Integer")) {
      int x = -1;
      try {
        x = Integer.parseInt(valueStr);
      } catch (Exception e) {
        return null;
      }
      newValue = new Integer(x);
    } else if (className.equals("short") || className.equals("java.lang.Short")) {
      short x = -1;
      try {
        x = Short.parseShort(valueStr);
      } catch (Exception e) {
        return null;
      }
      newValue = new Short(x);

    } else if (className.equals("char") || className.equals("java.lang.Character")) {

      if (valueStr.length() != 1) {
        return null;
      }

      newValue = new Character(valueStr.charAt(0));

    } else if (className.equals("long") || className.equals("java.lang.Long")) {
      long x = -1;
      try {
        x = Long.parseLong(valueStr);
      } catch (Exception e) {
        return null;
      }
      newValue = new Long(x);
    } else if (className.equals("float") || className.equals("java.lang.Float")) {
      float x = -1;
      try {
        x = Float.parseFloat(valueStr);
      } catch (Exception e) {
        return null;
      }
      newValue = new Float(x);
    } else if (className.equals("double") || className.equals("java.lang.Double")) {
      double x = -1;
      try {
        x = Double.parseDouble(valueStr);
      } catch (Exception e) {
        return null;
      }
      newValue = new Double(x);
    } else if (className.equals("boolean") || className.equals("java.lang.Boolean")) {
      try {
        int x = Integer.parseInt(valueStr);
        if (x == 1) {
          newValue = Boolean.TRUE;
        } else if (x == 0) {
          newValue = Boolean.FALSE;
        } else {
          return null;
        }
      } catch (Exception e) {
        if (valueStr.equalsIgnoreCase("true") || valueStr.equals("on")) {
          newValue = Boolean.TRUE;
        } else if (valueStr.equalsIgnoreCase("false")) {
          newValue = Boolean.FALSE;
        } else {
          return null;
        }
      }
    } else if (targetType.isEnum()) {

      try {

        newValue = Enum.valueOf(targetType, valueStr);

      } catch (Exception e) {

        return null;
      }

    }

    return newValue;

  }

}
