//
// $Id$

package wiggle.test

import wiggle.util.ResourceLoader

/**
 * Tests the {@link ResourceLoader} class. TODO: wire up a unit test framework.
 */
object ResourceLoaderTest
{
  def main (args :Array[String]) {
    val floader = new ResourceLoader(ResourceLoader.viaFileSystem(System.getProperty("user.dir")))
    val frsrc = floader.get("rsrc/test.txt")
    println(frsrc.asFile match {
      case None => "Only stream: " + frsrc.asStream
      case Some(file) => file.getPath
    })

    val cploader = new ResourceLoader(ResourceLoader.viaClassPath(getClass.getClassLoader))
    val cprsrc = cploader.get("test.txt")
    println(cprsrc.asFile match {
      case None => "Only stream: " + cprsrc.asStream
      case Some(file) => file.getPath
    })
  }
}
