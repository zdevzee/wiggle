//
// $Id$

package wiggle.util

import wiggle.sim.Task

/**
 * Represents a floating point value. Can be operated on by tasks to effect changes to the position,
 * scale, etc. of elements.
 */
class Value (start :Float)
{
  def get = _value

  def set (value :Float) {
    _value = value
  }

  def toFloat = _value

  def easeIn (toValue :Float, duration :Float) = interp(Interpolator.easeIn, toValue, duration)

  def easeOut (toValue :Float, duration :Float) = interp(Interpolator.easeOut, toValue, duration)

  def easeInOut (toValue :Float, duration :Float) = interp(Interpolator.easeInOut, toValue, duration)

  def interp (interp :Interpolator, toValue :Float, duration :Float) = new Task {
    def init (time :Float) {
      _t0 = time
      _start = _value
      _range = toValue - _start
    }

    def tick (time :Float) = {
      val dt = time - _t0;
      if (dt < duration) {
        _value = interp(_start, _range, dt, duration); false
      } else {
        _value = toValue; true
      }
    }

    protected var _t0 = 0f
    protected var _start = 0f
    protected var _range = 0f
  }

  override def toString = _value.toString

  protected var _value :Float = start
}
