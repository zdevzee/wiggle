//
// $Id$
//
// Wiggle - a 2D game development library
// Copyright 2008-2010 Michael Bayne
// Distributed under the "Simplified BSD License" in LICENSE.txt

package wiggle.app

import wiggle.util.Taskable

/**
 * A trait used by objects that are managed by the game system.
 */
trait Entity extends Taskable
{
  // TODO: keep reference to the container, be able to remove ourselves?
}
