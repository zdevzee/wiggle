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

import scala.collection.jcl.ArrayList

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
    _deadTasks = task :: _deadTasks
  }

  /** Adds newly registered tasks, removes pending deletions and ticks all active tasks. */
  def tick (time :Float) {
    // add any new tasks to our current tasks list
    while (_newTasks.length > 0) {
      _newTasks.head.init(time)
      _tasks.add(_newTasks.head)
      _newTasks = _newTasks.tail
    }
    // remove any dead tasks from our list
    while (_deadTasks.length > 0) {
      _tasks.remove(_deadTasks.head)
      _deadTasks = _deadTasks.tail
    }
    // and tick all of our registered tasks
    var idx = 0; while (idx < _tasks.length) {
      if (_tasks(idx).tick(time)) {
        _tasks.remove(idx)
      }
      idx = idx+1
    }
  }

  private[this] var _tasks :ArrayList[Task] = new ArrayList()
  private[this] var _newTasks :List[Task] = Nil
  private[this] var _deadTasks :List[Task] = Nil
}
