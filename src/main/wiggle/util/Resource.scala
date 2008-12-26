//
// $Id$

package wiggle.util

import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.net.URL

/**
 * Provides access to a resource's data. An {@link InputStream} is always available and optionally a
 * {@link File} can be made available by some resource providers.
 */
abstract class Resource
{
  /** Creates and provides an {@link InputStream} that can be used to read this resource's data.
   * The caller is responsible for closing the stream when they are done reading from it. */
  def asStream :InputStream

  /** Provides optional access to this resource as a file. */
  def asFile :Option[File] = None
}

/**
 * Provides common resource classes.
 */
object Resource
{
  /** Provides a resource view of the supplied file. */
  def fileResource (file :File) = new Resource {
    override def asStream = new FileInputStream(file)
    override def asFile = Some(file)
  }

  /** Provides a resource view of the supplied URL. */
  def urlResource (url :URL) = new Resource {
    override def asStream = url.openStream
  }
}
