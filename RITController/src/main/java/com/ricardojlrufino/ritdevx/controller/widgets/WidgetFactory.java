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
package com.ricardojlrufino.ritdevx.controller.widgets;

import java.awt.Point;
import java.util.List;
import javax.swing.JComponent;

/**
 * Interface that defines widget builders, and for initializing them for both Designer and
 * Controller. <br/>
 * Note that you can have multiple factories specializing in creating component families.
 * 
 * @see DefaultWidgetsFactory
 * @author Ricardo JL Rufino - (ricardo.jl.rufino@gmail.com)
 * @date 13 de jun de 2020
 */
public interface WidgetFactory {

  /**
   * Lists all available widgets for this factory
   * 
   * @return
   */
  public List<WidgetInfo> list();

  /**
   * Create instante of {@link WidgetInfo} reated component
   * @param info
   * @param point - drop point location
   */
  public JComponent create(WidgetInfo info, Point point) throws Exception;


}
