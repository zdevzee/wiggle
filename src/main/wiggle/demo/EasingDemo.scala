//
// $Id$

package wiggle.demo

import org.lwjgl.opengl.GL11

import game.{DisplayConfig, Entity, GameLoop}
import gfx.{Color, Element, Group, Primitive, Image}
import rsrc.{PixelsLoader, PixelsKey, ResourceLoader, TextureCache}
import util.{Interpolator, Task, Taskable}

/**
 * A demonstration of the easing functions.
 */
object EasingDemo
{
  def makeSquare = Primitive.makeColorVertex(4).
    color(Color.Blue).vertex(-50, -50).
    color(Color.White).vertex(50, -50).
    color(Color.Blue).vertex(50, 50).
    color(Color.White).vertex(-50, 50).buildQuads

  def main (args :Array[String]) {
    val config = DisplayConfig("Easing Demo", 60, 800, 600)
    var loop :GameLoop = new GameLoop(config)
    loop.init()

    val tcache = new TextureCache(loop.renderer, new PixelsLoader(new ResourceLoader(ResourceLoader.viaClassPath(getClass.getClassLoader))))

    val group = new Group with Entity
    var idx = 0
    for (y <- 600.to(0).by(-100); x <- 0.to(800).by(100); if (y != 300)) {
      var square = new Image(tcache.get(PixelsKey("card.gif", true, false)))
      square.move(x, -100)
      group.add(square)
      group.add(square.yM.delay((600-y)/600f + 0.5f*(x/600f)).easeIn(y, 1))
      group.add(square.orientM.delay(2.5f).easeInOut(90, 1))
      idx = idx + 1
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
