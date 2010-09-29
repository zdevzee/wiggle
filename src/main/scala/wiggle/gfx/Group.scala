//
// $Id$
//
// Wiggle - a 2D game development library - http://code.google.com/p/wiggle/
// Copyright 2008-2010 Michael Bayne
// Distributed under the "Simplified BSD License" in LICENSE.txt

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
