//
// $Id$
//
// Wiggle - a 2D game development library - http://code.google.com/p/wiggle/
// Copyright 2008-2010 Michael Bayne
// Distributed under the "Simplified BSD License" in LICENSE.txt

package wiggle.app

import org.lwjgl.LWJGLException
import org.lwjgl.opengl.Display
import org.lwjgl.opengl.DisplayMode
import org.lwjgl.opengl.GL11
import org.lwjgl.util.Timer

import wiggle.gfx.Element
import wiggle.gfx.Renderer
import wiggle.input.Keyboard
import wiggle.util.Taskable

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

      // TEMP: wire up a handler for exiting via they escape key
      _keyboard.head.key(Keyboard.KEY_ESCAPE).addOnPress(key => { stop() ; false })

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

  def pushKeyboard (kbd :Keyboard) {
    _keyboard = kbd :: _keyboard
  }

  def popKeyboard () {
    require(_keyboard.tail != Nil, "Can't pop the default keyboard")
    _keyboard = _keyboard.tail
  }

  def stop () {
    _running = false
  }

  protected def logic () {
    // poll the keyboard, firing any registered handlers
    _keyboard.head.poll()

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

  private[this] val _timer = new Timer

  private[this] var _running = false
  private[this] var _taskables :List[Taskable] = Nil
  private[this] var _elements :List[Element] = Nil

  private[this] var _keyboard = List(new Keyboard(false))
}
