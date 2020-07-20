package com.ricardojlrufino.ritdevx.controller.widgets.model;

public class DoubleRangeModel implements RangeModel<Double> {

  private Double minimum = new Double(0);
  private Double maximum = new Double(0);
  private Double value = new Double(0);

  public DoubleRangeModel(double value, double min, double max) {
    if ((max >= min) &&
        (value >= min)) {
      this.value = value;
      this.minimum = min;
      this.maximum = max;
    } else {
      throw new IllegalArgumentException("invalid range properties");
    }
  }

  public Double getMinimum() {
    return minimum;
  }

  public void setMinimum(Double minimum) {
    this.minimum = minimum;
  }

  public Double getMaximum() {
    return maximum;
  }

  public void setMaximum(Double maximum) {
    this.maximum = maximum;
  }

  public Double getValue() {
    return value;
  }

  public void setValue(Double value) {
    this.value = value;
  }



}
