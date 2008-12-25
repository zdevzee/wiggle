//
// $Id$

package wiggle.gfx

import org.lwjgl.opengl.GL11

import wiggle.util.Value

/**
 * A visual element, something rendered to the screen.
 */
abstract class Element
{
  /** This element's x position. */
  val x = new Value(0)

  /** This element's y position. */
  val y = new Value(0)

  /** This element's horizontal scale. */
  val xscale = new Value(1)

  /** This element's vertical scale. */
  val yscale = new Value(1)

  /** This element's orientation, in degrees (blame OpenGL). */
  val orient = new Value(1)

  /** Returns an option on this element's parent. */
  def parent :Option[Group] = _parent

  /** Positions this element at the specified coordinates. */
  def move (nx :Float, ny :Float) {
    x.set(nx)
    y.set(ny)
  }

  /** Sets up our transforms and renders this element. */
  def render (time :Float) {
    GL11.glPushMatrix
    val cx = x.get
    val cy = y.get
    if (cx != 0 || cy != 0) {
      GL11.glTranslatef(cx, cy, 0f)
    }
    val corient = orient.get
    if (corient != 0) {
      GL11.glRotatef(corient, 0f, 0f, 1f)
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
