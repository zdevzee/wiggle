//
// $Id$
//
// Wiggle - a 2D game development library - http://code.google.com/p/wiggle/
// Copyright 2008-2010 Michael Bayne
// Distributed under the "Simplified BSD License" in LICENSE.txt

package wiggle.util

/**
 * A trait used by things that wish to maintain a list of tasks and tick those tasks every frame.
 */
trait Taskable
{
  /** Returns a view of our tasks. */
  def tasks :Seq[Task] = _tasks

  /** Adds a task to this taskable. The task will be initialized and ticked on the next call to
   * {@link #tick}. If we're in the middle of ticking, this task won't participate in this tick. */
  def add (task :Task) {
    _newTasks = task :: _newTasks
  }

  /** Removes a task from this taskable. The task will be removed on the next call to {@link #tick}.
   * If we're in the middle of ticking this task will not be removed until the next call. */
  def remove (task :Task) = {
    _deadTasks += task
  }

  /** Adds newly registered tasks, removes pending deletions and ticks all active tasks. */
  def tick (time :Float) {
    // remove any dead tasks from our list
    if (!_deadTasks.isEmpty) {
      _tasks = _tasks.filterNot(_deadTasks.contains)
      _deadTasks = Set()
    }

    // add any new tasks to our current tasks list
    if (!_newTasks.isEmpty) {
      val olen = _tasks.length
      val tasks = new Array[Task](olen + _newTasks.length)
      System.arraycopy(_tasks, 0, tasks, 0, olen)
      var idx = olen; while (_newTasks.length > 0) {
        _newTasks.head.init(time)
        tasks(idx) = _newTasks.head
        _newTasks = _newTasks.tail
        idx = idx+1
      }
      _tasks = tasks
    }

    // and tick all of our registered tasks (this is ugly because it's optimized)
    val tasks = _tasks
    val len = tasks.length
    var idx = 0; while (idx < len) {
      val t = tasks(idx)
      if (t.tick(time)) remove(t)
      idx = idx+1
    }
  }

  private[this] var _tasks :Array[Task] = new Array(0)
  private[this] var _newTasks :List[Task] = Nil
  private[this] var _deadTasks :Set[Task] = Set()
}
