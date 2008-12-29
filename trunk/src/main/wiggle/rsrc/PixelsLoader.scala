//
// $Id$

package wiggle.rsrc

import java.awt.Color
import java.awt.Transparency
import java.awt.color.ColorSpace
import java.awt.image.BufferedImage
import java.awt.image.ComponentColorModel
import java.awt.image.DataBuffer
import java.awt.image.DataBufferByte
import java.awt.image.Raster
import javax.imageio.ImageIO

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11

/**
 * Loads image data and prepares it for use as textures.
 */
class PixelsLoader (rldr :ResourceLoader)
{
  /** Loads and returns the pixels with the specified key.
   * @throws NoSuchElementException if the pixels resource could not be found.
   */
  def get (key :PixelsKey) = loadPixels(rldr.get(key.path), key)

  /** Locates and returns an option on the specified pixels. */
  def getOption (key :PixelsKey) = rldr.getOption(key.path) match {
    case Some(rsrc) => Some(loadPixels(rsrc, key))
    case None => None
  }

  /** Loads and converts image data. */
  protected def loadPixels (rsrc :Resource, key :PixelsKey) :Pixels = {
    val image = rsrc.asFile match {
      case Some(file) => ImageIO.read(file)
      case None => ImageIO.read(rsrc.asStream)
    }

    // compute a texture size that is a power of two in both dimensions
    val texWidth = nextPowTwo(image.getWidth, 2)
    val texHeight = nextPowTwo(image.getHeight, 2)
    val hasAlpha = image.getColorModel.hasAlpha || key.forceAlpha

    // for now we support only 24 or 32 bit depth
    val bands = if (hasAlpha) 4 else 3
    val raster = Raster.createInterleavedRaster(
      DataBuffer.TYPE_BYTE, texWidth, texHeight, bands, null)
    val cmodel = if (hasAlpha) GlAlphaColorModel else GlColorModel
    val texImage = new BufferedImage(cmodel, raster, false, null)

    // draw the original image into the target image (which converts it to the appropriate format
    // for OpenGL) optionally flipping it in the process
    val gfx = texImage.createGraphics
    try {
      gfx.setColor(Blank)
      gfx.fillRect(0, 0, texWidth, texHeight)
      if (key.flipped) {
        gfx.scale(1, -1)
        gfx.drawImage(image, 0, -image.getHeight, null)
      } else {
        gfx.drawImage(image, 0, 0, null)
      }
    } finally {
      gfx.dispose()
    }

    // extract the raw image data from the target image's raster
    val bytes = texImage.getData.getDataBuffer.asInstanceOf[DataBufferByte].getData
    val data = BufferUtils.createByteBuffer(bytes.length)
    data.put(bytes, 0, bytes.length)
    data.flip()

    new Pixels(image.getWidth, image.getHeight, bands * BitsPerByte, texWidth, texHeight,
               if (hasAlpha) GL11.GL_RGBA else GL11.GL_RGB, data)
  }

  /** Helper function for computing the next higher power of two for a value. */
  protected def nextPowTwo (value :Int, current :Int) :Int =
    if (current > value) current else nextPowTwo(value, current*2)

  /** A blank color used as the default pixel for texture image data. */
  protected val Blank = new Color(0f, 0f, 0f, 0f)

  /** The color model used for images with an alpha-channel. */
  protected val GlAlphaColorModel = new ComponentColorModel(
    ColorSpace.getInstance(ColorSpace.CS_sRGB), Array(8, 8, 8, 8), true, false,
    Transparency.TRANSLUCENT, DataBuffer.TYPE_BYTE);

  /** The color model used for images without an alpha-channel. */
  protected val GlColorModel = new ComponentColorModel(
    ColorSpace.getInstance(ColorSpace.CS_sRGB), Array(8, 8, 8, 0), false, false,
    Transparency.OPAQUE, DataBuffer.TYPE_BYTE);

  /** The number of bits per byte. For great magic number avoidance. */
  protected val BitsPerByte = 8
}
