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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.beans.PropertyEditorManager;
import java.io.File;

import jiconfont.IconCode;

public class PropertyEditorManagerInitializer {

  public static void init() {

    // Java Default

    PropertyEditorManager.registerEditor(boolean.class, BooleanAsCheckBoxPropertyEditor.class);
    PropertyEditorManager.registerEditor(Boolean.class, BooleanAsCheckBoxPropertyEditor.class);

    PropertyEditorManager.registerEditor(int.class, IntegerPropertyEditor.class);
    PropertyEditorManager.registerEditor(Integer.class, IntegerPropertyEditor.class);

    PropertyEditorManager.registerEditor(long.class, LongPropertyEditor.class);
    PropertyEditorManager.registerEditor(Long.class, LongPropertyEditor.class);

    PropertyEditorManager.registerEditor(float.class, FloatPropertyEditor.class);
    PropertyEditorManager.registerEditor(Float.class, FloatPropertyEditor.class);

    PropertyEditorManager.registerEditor(double.class, DoublePropertyEditor.class);
    PropertyEditorManager.registerEditor(Double.class, DoublePropertyEditor.class);

    PropertyEditorManager.registerEditor(Color.class, ColorPropertyEditor.class);
    PropertyEditorManager.registerEditor(Dimension.class, DimensionPropertyEditor.class);
    PropertyEditorManager.registerEditor(Point.class, PointPropertyEditor.class);

    PropertyEditorManager.registerEditor(File.class, FilePropertyEditor.class);

    // Custom

    PropertyEditorManager.registerEditor(IconCode.class, IconCodePropertyEditor.class);
  }
}
