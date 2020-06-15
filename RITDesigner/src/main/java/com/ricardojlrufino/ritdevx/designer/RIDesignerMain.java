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
package com.ricardojlrufino.ritdevx.designer;

import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;

import javax.swing.UIDefaults;
import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.formdev.flatlaf.FlatDarkLaf;
import com.ricardojlrufino.ritdevx.controller.RIController;

import tests.SwingHotswapPlugin;

/**
 * Starts {@link RIDesigner} from JAR/CmdLine
 * @author Ricardo JL Rufino - (ricardo.jl.rufino@gmail.com)
 * @date 13 de jun de 2020
 */
public class RIDesignerMain {

  private static Logger log;

  public static void main(String[] args) throws IOException {
    
    
    try {
      FlatDarkLaf.install();
      UIDefaults defaults = UIManager.getDefaults();
      defaults.put("Table.showVerticalLines", Boolean.TRUE);
      defaults.put("Table.showHorizontalLines", Boolean.TRUE);
      defaults.put("Table.intercellSpacing", new Dimension(1, 1));
//      defaults.put("Table.gridColor", new Color(0xd9d9d9));
    } catch (Exception e) {
    }

//    try {
//      for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
//        System.out.println(info.getName());
//        if ("Flat".equals(info.getName())) {
//          UIManager.setLookAndFeel(info.getClassName());
//          break;
//        }
//      }
//    } catch (Exception e) {
//      try {
//        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//      } catch (Exception ex) {
//         ex.printStackTrace();
//      }
//    }

    RIController.configureLog4j();

    log = LoggerFactory.getLogger(RIDesignerMain.class);

    log.info("Starting...");

    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        RIDesigner hmiDesigner = new RIDesigner();
        SwingHotswapPlugin.register(hmiDesigner);
      }
    });

  }

}
