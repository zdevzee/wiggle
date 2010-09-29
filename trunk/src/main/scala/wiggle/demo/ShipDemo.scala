//
// $Id$
//
// Wiggle - a 2D game development library - http://code.google.com/p/wiggle/
// Copyright 2008-2010 Michael Bayne
// Distributed under the "Simplified BSD License" in LICENSE.txt

package wiggle.demo

import wiggle.app.{App, DisplayConfig, Entity}
import wiggle.geom.Vector2f
import wiggle.gfx.{Color, Group, Primitive}
import wiggle.input.{DPad, Keyboard}
import wiggle.util.DeltaTask

/**
 * Displays a little asteroids-like ship that can be controlled with the arrow keys.
 */
object ShipDemo
{
  val config = DisplayConfig("Ship Demo", 60, 800, 600)

  class Ship extends Group with Entity
  {
    def accel (accel :Float) = new DeltaTask {
      override def deltaTick (dt :Float) = {
        // compute our current acceleration vector
        _curacc.update(Vector2f.UnitMinusY).rotateEq(orientR).multEq(accel)
        // update our current velocity vector with said acceleration
        _curvel.addScaledEq(_curacc, dt)
        false
      }
      private[this] var _curacc = Vector2f(0, 0)
    }

    override def tick (time :Float) {
      super.tick(time)
      def wrap (v :Float, r :Float) = if (v < 0) v + r else if (v > r) v - r else v
      x = wrap(x + _curvel.x, config.width)
      y = wrap(y + _curvel.y, config.height)
    }

    add(Primitive.makeColorVertex(3).
          color(Color.White).vertex(0, -10).
          color(Color.Blue).vertex(5, 10).
          color(Color.Blue).vertex(-5, 10).buildTriangles)

    private[this] val _curvel = Vector2f(0, 0)
  }

  def main (args :Array[String]) {
    var app :App = new App(config)
    app.init()

    val ship = new Ship
    ship.x = config.width/2
    ship.y = config.height/2
    app.add(ship)

    val kbd = new Keyboard(false)
    val dpad = new DPad(kbd, ship)
    dpad.group.bindUp(ship.accel(10)) // .bindDown(ship.accel(-10)) // uncomment for reverse!
    dpad.group.bindLeft(ship.orientM.velocity(-90)).bindRight(ship.orientM.velocity(90))
    app.pushKeyboard(kbd)

    app.run()
  }
}
