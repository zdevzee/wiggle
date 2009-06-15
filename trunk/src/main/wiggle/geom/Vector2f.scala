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
 * Represents a 2D vector.
 */
final class Vector2f (private[this] var _x :Float, private[this] var _y :Float) extends AnyRef
  with Product2[Float, Float]
{
  /** Returns the x value of this vector. */
  def x :Float = _x

  /** Returns the y value of this vector. */
  def y :Float = _y

  // from Product2
  def _1: Float = _x

  // from Product2
  def _2: Float = _y

  /** Updates the values of this vector with the supplied values. Returns this. */
  def update (x :Float, y :Float) :this.type = {
    _x = x
    _y = y
    this
  }

  /** Updates the values of this vector with the supplied values. Returns this. */
  def update (vals :Product2[Float, Float]) :this.type = update(vals._1, vals._2)

  /** Computes the dot product of this and the other vector. */
  def dot (other :Vector2f) :Float = _x * other.x + _y * other.y

  /** Returns the length of this vector. */
  def length :Float = Math.sqrt(lengthSq).toFloat

  /** Returns the squared length of this vector. */
  def lengthSq :Float = (_x*_x + _y*_y)

  /** Returns the angle between this vector and other. */
  def angle (other :Vector2f) :Float = {
    val cos = dot(other) / (length*other.length)
    if (cos >= 1f) 0f else Math.acos(cos).toFloat
  }

  /** Returns the direction of a vector pointing from this point to other. */
  def direction (other :Vector2f) :Float = Math.atan2(other.y - _y, other.x - _x).toFloat

  /** Returns the distance from this vector to other. */
  def distance (other :Vector2f) :Float = Math.sqrt(distanceSq(other)).toFloat

  /** Returns the squared distance from this vector to other. */
  def distanceSq (other :Vector2f) :Float = {
    val dx = _x - other.x
    val dy = _y - other.y
    dx*dx + dy*dy
  }

  /** Returns a new vector containing the negation of this vector. */
  def neg = new Vector2f(-_x, -_y)

  /** Negates this vector in place. Returns this. */
  def negEq = neg(this)

  /** Stores the negation of this vector into target. Returns target. */
  def neg (target :Vector2f) = target.update(-_x, -_y)

  /** Stores the normalization of this vector into target. Returns target. */
  def norm (target :Vector2f) = mult(1f / length, target)

  /** Normalizes this vector in place. */
  def normEq = norm(this)

  /** Returns a new vector containing the normalization of this vector. */
  def norm = this * (1f / length)

  /** Returns a new vector containing this multiplied by k. */
  def * (k :Float) = new Vector2f(_x*k, _y*k)

  /** Multiplies this vector in place by a scalar. Returns this. */
  def *= (k :Float) = mult(k, this)

  /** Stores this vector times a scalar into target. Returns target. */
  def mult (k :Float, target :Vector2f) = target.update(_x*k, _y*k)

  /** Returns a new vector containing this multiplied by vec. */
  def * (vec :Vector2f) = new Vector2f(_x*vec.x, _y*vec.y)

  /** Multiplies this vector in place by vec. Returns this. */
  def *= (vec :Vector2f) = mult(vec, this)

  /** Stores this vector times vec into target. Returns target. */
  def mult (vec :Vector2f, target :Vector2f) = target.update(_x*vec.x, _y*vec.y)

  /** Returns a new vector containing this plus vec. */
  def + (vec :Vector2f) = new Vector2f(_x+vec.x, _y+vec.y)

  /** Adds this vector in place to vec. Returns this. */
  def += (vec :Vector2f) = add(vec, this)

  /** Stores this vector plus vec into target. Returns target. */
  def add (vec :Vector2f, target :Vector2f) = target.update(_x+vec.x, _y+vec.y)

  /** Returns a new vector containing this plus x and y. */
  def add (x :Float, y :Float) = new Vector2f(_x + x, _y + y)

  /** Adds x and y to this vector in place. Returns this. */
  def addEq (x :Float, y :Float) = update(_x + x, _y + y)

  /** Returns a new vector containing this minus vec. */
  def - (vec :Vector2f) = new Vector2f(_x+vec.x, _y+vec.y)

  /** Subtracts this vector in place to vec. Returns this. */
  def -= (vec :Vector2f) = subtract(vec, this)

  /** Stores this vector minus vec into target. Returns target. */
  def subtract (vec :Vector2f, target :Vector2f) = target.update(_x+vec.x, _y+vec.y)

  /** Returns a new vector containing this plus vec scaled by k. */
  def addScaled (vec :Vector2f, k :Float) = new Vector2f(_x + k*vec.x, _y + k*vec.y)

  /** Adds this vector to vec scaled by k in place. Returns this. */
  def addScaledEq (vec :Vector2f, k :Float) = addScaled(vec, k, this)

  /** Stores this vector to vec scaled by k into target. Returns target. */
  def addScaled (vec :Vector2f, k :Float, target :Vector2f) =
    target.update(_x + k*vec.x, _y + k*vec.y)

  /** Returns a new vector containing this vector rotated by angle radians. */
  def rotate (angle :Float) = {
    val sina = Math.sin(angle).toFloat
    val cosa = Math.cos(angle).toFloat
    new Vector2f(_x*cosa - _y*sina, _x*sina + _y*cosa)
  }

  /** Rotates this vector by angle radians in place. Returns this. */
  def rotateEq (angle :Float) = rotate(angle, this)

  /** Stores this vector rotated by angle radians into target. Returns target. */
  def rotate (angle :Float, target :Vector2f) = {
    val sina = Math.sin(angle).toFloat
    val cosa = Math.cos(angle).toFloat
    target.update(_x*cosa - _y*sina, _x*sina + _y*cosa)
  }

  /** Returns a new vector containing the linear interpolation of this and vec by t. */
  def lerp (vec :Vector2f, t :Float) = new Vector2f(_x + t*(vec.x - _x), _y + t*(vec.y - _y))

  /** Linearly interpolates between this and vec by t in place. Returns this. */
  def lerpEq (vec :Vector2f, t :Float) = lerp(vec, t, this)

  /** Stores the linear interpolation of this and vec by t into target. Returns target. */
  def lerp (vec :Vector2f, t :Float, target :Vector2f) =
    target.update(_x + t*(vec.x - _x), _y + t*(vec.y - _y))
}

/**
 * Vector related factory methods and constants.
 */
object Vector2f
{
  /** The zero vector. */
  val Zero = new Vector2f(0f, 0f)

  /** The unit vector. */
  val Unit = new Vector2f(1f, 1f)

  /** The unit vector in the +x direction. */
  val UnitX = new Vector2f(1f, 0f)

  /** The unit vector in the +y direction. */
  val UnitY = new Vector2f(0f, 1f)

  /** The unit vector in the -x direction. */
  val UnitMinusX = new Vector2f(-1f, 0f)

  /** The unit vector in the -y direction. */
  val UnitMinusY = new Vector2f(0f, -1f)

  /** Creates a new vector with the supplied value. */
  def apply (x :Float, y :Float) = new Vector2f(x, y)
}
