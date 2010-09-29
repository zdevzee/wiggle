import java.io.File
import sbt._

class Coreen (info :ProjectInfo) extends DefaultProject(info) {
  // hopefully LWJGL will be in Maven Central soonish
  // val lwjgl = "org.lwjgl" % "lwjgl" % "2.1.0"
  val scalatest = "org.scalatest" % "scalatest" % "1.2" % "test"

  override def fork = Some(new ForkScalaRun {
    val os = System.getProperty("os.name").split(" ")(0).toLowerCase match {
      case "linux" => "linux"
      case "mac" => "macosx"
      case "windows" => "win32"
      case x => error("Unsupported platform " + x)
    }
    override def runJVMOptions = super.runJVMOptions ++ Seq(
      "-Djava.library.path=" + System.getProperty("java.library.path") + File.pathSeparator +
      ("lib" / "native" / os))
    override def scalaJars = Seq(buildLibraryJar.asFile, buildCompilerJar.asFile)
  })
}
