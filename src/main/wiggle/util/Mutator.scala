//
// $Id$

package wiggle.util

/**
 * A special sequential task that handles the mutation of a single value.
 */
abstract class Mutator extends Task
{
  /** Adds a task that changes the value to the specified target in the specified duration. */
  def linear (toValue :Float, duration :Float) = interp(Interpolator.linear, toValue, duration)

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

  /** Adds a task that instantly sets our value to the specified value. */
  def set (value :Float) = {
    _tasks = new Task {
      override def tick (time :Float) = {
        update(value)
        true
      }
    } :: _tasks
    this
  }

  /** Wraps this mutator in a task that causes it to repeat indefinitely. */
  def repeat = Task.repeat(this)

  // from Task
  override def init (time :Float) {
    _remain = _tasks.reverse
    next(time)
  }

  // from Task
  override def tick (time :Float) = {
    while (_active != null && _active.tick(time)) {
      next(time)
    }
    _active == null && _remain.isEmpty
  }

  /** Returns the current value of the target. */
  protected def apply :Float

  /** Updates the target with the supplied value. */
  protected def update (value :Float)

  private def next (time :Float) = {
    if (_remain.isEmpty) {
      _active = null
    } else {
      _active = _remain.head
      _active.init(time)
      _remain = _remain.tail
    }
  }

  private[this] var _tasks :List[Task] = Nil
  private[this] var _active :Task = null
  private[this] var _remain :List[Task] = null

  private class InterpTask (interp :Interpolator, toValue :Float, duration :Float) extends Task
  {
    override def init (time :Float) {
      _t0 = time
      _start = apply
      _range = toValue - _start
    }

    override def tick (time :Float) = {
      val dt = time - _t0
      if (dt < duration) {
        update(interp(_start, _range, dt, duration)); false
      } else {
        update(toValue); true
      }
    }

    private[this] var _t0 = 0f
    private[this] var _start = 0f
    private[this] var _range = 0f
  }
}
