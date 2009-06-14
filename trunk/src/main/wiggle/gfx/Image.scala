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
