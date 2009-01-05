//
// $Id$

package wiggle.rsrc

import java.io.File

/**
 * Handles the loading of data from the classpath or filesystem.
 */
class ResourceLoader (providers :Function1[String, Option[Resource]]*)
  extends Function1[String, Resource]
{
  /** Locates and returns the resource with the specified path.
   * @throws NoSuchElementException if the resource could not be found.
   */
  def get (path :String) = getOption(path) match {
    case Some(resource) => resource
    case None => throw new NoSuchElementException("Could not find resource '" + path + "'")
  }

  /** Locates and returns an option on the specified resource. */
  def getOption (path :String) = providers.flatMap(_(path)).firstOption

  /** Treats the resource loader as a function from path to Resource.
   * @see #get */
  def apply (path :String) = get(path)
}

/**
 * Defines standard resource loading functions which can be composed into a loader.
 */
object ResourceLoader
{
  /** Returns a loader that obtains resources from the supplied classloader. */
  def viaClassPath (loader :ClassLoader) = (path :String) => {
    val url = loader.getResource(path)
    if (url == null) None else Some(Resource.urlResource(url))
  }

  /** Returns a loader that obtains resources from the specified directory. */
  def viaFileSystem (root :String) = (path :String) => {
    val file = new File(new File(root), path)
    if (file.exists) Some(Resource.fileResource(file)) else None
  }
}

/**
 * Unit tests for the ResourceLoader.
 */
package tests {
  import java.io.InputStream
  import scala.io.Source
  import org.scalatest.Suite

  class ResourceLoaderSuite extends Suite {
    val root = System.getProperty("user.dir")
    val refpath = "rsrc/test.txt"

    def contents (file :File) = Source.fromFile(file).getLines.toList.reduceLeft(_+_)
    def contents (is :InputStream) = Source.fromInputStream(is).getLines.toList.reduceLeft(_+_)

    def testViaFileSystem () {
      val floader = new ResourceLoader(ResourceLoader.viaFileSystem(root))

      // load things that don't exist
      expect(None) { floader.getOption("nonexistent") }
      intercept(classOf[NoSuchElementException]) { floader.get("nonexistent") }

      // load something that does exist
      floader.get(refpath).asFile match {
        case None => fail("FileSystem loader found no file at " + refpath)
        case Some(file) => {
          val reference = new File(root, refpath)
          expect(reference.getPath) { file.getPath }
          expect(contents(reference)) { contents(file) }
        }
      }
    }

    def testViaClassPath () {
      val cppath = "test.txt"
      val cploader = new ResourceLoader(ResourceLoader.viaClassPath(getClass.getClassLoader))

      // load things that don't exist
      expect(None) { cploader.getOption("nonexistent") }
      intercept(classOf[NoSuchElementException]) { cploader.get("nonexistent") }

      // load something that does exist
      val cprsrc = cploader.get(cppath)
      expect(None) { cprsrc.asFile }
      expect(contents(new File(root, refpath))) { contents(cprsrc.asStream) }
    }
  }
}
