//
// $Id$

package wiggle.test

import org.lwjgl.opengl.GL11

import wiggle.game.DisplayConfig
import wiggle.game.GameLoop
import wiggle.gfx.Element
import wiggle.gfx.Group
import wiggle.sim.Entity
import wiggle.sim.Task
import wiggle.util.Interpolator

/**
 * A simple test program.
 */
object Wiggle
{
  class Square(sx :Int, sy :Int) extends Element {
    move(sx, sy)

    override def renderElement (time :Float) {
      orient.set(orient.get + 4.0f % 360); // rotate the square

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
    val config = DisplayConfig("Hello world!", 60, 800, 600)
    var loop :GameLoop = new GameLoop(config)

//     val group = new Group
//     for (x <- 0.to(800).by(100) ; y <- 0.to(600).by(100)) group.add(new Square(x, y))
//     loop.add(group)

    val square = new Square(0, config.height/2)
    loop.add(square)
    val mover = new Object with Entity
    mover.add(square.x.easeInOut(config.width/2, 1).then(Task.delay(1))
              .then(square.x.easeInOut(0, 1)))
    loop.add(mover)
    loop.run
  }

  protected val FRAMERATE = 60
}
