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
