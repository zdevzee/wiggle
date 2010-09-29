//
// $Id$
//
// Wiggle - a 2D game development library - http://code.google.com/p/wiggle/
// Copyright 2008-2010 Michael Bayne
// Distributed under the "Simplified BSD License" in LICENSE.txt

package wiggle.gfx

import org.lwjgl.opengl.GL11

import wiggle.util.Mutator

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
  var orient :Float = 0

  /** This element's orientation in radians. */
  def orientR :Float = math.Pi.toFloat * orient / 180f

  /** Returns an option on this element's parent. */
  def parent :Option[Group] = _parent

  /** Returns a mutator for our x position. */
  def xM = new Mutator {
    override protected def apply = x
    override protected def update (value :Float) {
      x = value
    }
  }

  /** Returns a mutator for our y position. */
  def yM = new Mutator {
    override protected def apply = y
    override protected def update (value :Float) {
      y = value
    }
  }

  /** Returns a mutator for our x scale. */
  def xscaleM = new Mutator {
    override protected def apply = xscale
    override protected def update (value :Float) {
      xscale = value
    }
  }

  /** Returns a mutator for our y scale. */
  def yscaleM = new Mutator {
    override protected def apply = yscale
    override protected def update (value :Float) {
      yscale = value
    }
  }

  /** Returns a mutator for our orientation. */
  def orientM = new Mutator {
    override protected def apply = orient
    override protected def update (value :Float) {
      orient = value
    }
  }

  /** Positions this element at the specified coordinates. */
  def move (nx :Float, ny :Float) {
    x = nx
    y = ny
  }

  /** Sets up our transforms and renders this element. */
  def render (rend :Renderer, time :Float) {
    GL11.glPushMatrix
    if (x != 0 || y != 0) {
      GL11.glTranslatef(x, y, 0f)
    }
    if (orient != 0) {
      GL11.glRotatef(orient, 0f, 0f, 1f)
    }
    // TODO: scale
    try {
      renderElement(rend, time)
    } finally {
      GL11.glPopMatrix
    }
  }

  /** Called once the transforms are set up to render this element. */
  protected def renderElement (rend :Renderer, time :Float)

  /** Called by {@link Group} when we are added to or removed from it. */
  private[gfx] def setParent (parent :Option[Group]) {
    _parent match {
      case Some(parent) => parent.remove(this)
      case None => // noop
    }
    _parent = parent
  }

  /** A reference to our parent in the display hierarchy. */
  private[this] var _parent :Option[Group] = None
}
