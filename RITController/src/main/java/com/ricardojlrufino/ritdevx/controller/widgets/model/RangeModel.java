package com.ricardojlrufino.ritdevx.controller.widgets.model;

public interface RangeModel<T> {
  /**
   * Returns the minimum acceptable value.
   *
   * @return the value of the minimum property
   * @see #setMinimum
   */
  T getMinimum();


  /**
   * Sets the model's minimum to <I>newMinimum</I>.   The
   * other three properties may be changed as well, to ensure
   * that:
   * <pre>
   * minimum &lt;= value &lt;= value+extent &lt;= maximum
   * </pre>
   * <p>
   * Notifies any listeners if the model changes.
   *
   * @param newMinimum the model's new minimum
   * @see #getMinimum
   * @see #addChangeListener
   */
  void setMinimum(T newMinimum);


  /**
   * Returns the model's maximum.  Note that the upper
   * limit on the model's value is (maximum - extent).
   *
   * @return the value of the maximum property.
   * @see #setMaximum
   * @see #setExtent
   */
  T getMaximum();


  /**
   * Sets the model's maximum to <I>newMaximum</I>. The other
   * three properties may be changed as well, to ensure that
   * <pre>
   * minimum &lt;= value &lt;= value+extent &lt;= maximum
   * </pre>
   * <p>
   * Notifies any listeners if the model changes.
   *
   * @param newMaximum the model's new maximum
   * @see #getMaximum
   * @see #addChangeListener
   */
  void setMaximum(T newMaximum);


  /**
   * Returns the model's current value.  Note that the upper
   * limit on the model's value is <code>maximum - extent</code>
   * and the lower limit is <code>minimum</code>.
   *
   * @return  the model's value
   * @see     #setValue
   */
  T getValue();


  /**
   * Sets the model's current value to <code>newValue</code> if <code>newValue</code>
   * satisfies the model's constraints. Those constraints are:
   * <pre>
   * minimum &lt;= value &lt;= value+extent &lt;= maximum
   * </pre>
   * Otherwise, if <code>newValue</code> is less than <code>minimum</code>
   * it's set to <code>minimum</code>, if its greater than
   * <code>maximum</code> then it's set to <code>maximum</code>, and
   * if it's greater than <code>value+extent</code> then it's set to
   * <code>value+extent</code>.
   * <p>
   * When a BoundedRange model is used with a scrollbar the value
   * specifies the origin of the scrollbar knob (aka the "thumb" or
   * "elevator").  The value usually represents the origin of the
   * visible part of the object being scrolled.
   * <p>
   * Notifies any listeners if the model changes.
   *
   * @param newValue the model's new value
   * @see #getValue
   */
  void setValue(T newValue);

}
