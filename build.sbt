import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.example",
      scalaVersion := "2.11.7",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "Translation",
//    resolvers += "jgit-repo" at "http://download.eclipse.org/jgit/maven",
    libraryDependencies += scalaTest % Test,
    libraryDependencies += swing % Compile
//    libraryDependencies += "org.eclipse.jgit" % "org.eclipse.jgit" % "[2.1,)"
  )

mainClass in Compile := Some("translate.ConvertMessages")
fork in run := true
