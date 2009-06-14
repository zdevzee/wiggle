//
// $Id$
//
// Copyright (C) 2008-2009 Michael Bayne
//
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use
// this file except in compliance with the License. You may obtain a copy of the
// License at: http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software distributed
// under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
// CONDITIONS OF ANY KIND, either express or implied. See the License for the
// specific language governing permissions and limitations under the License.

package wiggle.rsrc

import scala.collection.mutable.WeakHashMap
import scala.ref.WeakReference

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11

import wiggle.gfx.{Renderer, Texture}

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
  private[this] def lookupTexture (key :PixelsKey) = _cache.get(key) match {
    case Some(texref) => texref.get
    case None => None
  }

  /** Creates a texture from the supplied configuration and data, caches and returns it. */
  private[this] def createTexture (key :PixelsKey, pixels :Pixels) = {
    // generate an id for our new texture and bind to it
    _idbuf.rewind()
    GL11.glGenTextures(_idbuf)
    val id = _idbuf.get(0)
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, id); 

    // TODO: allow customization of our minify and magnify filters
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR)
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR) 

    // send our texture data over to OpenGL for grindy grindy
    GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, pixels.texWidth, pixels.texHeight, 0,
                      pixels.format, GL11.GL_UNSIGNED_BYTE, pixels.data)

    // create, cache and return our texture handle
    val texture = new Texture(key, pixels, renderer, id)
    _cache.put(texture.key, new WeakReference(texture))
    texture
  }

  /** A weak cache of resolved textures. */
  private[this] val _cache = new WeakHashMap[PixelsKey, WeakReference[Texture]]

  /** A buffer used to communicate with OpenGL. */
  private[this] var _idbuf = BufferUtils.createIntBuffer(1)
}
