//
// $Id$

package wiggle.rsrc

import scala.collection.jcl.WeakHashMap
import scala.ref.WeakReference

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11

import gfx.{Renderer, Texture}

/**
 * Handles the creation and caching of textures.
 */
class TextureCache (renderer :Renderer, pxlldr :PixelsLoader)
{
  /** Returns the specified texture. 
   * @throws NoSuchElementException if the image data could not be found.
   */
  def get (key :PixelsKey) = lookupTexture(key) match {
    case Some(texture) => texture
    case None => createTexture(key, pxlldr.get(key))
  }

  /** Returns an option on the specified texture. */
  def getOption (key :PixelsKey) :Option[Texture] = lookupTexture(key).orElse {
    pxlldr.getOption(key) match {
      case Some(pixels) => Some(createTexture(key, pixels))
      case None => None
    }
  }

  /** Looks up a texture in the cache, returns an option on the texture. */
  protected def lookupTexture (key :PixelsKey) = _cache.get(key) match {
    case Some(texref) => texref.get
    case None => None
  }

  /** Creates a texture from the supplied configuration and data, caches and returns it. */
  protected def createTexture (key :PixelsKey, pixels :Pixels) = {
    // generate an id for our new texture and bind to it
    _idbuf.rewind()
    GL11.glGenTextures(_idbuf)
    val id = _idbuf.get(0)
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, id); 

    // TODO: configure our minify and magnify filters
    // GL11.glTexParameteri(target, GL11.GL_TEXTURE_MIN_FILTER, minFilter)
    // GL11.glTexParameteri(target, GL11.GL_TEXTURE_MAG_FILTER, magFilter) 

    // send our texture data over to OpenGL for grindy grindy
    GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, pixels.texWidth, pixels.texHeight, 0,
                      pixels.format, GL11.GL_UNSIGNED_BYTE, pixels.data)

    // create, cache and return our texture handle
    val texture = new Texture(key, pixels, renderer, id)
    _cache.put(texture.key, new WeakReference(texture))
    texture
  }

  /** A weak cache of resolved textures. */
  protected val _cache = new WeakHashMap[PixelsKey, WeakReference[Texture]]

  /** A buffer used to communicate with OpenGL. */
  protected var _idbuf = BufferUtils.createIntBuffer(1)
}
