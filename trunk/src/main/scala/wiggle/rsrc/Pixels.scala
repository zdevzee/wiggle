//
// $Id$
//
// Wiggle - a 2D game development library - http://code.google.com/p/wiggle/
// Copyright 2008-2010 Michael Bayne
// Distributed under the "Simplified BSD License" in LICENSE.txt

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
