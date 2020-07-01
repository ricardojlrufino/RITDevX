package com.ricardojlrufino.ritdevx.controller.widgets.steelseries;

import eu.hansolo.steelseries.gauges.LinearBargraph;

public class LinearBargraphImpl extends LinearBargraph {
  
  private boolean minMeasuredValueVisible;
  
  public LinearBargraphImpl() {
    setMinMeasuredValueVisible(false);
  }
  
  @Override
  public void setMinMeasuredValueVisible(boolean MIN_MEASURED_VALUE_VISIBLE) {
    this.minMeasuredValueVisible = MIN_MEASURED_VALUE_VISIBLE;
    super.setMinMeasuredValueVisible(MIN_MEASURED_VALUE_VISIBLE);
  }
  
  public boolean getMinMeasuredValueVisible() {
    return minMeasuredValueVisible;
  }

}
