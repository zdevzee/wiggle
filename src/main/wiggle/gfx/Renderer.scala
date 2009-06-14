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
