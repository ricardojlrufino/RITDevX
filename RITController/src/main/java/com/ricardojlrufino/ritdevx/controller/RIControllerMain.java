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
package com.ricardojlrufino.ritdevx.controller;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import com.ricardojlrufino.ritdevx.controller.configuration.HmiConfig;

/**
 * Standalone laucher for HMI Interface.
 * 
 * @author Ricardo JL Rufino - (ricardo.jl.rufino@gmail.com)
 * @date 5 de jun de 2020
 */
public class RIControllerMain {

  public static void main(String[] args) throws IOException {
    
    // Set LookAndFeel ...
    try {
      for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
        if ("Nimbus".equals(info.getName())) {
          UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }
    } catch (Exception e) {
      try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } catch (Exception ex) {

      }
    }

    RIController.configureLog4j();

//    File selectedFile = new File("/media/ricardo/Dados/Workspace/Arduino/RITDevX-Project/RITDesigner/src/assembly/release/examples/DemoKeyboard/interface.ritd");
    
    File selectedFile = null;
    
    if(args.length > 0) {
      
      selectedFile = new File(args[0]);
    
    }else {
      
      JFileChooser jfc = new JFileChooser();
      jfc.setDialogTitle("Open");
      jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
      
      if (JFileChooser.APPROVE_OPTION == jfc.showOpenDialog(null)) {
        selectedFile = jfc.getSelectedFile();
      }
      
    }
    
    HmiConfig hmiConfig = HmiConfig.load(selectedFile);

    RIController controller = new RIController(hmiConfig);
    controller.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

  }
}
