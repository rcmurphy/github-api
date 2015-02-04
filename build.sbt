name := "github-api"

organization := "me.rcmurphy"

scalaVersion := "2.11.8"

licenses += ("Apache-2.0", url("http://opensource.org/licenses/apache-2.0"))

resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.1.2",
  "com.beachape" %% "enumeratum" % "1.3.7",
  //"com.beachape" %% "enumeratum-play-json" % "1.3.7",
  "com.typesafe.play" %% "play-json" % "2.3.10",
  "com.typesafe.play" %% "play-ws" % "2.3.10",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
  // Testing
  "org.scalamock" %% "scalamock-scalatest-support" % "3.2.2" % "test",
  "org.scalatest" %% "scalatest" % "2.2.1" % "test",
  "com.typesafe" % "config" % "1.2.1" % "test",
  "com.typesafe.play" %% "play-test" % "2.3.10",
  "de.leanovate.play-mockws" %% "play-mockws" % "2.3.2" % "test"
)

publishMavenStyle := false
