//
// $Id$
//
// Copyright (C) 2008-2009 Michael Bayne
//
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use
// this file except in compliance with the License. You may obtain a copy of the
// License at: http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software distributed
// under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
// CONDITIONS OF ANY KIND, either express or implied. See the License for the
// specific language governing permissions and limitations under the License.

package wiggle.util

/**
 * A special sequential task that handles the mutation of a single value.
 */
abstract class Mutator extends Task
{
  /** Adds a task that changes the value to the specified target in the specified duration. */
  def linear (toVal :Float, duration :Float) = interp(Interpolator.linear, toVal, duration)

  /** Adds a task that eases the value to the specified target in the specified duration. */
  def easeIn (toVal :Float, duration :Float) = interp(Interpolator.easeIn, toVal, duration)

  /** Adds a task that eases the value to the specified target in the specified duration. */
  def easeOut (toVal :Float, duration :Float) = interp(Interpolator.easeOut, toVal, duration)

  /** Adds a task that eases the value to the specified target in the specified duration. */
  def easeInOut (toVal :Float, duration :Float) = interp(Interpolator.easeInOut, toVal, duration)

  /** Adds a task that changes the value to the specified target in the specified duration. */
  def interp (interp :Interpolator, toVal :Float, duration :Float) = {
    add(new InterpTask(interp, toVal, duration))
    this
  }

  /** Adds a task that changes the value with the specified velocity. */
  def velocity (dvdt :Float) = {
    add(new Task {
      override def init (time :Float) {
        _last = time
      }
      override def tick (time :Float) = {
        update(apply + (time - _last) * dvdt)
        _last = time
        false
      }
      private[this] var _last :Float = 0
    })
    this
  }

  /** Adds a task that accelerates by the specified rate from the specified initial velocity up to
   * the specified maximum velocity (in units per second). */
  def inertial (accel :Float, maxvel :Float, initvel :Float) = {
    add(new Task {
      override def init (time :Float) {
        _last = time
        _curvel = initvel
      }
      override def tick (time :Float) = {
        val dt = time - _last
        if (accel > 0) {
          _curvel = Math.min(_curvel + dt * accel, maxvel)
        } else {
          _curvel = Math.max(_curvel + dt * accel, maxvel)
        }
        update(apply + dt * _curvel)
        _last = time
        false
      }
      private[this] var _last :Float = 0
      private[this] var _curvel :Float = 0
    })
    this
  }

  /** Adds a task that accelerates by the specified rate from zero up to the specified maximum
   * velocity (in pixels per second). */
  def inertial (accel :Float, maxvel :Float) :Mutator = inertial(accel, maxvel, 0)

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

  /** Adds the supplied task to our chain of tasks. */
  def add (task :Task) = {
    _tasks = task :: _tasks
    this
  }

  /** Creates a mutator that bounds all changes into the supplied range. */
  def limit (min :Float, max :Float) = new Mutator {
    override protected def apply = Mutator.this.apply
    override protected def update (value :Float) {
      Mutator.this.update(Math.min(Math.max(value, min), max))
    }
  }

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

  private class InterpTask (interp :Interpolator, toVal :Float, duration :Float) extends Task
  {
    override def init (time :Float) {
      _t0 = time
      _start = apply
      _range = toVal - _start
    }

    override def tick (time :Float) = {
      val dt = time - _t0
      if (dt < duration) {
        update(interp(_start, _range, dt, duration)); false
      } else {
        update(toVal); true
      }
    }

    private[this] var _t0 = 0f
    private[this] var _start = 0f
    private[this] var _range = 0f
  }
}
