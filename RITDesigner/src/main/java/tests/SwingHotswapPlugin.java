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
package tests;

import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.hotswap.agent.annotation.Init;
import org.hotswap.agent.annotation.LoadEvent;
import org.hotswap.agent.annotation.OnClassLoadEvent;
import org.hotswap.agent.annotation.Plugin;
import org.hotswap.agent.command.Scheduler;
import org.hotswap.agent.config.PluginManager;
import org.hotswap.agent.javassist.CtClass;
import org.hotswap.agent.javassist.NotFoundException;
import org.hotswap.agent.logging.AgentLogger;
import org.hotswap.agent.watch.Watcher;

import com.ricardojlrufino.ritdevx.designer.RIDesigner;

/**
 * Log4j2 configuration file reload.
 *
 * @author Lukasz Warzecha
 */
@Plugin(name = "Swing", description = "Swing configuration reload.", testedVersions = { "2.1", "2.5", "2.7" })
public class SwingHotswapPlugin {

  private static final AgentLogger LOGGER = AgentLogger.getLogger(SwingHotswapPlugin.class);

  @Init
  Watcher watcher;

  @Init
  ClassLoader appClassLoader;

  // ensure uri registered only once
  Set<URI> registeredURIs = new HashSet<>();

  volatile boolean initialized;

  RIDesigner frame;

  private static Class targetClass;

  /**
   * Application should create and inicialize plugin instance
   *
   * @param frame 
   */
  public SwingHotswapPlugin(RIDesigner frame) {
    this.frame = frame;
    this.targetClass = frame.getClass();
  }

  /**
   * Hotswap Agent scheduling service, see javadoc for usage. See @{@link Init} annotation javadoc
   * for list of supported services.
   */
  @Init
  Scheduler scheduler;

  /**
   * Call framework class HelloWorldService, typical usage is invalidate caches.
   */
  @OnClassLoadEvent(classNameRegexp = ".*", events = LoadEvent.REDEFINE)
  public void reloadClass(CtClass clazz) throws NotFoundException {

    System.out.println("SwingPlugin reload: " + clazz);

    //        if (isTargetClass(clazz, targetClass)) {
    //            
    //            System.out.println("reload [OK]: " + clazz);
    //            
    //            if(frame != null) {
    //                // use scheduler to run the refresh AFTER the class is replaced in classloader
    //                scheduler.scheduleCommand(() -> frame.hotswapTest());
    //            }
    //        }
    //        if (isTargetClass(clazz, WidgetSelectorPanel.class)) {

    System.out.println("frame.hotswapTest [OK]: " + clazz);

    if (frame != null) {
      // use scheduler to run the refresh AFTER the class is replaced in classloader
      scheduler.scheduleCommand(() -> frame.hotswapTest());
    }
    //        }

    //            System.out.println("set theme... [OK]: " + clazz);
    //            
    //            if(docks != null) {
    //                // use scheduler to run the refresh AFTER the class is replaced in classloader
    //                scheduler.scheduleCommand(() -> docks.reloadTheme());
    //            }

  }

  private boolean isTargetClass(CtClass clazz, Class<?> targetClass) throws NotFoundException {

    if (clazz.getName().equals(targetClass.getName())) {
      return true;
    }

    return Arrays.stream(clazz.getInterfaces()).anyMatch(i -> i.getName().equals(targetClass.getName()));
  }

  /**
   * Plugin should be started from a application framework class on startup.
   */
  public static void register(RIDesigner frame) {
    targetClass = frame.getClass();
    PluginManager.getInstance().getPluginRegistry().initializePluginInstance(new SwingHotswapPlugin(frame));
  }

}