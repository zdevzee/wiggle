//
// $Id$

package wiggle.gfx

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL12
import org.lwjgl.opengl.GL13

import rsrc.Pixels
import rsrc.PixelsKey

/**
 * Contains metadata for an OpenGL 2D texture. (1D, 3D and cube map textures are not supported.)
 */
class Texture (val key :PixelsKey, source :Pixels, renderer :Renderer, val id :Int)
{
  /** The width of our image data in pixels. */
  val width = source.width

  /** The width of our image data in pixels. */
  val height = source.height

  /** The width of our texture image in pixels (will be a power of 2). */
  val texWidth = source.texWidth

  /** The height of our texture image in pixels (will be a power of 2). */
  val texHeight = source.texHeight

  /** When we are finalized, we tell the renderer about it so that it can free our resources. */
  override protected def finalize () {
    renderer.textureFinalized(this)
  }
}
