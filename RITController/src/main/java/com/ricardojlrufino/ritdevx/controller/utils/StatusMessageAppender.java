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

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;
import com.ricardojlrufino.ritdevx.controller.components.LogViewer;

/**
 * Append logs into {@link LogViewer}
 * 
 * @author Ricardo JL Rufino - (ricardo.jl.rufino@gmail.com)
 * @date 13 de jun de 2020
 */
public class StatusMessageAppender extends AppenderSkeleton {
  private final LogViewer logViewer;

  public StatusMessageAppender(LogViewer logViewer) {
    this.logViewer = logViewer;

  }

  protected void append(LoggingEvent event) {
    logViewer.append(getLayout().format(event));

  }

  public void close() {
    // Ignore
  }

  public boolean requiresLayout() {
    return false;
  }
}
