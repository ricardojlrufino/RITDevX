/*******************************************************************************
 * This file is part of RITDevX Controller project. Copyright (c) 2020 Ricardo JL Rufino.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, version 3 with Classpath
 * Exception.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. See
 * the included LICENSE file for details.
 *******************************************************************************/
package com.ricardojlrufino.ritdevx.controller.widgets.skinnable;

import com.ricardojlrufino.ritdevx.controller.widgets.ButtonInterface;
import com.ricardojlrufino.ritdevx.controller.widgets.model.OperationMode;

/**
 * This component uses two images to represent the ON and OFF states (optional). <br/>
 * It can be used as a normal image, it can be used as a <b>button</b> (responding to clicks) and it
 * can act as a <b>sensor</b>. It also has an optional 'CustomCmd' attribute, which identifies which
 * command should be sent when it is pressed <br/>
 * 
 * @author Ricardo JL Rufino - (ricardo.jl.rufino@gmail.com)
 * @date 11 de jun de 2020
 */
public class ImageButton extends ImageState implements ButtonInterface {

  private static final long serialVersionUID = 1L;

  private OperationMode operationMode = OperationMode.SWITCH;

  private String customCmd;



  //===================================
  // OnOffInterface
  // ===================================

  @Override
  public String getCustomCmd() {
    return customCmd;
  }

  @Override
  public void setCustomCmd(String customCmd) {
    this.customCmd = customCmd;
  }

  @Override
  public boolean hasCustomCmd() {
    return customCmd != null;
  }

  public OperationMode getOperationMode() {
    return operationMode;
  }

  public void setOperationMode(OperationMode mode) {
    this.operationMode = mode;
  }

}
