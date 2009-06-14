//
// $Id$
//
// Copyright (C) 2008-2009 Michael Bayne
//
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use
// this file except in compliance with the License. You may obtain a copy of the
// License at: http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software distributed
// under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
// CONDITIONS OF ANY KIND, either express or implied. See the License for the
// specific language governing permissions and limitations under the License.

package wiggle.rsrc

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
