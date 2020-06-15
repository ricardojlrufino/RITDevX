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
/*******************************************************************************
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
 * 
 * Retired from: https://github.com/arduino/Arduino/blob/ee1967cd530ceb9a1d638875e385157e90d532e8/app/src/processing/app/helpers/CircularBuffer.java
 *******************************************************************************/
package com.ricardojlrufino.ritdevx.controller.utils;

import java.util.NoSuchElementException;

public class CircularBuffer {

  private final double[] elements;
  private int start = -1;
  private int end = -1;
  private final int capacity;

  public void add(double num) {
    end = (end + 1) % capacity;
    elements[end] = num;
    if (start == end || start == -1) {
      start = (start + 1) % capacity;
    }
  }

  public double get(int index) {
    if (index >= capacity) {
      throw new IndexOutOfBoundsException();
    }
    if (index >= size()) {
      throw new IndexOutOfBoundsException();
    }

    return elements[(start + index) % capacity];
  }

  public boolean isEmpty() {
    return start == -1 && end == -1;
  }

  public CircularBuffer(int capacity) {
    this.capacity = capacity;
    elements = new double[capacity];
  }

  public double min() {
    if (size() == 0) {
      throw new NoSuchElementException();
    }

    double out = get(0);
    for (int i = 1; i < size(); ++i) {
      out = Math.min(out, get(i));
    }

    return out;
  }

  public double max() {
    if (size() == 0) {
      throw new NoSuchElementException();
    }

    double out = get(0);
    for (int i = 1; i < size(); ++i) {
      out = Math.max(out, get(i));
    }

    return out;
  }

  public int size() {
    if (end == -1) {
      return 0;
    }

    return (end - start + capacity) % capacity + 1;
  }

  public int capacity() {
    return capacity;
  }

}
