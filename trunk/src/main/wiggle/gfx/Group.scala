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

package wiggle.gfx

import scala.collection.mutable.ArrayBuffer

/**
 * Convenience methods for creating groups of elements.
 */
object Group
{
  def of (elems :Element*) = {
    val group :Group = new Group
    for (elem <- elems) group.add(elem)
    group
  }
}

/**
 * A group of elements. The elements are rendered relative to the transform of the group.
 */
class Group extends Element
{
  /** Returns a view of this element's children. */
  def children :Seq[Element] = _children

  /** Adds the specified element as a child of this element. */
  def add (elem :Element) {
    elem.setParent(Some(this))
    _children += elem
  }

  /** Removes the specified child element. */
  def remove (elem :Element) = _children.indexOf(elem) match {
    case -1 => false
    case idx => {
      _children.remove(idx, 1)
      elem.setParent(None)
      true
    }
  }

  override protected def renderElement (rend :Renderer, time :Float) {
    // this is the only way to iterate without creating garbage, sigh
    var idx = 0; val len = _children.length; while (idx < len) {
      _children(idx).render(rend, time)
      idx = idx+1
    }
  }

  private[this] val _children :ArrayBuffer[Element] = new ArrayBuffer()
}
