//
// $Id$

package wiggle.gfx

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
  /** Returns a list of this element's children. */
  def children :List[Element] = _children.toList

  /** Adds the specified element as a child of this element. */
  def add (elem :Element) {
    elem.setParent(Some(this))
    _children = _children ++ Array(elem)
  }

  /** Removes the specified child element. */
  def remove (elem :Element) = {
    val eidx = _children.indexOf(elem)
    if (eidx == -1) false
    else {
      _children = _children.subArray(0, eidx) ++ _children.drop(eidx+1)
      elem.setParent(None)
      true
    }
  }

  override protected def renderElement (time :Float)
  {
    for (child <- _children) child.render(time)
  }

  protected var _children :Array[Element] = Array()
}
