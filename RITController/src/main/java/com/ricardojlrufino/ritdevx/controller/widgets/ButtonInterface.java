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

import com.ricardojlrufino.ritdevx.controller.widgets.model.OperationMode;

/**
 * Identifies components that can transition between two ON and OFF states. <br/>
 * It also has an optional 'CustomCmd' attribute, which identifies which command should be sent when
 * it is pressed
 * 
 * @author Ricardo JL Rufino - (ricardo.jl.rufino@gmail.com)
 * @date 13 de jun de 2020
 */
public interface ButtonInterface extends OnOffInterface {

  public boolean hasCustomCmd();

  public String getCustomCmd();

  public void setCustomCmd(String customCmd);
  
  public OperationMode getOperationMode();

  public void setOperationMode(OperationMode mode);

}
