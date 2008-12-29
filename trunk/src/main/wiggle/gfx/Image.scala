//
// $Id$

package wiggle.gfx

/**
 * An element that displays a bitmapped image.
 */
class Image (texture :Texture) extends Element
{
  override protected def renderElement (rend :Renderer, time :Float) {
    rend.bind(texture)
    _quad.render(rend, time)
  }

  protected val _quad = Primitive.makeTexCoordVertex(4).texCoord(0, 0).vertex(0, 0).
    texCoord(1, 0).vertex(texture.width, 0).
    texCoord(1, 1).vertex(texture.width, texture.height).
    texCoord(0, 1).vertex(0, texture.height).buildQuads
}
