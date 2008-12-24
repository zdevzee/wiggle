//
// $Id$

package wiggle.sim

/**
 * Does something extraordinary.
 */
trait Entity
{
  def logic (time :Float)

  def render (time :Float)
}
