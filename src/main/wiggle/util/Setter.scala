//
// $Id$

package wiggle.util

/**
 * A special sequential task that handles the mutation of a single value.
 */
abstract class Setter extends Task
{
  /** Adds a task that eases the value to the specified target in the specified duration. */
  def easeIn (toValue :Float, duration :Float) = interp(Interpolator.easeIn, toValue, duration)

  /** Adds a task that eases the value to the specified target in the specified duration. */
  def easeOut (toValue :Float, duration :Float) = interp(Interpolator.easeOut, toValue, duration)

  /** Adds a task that eases the value to the specified target in the specified duration. */
  def easeInOut (toValue :Float, duration :Float) = interp(Interpolator.easeInOut, toValue, duration)

  /** Adds a task that changes the value to the specified target in the specified duration. */
  def interp (interp :Interpolator, toValue :Float, duration :Float) = {
    _tasks = new InterpTask(interp, toValue, duration) :: _tasks
    this
  }

  /** Adds a delay to our task list. */
  def delay (time :Float) = {
    _tasks = Task.delay(time) :: _tasks
    this
  }

  // from Task
  override def init (time :Float) = {
    _remain = _tasks.reverse
  }

  // from Task
  override def tick (time :Float) = {
    if (_active == null) {
      _active = _remain.head
      _remain = _remain.tail
      _active.init(time)
    }
    if (_active.tick(time)) {
      _active = null
    }
    _active == null && _remain.length == 0
  }

  /** Returns the current value of the target. */
  protected def get :Float

  /** Updates the target with the supplied value. */
  protected def set (value :Float)

  protected var _tasks :List[Task] = Nil
  protected var _active :Task = null
  protected var _remain :List[Task] = null

  protected class InterpTask (interp :Interpolator, toValue :Float, duration :Float) extends Task
  {
      def init (time :Float) {
        _t0 = time
        _start = get
        _range = toValue - _start
      }

      def tick (time :Float) = {
        val dt = time - _t0
        if (dt < duration) {
          set(interp(_start, _range, dt, duration)); false
        } else {
          set(toValue); true
        }
      }

      protected var _t0 = 0f
      protected var _start = 0f
      protected var _range = 0f
  }
}
