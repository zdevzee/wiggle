//
// $Id$
//
// Wiggle - a 2D game development library
// Copyright 2008-2010 Michael Bayne
// Distributed under the "Simplified BSD License" in LICENSE.txt

package wiggle.rsrc

/**
 * Specifies the location and configuration of some pixels. This can be provided to the
 * {@link PixelsLoader} and {@link TextureCache} when loading image data.
 */
case class PixelsKey (path :String, flipped :Boolean, forceAlpha :Boolean)
{
//   /** A constructor that assumes no alpha forcing. */
//   this (path :String, flipped :Boolean) = this(path, flipped, false)

//   /** A constructor that assumes no flipping or alpha forcing. */
//   this (path :String) = this(path, false, false)
}
