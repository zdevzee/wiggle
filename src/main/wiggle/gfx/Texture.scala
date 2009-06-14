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

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL12
import org.lwjgl.opengl.GL13

import wiggle.rsrc.Pixels
import wiggle.rsrc.PixelsKey

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

  /** Returns coordinate to use for the right side of the texture (left being 0). */
  def texRight = width / texWidth.toFloat

  /** Returns coordinate to use for the bottom of the texture (top being 0). */
  def texBottom = height / texHeight.toFloat

  /** When we are finalized, we tell the renderer about it so that it can free our resources. */
  override protected def finalize () {
    renderer.textureFinalized(this)
  }
}
