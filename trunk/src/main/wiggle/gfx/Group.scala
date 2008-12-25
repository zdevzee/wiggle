//
// $Id$

package wiggle.gfx

import scala.collection.jcl.ArrayList

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
    _children.add(elem)
  }

  /** Removes the specified child element. */
  def remove (elem :Element) = {
    if (!_children.remove(elem)) false
    else {
      elem.setParent(None)
      true
    }
  }

  override protected def renderElement (time :Float) {
    // this is the only way to iterate without creating garbage, sigh
    var idx = 0; val len = _children.length; while (idx < len) {
      _children(idx).render(time)
      idx = idx+1
    }
  }

  protected var _children :ArrayList[Element] = new ArrayList()
}
