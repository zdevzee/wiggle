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

import java.nio.ByteBuffer

/**
 * Contains pixel data converted for use as a texture.
 */
class Pixels (
  /** The image data's width in pixels. */
  val width :Int,

  /** The image data's height in pixels. */
  val height :Int,

  /** The width of the texture image in pixels. */
  val texWidth :Int,

  /** The height of the texture image in pixels. */
  val texHeight :Int,

  /** The OpenGL image format identifier for this image data: GL_RGBA or GL_RGB. */
  val format :Int,

  /** This image's raw data. */
  val data :ByteBuffer
)
