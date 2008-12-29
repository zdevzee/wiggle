//
// $Id$

package wiggle.gfx

import java.nio.ByteBuffer

import scala.collection.mutable.ArrayBuffer

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11

/**
 * Represents an OpenGL graphics primitive. See {@link Primitive.Mode} for the supported primitive
 * modes.
 */
class Primitive (mode :Primitive.Mode, format :Primitive.Format, data :ByteBuffer, vcount :Int)
  extends Element
{
  override def renderElement (rend :Renderer, time :Float) {
    // if we have no texture coordinates, we need to clear out any active texture
    if (format == Primitive.Format.Vertex || format == Primitive.Format.ColorVertex) rend.bindNone()
    GL11.glInterleavedArrays(format.code, 0, data)
    GL11.glDrawArrays(mode.code, 0, vcount)
  }
}

object Primitive
{
  /** An enumeration of our supported GL primitive modes. */
  final case class Mode (val code :Int)
  object Mode {
    val Points = Mode(GL11.GL_POINTS)
    val Lines = Mode(GL11.GL_LINES)
    val LineStrip = Mode(GL11.GL_LINE_STRIP)
    val LineLoop = Mode(GL11.GL_LINE_LOOP)
    val Triangles = Mode(GL11.GL_TRIANGLES)
    val TriangleStrip = Mode(GL11.GL_TRIANGLE_STRIP)
    val TriangleFan = Mode(GL11.GL_TRIANGLE_FAN)
    val Quads = Mode(GL11.GL_QUADS)
    val QuadStrip = Mode(GL11.GL_QUAD_STRIP)
    val Polygon = Mode(GL11.GL_POLYGON)
  }

  /** The different formats in which primitive data can be supplied. See {@link #make}. */
  final case class Format (val code :Int, val bytesPerVertex :Int)
  object Format {
    val Vertex = Format(GL11.GL_V2F, 2*FloatSize)
    val ColorVertex = Format(GL11.GL_C4UB_V2F, 4+2*FloatSize)
    val TexCoordVertex = Format(GL11.GL_T2F_V3F, 5*FloatSize)
    val TexCoordColorVertex = Format(GL11.GL_T2F_C4UB_V3F, 4+5*FloatSize)
  }

  /** Used to build a primitive. */
  class Builder (format :Format, vcount :Int)
  {
    /** Appends a vertex to our in-progress primitive. */
    def vertex (x :Float, y :Float) = {
      // TODO: ensure we're in the right state
      _data.putFloat(x).putFloat(y)
      // we have to use V3F when using tex coords
      if (format == Format.TexCoordVertex || format == Format.TexCoordColorVertex) {
        _data.putFloat(0f)
      }
      this
    }

    /** Appends a color to our in-progress primitive. */
    def color (color :Color) = {
      // TODO: ensure we're in the right state
      _data.put(color.red.toByte).put(color.green.toByte).put(color.blue.toByte)
      _data.put(color.alpha.toByte)
      this
    }

    /** Appends a texture coordinate color to our in-progress primitive. */
    def texCoord (tx :Float, ty :Float) = {
      // TODO: ensure we're in the right state
      _data.putFloat(tx).putFloat(ty)
      this
    }

    /** Builds and returns the primitive in the specified mode. */
    def build (mode :Mode) = {
      // TODO: validate that we've added vcount vertices
      _data.position(0)
      new Primitive(mode, format, _data, vcount)
    }

    /** Builds and returns the primitive as Points. */
    def buildPoints = build(Mode.Points)

    /** Builds and returns the primitive as Lines. */
    def buildLines = build(Mode.Lines)

    /** Builds and returns the primitive as a LineStrip. */
    def buildLineStrip = build(Mode.LineStrip)

    /** Builds and returns the primitive as a LineLoop. */
    def buildLineLoop = build(Mode.LineLoop)

    /** Builds and returns the primitive as Triangles. */
    def buildTriangles = build(Mode.Triangles)

    /** Builds and returns the primitive as a TriangleStrip. */
    def buildTriangleStrip = build(Mode.TriangleStrip)

    /** Builds and returns the primitive as a TriangleFan. */
    def buildTriangleFan = build(Mode.TriangleFan)

    /** Builds and returns the primitive as Quads. */
    def buildQuads = build(Mode.Quads)

    /** Builds and returns the primitive as a QuadStrip. */
    def buildQuadStrip = build(Mode.QuadStrip)

    /** Builds and returns the primitive as a Polygon. */
    def buildPolygon = build(Mode.Polygon)

    protected val _data = BufferUtils.createByteBuffer(format.bytesPerVertex * vcount)
  }

  /**
   * Creates a builder that expects data in the specified format and with the specified number of
   * vertices. Note: it is required that data be added in the order specified in the format
   * enumeration. For example:
   * <pre>
   * // correct
   * val square = Primitive.makeColorVertex(4).
   *   color(c1).vertex(0f, 0f).
   *   color(c2).vertex(5f, 0f).
   *   color(c3).vertex(5f, 5f).
   *   color(c4).vertex(0f, 5f).
   *   buildQuads
   *
   * // incorrect
   * val square = Primitive.makeColorVertex(4).
   *   vertex(0f, 0f).color(c1)
   * // ...
   * </pre>
   */
  def make (format :Format, vcount :Int) = new Builder(format, vcount)

  /**
   * Creates a builder that expects {@link Primitive.Format.Vertex} data. See {@link #make} for an
   * important note on data ordering.
   */
  def makeVertex (vcount :Int) = new Builder(Format.Vertex, vcount)

  /**
   * Creates a builder that expects {@link Primitive.Format.ColorVertex} data. See {@link #make}
   * for an important note on data ordering.
   */
  def makeColorVertex (vcount :Int) = new Builder(Format.ColorVertex, vcount)

  /**
   * Creates a builder that expects {@link Primitive.Format.TexCoordVertex} data. See {@link #make}
   * for an important note on data ordering.
   */
  def makeTexCoordVertex (vcount :Int) = new Builder(Format.TexCoordVertex, vcount)

  /**
   * Creates a builder that expects {@link Primitive.Format.TexCoordColorVertex} data. See
   * {@link #make} for an important note on data ordering.
   */
  def makeTexCoordColorVertex (vcount :Int) = new Builder(Format.TexCoordColorVertex, vcount)

  /** Brought to you by the committe against the proliferation of magic numbers. */
  protected val FloatSize = 4
}
