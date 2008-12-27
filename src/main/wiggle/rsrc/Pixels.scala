//
// $Id$

package wiggle.rsrc

import java.nio.ByteBuffer

/**
 * Contains pixel data converted for use as a texture.
 */
abstract class Pixels
{
  /** This image's width. */
  val width :Float

  /** This image's height. */
  val height :Float

  /** This image's raw data. */
  val data :ByteBuffer
}
