//
// $Id$

package wiggle.util

/**
 * Represents an activity performed bit by bit every frame. Tasks can be composed in sequence or in
 * parallel.
 */
abstract class Task
{
  /** Initializes a task and prepares it for execution. A task should reset any internal state in
   * this method as it may be reinitialized and reused after being used once. The call to
   * {@link #init} will immediately be followed by a call to {@link #tick}. */
  def init (time :Float) :Unit {
  }

  /** Ticks the task, causing it to perform its operation.
   *
   *  @param time the current timestamp.
   *
   *  @return true if the task is complete (and should be removed), false if the task is still
   *  processing.
   */
  def tick (time :Float) :Boolean
}

/**
 * Convenience methods for creating many standard kinds of tasks.
 */
object Task
{
  /** Creates a task that executes the supplied tasks in parallel. */
  def parallel (tasks :Task*) = new Parallel(tasks)

  /** Creates a task that executes the supplied tasks one after another. */
  def sequence (tasks :Task*) = new Sequence(tasks)

  /** Creates a task that delays for the specified time. Useful in a sequence. */
  def delay (delay :Float) = new Delay(delay)

  /** Creates a task that executes the supplied tasks after the specified delay. */
  def after (delay :Float, task :Task) = sequence(new Delay(delay), task)

  /** Delays for the specified period. Generally used with {@link SerialTask} for fun and profit. */
  class Delay (delay :Float) extends Task
  {
    override def init (time :Float) = {
      _end = time + delay
    }

    override def tick (time :Float) = {
      time > _end
    }

    protected var _end :Float = 0
  }

  /** Performs many tasks in parallel and completes when all of its subtasks have completed. */
  class Parallel (tasks :Seq[Task]) extends Task
  {
    assert(tasks.length > 0)

    override def init (time :Float) :Unit = {
      var idx = 0; while (idx < _tasks.length) {
        _tasks(idx).init(time)
      }
    }

    override def tick (time :Float) = {
      var complete = true
      var idx = 0; while (idx < _tasks.length) {
        if (_tasks(idx) != null) {
          if (_tasks(idx).tick(time)) _tasks(idx) = null
          else complete = false
        }
        idx = idx+1
      }
      complete
    }

    protected val _tasks = tasks.toArray
  }

  /** Performs a set of tasks in order, starting the next task after the previous has completed. */
  class Sequence (tasks :Seq[Task]) extends Task
  {
    assert(tasks.length > 0)

    override def init (time :Float) = {
      _remain = _tasks
    }

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

    protected val _tasks = tasks.toList
    protected var _active :Task = null
    protected var _remain :List[Task] = null
  }
}
