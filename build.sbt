tutSettings

name := "my-dispatch-user-notes-examples"
organization := "example"
version := "0.1.0"
scalaVersion := "2.11.8"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

resolvers += Resolver.url("file://" + Path.userHome.absolutePath + "/.ivy/local")
resolvers += "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository"
resolvers += Resolver.sonatypeRepo("releases")
resolvers += Resolver.sonatypeRepo("snapshots")


libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "latest.release"
    ,"com.typesafe.akka" %% "akka-contrib" % "latest.release"
    ,"com.typesafe.akka" %% "akka-http-core" % "latest.release"
    ,"com.typesafe.akka" %% "akka-slf4j" % "latest.release"
    ,"com.typesafe.akka" %% "akka-stream" % "latest.release"
    ,"com.typesafe.akka" %% "akka-http-experimental" % "latest.release"
    ,"com.typesafe.akka" %% "akka-http-jackson-experimental" % "latest.release"
    ,"com.typesafe.akka" %% "akka-http-spray-json-experimental" % "latest.release"
    ,"com.typesafe.akka" %% "akka-http-xml-experimental" % "latest.release"
    ,"com.typesafe" % "config" % "1.3.0"
    ,"org.scala-lang.modules" %% "scala-xml" % "latest.release"
    ,"com.beust" % "jcommander" % "latest.release"
    ,"ch.qos.logback" % "logback-classic" % "latest.release"
    ,"ch.qos.logback" % "logback-core" % "latest.release"
    ,"net.databinder.dispatch" %% "dispatch-core" % "latest.release"
    ,"net.databinder.dispatch" %% "dispatch-lift-json" % "0.11.3"
    ,"net.liftweb" %% "lift-json" % "latest.release"
    ,"com.typesafe.scala-logging" %% "scala-logging" % "latest.release"
    ,"org.scala-lang.modules" %% "scala-async" % "latest.release"
    ,"com.fortysevendeg" %% "fetch" % "0.3.0-SNAPSHOT"
    ,"io.getclump" %% "clump-scala" % "latest.version"
    ,"com.lihaoyi" %% "ammonite-repl" % "latest.release" % "test" cross CrossVersion.full
) ++ Seq("io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser").map(_ % "latest.release")

EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource

EclipseKeys.withSource := true

initialCommands in (Test, console) := """ammonite.repl.Main().run()"""

