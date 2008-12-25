//
// $Id$

package wiggle.util

/**
 * Encapsulates a function that interpolates between two values.
 */
trait Interpolator
{
  /** Interpolates between two values.
   *
   *  @param start the starting value.
   *  @param range the difference between the ending value and the starting value.
   *  @param dt the amount of time that has elapsed.
   *  @param t the total amount of time for the interpolation.
   */
  def apply (start :Float, range :Float, dt :Float, t :Float) :Float
}

/**
 * Provides convenience functions for creating various interpolators.
 */
object Interpolator
{
  /** An interpolator that always returns the starting position. */
  val noop = new Interpolator {
    def apply (start :Float, range :Float, dt :Float, t :Float) = start
  }

  /** A linear interpolator. */
  val linear = new Interpolator {
    def apply (start :Float, range :Float, dt :Float, t :Float) = start + range * dt / t
  }

  /** An interpolator that starts to change slowly and ramps up to full speed. */
  val easeIn = new Interpolator {
    def apply (start :Float, range :Float, dt :Float, t :Float) = {
      val dtt = dt / t
      start + range * dtt * dtt * dtt
    }
  }

  /** An interpolator that starts to change quickly and eases into the final value. */
  val easeOut = new Interpolator {
    def apply (start :Float, range :Float, dt :Float, t :Float) = {
      val dtt = dt / t - 1
      start + range * (1 + dtt * dtt * dtt)
    }
  }

  /** An interpolator that eases away from the starting value, speeds up, then eases into the final
   *  value. */
  val easeInOut = new Interpolator {
    def apply (start :Float, range :Float, dt :Float, t :Float) = {
      val hdtt = dt / (t/2)
      if (hdtt < 1) start + range/2 * hdtt * hdtt * hdtt
      else {
        val nhdtt = hdtt - 2
        start + range/2 * (2 + nhdtt * nhdtt * nhdtt)
      }
    }
  }
}
