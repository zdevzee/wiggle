//
// $Id$

package wiggle.demo

import org.lwjgl.opengl.GL11

import wiggle.game.DisplayConfig
import wiggle.game.Entity
import wiggle.game.GameLoop
import wiggle.gfx.Element
import wiggle.gfx.Group
import wiggle.util.Interpolator
import wiggle.util.Task
import wiggle.util.Taskable

/**
 * A demonstration of the easing functions.
 */
object EasingDemo
{
  class Square extends Element {
    override def renderElement (time :Float) {
      GL11.glBegin(GL11.GL_QUADS)
      GL11.glColor3f(1, 0, 1)
      GL11.glVertex2i(-50, -50)
      GL11.glColor3f(1, 1, 0)
      GL11.glVertex2i(50, -50)
      GL11.glColor3f(0, 1, 1)
      GL11.glVertex2i(50, 50)
      GL11.glColor3f(1, 1, 1)
      GL11.glVertex2i(-50, 50)
      GL11.glEnd()
    }
  }

  def main (args :Array[String]) {
    val config = DisplayConfig("Easing Demo", 60, 800, 600)
    var loop :GameLoop = new GameLoop(config)

    val group = new Group with Entity
    for (x <- 0.to(800).by(100) ; y <- 0.to(600).by(100); if (y != 300)) {
      var square = new Square
      square.move(x, y)
      group.add(square)
    }
    loop.add(group)

    val square = new Square with Entity
    square.move(0, config.height/2)
    square.add(square.orientM.linear(360, 2).set(0).repeat)
    square.add(square.xM.easeInOut(config.width/2, 1).delay(1).easeInOut(config.width, 1).
               easeIn(config.width/2, 1).easeOut(0, 1).repeat)
    loop.add(square)
    loop.run
  }
}
