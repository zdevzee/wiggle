//
// $Id$
//
// Wiggle - a 2D game development library
// Copyright 2008-2010 Michael Bayne
// Distributed under the "Simplified BSD License" in LICENSE.txt

package wiggle.rsrc

import java.io.{InputStream, File}
import scala.io.Source
import org.scalatest.Suite

/**
 * Unit tests for the ResourceLoader.
 */
class ResourceLoaderSuite extends Suite
{
  val root = System.getProperty("user.dir")
  val refpath = "src/test/resources/test.txt"

  def contents (file :File) = Source.fromFile(file).getLines.toList.reduceLeft(_+_)
  def contents (is :InputStream) = Source.fromInputStream(is).getLines.toList.reduceLeft(_+_)

  def testViaFileSystem () {
    val floader = new ResourceLoader(ResourceLoader.viaFileSystem(root))

    // load things that don't exist
    expect(None) { floader.getOption("nonexistent") }
    intercept[NoSuchElementException] { floader.get("nonexistent") }

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
    intercept[NoSuchElementException] { cploader.get("nonexistent") }

    // load something that does exist
    val cprsrc = cploader.get(cppath)
    expect(None) { cprsrc.asFile }
    expect(contents(new File(root, refpath))) { contents(cprsrc.asStream) }
  }
}
