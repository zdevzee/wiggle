//
// $Id$
//
// Wiggle - a 2D game development library
// Copyright 2008-2010 Michael Bayne
// Distributed under the "Simplified BSD License" in LICENSE.txt

package wiggle.geom

/**
 * Defines a rectangle by an upper left point and a size.
 */
case class Rectangle (x :Float, y :Float, width :Float, height :Float)
{
  /** Returns the upper left of this rectangle as a Point. */
  def upperLeft = Point(x, y)

  /** Returns the upper right of this rectangle as a Point. */
  def upperRight = Point(x + width, y)

  /** Returns the lower left of this rectangle as a Point. */
  def lowerLeft = Point(x, y + height)

  /** Returns the lower right of this rectangle as a Point. */
  def lowerRight = Point(x + width, y + height)

  /** Returns the size of this rectangle as a Size. */
  def size = Size(width, height)
}
