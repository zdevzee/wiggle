//
// $Id$

package wiggle.demo

import org.lwjgl.opengl.GL11

import wiggle.game.{DisplayConfig, Entity, GameLoop}
import wiggle.gfx.{Color, Element, Group, Primitive}
import wiggle.util.{Interpolator, Task, Taskable}

/**
 * A demonstration of the easing functions.
 */
object EasingDemo
{
  def makeSquare = Primitive.makeColorVertex(4).
    color(Color.Red).vertex(-50, -50).
    color(Color.Green).vertex(50, -50).
    color(Color.Blue).vertex(50, 50).
    color(Color.White).vertex(-50, 50).buildQuads

  def main (args :Array[String]) {
    val config = DisplayConfig("Easing Demo", 60, 800, 600)
    var loop :GameLoop = new GameLoop(config)

    val group = new Group with Entity
    for (x <- 0.to(800).by(100) ; y <- 0.to(600).by(100); if (y != 300)) {
      var square = makeSquare
      square.move(x, y)
      group.add(square)
    }
    loop.add(group)

    val mover = new Group with Entity
    mover.add(makeSquare)
    mover.move(0, config.height/2)
    mover.add(mover.orientM.linear(360, 2).set(0).repeat)
    mover.add(mover.xM.easeInOut(config.width/2, 1).delay(1).easeInOut(config.width, 1).
              easeIn(config.width/2, 1).easeOut(0, 1).repeat)
    loop.add(mover)
    loop.run
  }
}
