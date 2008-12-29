//
// $Id$

package wiggle.rsrc

/**
 * Specifies the location and configuration of some pixels. This can be provided to the
 * {@link PixelsLoader} and {@link TextureCache} when loading image data.
 */
case class PixelsKey (path :String, flipped :Boolean, forceAlpha :Boolean)
