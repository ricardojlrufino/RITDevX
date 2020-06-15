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

    if ("int".equals(className) || "java.lang.Integer".equals(className)) {
      int x = -1;
      try {
        x = Integer.parseInt(valueStr);
      } catch (Exception e) {
        return null;
      }
      newValue = new Integer(x);
    } else if ("short".equals(className) || "java.lang.Short".equals(className)) {
      short x = -1;
      try {
        x = Short.parseShort(valueStr);
      } catch (Exception e) {
        return null;
      }
      newValue = new Short(x);

    } else if ("char".equals(className) || "java.lang.Character".equals(className)) {

      if (valueStr.length() != 1) {
        return null;
      }

      newValue = new Character(valueStr.charAt(0));

    } else if ("long".equals(className) || "java.lang.Long".equals(className)) {
      long x = -1;
      try {
        x = Long.parseLong(valueStr);
      } catch (Exception e) {
        return null;
      }
      newValue = new Long(x);
    } else if (("float".equals(className) || className.equals("java.lang.Float"))) {
      float x = -1;
      try {
        x = Float.parseFloat(valueStr);
      } catch (Exception e) {
        return null;
      }
      newValue = new Float(x);
    } else if ("double".equals(className) || "java.lang.Double".equals(className)) {
      double x = -1;
      try {
        x = Double.parseDouble(valueStr);
      } catch (Exception e) {
        return null;
      }
      newValue = new Double(x);
    } else if ("boolean".equals(className) || "java.lang.Boolean".equals(className)) {
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
        if ("true".equalsIgnoreCase(valueStr) || "on".equals(valueStr)) {
          newValue = Boolean.TRUE;
        } else if ("false".equalsIgnoreCase(valueStr)) {
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
