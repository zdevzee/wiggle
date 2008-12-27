//
// $Id$

package wiggle.rsrc

/**
 * Loads image data and prepares it for use as textures.
 */
class PixelsLoader (rldr :ResourceLoader)
  extends Function1[String, Pixels]
{
  /** Loads and returns the pixels with the specified path.
   * @throws NoSuchElementException if the pixels resource could not be found.
   */
  def get (path :String) = loadPixels(rldr.get(path))

  /** Locates and returns an option on the specified pixels. */
  def getOption (path :String) = rldr.getOption(path) match {
    case Some(rsrc) => loadPixels(rsrc)
    case None => None
  }

  /** Treats the pixels loader as a function from path to Pixels.
   * @see #get */
  def apply (path :String) = get(path)

  protected def loadPixels (rsrc :Resource) :Pixels = {
    throw new UnsupportedOperationException("TODO")
  }
}
