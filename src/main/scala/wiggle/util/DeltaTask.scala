//
// $Id$
//
// Wiggle - a 2D game development library
// Copyright 2008-2010 Michael Bayne
// Distributed under the "Simplified BSD License" in LICENSE.txt

package wiggle.util

/**
 * A task that handles calculation of elapsed time.
 */
abstract class DeltaTask extends Task
{
  /**
   * Called on every tick with the amount of time that has elapsed since the previous tick.
   */
  def deltaTick (dt :Float) :Boolean

  override def init (time :Float) {
    _last = time;
  }

  override def tick (time :Float) = {
    val result = deltaTick(time - _last)
    _last = time
    result
  }

  private[this] var _last :Float = _;
}
