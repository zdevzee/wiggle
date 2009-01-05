//
// $Id$

package wiggle.gfx

/**
 * Represents a color.
 */
case class Color (rgb :Int, alpha :Int)
{
  assert((rgb >> 24) == 0, "'rgb' must be zero in bits 24-31")
  assert((alpha & 0xFF) == alpha, "'alpha' must be between 0 and 0xFF")

  def red = (rgb >> 16) & 0xFF
  def green = (rgb >> 8) & 0xFF
  def blue = (rgb >> 0) & 0xFF

  def toARGB = alpha << 24 | rgb
  def toRGBA = rgb << 8 | alpha

  /** Returns this color in ARGB hex representation, e.g. 0x00FFCC99. */
  def toHexString = (Integer.toString(alpha, 16) + Integer.toString(rgb, 16)).toUpperCase

  override def toString = "(" + red + "," + green + "," + blue + "," + alpha + ")"
}

object Color
{
  val Red = Color.from(0xFF, 0, 0, 0xFF)
  val Green = Color.from(0, 0xFF, 0, 0xFF)
  val Blue = Color.from(0, 0, 0xFF, 0xFF)
  val White = Color.from(0xFF, 0xFF, 0xFF, 0xFF)
  val Black = Color.from(0, 0, 0, 0xFF)

  /** Creates a color from individual component values. */
  def from (red :Int, green :Int, blue :Int, alpha :Int) =
    Color(red << 16 | green << 8 | blue, alpha)

  /** Creates a color from a single RGB value, e.g. 0xFFCC99. */
  def fromRGB (rgb :Int) = Color(rgb, 0xFF)

  /** Creates a color from a single aRGB value, e.g. 0xAAFFCC99. */
  def fromARGB (argb :Int) = Color(argb & 0xFFFFFF, (argb >> 24) & 0xFF)

  /** Creates a color from a single aRGB value, e.g. 0xAAFFCC99. */
  def fromRGBA (rgba :Int) = Color((rgba >> 8) & 0xFFFFFF, rgba & 0xFF)
}

package tests {
  import org.scalatest.Suite

  class ColorSuite extends Suite {
    def testColor () {
      expect(0xFFFFFF) { Color.from(255, 255, 255, 0).toARGB }
      expect(0xFFFFFFFF) { Color.from(255, 255, 255, 255).toARGB }
      expect(0xFFFFCC99) { Color.fromRGB(0xFFCC99).toARGB }
      expect(0xCC9933FF) { Color.fromARGB(0xFFCC9933).toRGBA }
      expect(0xFFCC9933) { Color.fromARGB(0xFFCC9933).toARGB }
      expect(0xFFFFCC99) { Color.fromARGB(0xFFFFCC99).toARGB }
      expect(0xFFCC99FF) { Color.fromARGB(0xFFFFCC99).toRGBA }
      expect("0191919") { Color.from(25, 25, 25, 0).toHexString }
      expect("(255,204,153,255)") { Color.fromRGB(0xFFCC99).toString }
      expect("33FFCC99") { Color.fromARGB(0x33FFCC99).toHexString }
      expect("FFFFCC99") { Color.fromARGB(0xFFFFCC99).toHexString }
    }
  }
}
