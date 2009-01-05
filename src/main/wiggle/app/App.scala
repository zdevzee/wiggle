//
// $Id$

package wiggle.app

import org.lwjgl.LWJGLException
import org.lwjgl.input.Keyboard
import org.lwjgl.opengl._
import org.lwjgl.util.Timer

import gfx.Element
import gfx.Renderer
import util.Taskable

/**
 * Handles the main application loop.
 */
class App (config :DisplayConfig)
{
  val renderer = new Renderer

  def init () {
    try {
      Display.setTitle(config.title)
      Display.setFullscreen(false)
      Display.setVSyncEnabled(true)
      Display.setDisplayMode(new DisplayMode(config.width, config.height))
      Display.create // start by creating the display
      _running = true

      // disable the OpenGL depth test and lighting since we're rendering 2D graphics
      GL11.glDisable(GL11.GL_DEPTH_TEST)
      GL11.glDisable(GL11.GL_LIGHTING)
      // GL11.glShadeModel(GL11.GL_SMOOTH)

      // set the background to black
      GL11.glClearColor(0, 0, 0, 0)
      GL11.glClearDepth(1)

      // TODO: do we want to do this globally?
      GL11.glEnable(GL11.GL_BLEND)
      GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)

//       GL11.glViewport(0, 0, config.width, config.height)
//       GL11.glMatrixMode(GL11.GL_MODELVIEW)

      // set up ortho rendering mode and the canvas size
      GL11.glMatrixMode(GL11.GL_PROJECTION)
      GL11.glLoadIdentity()
      GL11.glOrtho(0, config.width, config.height, 0, -1, 1)
      GL11.glMatrixMode(GL11.GL_MODELVIEW)
      GL11.glLoadIdentity()
      
    } catch {
      case e :LWJGLException => e.printStackTrace(System.err)
    }
  }

  def run () {
    while (_running) {
      Timer.tick() // update the LWJGL Timer system

      if (Display.isCloseRequested) {
        stop()

      } else {
        logic()
        if (Display.isVisible || Display.isDirty) render()
        Display.update()
        Display.sync(config.framerate)
      }
    }
  }

  def add (entity :Entity) {
    if (entity.isInstanceOf[Element]) {
      _elements = entity.asInstanceOf[Element] :: _elements
    }
    _taskables = entity :: _taskables
  }

  def remove (entity :Entity) {
    // TODO
  }

  def stop () {
    _running = false
  }

  protected def logic () {
    if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) stop

    // tick our entities
    for (taskable <- _taskables) taskable.tick(_timer.getTime)
  }

  protected def render () {
    // clear the screen
    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_STENCIL_BUFFER_BIT)
    GL11.glMatrixMode(GL11.GL_MODELVIEW)
    GL11.glLoadIdentity()

    // render our elements
    for (element <- _elements) element.render(this.renderer, _timer.getTime)
  }

  protected val _timer = new Timer

  protected var _running = false
  protected var _taskables :List[Taskable] = Nil
  protected var _elements :List[Element] = Nil
}
