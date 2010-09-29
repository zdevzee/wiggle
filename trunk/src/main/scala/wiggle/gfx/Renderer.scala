//
// $Id$
//
// Wiggle - a 2D game development library - http://code.google.com/p/wiggle/
// Copyright 2008-2010 Michael Bayne
// Distributed under the "Simplified BSD License" in LICENSE.txt

package wiggle.gfx

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11

/**
 * Manages the graphics context and tracks the current OpenGL state.
 */
class Renderer
{
  /** Binds the supplied texture. NOOPs if the texture is already bound. */
  def bind (texture :Texture) {
    if (texture != _curtex) {
      _curtex = texture
      GL11.glEnable(GL11.GL_TEXTURE_2D)
      GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.id)
    }
  }

  /** Clears out any current texture binding. */
  def bindNone () {
    if (_curtex != null) {
      _curtex = null
      GL11.glDisable(GL11.GL_TEXTURE_2D)
    }
  }

  /** Destroys pending finalized resources. */
  def destroyFinalized () {
    // destroy any finalized textures
    for (tex <- takeFinalTexs) {
      _idbuf.put(tex.id).rewind()
      GL11.glDeleteTextures(_idbuf)
    }
  }

  /** Called by a texture when it is finalized. */
  private[gfx] def textureFinalized (texture :Texture) :Unit = synchronized {
    _finalTexs = texture :: _finalTexs
  }

  /** Returns the list of textures waiting for destruction, and clears the list. */
  private[this] def takeFinalTexs () = synchronized {
    val ftexs = _finalTexs
    _finalTexs = Nil
    ftexs
  }

  /** The currently bound texture. */
  private[this] var _curtex :Texture = null

  /** A buffer used to communicate with OpenGL. */
  private[this] var _idbuf = BufferUtils.createIntBuffer(1)

  /** A list of finalized textures to be destroyed on the next frame. Beware: this field must only
   * be accessed in a synchronized block */
  private[this] var _finalTexs :List[Texture] = Nil
}
