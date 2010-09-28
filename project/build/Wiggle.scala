import sbt._

class Coreen (info :ProjectInfo) extends DefaultProject(info) {
  // hopefully LWJGL will be in Maven Central soonish
  // val lwjgl = "org.lwjgl" % "lwjgl" % "2.1.0"
  val scalatest = "org.scalatest" % "scalatest" % "1.2" % "test"

}
