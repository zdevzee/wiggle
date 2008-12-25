//
// $Id$

package wiggle.gfx

import org.lwjgl.opengl.GL11

import wiggle.util.Setter

/**
 * A visual element, something rendered to the screen.
 */
abstract class Element
{
  /** This element's x position. */
  var x :Float = 0

  /** This element's y position. */
  var y :Float = 0

  /** This element's horizontal scale. */
  var xscale :Float = 1

  /** This element's vertical scale. */
  var yscale :Float = 1

  /** This element's orientation, in degrees (blame OpenGL). */
  var orient :Float = 1

  /** Returns an option on this element's parent. */
  def parent :Option[Group] = _parent

  def xS = new Setter {
    override protected def get = x
    override protected def set (value :Float) {
      x = value
    }
  }

  def yS = new Setter {
    override protected def get = y
    override protected def set (value :Float) {
      y = value
    }
  }

  /** Positions this element at the specified coordinates. */
  def move (nx :Float, ny :Float) {
    x = nx
    y = ny
  }

  /** Sets up our transforms and renders this element. */
  def render (time :Float) {
    GL11.glPushMatrix
    if (x != 0 || y != 0) {
      GL11.glTranslatef(x, y, 0f)
    }
    if (orient != 0) {
      GL11.glRotatef(orient, 0f, 0f, 1f)
    }
    // TODO: scale
    try {
      renderElement(time)
    } finally {
      GL11.glPopMatrix
    }
  }

  /** Called by {@link Group} when we are added to or removed from it. */
  protected[gfx] def setParent (parent :Option[Group]) {
    _parent match {
      case Some(parent) => parent.remove(this)
      case None => // noop
    }
    _parent = parent
  }

  /** Called once the transforms are set up to render this element. */
  protected def renderElement (time :Float)

  /** A reference to our parent in the display hierarchy. */
  protected var _parent :Option[Group] = None
}