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
