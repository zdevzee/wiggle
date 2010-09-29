//
// $Id$
//
// Wiggle - a 2D game development library - http://code.google.com/p/wiggle/
// Copyright 2008-2010 Michael Bayne
// Distributed under the "Simplified BSD License" in LICENSE.txt

package wiggle.geom

/**
 * Provides point-related utility methods.
 */
object Point
{
  /** Returns the supplied pair of values as an X-style string: +2+4 (or -4+3, etc.) */
  def toString (x :Float, y :Float) = {
    val sb = new StringBuilder
    if (x >= 0) sb.append("+")
    sb.append(x)
    if (y >= 0) sb.append("+")
    sb.append(y)
    sb.toString
  }
}

/**
 * Defines a single point.
 */
case class Point (x :Float, y :Float)
{
  override def toString = Point.toString(x, y)
}
