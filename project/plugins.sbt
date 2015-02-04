logLevel := Level.Warn

resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"

addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.3")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.3.5")

addSbtPlugin("me.lessis" % "bintray-sbt" % "0.3.0")
