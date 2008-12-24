//
// $Id$

package wiggle.game

import org.lwjgl.LWJGLException
import org.lwjgl.input.Keyboard
import org.lwjgl.opengl._
import org.lwjgl.util.Timer

import wiggle.sim.Entity

/**
 * Handles the main game loop.
 */
class GameLoop (config :DisplayConfig)
{
  def run () {
    init

    while (_running) {
      Display.update // update the display
      Timer.tick // update the Timer system

      if (Display.isCloseRequested) {
        stop

      } else if (Display.isActive) {
        logic
        render
        Display.sync(config.framerate)

      } else {
        // our window is not active, sleep for a bit to consume less CPU
        try {
          Thread.sleep(100)
        } catch {
          case e :Exception => Console.println("Sleep failed " + e)
        }

        logic
        if (Display.isVisible || Display.isDirty) render
      }
    }
  }

  def addEntity (entity :Entity) {
    _entities = entity :: _entities
  }

  def stop () {
    _running = false
  }

  protected def init () {
    try {
      Display.setTitle(config.title)
      Display.setFullscreen(false)
      Display.setVSyncEnabled(true)
      Display.setDisplayMode(new DisplayMode(config.width, config.height))
      Display.create // start by creating the display
      _running = true

      // set the background to black
      GL11.glClearColor(0, 0, 0, 0)

      // enable textures since we're going to use these for our sprites
      GL11.glEnable(GL11.GL_TEXTURE_2D)

      // disable the OpenGL depth test since we're rendering 2D graphics
      GL11.glDisable(GL11.GL_DEPTH_TEST)

      // set up ortho rendering mode and the canvas size
      GL11.glMatrixMode(GL11.GL_PROJECTION)
      GL11.glLoadIdentity()
      GL11.glOrtho(0, config.width, config.height, 0, -1, 1)

    } catch {
      case e :LWJGLException => e.printStackTrace(System.err)
    }
  }

  protected def logic () {
    if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) stop
    _entities.foreach(_.logic(_timer.getTime))
  }

  protected def render () {
    // clear the screen
    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_STENCIL_BUFFER_BIT)
    GL11.glMatrixMode(GL11.GL_MODELVIEW)
    GL11.glLoadIdentity()

    // render our entities
    _entities.foreach(_.render(_timer.getTime))
  }

  protected var _running = false
  protected var _timer = new Timer
  protected var _entities :List[Entity] = Nil
}
