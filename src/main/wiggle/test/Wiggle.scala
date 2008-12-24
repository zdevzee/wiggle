//
// $Id$

package wiggle.test

import org.lwjgl.opengl.GL11

import wiggle.game.DisplayConfig
import wiggle.game.GameLoop
import wiggle.gfx.Element
import wiggle.sim.Entity

/**
 * Does something extraordinary.
 */
object Wiggle
{
  def main (args :Array[String]) {
    val config = DisplayConfig("Hello world!", 60, 800, 600)
    var loop :GameLoop = new GameLoop(config)

    loop.addEntity(new Element with Entity {
      x = config.width/2
      y = config.height/2

      override def logic (time :Float) {
        orient += 4.0f % 360; // rotate the square
      }

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
    })

    loop.run
  }

  protected val FRAMERATE = 60
}
