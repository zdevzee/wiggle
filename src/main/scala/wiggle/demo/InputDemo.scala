//
// $Id$
//
// Wiggle - a 2D game development library - http://code.google.com/p/wiggle/
// Copyright 2008-2010 Michael Bayne
// Distributed under the "Simplified BSD License" in LICENSE.txt

package wiggle.demo

import wiggle.app.{App, DisplayConfig, Entity}
import wiggle.util.Task
import wiggle.input.DPad
import wiggle.input.Keyboard
import wiggle.gfx.Element
import wiggle.gfx.{Group, Image}
import wiggle.rsrc.{PixelsKey, PixelsLoader}
import wiggle.rsrc.{ResourceLoader, TextureCache}

/**
 * A demonstration of mouse and keyboard input.
 */
object InputDemo
{
  def main (args :Array[String]) {
    val config = DisplayConfig("Input Demo", 60, 800, 600)
    var app :App = new App(config)
    app.init()

    val rldr = new ResourceLoader(ResourceLoader.viaClassPath(getClass.getClassLoader))
    val pldr = new PixelsLoader(rldr)
    val tcache = new TextureCache(app.renderer, pldr)

    val top = new Group with Entity
    val image = new Image(tcache.get(PixelsKey("card.gif", false, false)))
    image.move(config.width/2-image.width/2, config.height/2-image.height/2)
    top.add(image)
    app.add(top)

    val maxx = config.width-image.width
    val maxy = config.height-image.height

    val kbd = new Keyboard(false)
    val dpad = new DPad(kbd, top)
    dpad.group.bindLeft(image.xM.limit(0, maxx).inertial(-Accel, -Velocity)).
               bindRight(image.xM.limit(0, maxx).inertial(Accel, Velocity))
    dpad.group.bindUp(image.yM.limit(0, maxy).inertial(-Accel, -Velocity)).
               bindDown(image.yM.limit(0, maxy).inertial(Accel, Velocity)).
               setFirstWins(true)
    app.pushKeyboard(kbd)

    app.run()
  }

  protected val Velocity = 200
  protected val Accel = 4*Velocity
}
