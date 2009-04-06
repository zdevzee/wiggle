//
// $Id$

package wiggle.gfx

import org.lwjgl.opengl.GL11

/**
 * An element that displays a bitmapped image.
 */
class Image (texture :Texture) extends Element
{
  def width = texture.width
  def height = texture.height

  override protected def renderElement (rend :Renderer, time :Float) {
    rend.bind(texture)
    _quad.render(rend, time)
  }

  private[this] val _quad = Primitive.makeTexCoordVertex(4).texCoord(0, 0).vertex(0, 0).
    texCoord(texture.texRight, 0).vertex(texture.width, 0).
    texCoord(texture.texRight, texture.texBottom).vertex(texture.width, texture.height).
    texCoord(0, texture.texBottom).vertex(0, texture.height).buildQuads
}
